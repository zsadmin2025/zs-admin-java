package com.zs.lawyer.cases.info.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.info.domain.entity.CaseRelatedPartiesEntity;
import com.zs.lawyer.cases.info.domain.params.CaseRelatedPartiesAddParams;
import com.zs.lawyer.cases.info.domain.params.CaseRelatedPartiesPageQueryParams;
import com.zs.lawyer.cases.info.domain.params.CaseRelatedPartiesSelectQueryParams;
import com.zs.lawyer.cases.info.domain.params.CaseRelatedPartiesUpdateParams;
import com.zs.lawyer.cases.info.domain.vo.CaseRelatedPartiesVO;
import com.zs.lawyer.cases.info.mapper.CaseRelatedPartiesMapper;
import com.zs.lawyer.cases.info.service.CaseRelatedPartiesService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 案件相关方 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:08:42
 */
@Service
public class CaseRelatedPartiesServiceImpl extends ServiceImpl<CaseRelatedPartiesMapper, CaseRelatedPartiesEntity> implements CaseRelatedPartiesService {

    @Override
    public PageResult<CaseRelatedPartiesVO> page(@NotNull CaseRelatedPartiesPageQueryParams caseRelatedPartiesPageQueryParams) {

        Page<CaseRelatedPartiesEntity> page = new PageInfo<>(caseRelatedPartiesPageQueryParams);
        QueryWrapper<CaseRelatedPartiesEntity> wrapper = new QueryWrapper<>();

        IPage<CaseRelatedPartiesEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<CaseRelatedPartiesVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseRelatedPartiesVO.class);

        return new PageResult<>(list, page.getTotal(), CaseRelatedPartiesVO.class);
    }

    @Override
    public List<CaseRelatedPartiesVO> getList(@NotNull CaseRelatedPartiesSelectQueryParams caseRelatedPartiesSelectQueryParams) {
        QueryWrapper<CaseRelatedPartiesEntity> wrapper = new QueryWrapper<>();
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), CaseRelatedPartiesVO.class);
    }

    @Override
    public void save(@NotNull CaseRelatedPartiesAddParams caseRelatedPartiesAddParams) {
        CaseRelatedPartiesEntity caseRelatedPartiesEntity = BeanUtil.copyProperties(caseRelatedPartiesAddParams, CaseRelatedPartiesEntity.class);
        baseMapper.insert(caseRelatedPartiesEntity);
    }

    @Override
    public void update(@NotNull CaseRelatedPartiesUpdateParams caseRelatedPartiesUpdateParams) {
        CaseRelatedPartiesEntity caseRelatedPartiesEntity = BeanUtil.copyProperties(caseRelatedPartiesUpdateParams, CaseRelatedPartiesEntity.class);
        baseMapper.updateById(caseRelatedPartiesEntity);
    }

    @Override
    public CaseRelatedPartiesVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), CaseRelatedPartiesVO.class);
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
    public List<CaseRelatedPartiesVO> getOurSideListByCaseInfoId(Long caseInfoId) {
        List<CaseRelatedPartiesEntity> list = this.baseMapper.selectList(new LambdaQueryWrapper<CaseRelatedPartiesEntity>()
                .eq(CaseRelatedPartiesEntity::getCaseInfoId, caseInfoId)
                .eq(CaseRelatedPartiesEntity::getRole, "our_side"));
        return BeanUtil.copyToList(list, CaseRelatedPartiesVO.class);
    }

    @Override
    public List<CaseRelatedPartiesVO> getOtherSideListByCaseInfoId(Long caseInfoId) {
        List<CaseRelatedPartiesEntity> list = this.baseMapper.selectList(new LambdaQueryWrapper<CaseRelatedPartiesEntity>()
                .eq(CaseRelatedPartiesEntity::getCaseInfoId, caseInfoId)
                .eq(CaseRelatedPartiesEntity::getRole, "other_side"));
        return BeanUtil.copyToList(list, CaseRelatedPartiesVO.class);
    }
}