package com.zs.bpm.process.domain.vo;

import com.zs.common.core.enums.bpmn.ProcessInstanceStateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 流程实例列表VO（含审批轨迹）
 * <p>
 * 用于管理员查看所有流程实例列表，每条记录包含：
 * 基本信息 + 已完成审批节点轨迹 + 当前活跃节点 + 审批意见/操作记录
 *
 * @author zsadmin
 */
@Data
@Schema(description = "流程实例列表VO")
public class ProcessInstanceVO implements Serializable {

    // ====================== 基本信息 ======================
    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "流程定义ID")
    private String processDefinitionId;

    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

    @Schema(description = "流程定义名称（如：请假申请单）")
    private String processDefinitionName;

    @Schema(description = "流程实例名称/单据标题")
    private String processInstanceName;

    @Schema(description = "业务表单单号/业务主键Key")
    private String businessKey;

    @Schema(description = "发起人用户ID")
    private String startUserId;

    @Schema(description = "发起人用户名")
    private String startUserName;

    @Schema(description = "发起人部门ID")
    private String startDeptId;

    @Schema(description = "发起人部门名称")
    private String startDeptName;

    @Schema(description = "流程启动时间")
    private Date startTime;

    @Schema(description = "流程结束时间（未结束为null）")
    private Date endTime;

    @Schema(description = "流程状态")
    private ProcessInstanceStateEnum status;

    @Schema(description = "总耗时（毫秒）")
    private Long durationInMillis;

    @Schema(description = "当前审批人姓名（多任务时逗号分隔）")
    private String assigneeName;

    @Schema(description = "当前审批任务列表")
    private List<TaskVO> currentTasks;

    // ====================== 审批轨迹 ======================

    // @Schema(description = "流程走过的所有活动节点轨迹（含发起人、审批节点、结束等），每个节点包含审批任务和操作记录")
    // private List<ActivityNodeVO> activityNodes;
}
