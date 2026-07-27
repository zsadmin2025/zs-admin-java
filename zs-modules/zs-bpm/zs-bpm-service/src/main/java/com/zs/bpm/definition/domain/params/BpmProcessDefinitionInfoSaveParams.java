package com.zs.bpm.definition.domain.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 流程定义信息保存参数
 *
 * @author zsadmin
 */
@Data
@Schema(description = "流程定义信息保存参数")
public class BpmProcessDefinitionInfoSaveParams {

    @Schema(description = "主键ID（更新时必填）")
    private Long id;

    @Schema(description = "流程名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String processName;

    @Schema(description = "流程定义Key", requiredMode = Schema.RequiredMode.REQUIRED)
    private String processKey;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "表单类型")
    private Integer formType;

    @Schema(description = "表单ID")
    private Long formId;

    @Schema(description = "表单规则")
    private String formRule;

    @Schema(description = "表单选项")
    private String formOption;

    @Schema(description = "模型JSON")
    private String modelJson;

    @Schema(description = "BPMN XML")
    private String bpmnXml;

    @Schema(description = "状态")
    private Integer status;
}
