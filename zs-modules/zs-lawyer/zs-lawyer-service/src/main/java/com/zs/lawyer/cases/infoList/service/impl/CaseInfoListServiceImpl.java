package com.zs.lawyer.cases.infoList.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.lawyer.cases.infoList.domain.entity.CaseInfoListEntity;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListAddParams;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListPageQueryParams;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListSelectQueryParams;
import com.zs.lawyer.cases.infoList.domain.params.CaseInfoListUpdateParams;
import com.zs.lawyer.cases.infoList.domain.vo.CaseInfoListVO;
import com.zs.lawyer.cases.infoList.mapper.CaseInfoListMapper;
import com.zs.lawyer.cases.infoList.service.CaseInfoListService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 案件结案目录表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-06-21 12:20:27
 */
@Service
public class CaseInfoListServiceImpl extends ServiceImpl<CaseInfoListMapper, CaseInfoListEntity> implements CaseInfoListService {

    @Override
    public PageResult<CaseInfoListVO> page(@NotNull CaseInfoListPageQueryParams caseInfoListPageQueryParams) {

        Page<CaseInfoListEntity> page = new PageInfo<>(caseInfoListPageQueryParams);
        QueryWrapper<CaseInfoListEntity> wrapper = new QueryWrapper<>();

        IPage<CaseInfoListEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<CaseInfoListVO> list = BeanUtil.copyToList(iPage.getRecords(), CaseInfoListVO.class);

        return new PageResult<>(list, page.getTotal(), CaseInfoListVO.class);
    }

    @Override
    public List<CaseInfoListVO> getList(@NotNull CaseInfoListSelectQueryParams caseInfoListSelectQueryParams) {
        QueryWrapper<CaseInfoListEntity> wrapper = new QueryWrapper<>();
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), CaseInfoListVO.class);
    }

    @Override
    public void save(@NotNull CaseInfoListAddParams caseInfoListAddParams) {
        CaseInfoListEntity caseInfoListEntity = BeanUtil.copyProperties(caseInfoListAddParams, CaseInfoListEntity.class);
        baseMapper.insert(caseInfoListEntity);
    }

    @Override
    public void update(@NotNull CaseInfoListUpdateParams caseInfoListUpdateParams) {
        CaseInfoListEntity caseInfoListEntity = BeanUtil.copyProperties(caseInfoListUpdateParams, CaseInfoListEntity.class);
        baseMapper.updateById(caseInfoListEntity);
    }

    @Override
    public CaseInfoListVO getById(Long id) {
        return BeanUtil.copyProperties(baseMapper.selectById(id), CaseInfoListVO.class);
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
    public List<CaseInfoListVO> getList(Long caseInfoId, String caseType) {
        List<CaseInfoListEntity> list = baseMapper.getList(caseInfoId, caseType);
        return BeanUtil.copyToList(list, CaseInfoListVO.class);
    }
}