package com.zs.sys.tenant.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 租户套餐表
 * </p>
 *
 * @author zs
 * @since 2025-08-07 23:42:40
 */
@Getter
@Setter
@Schema(description = "租户套餐ageQueryParams对象")
public class SysTenantPackagePageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "租户套餐ID")
    private Long sysTenantPackageId;

    @Schema(description = "套餐编码")
    private String packageCode;

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "套餐价格")
    private BigDecimal price;

    @Schema(description = "最大用户数（-1表示无限制）")
    private Integer maxUser;

    @Schema(description = "最大存储空间(GB，-1表示无限制)")
    private Integer maxStorage;

    @Schema(description = "包含功能（JSON格式）")
    private String features;

    @Schema(description = "状态（0-下架，1-正常）")
    private Integer status;

}
