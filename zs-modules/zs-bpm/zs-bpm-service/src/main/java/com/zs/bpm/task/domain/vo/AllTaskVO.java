package com.zs.bpm.task.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 流程任务菜单 —— 全部任务统一视图（待办 + 已办）
 * <p>
 * 用于前端「流程任务」菜单，查询系统内所有任务（不限用户），
 * 包含流程单据名称、发起人、任务节点、审批人、审批意见、耗时等核心字段。
 * </p>
 *
 * @author zsadmin
 */
@Data
@Schema(description = "全部任务统一视图")
public class AllTaskVO {

    /* ==================== 流程实例信息 ==================== */

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "流程定义ID")
    private String processDefinitionId;

    @Schema(description = "流程定义Key")
    private String processDefinitionKey;

    @Schema(description = "流程单据名称（流程定义名称）")
    private String processDefinitionName;

    @Schema(description = "流程实例名称/单据标题")
    private String processInstanceName;

    @Schema(description = "业务单号")
    private String businessKey;

    @Schema(description = "流程启动时间")
    private Date processStartTime;

    @Schema(description = "流程结束时间")
    private Date processEndTime;

    @Schema(description = "流程状态：RUNNING / COMPLETED / CANCELLED")
    private String processState;

    /* ==================== 发起人信息 ==================== */

    @Schema(description = "发起人用户ID")
    private Long startUserId;

    @Schema(description = "发起人姓名")
    private String startUserName;

    /* ==================== 任务节点信息 ==================== */

    @Schema(description = "任务ID")
    private String taskId;

    @Schema(description = "当前任务节点名称")
    private String taskName;

    @Schema(description = "任务节点Key")
    private String taskDefinitionKey;

    @Schema(description = "任务开始时间")
    private Date taskStartTime;

    @Schema(description = "任务结束时间（未完成时为null）")
    private Date taskEndTime;

    /* ==================== 审批人信息 ==================== */

    @Schema(description = "审批人用户ID")
    private Long assigneeId;

    @Schema(description = "审批人姓名")
    private String assigneeName;

    /* ==================== 审批状态与意见 ==================== */

    @Schema(description = "审批状态：RUNNING-审批中 / COMPLETED-已完成 / CANCELLED-已取消")
    private String approvalStatus;

    @Schema(description = "审批意见（同一任务多条意见用；拼接）")
    private String comment;

    /* ==================== 耗时 ==================== */

    @Schema(description = "耗时（毫秒），完成后才有值")
    private Long durationInMillis;
}
