package com.zs.sys.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.sys.role.domain.entity.SysRoleMenuEntity;

import java.util.List;

/**
 * @author zsadmin
 */
public interface ISysRoleMenuService extends IService<SysRoleMenuEntity> {


    /**
     * 保存角色菜单
     *
     * @param sysRoleId 角色ID
     * @param menuList  菜单列表
     */
    void save(Long sysRoleId, List<Long> menuList);


    /**
     * 根据角色获取对应的菜单ID列表集合
     *
     * @param sysRoleId 角色ID
     * @return 菜单ID列表集合
     */
    List<Long> getMenuList(Long sysRoleId);

    /**
     * 根据菜单ID获取对应的角色数量
     * @param sysMenuId 菜单ID
     * @return  角色数量
     */
    Long getCount(Long sysMenuId);

    /**
     * 保存角色菜单
     *
     * @param sysRoleId 角色ID
     * @param menuIdList 菜单ID列表
     * @param sysTenantId 租户ID
     */
    void save(Long sysRoleId, List<Long> menuIdList, Long sysTenantId);

    /**
     * 根据菜单ID删除角色菜单关联关系
     * @param sysMenuId 菜单ID
     */
    void delByMenuId(Long sysMenuId);
}
