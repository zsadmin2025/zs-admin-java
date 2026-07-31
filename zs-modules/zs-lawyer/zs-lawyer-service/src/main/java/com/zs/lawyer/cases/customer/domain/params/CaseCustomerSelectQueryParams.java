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
@Schema(description = "案件客户信息electQueryParams对象")
public class CaseCustomerSelectQueryParams implements Serializable {

    @Schema(description = "客户状态")
    private String customerType;

}
