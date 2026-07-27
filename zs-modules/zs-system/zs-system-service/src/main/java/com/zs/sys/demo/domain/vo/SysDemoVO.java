package com.zs.sys.demo.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
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
@Schema(description = "代码生成测试表VO对象")
public class SysDemoVO implements Serializable {

    @Schema(description = "主键ID")
    private Long sysDemoId;

    @Schema(description = "文本框测试字段")
    private String inputField;

    @Schema(description = "文本域测试字段")
    private String textareaField;

    @Schema(description = "数字框测试字段")
    private BigDecimal numberField;

    @Schema(description = "下拉框测试字段")
    private String selectField;

    @Schema(description = "单选框测试字段")
    private String radioField;

    @Schema(description = "复选框测试字段")
    private String checkboxField;

    @Schema(description = "日期控件测试字段")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dateField;

    @Schema(description = "日期时间控件测试字段")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date datetimeField;

    @Schema(description = "时间控件测试字段")
    @DateTimeFormat(pattern = "HH:mm:ss")
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    private Date timeField;

    @Schema(description = "图片上传测试字段")
    private String imageField;

    @Schema(description = "文件上传测试字段")
    private String uploadField;

    @Schema(description = "富文本测试字段")
    private String editorField;

    @Schema(description = "是否删除")
    private Integer isDelete;

    @Schema(description = "状态")
    private Integer status;

}
