package com.zs.lawyer.cases.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.customer.domain.entity.CaseCustomerEntity;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerAddParams;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerPageQueryParams;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerSelectQueryParams;
import com.zs.lawyer.cases.customer.domain.params.CaseCustomerUpdateParams;
import com.zs.lawyer.cases.customer.domain.vo.CaseCustomerVO;
import com.zs.lawyer.cases.customer.mapper.CaseCustomerMapper;
import com.zs.lawyer.cases.customer.service.CaseCustomerService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 案件客户表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:55:28
 */
@Service
public class CaseCustomerServiceImpl extends ServiceImpl<CaseCustomerMapper, CaseCustomerEntity> implements CaseCustomerService {

    @Override
    public PageResult<CaseCustomerVO> page(@NotNull CaseCustomerPageQueryParams caseCustomerPageQueryParams) {

        Page<CaseCustomerEntity> page = new PageInfo<>(caseCustomerPageQueryParams);
        QueryWrapper<CaseCustomerEntity> wrapper = new QueryWrapper<>();

        IPage<CaseCustomerEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<CaseCustomerVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseCustomerVO.class);

        return new PageResult<>(list, page.getTotal(), CaseCustomerVO.class);
    }

    @Override
    public List<CaseCustomerVO> getList(@NotNull CaseCustomerSelectQueryParams caseCustomerSelectQueryParams) {
        QueryWrapper<CaseCustomerEntity> wrapper = new QueryWrapper<>();
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), CaseCustomerVO.class);
    }

    @Override
    public void save(@NotNull CaseCustomerAddParams caseCustomerAddParams) {
        CaseCustomerEntity caseCustomerEntity = BeanUtil.copyProperties(caseCustomerAddParams, CaseCustomerEntity.class);
        baseMapper.insert(caseCustomerEntity);
    }

    @Override
    public void update(@NotNull CaseCustomerUpdateParams caseCustomerUpdateParams) {
        CaseCustomerEntity caseCustomerEntity = BeanUtil.copyProperties(caseCustomerUpdateParams, CaseCustomerEntity.class);
        baseMapper.updateById(caseCustomerEntity);
    }

    @Override
    public void saveOrUpdate(CaseCustomerAddParams caseCustomerAddParams) {
        CaseCustomerEntity caseCustomerEntity = BeanUtil.copyProperties(caseCustomerAddParams, CaseCustomerEntity.class);
        baseMapper.insertOrUpdate(caseCustomerEntity);
    }

    @Override
    public CaseCustomerVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), CaseCustomerVO.class);
    }

    @Override
    public CaseCustomerVO getByCaseInfoId(Long caseInfoId) {
        CaseCustomerEntity caseCustomerEntity = baseMapper.selectOne(new LambdaQueryWrapper<CaseCustomerEntity>().eq(CaseCustomerEntity::getCaseInfoId, caseInfoId));
        return BeanUtil.copyProperties(caseCustomerEntity, CaseCustomerVO.class);
    }

    @Override
    public void deleteById(Long id) {
        baseMapper.deleteById(id);
    }

    @Override
    public void batchDelById(@NotNull Long[] ids) {
        baseMapper.deleteByIds(Arrays.asList(ids));
    }
}