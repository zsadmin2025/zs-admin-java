package com.zs.sys.tenant.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.tenant.domain.entity.SysTenantEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 租户信息表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:45
 */
@Mapper
public interface SysTenantMapper extends DataPermissionMapper<SysTenantEntity> {

}
