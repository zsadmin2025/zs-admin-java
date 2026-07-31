package com.zs.lawyer.cases.hearing.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
/**
 * <p>
 * $!{table.comment}
 * </p>
 *
 * @author zs
 * @since 2025-06-08 17:58:57
 */
@Getter
@Setter
@Schema(description = "案件开庭信息Excel对象")
@ExcelIgnoreUnannotated
public class CaseHearingExcel {

    @ExcelProperty("表ID")
    private Long caseHearing;

    @ExcelProperty("管理的案件信息表")
    private Long caseInfoId;

    @ExcelProperty("法院受理日期")
    private Date courtAcceptDate;

    @ExcelProperty("审理程序")
    private String hearingProcedure;

    @ExcelProperty("开庭律师")
    private String courtLawyer;

    @ExcelProperty("法院/仲裁委员会")
    private String court;

    @ExcelProperty("法院案号")
    private String courtCaseNumber;

    @ExcelProperty("法官")
    private String judge;

    @ExcelProperty("法官电话")
    private String judgePhone;

    @ExcelProperty("书记员")
    private String courtClerk;

    @ExcelProperty("书记员电话")
    private String courtClerkPhone;

    @ExcelProperty("判决结果")
    private String judgmentResult;

    @ExcelProperty("公告送达日期")
    private Date serviceByPublicationDate;

    @ExcelProperty("判决签发日期")
    private Date judgmentIssuedDate;

    @ExcelProperty("判决签收日期")
    private Date judgmentSignDate;

    @ExcelProperty("判决生效日期")
    private Date judgmentTakesEffect;

    @ExcelProperty("判决内容")
    private String judgmentContent;

    @ExcelProperty("跟进情况")
    private String followUpSituation;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

}
