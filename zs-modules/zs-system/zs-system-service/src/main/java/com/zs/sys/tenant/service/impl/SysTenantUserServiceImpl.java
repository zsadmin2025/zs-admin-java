package com.zs.sys.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sys.tenant.domain.entity.SysTenantUserEntity;
import com.zs.sys.tenant.domain.params.SysTenantUserAddParams;
import com.zs.sys.tenant.domain.params.SysTenantUserPageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantUserSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantUserUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantUserVO;
import com.zs.sys.tenant.mapper.SysTenantUserMapper;
import com.zs.sys.tenant.service.SysTenantUserService;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 租户用户关联表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:38
 */
@Service
public class SysTenantUserServiceImpl extends ServiceImpl<SysTenantUserMapper, SysTenantUserEntity> implements SysTenantUserService {

        @Override
        public PageResult<SysTenantUserVO> page(@NotNull SysTenantUserPageQueryParams sysTenantUserPageQueryParams) {

            Page<SysTenantUserEntity> page = new PageInfo<>(sysTenantUserPageQueryParams);
            QueryWrapper<SysTenantUserEntity> wrapper = new QueryWrapper<>();

            IPage<SysTenantUserEntity> iPage = baseMapper.selectPage(page, wrapper);
            List<SysTenantUserVO> list = BeanUtil.copyToList(iPage.getRecords(), SysTenantUserVO.class);

            return new PageResult<>(list, page.getTotal(), SysTenantUserVO.class);
        }

        @Override
        public List<SysTenantUserVO> getList(@NotNull SysTenantUserSelectQueryParams sysTenantUserSelectQueryParams) {
            QueryWrapper<SysTenantUserEntity> wrapper = new QueryWrapper<>();
            return BeanUtil.copyToList(baseMapper.selectList(wrapper), SysTenantUserVO.class);
        }

        @Override
        public void save(@NotNull SysTenantUserAddParams sysTenantUserAddParams) {
            SysTenantUserEntity sysTenantUserEntity = BeanUtil.copyProperties(sysTenantUserAddParams, SysTenantUserEntity.class);
            baseMapper.insert(sysTenantUserEntity);
        }

        @Override
        public void update(@NotNull SysTenantUserUpdateParams sysTenantUserUpdateParams) {
            SysTenantUserEntity sysTenantUserEntity = BeanUtil.copyProperties(sysTenantUserUpdateParams, SysTenantUserEntity.class);
            baseMapper.updateById(sysTenantUserEntity);
        }

        @Override
        public SysTenantUserVO getById(Long id) {
            SysTenantUserVO sysTenantUserVO = BeanUtil.copyProperties(baseMapper.selectById(id), SysTenantUserVO.class);
            return sysTenantUserVO;
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