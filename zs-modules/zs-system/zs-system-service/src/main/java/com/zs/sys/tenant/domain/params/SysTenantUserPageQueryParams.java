package com.zs.sys.tenant.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 租户用户关联表
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:38
 */
@Getter
@Setter
@Schema(description = "租户用户ageQueryParams对象")
public class SysTenantUserPageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "租户用户ID")
    private Long sysTenantUserId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户类型（0-普通用户，1-租户管理员）")
    private Long userType;

    @Schema(description = "加入租户时间")
    private Date joinTime;

    @Schema(description = "状态（0-禁用，1-正常）")
    private Integer status;

}
