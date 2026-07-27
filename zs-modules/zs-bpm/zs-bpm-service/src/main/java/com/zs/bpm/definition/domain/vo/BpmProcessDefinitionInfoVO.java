package com.zs.bpm.definition.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 流程定义信息VO
 *
 * @author zsadmin
 */
@Data
@Schema(description = "流程定义信息VO")
public class BpmProcessDefinitionInfoVO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "流程定义ID")
    private String processDefinitionId;

    @Schema(description = "部署ID")
    private String deploymentId;

    @Schema(description = "流程模型ID")
    private String modelId;

    @Schema(description = "流程定义Key")
    private String processKey;

    @Schema(description = "流程名称")
    private String processName;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "版本")
    private Integer version;


    @Schema(description = "表单类型，1-动态表单、2-业务表单")
    private Integer formType;

    @Schema(description = "表单ID")
    private Long formId;

    @Schema(description = "表单规则")
    private String formRule;

    @Schema(description = "表单选项")
    private String formOption;

    @Schema(description = "BPMN模型JSON")
    private String modelJson;

    @Schema(description = "BPMN XML")
    private String bpmnXml;

    @Schema(description = "状态: 0=禁用,1=已启用")
    private Integer status;

    @Schema(description = "发布时间")
    private Date publishTime;

    @Schema(description = "部署时间")
    private Date deployTime;

    @Schema(description = "启用时间")
    private Date activateTime;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "创建时间")
    private String createTime;
}
