package com.zs.lawyer.cases.infoApprovalForm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.common.core.utils.SecurityUtil;
import com.zs.lawyer.cases.infoApprovalForm.domain.entity.CaseInfoApprovalFormEntity;
import com.zs.lawyer.cases.infoApprovalForm.domain.params.*;
import com.zs.lawyer.cases.infoApprovalForm.domain.vo.CaseInfoApprovalFormVO;
import com.zs.lawyer.cases.infoApprovalForm.mapper.CaseInfoApprovalFormMapper;
import com.zs.lawyer.cases.infoApprovalForm.service.CaseInfoApprovalFormService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 案件审批表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-07-10 07:07:27
 */
@Service
public class CaseInfoApprovalFormServiceImpl extends ServiceImpl<CaseInfoApprovalFormMapper, CaseInfoApprovalFormEntity> implements CaseInfoApprovalFormService {


    @Override
    public PageResult<CaseInfoApprovalFormVO> page(@NotNull CaseInfoApprovalFormPageQueryParams caseInfoApprovalFormPageQueryParams) {

        Page<CaseInfoApprovalFormEntity> page = new PageInfo<>(caseInfoApprovalFormPageQueryParams);
        Map<String, Object> params = BeanUtil.beanToMap(caseInfoApprovalFormPageQueryParams);

        IPage<CaseInfoApprovalFormEntity> iPage = baseMapper.page(page, params);

        List<CaseInfoApprovalFormVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseInfoApprovalFormVO.class);


        return new PageResult<>(list, page.getTotal(), CaseInfoApprovalFormVO.class);
    }

    @Override
    public List<CaseInfoApprovalFormVO> getList(@NotNull CaseInfoApprovalFormSelectQueryParams caseInfoApprovalFormSelectQueryParams) {
        QueryWrapper<CaseInfoApprovalFormEntity> wrapper = new QueryWrapper<>();
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), CaseInfoApprovalFormVO.class);
    }

    @Override
    public void save(@NotNull CaseInfoApprovalFormAddParams caseInfoApprovalFormAddParams) {
        CaseInfoApprovalFormEntity caseInfoApprovalFormEntity = BeanUtil.copyProperties(caseInfoApprovalFormAddParams, CaseInfoApprovalFormEntity.class);
        caseInfoApprovalFormEntity.setApplicant(SecurityUtil.getUserId());
        caseInfoApprovalFormEntity.setApplicantName(SecurityUtil.getRealName());
        baseMapper.insert(caseInfoApprovalFormEntity);
    }

    @Override
    public void update(@NotNull CaseInfoApprovalFormUpdateParams caseInfoApprovalFormUpdateParams) {
        CaseInfoApprovalFormEntity caseInfoApprovalFormEntity = BeanUtil.copyProperties(caseInfoApprovalFormUpdateParams, CaseInfoApprovalFormEntity.class);
        baseMapper.updateById(caseInfoApprovalFormEntity);
    }

    @Override
    public CaseInfoApprovalFormVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), CaseInfoApprovalFormVO.class);
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
    public void passApprove(CaseInfoApprovalFormParams caseInfoApprovalFormParams) {
        CaseInfoApprovalFormEntity entity = new CaseInfoApprovalFormEntity();
        entity.setCaseInfoApprovalFormId(caseInfoApprovalFormParams.getCaseInfoApprovalFormId());
        entity.setApprovalStatus(2);
        entity.setApprovalTime(new Date());
        entity.setApprovalOpinion(caseInfoApprovalFormParams.getApprovalOpinion());

        baseMapper.updateById(entity);
    }

    @Override
    public CaseInfoApprovalFormVO getByCaseInfoId(Long caseInfoId) {
        CaseInfoApprovalFormEntity entity = baseMapper.selectOne(new LambdaQueryWrapper<CaseInfoApprovalFormEntity>().eq(CaseInfoApprovalFormEntity::getCaseInfoId, caseInfoId));
        return BeanUtil.copyProperties(entity, CaseInfoApprovalFormVO.class);
    }

    @Override
    public void submitApprovalForm(CaseInfoApprovalFormUpdateParams caseInfoApprovalFormUpdateParams) {

        Long formId = caseInfoApprovalFormUpdateParams.getCaseInfoApprovalFormId();

        // 查询当前记录
        CaseInfoApprovalFormEntity entity = baseMapper.selectOne(
                new LambdaQueryWrapper<CaseInfoApprovalFormEntity>()
                        .eq(CaseInfoApprovalFormEntity::getCaseInfoApprovalFormId, formId)
        );


        if (entity != null && (entity.getApprovalStatus() == 1 || entity.getApprovalStatus() == 2)){
            throw new RuntimeException("案件正在审批中");
        }

        CaseInfoApprovalFormEntity caseInfoApprovalFormEntity = new CaseInfoApprovalFormEntity();
        caseInfoApprovalFormEntity.setCaseInfoApprovalFormId(caseInfoApprovalFormUpdateParams.getCaseInfoApprovalFormId());
        caseInfoApprovalFormEntity.setApplyTime(new Date());
        caseInfoApprovalFormEntity.setApprovalStatus(1);

        baseMapper.updateById(caseInfoApprovalFormEntity);
    }
}