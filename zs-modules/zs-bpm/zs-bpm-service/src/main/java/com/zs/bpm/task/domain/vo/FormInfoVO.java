package com.zs.bpm.task.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
@Schema(description = "表单基本信息")
public class FormInfoVO {

    @Schema(description = "表单ID")
    private Long formId;

    @Schema(description = "表单名称")
    private String formName;

    @Schema(description = "表单类型（1-动态表单 2-业务表单）")
    private String formType;

    @Schema(description = "表单字段配置JSON")
    private String formRule;

    @Schema(description = "表单全局配置JSON")
    private String formOption;

    @Schema(description = "流程变量数据（表单填写的值）")
    private Map<String, Object> formData;

    @Schema(description = "流程定义Key")
    private String processKey;

    @Schema(description = "流程定义名称")
    private String processName;

    @Schema(description = "流程版本号")
    private Integer version;

}