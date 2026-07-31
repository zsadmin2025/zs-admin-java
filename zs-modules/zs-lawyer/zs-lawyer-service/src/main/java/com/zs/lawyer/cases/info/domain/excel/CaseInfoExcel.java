package com.zs.lawyer.cases.info.domain.excel;

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
 * @since 2025-06-08 16:43:20
 */
@Getter
@Setter
@Schema(description = "案件信息Excel对象")
@ExcelIgnoreUnannotated
public class CaseInfoExcel {

    @ExcelProperty("表ID")
    private Long caseInfoId;

    @ExcelProperty("案件名称")
    private String caseName;

    @ExcelProperty("案件编号")
    private String caseNo;

    @ExcelProperty("案件类型")
    private Integer caseType;

    @ExcelProperty("代理阶段")
    private String proxyStage;

    @ExcelProperty("当前阶段")
    private String nowStage;

    @ExcelProperty("诉讼地位")
    private String litigationStatus;

    @ExcelProperty("标的金额")
    private BigDecimal subjectAmount;

    @ExcelProperty("申请时间")
    private Date applyDate;

    @ExcelProperty("申请人")
    private Long applicant;

    @ExcelProperty("申请人名称")
    private String applicantName;

    @ExcelProperty("案情简介")
    private String caseIntroduction;

    @ExcelProperty("委托事宜")
    private String entrustmentMatters;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

}
