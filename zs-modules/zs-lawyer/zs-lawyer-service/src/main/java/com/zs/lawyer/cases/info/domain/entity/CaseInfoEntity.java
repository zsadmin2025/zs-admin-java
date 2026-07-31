package com.zs.lawyer.cases.info.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zs.common.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("case_info")
@Schema(description = "案件信息Entity对象")
public class CaseInfoEntity extends BaseEntity {

    /**  表ID */
    @TableId
    private Long caseInfoId;

    /**  案件名称 */
    private String caseName;

    /**  案件编号 */
    private String caseNo;

    /**  案件类型 */
    private Integer caseType;

    /**  案件类型 */
    private String projectType;

    /**  代理阶段 */
    private String proxyStage;

    /**  当前阶段 */
    private String nowStage;

    /**  诉讼地位 */
    private String litigationStatus;

    /**  标的金额 */
    private BigDecimal subjectAmount;

    /**  申请时间 */
    private Date applyDate;

    /**  申请人 */
    private Long applicant;

    /**  申请人名称 */
    private String applicantName;

    /**  案情简介 */
    private String caseIntroduction;

    /**  委托事宜 */
    private String entrustmentMatters;

    /**  委托权书 */
    private String powerAttorney;

    /** 是否提交纸质合同 */
    private Integer isPaperContractSubmitted;

    /** 是否提交扫描合同 */
    private Integer isScannedContractSubmitted;

    /** 所函是否已开 */
    private Integer isLetterIssued;

    /**  案件状态 */
    private Integer caseStatus;

    /** 是否作废 */
    private Integer isVoided;

    /** 是否审批 */
    private Integer isApprove;

    /** 审批状态 */
    private Integer approveStatus;

    /** 审批人 */
    private Long approvalLawyer;

    /** 审批人名称 */
    private String  approvalLawyerName;

    /** 审批意见 */
    private String  approvalOpinion;

    /** 审批时间 */
    private Date approvalTime;

}
