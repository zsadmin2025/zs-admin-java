package com.zs.lawyer.cases.contract.domain.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zs.lawyer.cases.infoFiles.domain.params.CaseInfoFilesUpdateParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
@Schema(description = "案件合同updateParams对象")
public class CaseContractUpdateParams implements Serializable {


    @Schema(description = "案件合同表id")
    private Long caseContractId;

    @Schema(description = "案件表id")
    private Long caseInfoId;

    @Schema(description = "开始日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Schema(description = "结束日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Schema(description = "合同金额")
    private BigDecimal contractAmount;

    @Schema(description = "付款方式")
    private String paymentMethod;

    @Schema(description = "付款方式明细")
    private String paymentMethodDetails;

    @Schema(description = "案件合同节点列表")
    private List<CaseContractNodeUpdateParams> contractNodeList = List.of();

    @Schema(description = "合同附件列表")
    private List<CaseInfoFilesUpdateParams> contractFileList = List.of();






}
