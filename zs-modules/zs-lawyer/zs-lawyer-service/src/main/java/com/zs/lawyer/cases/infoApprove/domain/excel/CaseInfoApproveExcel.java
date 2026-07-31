package com.zs.lawyer.cases.infoApprove.domain.excel;

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
 * @since 2025-06-30 09:04:42
 */
@Getter
@Setter
@Schema(description = "案件审批Excel对象")
@ExcelIgnoreUnannotated
public class CaseInfoApproveExcel {

    @ExcelProperty("案件审批表ID")
    private Long caseInfoApproveId;

    @ExcelProperty("案件表ID")
    private Long caseInfoId;

    @ExcelProperty("待审批律师")
    private Long approvalLawyer;

    @ExcelProperty("审批状态 0-拒绝 1-审批中 2-通过")
    private Integer approveStatus;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

}
