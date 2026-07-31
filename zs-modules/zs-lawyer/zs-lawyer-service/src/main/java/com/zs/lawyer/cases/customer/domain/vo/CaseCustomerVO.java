package com.zs.lawyer.cases.customer.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 案件客户表
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:55:28
 */
@Getter
@Setter
@Schema(description = "案件客户信息VO对象")
public class CaseCustomerVO implements Serializable {

    @Schema(description = "表id")
    private Long caseCustomerId;

    @Schema(description = "案件id")
    private Long caseInfoId;

    @Schema(description = "客户id")
    private Long customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "客户编号")
    private String customerCode;

    @Schema(description = "客户状态")
    private String customerType;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    private Date updateTime;

}
