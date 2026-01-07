package com.zs.sys.demo.domain.params;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;

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
@Schema(description = "代码生成测试表updateParams对象")
public class SysDemoUpdateParams implements Serializable {

    @Schema(description = "主键ID")
    @NotNull(message = "主键ID不能为空")
    private Long sysDemoId;

    @Schema(description = "文本框测试字段")
    @Size(max = 255, message = "文本框测试字段长度不能超过255")
    private String inputField;

    @Schema(description = "文本域测试字段")
    @Size(max = 65535, message = "文本域测试字段长度不能超过65,535")
    private String textareaField;

    @Schema(description = "数字框测试字段")
    private BigDecimal numberField;

    @Schema(description = "下拉框测试字段")
    @Size(max = 50, message = "下拉框测试字段长度不能超过50")
    private String selectField;

    @Schema(description = "单选框测试字段")
    @Size(max = 1, message = "单选框测试字段长度不能超过1")
    private String radioField;

    @Schema(description = "复选框测试字段")
    @Size(max = 255, message = "复选框测试字段长度不能超过255")
    private String checkboxField;

    @Schema(description = "日期控件测试字段")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date dateField;

    @Schema(description = "日期时间控件测试字段")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date datetimeField;

    @Schema(description = "时间控件测试字段")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeField;

    @Schema(description = "图片上传测试字段")
    @Size(max = 255, message = "图片上传测试字段长度不能超过255")
    private String imageField;

    @Schema(description = "文件上传测试字段")
    @Size(max = 255, message = "文件上传测试字段长度不能超过255")
    private String uploadField;

    @Schema(description = "富文本测试字段")
    private String editorField;

    @Schema(description = "是否删除")
    private Integer isDelete;

    @Schema(description = "状态")
    private Integer status;







}
