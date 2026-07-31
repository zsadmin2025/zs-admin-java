package com.zs.lawyer.cases.infoList.domain.excel;

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
 * @since 2025-06-21 12:20:27
 */
@Getter
@Setter
@Schema(description = "案件结案目录Excel对象")
@ExcelIgnoreUnannotated
public class CaseInfoListExcel {

    @ExcelProperty("案件结案目录表ID")
    private Long caseInfoListId;

    @ExcelProperty("案件信息表ID")
    private Long caseInfoId;

    @ExcelProperty("结案目录基础表ID")
    private Long caseListId;

    @ExcelProperty("结案目录文件名称")
    private String caseListFileName;

    @ExcelProperty("结案目录文件url")
    private String caseListFileUrl;

    @ExcelProperty("文件名称")
    private String fileName;

    @ExcelProperty("文件url")
    private String fileUrl;

    @ExcelProperty("创建者")
    private Long creator;

    @ExcelProperty("创建时间")
    private Date createTime;

    @ExcelProperty("更新者")
    private Long updater;

    @ExcelProperty("更新时间")
    private Date updateTime;

}
