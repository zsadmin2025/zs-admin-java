package com.zs.sys.menu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.page.PageResult;
import com.zs.sys.menu.domain.entity.SysMenuEntity;
import com.zs.sys.menu.domain.params.*;
import com.zs.sys.menu.domain.vo.SysMenuBtnPermissionsVO;
import com.zs.sys.menu.domain.vo.SysMenuListVO;
import com.zs.sys.menu.domain.vo.SysMenuVO;

import java.util.List;
import java.util.Set;

/**
 * @author zsadmin
 */
public interface ISysMenuService extends IService<SysMenuEntity> {

    /**
     * 分页查询
     * @param sysMenuQueryParams 查询参数实体
     * @return 分页结果
     */
    PageResult<SysMenuVO> page(SysMenuQueryParams sysMenuQueryParams);

    /**
     * 获取菜单导航
     * @return List<SysMenuVO> 菜单导航
     */
    List<SysMenuVO> getNavList();

    /**
     * 获取菜单列表
     * @param sysMenuQueryParams 查询参数实体
     * @return List<SysMenuVO> 菜单列表
     */
    List<SysMenuListVO> getList(SysMenuQueryParams sysMenuQueryParams);

    /**
     * 获取菜单列表
     * @param sysMenuQueryParams 查询参数实体
     * @return List<SysMenuVO> 菜单列表
     */
    List<SysMenuListVO> listPermission(SysMenuQueryParams sysMenuQueryParams);
    /**
     * 新增菜单
     * @param sysMenuAddParams 新增参数实体
     */
    void save(SysMenuAddParams sysMenuAddParams);

    /**
     * 修改菜单
     * @param sysMenuUpdateParams 修改参数实体
     */
    void update(SysMenuUpdateParams sysMenuUpdateParams);

    /**
     * 删除菜单
     * @param id 菜单ID
     * @throws ZsException 如果包含子菜单，则抛出异常。
     */
    void delete(Long id);

    /**
     * 根据ID获取菜单信息
     * @param id 菜单ID
     * @return SysMenuVO 菜单信息
     */
    SysMenuListVO getById(Long id);

    /**
     * 获取所有权限
     * @return Set<String> 权限集合
     */
    Set<String> getAllPermissions();

    /**
     * 根据用户ID获取用户权限标识集合
     * @param sysUserId 用户ID
     * @return Set<String> 权限集合
     */
    Set<String> getPermissions(Long sysUserId);

    /**
     * 根据菜单ID获取按钮权限
     * @param sysMenuId 菜单ID
     * @return List<SysMenuBtnPermissionsVO> 按钮权限集合
     */
    List<SysMenuBtnPermissionsVO> getPermissionsBySysMenuId(Long sysMenuId);

    /**
     * 新增菜单权限按钮
     * @param sysMenuBtnPermissionsAddParams 新增参数实体
     */
    void saveBtnPermissions(SysMenuBtnPermissionsAddParams sysMenuBtnPermissionsAddParams);

    /**
     * 修改菜单权限按钮
     * @param sysMenuBtnPermissionsUpdateParams 修改参数实体
     */
    void updateBtnPermissions(SysMenuBtnPermissionsUpdateParams sysMenuBtnPermissionsUpdateParams);

    /**
     * 根据菜单ID获取菜单权限按钮
     * @param sysMenuId 菜单ID
     * @return SysMenuBtnPermissionsVO 按钮权限
     */
    SysMenuBtnPermissionsVO getBtnPermissionsBySysMenuId(Long sysMenuId);

    /**
     * 删除菜单权限按钮
     * @param sysMenuId 菜单ID
     */
    void deleteBtnPermissions(Long sysMenuId);
}
