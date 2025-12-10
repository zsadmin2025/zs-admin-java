package com.zs.sys.tenant.mapper;

import com.zs.common.mp.base.DataPermissionMapper;
import com.zs.sys.tenant.domain.entity.SysTenantPackageMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 套餐菜单关联表 Mapper 接口
 * </p>
 *
 * @author zs
 * @since 2025-08-20 17:56:11
 */
@Mapper
public interface SysTenantPackageMenuMapper extends DataPermissionMapper<SysTenantPackageMenuEntity> {

}
