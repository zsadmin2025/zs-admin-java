package com.zs.sys.tenant.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

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
@Schema(description = "租户管理electQueryParams对象")
public class SysTenantSelectQueryParams implements Serializable {

    @Schema(description = "租户ID")
    private Long sysTenantId;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "联系邮箱")
    private String contactEmail;

    @Schema(description = "租户类型")
    private Integer type;

    @Schema(description = "状态（0-禁用，1-正常）")
    private Integer status;

    @Schema(description = "过期时间")
    private Date expireTime;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "更新者")
    private Long updater;

}
