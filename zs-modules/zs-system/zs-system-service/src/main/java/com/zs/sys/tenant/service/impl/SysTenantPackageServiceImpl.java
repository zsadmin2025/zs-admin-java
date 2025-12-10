package com.zs.sys.tenant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zs.common.core.page.PageInfo;
import com.zs.common.core.page.PageResult;
import com.zs.sys.tenant.domain.entity.SysTenantPackageEntity;
import com.zs.sys.tenant.domain.params.SysTenantPackageAddParams;
import com.zs.sys.tenant.domain.params.SysTenantPackagePageQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageSelectQueryParams;
import com.zs.sys.tenant.domain.params.SysTenantPackageUpdateParams;
import com.zs.sys.tenant.domain.vo.SysTenantPackageVO;
import com.zs.sys.tenant.mapper.SysTenantPackageMapper;
import com.zs.sys.tenant.service.SysTenantPackageMenuService;
import com.zs.sys.tenant.service.SysTenantPackageService;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 租户套餐表 服务实现类
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:40
 */
@Service
public class SysTenantPackageServiceImpl extends ServiceImpl<SysTenantPackageMapper, SysTenantPackageEntity> implements SysTenantPackageService {

    @Resource
    private SysTenantPackageMenuService sysTenantPackageMenuService;

    @Override
    public PageResult<SysTenantPackageVO> page(@NotNull SysTenantPackagePageQueryParams sysTenantPackagePageQueryParams) {

        Page<SysTenantPackageEntity> page = new PageInfo<>(sysTenantPackagePageQueryParams);
        LambdaQueryWrapper<SysTenantPackageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(sysTenantPackagePageQueryParams.getPackageCode()), SysTenantPackageEntity::getPackageCode, sysTenantPackagePageQueryParams.getPackageCode())
                .like(Strings.isNotEmpty(sysTenantPackagePageQueryParams.getPackageName()), SysTenantPackageEntity::getPackageName, sysTenantPackagePageQueryParams.getPackageName())
                .eq(Objects.nonNull(sysTenantPackagePageQueryParams.getStatus()), SysTenantPackageEntity::getStatus, sysTenantPackagePageQueryParams.getStatus());

        IPage<SysTenantPackageEntity> iPage = baseMapper.selectPage(page, wrapper);
        List<SysTenantPackageVO> list = BeanUtil.copyToList(iPage.getRecords(), SysTenantPackageVO.class);

        return new PageResult<>(list, page.getTotal(), SysTenantPackageVO.class);
    }

    @Override
    public List<SysTenantPackageVO> getList(@NotNull SysTenantPackageSelectQueryParams sysTenantPackageSelectQueryParams) {
        QueryWrapper<SysTenantPackageEntity> wrapper = new QueryWrapper<>();
        return BeanUtil.copyToList(baseMapper.selectList(wrapper), SysTenantPackageVO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(@NotNull SysTenantPackageAddParams sysTenantPackageAddParams) {
        SysTenantPackageEntity sysTenantPackageEntity = BeanUtil.copyProperties(sysTenantPackageAddParams, SysTenantPackageEntity.class);
        baseMapper.insert(sysTenantPackageEntity);

        // 保存租户套餐菜单关联关系
        sysTenantPackageMenuService.save(sysTenantPackageEntity.getSysTenantPackageId(), sysTenantPackageAddParams.getMenuIdList());
    }

    @Override
    public void update(@NotNull SysTenantPackageUpdateParams sysTenantPackageUpdateParams) {
        SysTenantPackageEntity sysTenantPackageEntity = BeanUtil.copyProperties(sysTenantPackageUpdateParams, SysTenantPackageEntity.class);
        baseMapper.updateById(sysTenantPackageEntity);

        // 更新租户套餐菜单关联关系
        sysTenantPackageMenuService.update(sysTenantPackageEntity.getSysTenantPackageId(), sysTenantPackageUpdateParams.getMenuIdList());

    }

    @Override
    public SysTenantPackageVO getById(Long id) {
        SysTenantPackageVO sysTenantPackageVO = BeanUtil.copyProperties(baseMapper.selectById(id), SysTenantPackageVO.class);
        List<Long> menuIdList = sysTenantPackageMenuService.listBySysTenantPackageId(id);

        if (menuIdList != null && !menuIdList.isEmpty()) {
            sysTenantPackageVO.setMenuIdList(menuIdList);
        }

        return sysTenantPackageVO;
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