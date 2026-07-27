package com.zs.bpm.task.domain.vo;

import com.zs.bpm.process.domain.vo.AssigneeUserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FlowNode {


    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "流程定义ID")
    private String processDefinitionId;

    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

    @Schema(description = "流程定义名称")
    private String processDefinitionName;

    @Schema(description = "流程实例名称/单据标题")
    private String processInstanceName;

    @Schema(description = "任务业务编号")
    private String businessKey;

    @Schema(description = "历史任务ID")
    private String taskId;

    @Schema(description = "节点定义Key")
    private String nodeKey;

    @Schema(description = "审批节点名称")
    private String nodeName;

    @Schema(description = "任务节点描述")
    private String description;

    @Schema(description = "原始处理人")
    private AssigneeUserVO originalAssigneeUser;

    @Schema(description = "当前处理人")
    private AssigneeUserVO assigneeUser;

    @Schema(description = "节点开始时间")
    private Date startTime;

    @Schema(description = "节点完成时间")
    private Date endTime;

    // @Schema(description = "审批意见")
    // private String comment;

    @Schema(description = "耗时（毫秒）")
    private Long durationInMillis;

    @Schema(description = "节点状态")
    private String status;

    @Schema(description = "节点权限配置")
    private NodePermissionConfig permissionConfig;

    @Schema(description = "审批人列表（会签/或签节点可能有多个审批人，每个审批人一条记录；单审批人节点也含1条记录）")
    private List<NodeApprover> approvers;
}
