package com.zs.sys.user.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zs.common.mp.annotation.DataScope;
import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.user.domain.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zsadmin
 */
@Mapper
public interface SysUserMapper extends DataPermissionMapper<SysUserEntity> {

    @DataScope
    IPage<SysUserEntity> page(Page<SysUserEntity> page, @Param("params") Map<String, Object> params);

    void updateDeleted(Long sysUserId);

    List<SysUserEntity> getList(@Param("params") Map<String, Object> params);

    SysUserEntity selectByUserName(String userName);

    SysUserEntity selectByUserNameAndTenant(@Param("username") String username, @Param("tenantId") String tenantId);

    List<SysUserEntity> getUserList(List<Long> sysUserIds);

}
