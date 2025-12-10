package com.zs.sys.tenant.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 套餐菜单关联表
 * </p>
 *
 * @author zs
 * @since 2025-08-20 17:56:11
 */
@Getter
@Setter
@TableName("sys_tenant_package_menu")
@Schema(description = "租户套餐与菜单关联Entity对象")
public class SysTenantPackageMenuEntity extends BaseEntity {

    /**  套餐菜单关联表ID */
    @TableId
    private Long sysTenantPackageMenuId;

    /**  套餐表ID */
    private Long sysTenantPackageId;

    /**  菜单表ID */
    private Long sysMenuId;




}
