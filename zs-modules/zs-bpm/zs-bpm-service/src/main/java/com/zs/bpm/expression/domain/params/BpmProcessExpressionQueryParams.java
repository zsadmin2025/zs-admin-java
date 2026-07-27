package com.zs.bpm.expression.domain.params;

import com.zs.common.core.page.BasePageParams;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "流程表达式查询参数")
public class BpmProcessExpressionQueryParams extends BasePageParams {

    @Schema(description = "表达式名称")
    private String name;

    @Schema(description = "表达式编码")
    private String code;

    @Schema(description = "返回值类型")
    private String returnType;
}
