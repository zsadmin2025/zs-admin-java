package com.zs.bpm.definition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.bpm.definition.domain.entity.BpmProcessDefinitionInfoEntity;
import com.zs.bpm.definition.domain.params.BpmProcessDefinitionInfoSaveParams;
import com.zs.bpm.definition.domain.vo.BpmProcessDefinitionInfoVO;
import com.zs.bpm.definition.mapper.BpmProcessDefinitionInfoMapper;
import com.zs.bpm.definition.service.IBpmProcessDefinitionInfoService;
import com.zs.bpm.model.domain.params.BpmProcessDefinitionInfoPageQueryParams;
import com.zs.common.core.enums.BpmModelStatusEnum;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程定义信息 Service 实现
 *
 * @author zsadmin
 */
@Service
@RequiredArgsConstructor
public class BpmProcessDefinitionInfoServiceImpl
        extends ServiceImpl<BpmProcessDefinitionInfoMapper, BpmProcessDefinitionInfoEntity>
        implements IBpmProcessDefinitionInfoService {

    @Resource
    private RepositoryService repositoryService;


    @Override
    public List<ProcessDefinition> queryProcessDefinition() {
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
        return processDefinitionList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteProcessDefinition(String processDefinitionId) {
        // 获取流程定义信息，用于获取部署ID
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        // 第二个参数是级联删除，如果流程启动了 相关的任务一并会被删除掉
        repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
        return true;
    }

    @Override
    public PageResult<BpmProcessDefinitionInfoVO> page(BpmProcessDefinitionInfoPageQueryParams params) {
        LambdaQueryWrapper<BpmProcessDefinitionInfoEntity> wrapper = new LambdaQueryWrapper<>();

        if (params.getCategoryId() != null) {
            wrapper.eq(BpmProcessDefinitionInfoEntity::getCategoryId, params.getCategoryId());
        }
        if (params.getStatus() != null) {
            wrapper.eq(BpmProcessDefinitionInfoEntity::getStatus, params.getStatus());
        }
        wrapper.orderByDesc(BpmProcessDefinitionInfoEntity::getCreateTime);

        Page<BpmProcessDefinitionInfoEntity> page = new PageInfo<>(params);
        baseMapper.selectPage(page, wrapper);

        List<BpmProcessDefinitionInfoVO> voList = page.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        return new PageResult<>(voList, page.getTotal());
    }

    @Override
    public BpmProcessDefinitionInfoVO getDetail(Long id) {
        BpmProcessDefinitionInfoEntity entity = getById(id);
        return entity != null ? toVO(entity) : null;
    }

    @Override
    public Long saveDefinition(BpmProcessDefinitionInfoSaveParams params) {
        BpmProcessDefinitionInfoEntity entity;
        if (params.getId() != null) {
            entity = getById(params.getId());
            if (entity == null) return null;
        } else {
            entity = new BpmProcessDefinitionInfoEntity();
        }
        entity.setProcessName(params.getProcessName());
        entity.setProcessKey(params.getProcessKey());
        entity.setCategoryId(params.getCategoryId());
        entity.setModelJson(params.getModelJson());
        entity.setBpmnXml(params.getBpmnXml());
        entity.setIcon(params.getIcon());
        entity.setDescription(params.getDescription());
        if (params.getStatus() != null) {
            entity.setStatus(params.getStatus());
        }
        saveOrUpdate(entity);
        return entity.getId();
    }

    /**
     * 将实体转换为VO
     *
     * @param entity 实体
     * @return VO
     */
    private BpmProcessDefinitionInfoVO toVO(BpmProcessDefinitionInfoEntity entity) {
        if (entity == null) return null;
        BpmProcessDefinitionInfoVO vo = new BpmProcessDefinitionInfoVO();
        BeanUtils.copyProperties(entity, vo);
        BpmModelStatusEnum statusEnum = BpmModelStatusEnum.of(entity.getStatus());

        return vo;
    }
}
