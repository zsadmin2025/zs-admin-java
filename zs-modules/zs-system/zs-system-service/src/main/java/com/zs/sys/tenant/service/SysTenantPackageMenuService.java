package com.zs.sys.tenant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.sys.tenant.domain.entity.SysTenantPackageMenuEntity;

import java.util.List;

/**
 * <p>
 * 套餐菜单关联表 服务类
 * </p>
 *
 * @author zs
 * @since 2025-08-20 17:56:11
 */
public interface SysTenantPackageMenuService extends IService<SysTenantPackageMenuEntity> {


    /**
     * 保存
     * @param sysTenantPackageId 套餐ID
     * @param menuIdList 菜单ID集合
     */
    void save(Long sysTenantPackageId, List<Long> menuIdList);

    /**
     * 修改
     * @param sysTenantPackageId 套餐ID
     * @param menuIdList 菜单ID集合
     */
    void update(Long sysTenantPackageId, List<Long> menuIdList);

    /**
     * 根据套餐ID查询
     * @param sysTenantPackageId 套餐ID
     * @return 菜单ID集合
     */
    List<Long> listBySysTenantPackageId(Long sysTenantPackageId);
}