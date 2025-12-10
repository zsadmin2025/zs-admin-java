package com.zs.sys.role.mapper;


import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.role.domain.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author zsadmin
 */
@Mapper
public interface SysRoleMapper extends DataPermissionMapper<SysRoleEntity> {


    List<SysRoleEntity> getList(Long userId);

    Set<Long> getDataScopeDeptIds(@Param("userId") Long userId);
}
