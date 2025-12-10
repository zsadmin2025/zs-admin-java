package com.zs.sys.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sys.tenant.domain.entity.SysTenantPackageRelEntity;
import com.zs.sys.tenant.domain.params.SysTenantPackageRelAddParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageRelPageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageRelSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageRelUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantPackageRelVO;
import com.zs.sys.tenant.mapper.SysTenantPackageRelMapper;
import com.zs.sys.tenant.service.SysTenantPackageRelService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 租户套餐关联表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:43
 */
@Service
public class SysTenantPackageRelServiceImpl extends ServiceImpl<SysTenantPackageRelMapper, SysTenantPackageRelEntity> implements SysTenantPackageRelService {

        @Override
        public PageResult<SysTenantPackageRelVO> page(@NotNull SysTenantPackageRelPageQueryParams sysTenantPackageRelPageQueryParams) {

            Page<SysTenantPackageRelEntity> page = new PageInfo<>(sysTenantPackageRelPageQueryParams);
            QueryWrapper<SysTenantPackageRelEntity> wrapper = new QueryWrapper<>();

            IPage<SysTenantPackageRelEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<SysTenantPackageRelVO> list = BeanUtil.copyToList(iPage.getRecords(), SysTenantPackageRelVO.class);

            return new PageResult<>(list, page.getTotal(), SysTenantPackageRelVO.class);
        }

        @Override
        public List<SysTenantPackageRelVO> getList(@NotNull SysTenantPackageRelSelectQueryParams sysTenantPackageRelSelectQueryParams) {
            QueryWrapper<SysTenantPackageRelEntity> wrapper = new QueryWrapper<>();
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), SysTenantPackageRelVO.class);
        }

        @Override
        public void save(@NotNull SysTenantPackageRelAddParams sysTenantPackageRelAddParams) {
            SysTenantPackageRelEntity sysTenantPackageRelEntity = BeanUtil.copyProperties(sysTenantPackageRelAddParams, SysTenantPackageRelEntity.class);
            baseMapper.insert(sysTenantPackageRelEntity);
        }

        @Override
        public void update(@NotNull SysTenantPackageRelUpdateParams sysTenantPackageRelUpdateParams) {
            SysTenantPackageRelEntity sysTenantPackageRelEntity = BeanUtil.copyProperties(sysTenantPackageRelUpdateParams, SysTenantPackageRelEntity.class);
            baseMapper.updateById(sysTenantPackageRelEntity);
        }

        @Override
        public SysTenantPackageRelVO getById(Long id) {
            SysTenantPackageRelVO sysTenantPackageRelVO = BeanUtil.copyProperties(baseMapper.selectById(id), SysTenantPackageRelVO.class);
            return sysTenantPackageRelVO;
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