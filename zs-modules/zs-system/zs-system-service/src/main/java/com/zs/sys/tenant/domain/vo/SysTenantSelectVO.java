package com.zs.sys.tenant.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 租户信息表
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:45
 */
@Getter
@Setter
@Schema(description = "租户管理VO对象")
public class SysTenantSelectVO implements Serializable {

    @Schema(description = "租户ID")
    private Long sysTenantId;

    @Schema(description = "租户名称")
    private String tenantName;



}
