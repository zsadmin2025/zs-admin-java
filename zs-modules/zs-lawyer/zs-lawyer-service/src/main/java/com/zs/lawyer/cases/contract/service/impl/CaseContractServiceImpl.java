package com.zs.lawyer.cases.contract.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.enums.FileSourceEnum;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.contract.domain.entity.CaseContractEntity;
import com.zs.lawyer.cases.contract.domain.entity.CaseContractNodeEntity;
import com.zs.lawyer.cases.contract.domain.params.*;
import com.zs.lawyer.cases.contract.domain.vo.CaseContractNodeVO;
import com.zs.lawyer.cases.contract.domain.vo.CaseContractVO;
import com.zs.lawyer.cases.contract.mapper.CaseContractMapper;
import com.zs.lawyer.cases.contract.service.CaseContractNodeService;
import com.zs.lawyer.cases.contract.service.CaseContractService;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesAddParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesSelectQueryParams;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesUpdateParams;
import com.zs.lawyer.cases.infoFiles.service.CaseInfoFilesService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 案件合同 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:02:46
 */
@Service
public class CaseContractServiceImpl extends ServiceImpl<CaseContractMapper, CaseContractEntity> implements CaseContractService {

    @Resource
    private CaseContractNodeService caseContractNodeService; // 案件合同节点信息
    @Resource
    private CaseInfoFilesService caseInfoFilesService; // 案件合同附件信息

    @Override
    public PageResult<CaseContractVO> page(@NotNull CaseContractPageQueryParams caseContractPageQueryParams) {

        Page<CaseContractEntity> page = new PageInfo<>(caseContractPageQueryParams);
        QueryWrapper<CaseContractEntity> wrapper = new QueryWrapper<>();

        IPage<CaseContractEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<CaseContractVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseContractVO.class);

        return new PageResult<>(list, page.getTotal(), CaseContractVO.class);
    }

    @Override
    public List<CaseContractVO> getList(@NotNull CaseContractSelectQueryParams caseContractSelectQueryParams) {
        QueryWrapper<CaseContractEntity> wrapper = new QueryWrapper<>();
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), CaseContractVO.class);
    }

    @Override
    public void save(@NotNull CaseContractAddParams caseContractAddParams) {
        CaseContractEntity caseContractEntity = BeanUtil.copyProperties(caseContractAddParams, CaseContractEntity.class);
        baseMapper.insert(caseContractEntity);

        // 合同节点信息
        List<CaseContractNodeAddParams> contractNodeList = caseContractAddParams.getContractNodeList();
        if (contractNodeList != null && !contractNodeList.isEmpty()) {
            contractNodeList.forEach(node -> {
                node.setCaseInfoId(caseContractEntity.getCaseInfoId());
                node.setCaseContractId(caseContractEntity.getCaseContractId());
            });
            caseContractNodeService.save(contractNodeList); // 确认该方法为批量插入
        }

        // 合同附件信息
        List<CaseInfoFilesAddParams> contractFileList = caseContractAddParams.getContractFileList();
        if (contractFileList != null && !contractFileList.isEmpty()) {
            contractFileList.forEach(file -> {
                file.setCaseInfoId(caseContractEntity.getCaseInfoId());
                file.setCaseOtherId(caseContractEntity.getCaseContractId());
                file.setFileSource(FileSourceEnum.CASE_CONTRACT.getValue());
            });
            caseInfoFilesService.save(contractFileList); // 确认该方法为批量插入
        }

    }

    @Override
    public void update(@NotNull CaseContractUpdateParams caseContractUpdateParams) {
        CaseContractEntity caseContractEntity = BeanUtil.copyProperties(caseContractUpdateParams, CaseContractEntity.class);
        baseMapper.updateById(caseContractEntity);

        // 合同节点信息
        List<CaseContractNodeUpdateParams> contractNodeList = caseContractUpdateParams.getContractNodeList();

        if (contractNodeList != null && !contractNodeList.isEmpty()) {
            // 删除旧节点
            caseContractNodeService.remove(new LambdaQueryWrapper<CaseContractNodeEntity>().eq(CaseContractNodeEntity::getCaseContractId, caseContractEntity.getCaseContractId()));

            // 转换并设置外键
            List<CaseContractNodeEntity> nodeEntityList = BeanUtil.copyToList(contractNodeList, CaseContractNodeEntity.class);
            nodeEntityList.forEach(entity -> {
                entity.setCaseInfoId(caseContractEntity.getCaseInfoId());
                entity.setCaseContractId(caseContractEntity.getCaseContractId());
            });

            // 批量保存新节点
            caseContractNodeService.saveBatch(nodeEntityList);

        }


        // 合同附件信息
        List<CaseInfoFilesUpdateParams> contractFileList = caseContractUpdateParams.getContractFileList();

        if (contractFileList == null) {
            contractFileList = Collections.emptyList(); // 避免 null 引发异常
        }

        caseInfoFilesService.saveOrUpdateBatch(
                contractFileList,
                caseContractEntity.getCaseInfoId(),
                caseContractEntity.getCaseContractId(),
                FileSourceEnum.CASE_CONTRACT.getValue()
        );


    }

    @Override
    public CaseContractVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), CaseContractVO.class);
    }

    @Override
    public CaseContractVO getByCaseInfoId(Long caseInfoId) {
        CaseContractEntity caseContractEntity = baseMapper.selectOne(new LambdaQueryWrapper<CaseContractEntity>().eq(CaseContractEntity::getCaseInfoId, caseInfoId));

        if (caseContractEntity == null) {
            // 可返回 null 或抛出自定义异常，根据业务需求决定
            return null;
        }
        CaseContractVO caseContractVO = BeanUtil.copyProperties(caseContractEntity, CaseContractVO.class);

        // 获取案件合同下的所有案件合同文件
        CaseInfoFilesSelectQueryParams caseInfoFilesSelectQueryParams = new CaseInfoFilesSelectQueryParams();
        caseInfoFilesSelectQueryParams.setCaseInfoId(caseInfoId);
        caseInfoFilesSelectQueryParams.setCaseOtherId(caseContractVO.getCaseContractId());
        caseInfoFilesService.getList(caseInfoFilesSelectQueryParams);

        caseContractVO.setContractFileList(caseInfoFilesService.getList(caseInfoFilesSelectQueryParams));


        // 获取合同节点
        CaseContractNodeSelectQueryParams caseContractNodeSelectQueryParams = new CaseContractNodeSelectQueryParams();
        caseContractNodeSelectQueryParams.setCaseContractId(caseContractVO.getCaseContractId());
        List<CaseContractNodeVO> contractNodeList = caseContractNodeService.getList(caseContractNodeSelectQueryParams);
        caseContractVO.setContractNodeList(contractNodeList);


        return caseContractVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
        caseContractNodeService.remove(new LambdaQueryWrapper<CaseContractNodeEntity>().eq(CaseContractNodeEntity::getCaseContractId, id));
    }

    @Override
    public void batchDelById(@NotNull Long[] ids) {
        baseMapper.deleteByIds(Arrays.asList(ids));
    }
}