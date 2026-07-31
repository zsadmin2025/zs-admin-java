package com.zs.lawyer.cases.contract.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 案件合同节点
 * </p>
 *
 * @author zs
 * @since 2025-06-08 18:03:56
 */
@Getter
@Setter
@Schema(description = "案件合同节点VO对象")
public class CaseContractNodeVO implements Serializable {

    @Schema(description = "表id")
    private Long caseContractNodeId;

    @Schema(description = "关联案件表id")
    private Long caseInfoId;

    @Schema(description = "关联合同表id")
    private Long caseContractId;

    @Schema(description = "款项类别(字典paymentCategory)")
    private String paymentCategory;

    @Schema(description = "款项名称")
    private String paymentName;

    @Schema(description = "应收金额")
    private BigDecimal receivableAmount;

    @Schema(description = "预计收款时间")
    private Date expectedCollectionDate;

    @Schema(description = "收款条件")
    private String paymentTerms;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    private Date updateTime;

}
