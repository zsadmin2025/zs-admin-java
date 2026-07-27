package com.zs.bpm.task.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "流程实例详情全景视图")
public class ProcessDetailVO {

    @Schema(description = "流程定义信息")
    private ProcessDefinitionInfo processDefinition;

    @Schema(description = "流程实例基本信息")
    private ProcessInstanceInfo instanceInfo;

    @Schema(description = "表单基本信息（表单定义+表单数据）")
    private FormInfoVO formInfo;

    @Schema(description = "审批节点列表（按流程顺序排列：开始->审批节点->结束，包含每个节点的审批人、审批时间、审批意见、审批状态）")
    private List<FlowNode> flowNodes;

    @Schema(description = "当前待办任务信息（用户正在处理的任务，含任务ID、名称、办理人、到期时间等）")
    private FlowNode todoTask;

}
