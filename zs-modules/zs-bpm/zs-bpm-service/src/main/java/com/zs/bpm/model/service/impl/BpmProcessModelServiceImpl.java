package com.zs.bpm.model.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.bpm.category.domain.entity.BpmProcessCategoryEntity;
import com.zs.bpm.category.service.IBpmProcessCategoryService;
import com.zs.bpm.definition.domain.entity.BpmProcessDefinitionInfoEntity;
import com.zs.bpm.definition.domain.params.BpmProcessDefinitionInfoSaveParams;
import com.zs.bpm.definition.domain.vo.BpmProcessDefinitionInfoVO;
import com.zs.bpm.definition.mapper.BpmProcessDefinitionInfoMapper;
import com.zs.bpm.model.domain.params.BpmProcessDefinitionInfoPageQueryParams;
import com.zs.bpm.model.domain.params.BpmProcessDefinitionInfoQueryParams;
import com.zs.bpm.model.domain.vo.ModelValidateVO;
import com.zs.bpm.model.manager.FlowToBpmnConverter;
import com.zs.bpm.model.service.IBpmProcessModelService;
import com.zs.common.core.constant.BpmConstants;
import com.zs.common.core.enums.BpmModelStatusEnum;
import com.zs.common.core.enums.StatusEnum;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.tenant.TenantContext;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BpmProcessModelServiceImpl extends ServiceImpl<BpmProcessDefinitionInfoMapper, BpmProcessDefinitionInfoEntity> implements IBpmProcessModelService {

    private static final Pattern PROCESS_KEY_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$");

    @Resource
    private IBpmProcessCategoryService bpmProcessCategoryService;

    // 流程仓库服务，管理流程部署/定义信息
    private final RepositoryService repositoryService;

    @Resource
    private FlowToBpmnConverter flowToBpmnConverter;

    @Override
    public PageResult<BpmProcessDefinitionInfoVO> page(BpmProcessDefinitionInfoPageQueryParams params) {
        // 链式条件构造，利用 condition 参数简化 if 判断
        LambdaQueryWrapper<BpmProcessDefinitionInfoEntity> wrapper = new LambdaQueryWrapper<BpmProcessDefinitionInfoEntity>()
                .like(StrUtil.isNotBlank(params.getProcessName()), BpmProcessDefinitionInfoEntity::getProcessName, params.getProcessName())
                .like(StrUtil.isNotBlank(params.getProcessKey()), BpmProcessDefinitionInfoEntity::getProcessKey, params.getProcessKey())
                .eq(Objects.nonNull(params.getCategoryId()), BpmProcessDefinitionInfoEntity::getCategoryId, params.getCategoryId())
                .eq(Objects.nonNull(params.getStatus()), BpmProcessDefinitionInfoEntity::getStatus, params.getStatus())
                .orderByDesc(BpmProcessDefinitionInfoEntity::getCreateTime);

        Page<BpmProcessDefinitionInfoEntity> page = new PageInfo<>(params);
        baseMapper.selectPage(page, wrapper);

        // 查询分类映射
        Map<Long, String> categoryMap = bpmProcessCategoryService.list().stream()
                .collect(Collectors.toMap(BpmProcessCategoryEntity::getId, BpmProcessCategoryEntity::getName));

        List<BpmProcessDefinitionInfoVO> voList = page.getRecords().stream()
                .map(entity -> toVO(entity, categoryMap))
                .collect(Collectors.toList());

        return new PageResult<>(voList, page.getTotal());
    }

    /**
     * 将实体转换为VO
     *
     * @param entity 实体
     * @return VO
     */
    private BpmProcessDefinitionInfoVO toVO(BpmProcessDefinitionInfoEntity entity, Map<Long, String> categoryMap) {
        if (entity == null) return null;
        BpmProcessDefinitionInfoVO vo = new BpmProcessDefinitionInfoVO();
        BeanUtils.copyProperties(entity, vo);
        vo.setCategoryName(categoryMap.get(entity.getCategoryId()));

        return vo;
    }

    @Override
    public List<BpmProcessDefinitionInfoVO> getCanStartProcessList(BpmProcessDefinitionInfoQueryParams params) {
        List<BpmProcessDefinitionInfoEntity> list = this.baseMapper.selectList(new LambdaQueryWrapper<BpmProcessDefinitionInfoEntity>()
                .eq(BpmProcessDefinitionInfoEntity::getStatus, StatusEnum.NORMAL.getValue())
                .eq(Objects.nonNull(params.getCategoryId()), BpmProcessDefinitionInfoEntity::getCategoryId, params.getCategoryId())
                .like(StrUtil.isNotBlank(params.getProcessName()), BpmProcessDefinitionInfoEntity::getProcessName, params.getProcessName())
                .like(StrUtil.isNotBlank(params.getProcessKey()), BpmProcessDefinitionInfoEntity::getProcessKey, params.getProcessKey())
                .isNotNull(BpmProcessDefinitionInfoEntity::getPublishTime)
        );

        return BeanUtil.copyToList(list, BpmProcessDefinitionInfoVO.class);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createModel(BpmProcessDefinitionInfoSaveParams params) {
        // 校验流程Key格式和唯一性
        validateProcessKey(params.getProcessKey());
        // 校验流程Key在当前租户下是否已存在
        checkProcessKeyExists(params.getProcessKey());

        // 1. 创建 Flowable Model
        String tenantId = TenantContext.getTenantId();
        String processKey = params.getProcessKey();
        String description = params.getDescription();

        Model model = repositoryService.newModel();
        model.setName(params.getProcessName());
        model.setKey(processKey);
        model.setCategory(params.getCategoryId().toString());
        model.setVersion(BpmConstants.DEFAULT_VERSION);
        model.setMetaInfo(JSONUtil.createObj().set("description", StrUtil.nullToEmpty(description)).toString());
        model.setTenantId(tenantId);

        repositoryService.saveModel(model);
        String modelId = model.getId();


        // 2. 保存 BPMN XML（用于部署）
        
        String bpmnXml = flowToBpmnConverter.convertToBpmn(params.getModelJson(), processKey, params.getProcessName());
        if (StrUtil.isNotBlank(bpmnXml)) {
            repositoryService.addModelEditorSource(modelId, bpmnXml.getBytes(StandardCharsets.UTF_8));
        }

        // 3. 保存 flow 原始JSON（用于编辑回显）
        String modelJson = params.getModelJson();
        if (StrUtil.isNotBlank(modelJson)) {
            repositoryService.addModelEditorSourceExtra(modelId, modelJson.getBytes(StandardCharsets.UTF_8));
        }

        // 4. 保存到业务表
        BpmProcessDefinitionInfoEntity entity = buildDefinitionInfoEntity(modelId, params);
        entity.setBpmnXml(bpmnXml);
        this.baseMapper.insert(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateModel(BpmProcessDefinitionInfoSaveParams params) {
        // 1. 获取现有的流程定义信息
        BpmProcessDefinitionInfoEntity existEntity = this.baseMapper.selectById(params.getId());
        if (existEntity == null) {
            throw new ZsException("流程定义不存在: " + params.getId());
        }

        // 2. 校验流程Key格式
        validateProcessKey(params.getProcessKey());

        // 3. 校验流程Key在当前租户下是否已存在（排除当前模型）
        checkProcessKeyExists(params.getProcessKey(), existEntity.getModelId());

        // 4. 更新 Flowable Model
        String tenantId = TenantContext.getTenantId();
        String processKey = params.getProcessKey();
        String description = params.getDescription();
        String modelId = existEntity.getModelId();
        Model model = null;
        if (StrUtil.isNotBlank(modelId)) {
            model = repositoryService.getModel(modelId);
        }
        if (model == null) {
            // 如果modelId为空或Flowable模型不存在，重新创建一个
            model = repositoryService.newModel();
            model.setName(params.getProcessName());
            model.setKey(processKey);
            model.setCategory(params.getCategoryId().toString());
            model.setMetaInfo(JSONUtil.createObj().set("description", StrUtil.nullToEmpty(description)).toString());
            model.setTenantId(tenantId);
            repositoryService.saveModel(model);
            modelId = model.getId();
        }

        model.setName(params.getProcessName());
        model.setKey(processKey);
        model.setCategory(params.getCategoryId().toString());
        model.setMetaInfo(JSONUtil.createObj().set("description", StrUtil.nullToEmpty(description)).toString());
        model.setTenantId(tenantId);

        repositoryService.saveModel(model);

        // 5. 更新 BPMN XML（用于部署）
        String bpmnXml = flowToBpmnConverter.convertToBpmn(params.getModelJson(), processKey, params.getProcessName());

        if (StrUtil.isNotBlank(bpmnXml)) {
            repositoryService.addModelEditorSource(modelId, bpmnXml.getBytes(StandardCharsets.UTF_8));
        }

        // 6. 更新 flow 原始JSON（用于编辑回显）
        String modelJson = params.getModelJson();
        if (StrUtil.isNotBlank(modelJson)) {
            repositoryService.addModelEditorSourceExtra(modelId, modelJson.getBytes(StandardCharsets.UTF_8));
        }

        // 7. 更新业务表
        BpmProcessDefinitionInfoEntity entity = buildDefinitionInfoEntity(modelId, params);
        entity.setId(params.getId()); // 设置主键ID
        entity.setBpmnXml(bpmnXml);
        this.baseMapper.updateById(entity);
        return entity.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deployModel(Long id) {
        // 根据id查询流程定义信息
        BpmProcessDefinitionInfoEntity entity = this.baseMapper.selectById(id);

        // 获取模型
        Model model = repositoryService.getModel(entity.getModelId());

        Deployment deployment = repositoryService.createDeployment()
                .enableDuplicateFiltering() // 过滤相同内容重复部署
                .key(entity.getProcessKey())
                .name(entity.getProcessName())
                .category(String.valueOf(entity.getCategoryId()))
                .addString(entity.getProcessKey() + BpmConstants.BPMN_FILE_SUFFIX, entity.getBpmnXml())
                .tenantId(TenantContext.getTenantId())
                .deploy();

        // 更新 部署id 到模型对象（将模型与部署数据绑定）
        model.setDeploymentId(deployment.getId());
        model.setVersion(entity.getVersion() + 1);
        repositoryService.saveModel(model);

        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        entity.setDeploymentId(deployment.getId());
        entity.setProcessDefinitionId(definition.getId());
        // 更新流程定义信息表
        this.baseMapper.updateById(entity);

        return true;
    }


    /**
     * 校验流程Key格式：必须以字母开头，只能包含字母、数字和下划线
     */
    private void validateProcessKey(String processKey) {
        if (!PROCESS_KEY_PATTERN.matcher(processKey).matches()) {
            throw new ZsException("流程key必须以字母开头，且只能包含字母、数字和下划线");
        }
    }

    /**
     * 校验流程Key在当前租户下是否已存在
     */
    private void checkProcessKeyExists(String processKey) {
        checkProcessKeyExists(processKey, null);
    }

    /**
     * 校验流程Key在当前租户下是否已存在（排除指定模型ID）
     */
    private void checkProcessKeyExists(String processKey, String excludeModelId) {
        Model existModel = repositoryService.createModelQuery()
                .modelTenantId(TenantContext.getTenantId())
                .modelKey(processKey)
                .singleResult();
        if (existModel != null) {
            // 如果指定了排除的模型ID，且找到的模型就是当前模型，则允许更新
            if (excludeModelId != null && excludeModelId.equals(existModel.getId())) {
                return;
            }
            throw new ZsException("流程key已存在: " + processKey);
        }
    }

    /**
     * 构建流程定义信息实体
     */
    private BpmProcessDefinitionInfoEntity buildDefinitionInfoEntity(String modelId, BpmProcessDefinitionInfoSaveParams params) {
        BpmProcessDefinitionInfoEntity entity = new BpmProcessDefinitionInfoEntity();
        entity.setModelId(modelId);
        entity.setProcessKey(params.getProcessKey());
        entity.setProcessName(params.getProcessName());
        entity.setCategoryId(params.getCategoryId());
        entity.setIcon(params.getIcon());
        entity.setDescription(params.getDescription());
        entity.setFormId(params.getFormId());
        entity.setFormRule(params.getFormRule());
        entity.setFormOption(params.getFormOption());
        entity.setModelJson(params.getModelJson());
        return entity;
    }


    @Override
    public ModelValidateVO validateModel(Long id) {
        BpmProcessDefinitionInfoEntity entity = this.baseMapper.selectById(id);
        if (entity == null) {
            return ModelValidateVO.builder().valid(false)
                    .errors(List.of("BPM-1001: 模型不存在")).build();
        }
        List<String> errors = validateBpmnXml(entity.getBpmnXml());
        return ModelValidateVO.builder().valid(errors.isEmpty()).errors(errors).build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long publishModel(Long id) {
        BpmProcessDefinitionInfoEntity entity = this.baseMapper.selectById(id);
        if (entity == null) throw new RuntimeException("BPM-1001: 模型不存在");
        if (!BpmModelStatusEnum.ACTIVE.getValue().equals(entity.getStatus())) {
            throw new RuntimeException("BPM-1002: 仅草稿状态的模型可以发布，当前状态: " + entity.getStatus());
        }
        entity.setStatus(BpmConstants.MODEL_STATUS_PUBLISHED);
        this.baseMapper.updateById(entity);
        return id;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String activateProcess(String processDefId) {

        return processDefId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deactivateProcess(String processDefId) {

        return processDefId;
    }



    @Override
    public BpmProcessDefinitionInfoVO getDetail(Long id) {
        BpmProcessDefinitionInfoEntity entity = getById(id);
        return BeanUtil.toBean(entity, BpmProcessDefinitionInfoVO.class);
    }



    public List<String> validateBpmnXml(String bpmnXml) {
        List<String> errors = new ArrayList<>();
        if (bpmnXml == null || bpmnXml.trim().isEmpty()) {
            errors.add("BPMN XML 内容为空");
            return errors;
        }
        try {
            InputStream inputStream = new ByteArrayInputStream(bpmnXml.getBytes(StandardCharsets.UTF_8));
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
            BpmnXMLConverter converter = new BpmnXMLConverter();
            BpmnModel bpmnModel = converter.convertToBpmnModel(reader);
            if (bpmnModel == null) {
                errors.add("BPMN XML 无法解析为有效的 BpmnModel");
                return errors;
            }
            org.flowable.bpmn.model.Process process = bpmnModel.getMainProcess();
            if (process == null) {
                errors.add("BPMN XML 中缺少主流程定义");
                return errors;
            }
            boolean hasStartEvent = process.getFlowElements().stream()
                    .anyMatch(e -> e instanceof org.flowable.bpmn.model.StartEvent);
            if (!hasStartEvent) {
                errors.add("流程缺少开始事件（StartEvent）");
            }
        } catch (Exception e) {
            errors.add("BPMN XML 解析失败: " + e.getMessage());
        }
        return errors;
    }
}
