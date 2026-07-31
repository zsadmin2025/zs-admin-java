package com.zs.lawyer.cases.info.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 案件信息表
 * </p>
 *
 * @author zs
 * @since 2025-06-08 16:43:20
 */
@Getter
@Setter
@Schema(description = "案件信息electQueryParams对象")
public class CaseInfoSelectQueryParams implements Serializable {

    @Schema(description = "表ID")
    private Long caseInfoId;

    @Schema(description = "案件名称")
    private String caseName;

    @Schema(description = "案件编号")
    private String caseNo;

    @Schema(description = "案件类型")
    private Integer caseType;

    @Schema(description = "代理阶段")
    private String proxyStage;

    @Schema(description = "当前阶段")
    private String nowStage;

    @Schema(description = "诉讼地位")
    private String litigationStatus;

    @Schema(description = "标的金额")
    private BigDecimal subjectAmount;

    @Schema(description = "申请时间")
    private Date applyDate;

    @Schema(description = "申请人")
    private Long applicant;

    @Schema(description = "申请人名称")
    private String applicantName;

    @Schema(description = "案情简介")
    private String caseIntroduction;

    @Schema(description = "委托事宜")
    private String entrustmentMatters;

    @Schema(description = "是否提交纸质合同")
    private Integer isPaperContractSubmitted;

    @Schema(description = "是否提交扫描合同")
    private Integer isScannedContractSubmitted;

    @Schema(description = "所函是否已开")
    private Integer isLetterIssued;


    @Schema(description = "案件状态")
    private Integer caseStatus;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "更新者")
    private Long updater;

}
