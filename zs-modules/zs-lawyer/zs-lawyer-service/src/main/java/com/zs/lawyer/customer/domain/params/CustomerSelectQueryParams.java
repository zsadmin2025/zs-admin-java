package com.zs.lawyer.customer.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 客户表
 * </p>
 *
 * @author zs
 * @since 2025-05-29 21:55:59
 */
@Getter
@Setter
@Schema(description = "客户管理electQueryParams对象")
public class CustomerSelectQueryParams implements Serializable {

    @Schema(description = "客户类别")
    private String customerCategory;
    @Schema(description = "客户性质")
    private String customerNature;
    @Schema(description = "客户名称")
    private String customerName;
    @Schema(description = "维系人")
    private String maintainingPeople;
    @Schema(description = "共享人")
    private String sharer;
    @Schema(description = "客户等级")
    private Integer customerGrade;
    @Schema(description = "是否顾问，0-否，1-是")
    private Integer isConsultant;
    @Schema(description = "录入人")
    private Long inputPerson;
    @Schema(description = "行业类别")
    private String industryCategory;
    @Schema(description = "客户状态")
    private Integer status;
}
