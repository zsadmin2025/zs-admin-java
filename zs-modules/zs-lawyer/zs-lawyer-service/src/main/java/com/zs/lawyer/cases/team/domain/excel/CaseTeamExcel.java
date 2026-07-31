package com.zs.lawyer.cases.team.domain.excel;

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
 * @since 2025-06-08 18:01:20
 */
@Getter
@Setter
@Schema(description = "案件团队Excel对象")
@ExcelIgnoreUnannotated
public class CaseTeamExcel {

    @ExcelProperty("")
    private Long caseTeamId;

    @ExcelProperty("关联案件信息表id")
    private Long caseInfoId;

    @ExcelProperty("承接律师")
    private Long undertakeLawyer;

    @ExcelProperty("协接律师")
    private Long coordinatingLawyer;

    @ExcelProperty("主办律师")
    private Long leadLawyer;

    @ExcelProperty("协办人员")
    private Long coOrganizer;

    @ExcelProperty("助理")
    private Long assistant;

    @ExcelProperty("秘书")
    private Long secretary;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

}
