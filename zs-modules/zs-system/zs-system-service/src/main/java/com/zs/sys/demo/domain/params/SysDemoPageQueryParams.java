package com.zs.sys.demo.domain.params;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zs.common.core.page.BasePageParams;
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
@Schema(description = "代码生成测试表ageQueryParams对象")
public class SysDemoPageQueryParams  extends BasePageParams implements Serializable {

    @Schema(description = "主键ID")
    private Long sysDemoId;

    @Schema(description = "文本框测试字段")
    private String inputField;

    @Schema(description = "数字框测试字段")
    private BigDecimal numberField;

    @Schema(description = "日期时间控件测试字段开始值")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date datetimeFieldStart;

    @Schema(description = "日期时间控件测试字段结束值")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date datetimeFieldEnd;

    @Schema(description = "是否删除")
    private Integer isDelete;

    @Schema(description = "状态")
    private Integer status;

}
