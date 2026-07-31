package com.zs.lawyer.cases.infoFiles.domain.excel;

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
 * @since 2025-06-21 09:34:39
 */
@Getter
@Setter
@Schema(description = "案件附件Excel对象")
@ExcelIgnoreUnannotated
public class CaseInfoFilesExcel {

    @ExcelProperty("案件附件表ID")
    private Long caseInfoFilesId;

    @ExcelProperty("案件相关其他表ID")
    private Long caseOtherId;

    @ExcelProperty("附件来源")
    private Integer fileSource;

    @ExcelProperty("文件名称")
    private String fileName;

    @ExcelProperty("文件原始名称")
    private String fileOriginalName;

    @ExcelProperty("文件类型")
    private String fileType;

    @ExcelProperty("文件大小")
    private Long fileSize;

    @ExcelProperty("文件访问url")
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
