package com.zs.sys.tenant.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
@Schema(description = "租户套餐VO对象")
public class SysTenantPackageVO implements Serializable {

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

    @Schema(description = "状态（0-下架，1-正常）")
    private Integer status;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "菜单ID集合")
    private List<Long> menuIdList = List.of();

}
