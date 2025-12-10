package com.zs.sys.tenant.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

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
@Schema(description = "租户套餐与菜单关联VO对象")
public class SysTenantPackageMenuVO implements Serializable {

    @Schema(description = "套餐菜单关联表ID")
    private Long sysTenantPackageMenuId;

    @Schema(description = "套餐表ID")
    private Long sysTenantPackageId;

    @Schema(description = "菜单表ID")
    private Long sysMenuId;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    private Date updateTime;

}
