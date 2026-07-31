package com.zs.lawyer.cases.infoApprove.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoApprove.domain.entity.CaseInfoApproveEntity;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApproveAddParams;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApprovePageQueryParams;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApproveSelectQueryParams;
import com.zs.lawyer.cases.infoApprove.domain.params.CaseInfoApproveUpdateParams;
import com.zs.lawyer.cases.infoApprove.domain.vo.CaseInfoApproveVO;
import com.zs.lawyer.cases.infoApprove.mapper.CaseInfoApproveMapper;
import com.zs.lawyer.cases.infoApprove.service.CaseInfoApproveService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 案件审批表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-06-30 09:04:42
 */
@Service
public class CaseInfoApproveServiceImpl extends ServiceImpl<CaseInfoApproveMapper, CaseInfoApproveEntity> implements CaseInfoApproveService {

    @Override
    public PageResult<CaseInfoApproveVO> page(@NotNull CaseInfoApprovePageQueryParams caseInfoApprovePageQueryParams) {

        Page<CaseInfoApproveEntity> page = new PageInfo<>(caseInfoApprovePageQueryParams);
        QueryWrapper<CaseInfoApproveEntity> wrapper = new QueryWrapper<>();

        IPage<CaseInfoApproveEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<CaseInfoApproveVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseInfoApproveVO.class);

        return new PageResult<>(list, page.getTotal(), CaseInfoApproveVO.class);
    }

    @Override
    public List<CaseInfoApproveVO> getList(@NotNull CaseInfoApproveSelectQueryParams caseInfoApproveSelectQueryParams) {
        QueryWrapper<CaseInfoApproveEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(CaseInfoApproveEntity::getCaseInfoId, caseInfoApproveSelectQueryParams.getCaseInfoId());
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), CaseInfoApproveVO.class);
    }


    @Override
    public void save(@NotNull CaseInfoApproveAddParams caseInfoApproveAddParams) {
        CaseInfoApproveEntity caseInfoApproveEntity = BeanUtil.copyProperties(caseInfoApproveAddParams, CaseInfoApproveEntity.class);
        baseMapper.insert(caseInfoApproveEntity);
    }

    @Override
    public void update(@NotNull CaseInfoApproveUpdateParams caseInfoApproveUpdateParams) {
        CaseInfoApproveEntity caseInfoApproveEntity = BeanUtil.copyProperties(caseInfoApproveUpdateParams, CaseInfoApproveEntity.class);
        baseMapper.updateById(caseInfoApproveEntity);
    }

    @Override
    public CaseInfoApproveVO getById(Long id) {
        CaseInfoApproveVO caseInfoApproveVO = BeanUtil.copyProperties(baseMapper.selectById(id), CaseInfoApproveVO.class);
        return caseInfoApproveVO;
    }

    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void batchDelById(@NotNull Long[] ids) {
        baseMapper.deleteByIds(Arrays.asList(ids));
    }

    @Override
    public void save(List<Long> approvalLawyerList, Long caseInfoId) {
        for (Long approvalLawyer : approvalLawyerList) {
            CaseInfoApproveEntity caseInfoApproveEntity = new CaseInfoApproveEntity();
            caseInfoApproveEntity.setApprovalLawyer(approvalLawyer);
            caseInfoApproveEntity.setCaseInfoId(caseInfoId);
            baseMapper.insert(caseInfoApproveEntity);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(List<Long> approvalLawyerList, Long caseInfoId) {
        baseMapper.delete(new LambdaQueryWrapper<CaseInfoApproveEntity>().eq(CaseInfoApproveEntity::getCaseInfoId, caseInfoId));
        for (Long approvalLawyer : approvalLawyerList) {
            CaseInfoApproveEntity caseInfoApproveEntity = new CaseInfoApproveEntity();
            caseInfoApproveEntity.setApprovalLawyer(approvalLawyer);
            caseInfoApproveEntity.setCaseInfoId(caseInfoId);
            baseMapper.insert(caseInfoApproveEntity);
        }
    }
}