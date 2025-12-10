package com.zs.sys.tenant.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 租户套餐关联表
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:43
 */
@Getter
@Setter
@Schema(description = "租户-套餐管理AddParams对象")
public class SysTenantPackageRelAddParams implements Serializable {


    @Schema(description = "租户套餐关联ID")
    private Long sysTenantPackageRelId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "套餐ID")
    private Long packageId;

    @Schema(description = "套餐生效时间")
    private Date startTime;

    @Schema(description = "套餐到期时间")
    private Date endTime;

    @Schema(description = "状态（0-已过期，1-生效中）")
    private Integer status;





}
