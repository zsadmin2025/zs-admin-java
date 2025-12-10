package com.zs.common.mp.service;



import org.springframework.stereotype.Component;

import java.util.Set;

/**
 */

@Component
public interface MpSysRoleService {

    /**
     * 根据用户ID查询角色数据权限类型集合
     */
    Set<Integer> getDataScope(Long userId);


    /** 根据用户ID查询自定义部门权限ID集合 */
    Set<Long> getRoleDeptIds(Long userId);
}
