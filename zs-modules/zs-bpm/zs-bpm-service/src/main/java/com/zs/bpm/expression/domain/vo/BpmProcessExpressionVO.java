package com.zs.bpm.expression.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "流程表达式VO")
public class BpmProcessExpressionVO implements Serializable {

    @Schema(description = "表达式ID")
    private Long id;

    @Schema(description = "表达式名称")
    private String name;

    @Schema(description = "表达式编码")
    private String code;

    @Schema(description = "表达式内容")
    private String expression;

    @Schema(description = "返回值类型")
    private String returnType;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "创建时间")
    private String createTime;
}
