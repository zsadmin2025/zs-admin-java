package com.zs.sys.demo.domain.excel;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 代码生成测试表
 * </p>
 *
 * @author zs
 * @date 2026-01-07 11:01:19
 */
@Getter
@Setter
@Schema(description = "代码生成测试表Excel对象")
@ExcelIgnoreUnannotated
public class SysDemoExcel {

    @ExcelProperty("主键ID")
    private Long sysDemoId;

    @ExcelProperty("文本框测试字段")
    private String inputField;

    @ExcelProperty("文本域测试字段")
    private String textareaField;

    @ExcelProperty("数字框测试字段")
    private BigDecimal numberField;

    @ExcelProperty("下拉框测试字段")
    private String selectField;

    @ExcelProperty("单选框测试字段")
    private String radioField;

    @ExcelProperty("复选框测试字段")
    private String checkboxField;

    @ExcelProperty("日期控件测试字段")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dateField;

    @ExcelProperty("日期时间控件测试字段")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date datetimeField;

    @ExcelProperty("时间控件测试字段")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date timeField;

    @ExcelProperty("图片上传测试字段")
    private String imageField;

    @ExcelProperty("文件上传测试字段")
    private String uploadField;

    @ExcelProperty("富文本测试字段")
    private String editorField;

    @ExcelProperty("是否删除")
    private Integer isDelete;

    @ExcelProperty("状态")
    private Integer status;

}
