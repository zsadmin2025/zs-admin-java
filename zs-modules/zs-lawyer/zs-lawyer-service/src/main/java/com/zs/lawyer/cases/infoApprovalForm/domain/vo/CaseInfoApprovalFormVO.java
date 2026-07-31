package com.zs.lawyer.cases.infoApprovalForm.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 案件审批表
 * </p>
 *
 * @author zs
 * @since 2025-07-10 07:07:27
 */
@Getter
@Setter
@Schema(description = "案件审批表VO对象")
public class CaseInfoApprovalFormVO implements Serializable {

    @Schema(description = "")
    private Long caseInfoApprovalFormId;

    @Schema(description = "案件表id")
    private Long caseInfoId;

    @Schema(description = "案件名称")
    private String caseName;

    @Schema(description = "案件编号")
    private String caseNo;

    @Schema(description = "案件类型")
    private String caseType;

    @Schema(description = "委托人")
    private String customerName;

    @Schema(description = "诉讼地位")
    private String litigationStatus;

    @Schema(description = "案由")
    private String causeAction;

    @Schema(description = "对方当事人")
    private String otherSide;

    @Schema(description = "对方诉讼地位")
    private String otherLitigationStatus;

    @Schema(description = "第三方")
    private String thirdParty;

    @Schema(description = "是否有利益冲突")
    private Integer conflictInterest;

    @Schema(description = "标的金额")
    private BigDecimal subjectAmount;

    @Schema(description = "指明律师")
    private Long indicateLawyer;

    @Schema(description = "指明律师名称")
    private String indicateLawyerName;

    @Schema(description = "指定律师")
    private Long appointLawyer;

    @Schema(description = "指定律师名称")
    private String appointLawyerName;

    @Schema(description = "受理法院")
    private String acceptingCourt;

    @Schema(description = "委托要求")
    private String entrustRequire;

    @Schema(description = "委托事项摘要")
    private String entrustedMattersAbstract;

    @Schema(description = "承接律师意见")
    private String undertakeLawyerOpinion;

    @Schema(description = "收费额")
    private BigDecimal feeAmount;

    @Schema(description = "审批意见")
    private String approvalOpinion;

    @Schema(description = "申请人")
    private Long applicant;

    @Schema(description = "申请人名称")
    private String applicantName;

    @Schema(description = "申请时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date applyTime;

    @Schema(description = "审批状态 0-拒绝 1-审批中 2-审批通过")
    private Integer approvalStatus;

    @Schema(description = "审批时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date approvalTime;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    private Date updateTime;

}
