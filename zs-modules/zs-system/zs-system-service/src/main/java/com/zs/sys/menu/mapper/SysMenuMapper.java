package com.zs.sys.menu.mapper;


import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.menu.domain.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zsadmin
 */
@Mapper
public interface SysMenuMapper extends DataPermissionMapper<SysMenuEntity> {


    /**
     * 分页查询菜单列表
     * @param page 分页参数 参数分解
     * @return List<SysMenuEntity> 菜单列表
     */
    List<SysMenuEntity> getList(Page<SysMenuEntity> page);

    /**
     * 根据用户ID查询菜单列表
     * @param sysUserId 用户ID
     * @return List<SysMenuEntity> 菜单列表
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysMenuEntity> getMenuList(Long sysUserId);

    /**
     * 根据用户ID查询权限列表
     * @param sysUserId 用户ID
     * @return Set<String> 权限列表
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysMenuEntity> getPermissions(Long sysUserId);

}
