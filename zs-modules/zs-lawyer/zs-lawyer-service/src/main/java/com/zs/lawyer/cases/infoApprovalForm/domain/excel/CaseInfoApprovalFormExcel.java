package com.zs.lawyer.cases.infoApprovalForm.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
/**
 * <p>
 * $!{table.comment}
 * </p>
 *
 * @author zs
 * @since 2025-07-10 07:07:27
 */
@Getter
@Setter
@Schema(description = "案件审批表Excel对象")
@ExcelIgnoreUnannotated
public class CaseInfoApprovalFormExcel {

    @ExcelProperty("")
    private Long caseInfoApprovalFormId;

    @ExcelProperty("案件表id")
    private Long caseInfoId;

    @ExcelProperty("案件编号")
    private String caseNo;

    @ExcelProperty("委托人")
    private String customerName;

    @ExcelProperty("诉讼地位")
    private String litigationStatus;

    @ExcelProperty("案由")
    private String causeAction;

    @ExcelProperty("对方当事人")
    private String otherSide;

    @ExcelProperty("对方诉讼地位")
    private String otherLitigationStatus;

    @ExcelProperty("第三方")
    private String thirdParty;

    @ExcelProperty("是否有利益冲突")
    private Integer conflictInterest;

    @ExcelProperty("标的金额")
    private BigDecimal subjectAmount;

    @ExcelProperty("指明律师")
    private Long indicateLawyer;

    @ExcelProperty("指明律师名称")
    private String indicateLawyerName;

    @ExcelProperty("指定律师")
    private Long appointLawyer;

    @ExcelProperty("指定律师名称")
    private String appointLawyerName;

    @ExcelProperty("受理法院")
    private String acceptingCourt;

    @ExcelProperty("委托要求")
    private String entrustRequire;

    @ExcelProperty("委托事项摘要")
    private String entrustedMattersAbstract;

    @ExcelProperty("承接律师意见")
    private String undertakeLawyerOpinion;

    @ExcelProperty("收费额")
    private BigDecimal feeAmount;

    @ExcelProperty("审批意见")
    private String approvalOpinion;

    @ExcelProperty("申请人")
    private Long applicant;

    @ExcelProperty("申请人名称")
    private String applicantName;

    @ExcelProperty("审批状态 0-拒绝 1-审批中 2-审批通过")
    private Integer approvalStatus;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

}
