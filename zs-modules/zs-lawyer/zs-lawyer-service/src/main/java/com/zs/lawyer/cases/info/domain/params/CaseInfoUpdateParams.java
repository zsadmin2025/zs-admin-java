package com.zs.lawyer.cases.info.domain.params;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 案件信息表
 * </p>
 *
 * @author zs
 * @since 2025-06-08 16:43:20
 */
@Getter
@Setter
@Schema(description = "案件信息updateParams对象")
public class CaseInfoUpdateParams implements Serializable {


    @Schema(description = "表ID")
    private Long caseInfoId;

    @Schema(description = "案件名称")
    private String caseName;

    @Schema(description = "案件编号")
    private String caseNo;

    @Schema(description = "案件类型")
    private String caseType;

    @Schema(description = "立项类型")
    private String projectType;

    @Schema(description = "代理阶段")
    private List<String> proxyStage;

    @Schema(description = "当前阶段")
    private String nowStage;

    @Schema(description = "诉讼地位")
    private String litigationStatus;

    @Schema(description = "标的金额")
    private BigDecimal subjectAmount;

    @Schema(description = "申请时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date applyDate;

    @Schema(description = "申请人")
    private Long applicant;

    @Schema(description = "申请人名称")
    private String applicantName;

    @Schema(description = "案情简介")
    private String caseIntroduction;

    @Schema(description = "委托事宜")
    private String entrustmentMatters;

    @Schema(description = "委托授权书")
    private String powerAttorney;

    @Schema(description = "是否提交纸质合同")
    private Integer isPaperContractSubmitted;

    @Schema(description = "是否提交扫描合同")
    private Integer isScannedContractSubmitted;

    @Schema(description = "所函是否已开")
    private Integer isLetterIssued;


    @Schema(description = "案件状态")
    private Integer caseStatus;

    private List<CaseRelatedPartiesUpdateParams> ourSide;

    private List<CaseRelatedPartiesUpdateParams> otherSide;


}
