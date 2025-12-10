package com.zs.sys.tenant.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.tenant.domain.entity.SysTenantPackageRelEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 租户套餐关联表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:43
 */
@Mapper
public interface SysTenantPackageRelMapper extends DataPermissionMapper<SysTenantPackageRelEntity> {

}
