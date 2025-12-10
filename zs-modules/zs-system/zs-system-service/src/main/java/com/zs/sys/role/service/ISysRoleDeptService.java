package com.zs.sys.role.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zs.sys.role.domain.entity.SysRoleDeptEntity;

import java.util.List;

public interface ISysRoleDeptService  extends IService<SysRoleDeptEntity> {

    /**
     * 保存角色与对应得数据权限部门关系
     * @param sysRoleId 角色ID
     * @param deptIds 部门ID集合
     */
    void save(Long sysRoleId, List<Long> deptIds);

    /**
     * 根据角色ID查询对应得数据权限部门ID集合
     * @param sysRoleId 角色ID
     * @return 部门ID集合
     */
    List<Long> getDeptIds(Long sysRoleId);
}
