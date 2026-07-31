package com.zs.lawyer.cases.infoApprovalForm.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
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
 * 案件审批表
 * </p>
 *
 * @author zs
 * @since 2025-07-10 07:07:27
 */
@Getter
@Setter
@TableName("case_info_approval_form")
@Schema(description = "案件审批表Entity对象")
public class CaseInfoApprovalFormEntity extends BaseEntity {

    /**   */
    @TableId
    private Long caseInfoApprovalFormId;

    /**  案件表id */
    private Long caseInfoId;

    /**  案件编号 */
    private String caseNo;

    /**  案件类型 */
    private String caseType;

    /**  委托人 */
    private String customerName;

    /**  诉讼地位 */
    private String litigationStatus;

    /**  案由 */
    private String causeAction;

    /**  对方当事人 */
    private String otherSide;

    /**  对方诉讼地位 */
    private String otherLitigationStatus;

    /**  第三方 */
    private String thirdParty;

    /**  是否有利益冲突 */
    private Integer conflictInterest;

    /**  标的金额 */
    private BigDecimal subjectAmount;

    /**  指明律师 */
    private Long indicateLawyer;

    /**  指明律师名称 */
    private String indicateLawyerName;

    /**  指定律师 */
    private Long appointLawyer;

    /**  指定律师名称 */
    private String appointLawyerName;

    /**  受理法院 */
    private String acceptingCourt;

    /**  委托要求 */
    private String entrustRequire;

    /**  委托事项摘要 */
    private String entrustedMattersAbstract;

    /**  承接律师意见 */
    private String undertakeLawyerOpinion;

    /**  收费额 */
    private BigDecimal feeAmount;


    /**  申请人 */
    private Long applicant;

    /**  申请人名称 */
    private String applicantName;

    /**  申请时间 */
    private Date applyTime;

    /**  审批时间 */
    private Date approvalTime;

    /**  审批意见 */
    private String approvalOpinion;

    /**  审批状态 0-拒绝 1-审批中 2-审批通过 */
    private Integer approvalStatus;


    @TableField(exist = false)
    private String caseName;
}
