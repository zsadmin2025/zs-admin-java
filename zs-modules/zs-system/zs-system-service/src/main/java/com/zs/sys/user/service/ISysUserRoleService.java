package com.zs.sys.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.sys.user.domain.entity.SysUserRoleEntity;

import java.util.List;

/**
 * @author zsadmin
 */
public interface ISysUserRoleService extends IService<SysUserRoleEntity> {

    /**
     * 保存用户角色关系
     * @param sysUserId 用户id
     * @param sysRoleIdList 角色id列表
     */
    void saveOrUpdate(Long sysUserId, List<Long> sysRoleIdList);

    /**
     * 根据用户id查询角色id列表
     * @param sysUserId 用户id
     * @return 角色id列表
     */
    List<Long> queryRoleIdList(Long sysUserId);

    /**
     * 根据用户id删除用户角色关系
     * @param sysUserId 用户id
     */
    void delByUserId(Long sysUserId);

    /**
     * 根据角色id查询用户id列表
     * @param sysRoleId 角色id
     * @return 用户id列表
     */
    List<Long> queryByRoleId(Long sysRoleId);

    /**
     * 保存用户角色关系
     * @param sysUserRoleEntity 用户角色关系
     */
    void saveUserRole(SysUserRoleEntity sysUserRoleEntity);

    /**
     * 根据角色id列表查询用户id列表
     * @param sysRoleIdList 角色id列表
     * @return 用户id列表
     */
    List<Long> queryUserIdList(List<Long> sysRoleIdList);

}
