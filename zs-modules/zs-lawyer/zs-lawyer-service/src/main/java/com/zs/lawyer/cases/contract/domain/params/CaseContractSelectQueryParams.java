package com.zs.lawyer.cases.contract.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 案件合同
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:02:46
 */
@Getter
@Setter
@Schema(description = "案件合同electQueryParams对象")
public class CaseContractSelectQueryParams implements Serializable {

    @Schema(description = "案件合同表id")
    private Long caseContractId;

    @Schema(description = "案件表id")
    private Long caseInfoId;

    @Schema(description = "开始日期")
    private Date startDate;

    @Schema(description = "结束日期")
    private Date endDate;

    @Schema(description = "合同金额")
    private BigDecimal contractAmount;

    @Schema(description = "付款方式")
    private String paymentMethod;

    @Schema(description = "付款方式明细")
    private String paymentMethodDetails;

}
