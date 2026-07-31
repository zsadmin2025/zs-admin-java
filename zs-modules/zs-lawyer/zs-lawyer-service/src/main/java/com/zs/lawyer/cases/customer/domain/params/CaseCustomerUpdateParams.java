package com.zs.lawyer.cases.customer.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@Schema(description = "案件客户信息updateParams对象")
public class CaseCustomerUpdateParams implements Serializable {


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





}
