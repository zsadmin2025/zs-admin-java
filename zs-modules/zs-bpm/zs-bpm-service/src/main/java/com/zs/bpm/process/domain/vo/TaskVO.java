package com.zs.bpm.process.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.flowable.task.api.Task;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

import java.io.Serializable;
import java.util.Date;

/**
 * 当前审批任务 VO（用于流程实例列表展示）
 *
 * @author zsadmin
 */
@Data
@Schema(description = "当前审批任务")
public class TaskVO implements Serializable {

    @Schema(description = "任务ID")
    private String id;

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "任务描述")
    private String description;

    @Schema(description = "任务定义Key")
    private String taskDefinitionKey;

    @Schema(description = "处理人")
    private String assignee;

    @Schema(description = "处理人姓名")
    private String assigneeName;

    @Schema(description = "负责人")
    private String owner;

    @Schema(description = "任务创建时间")
    private Date createTime;

    @Schema(description = "任务到期时间")
    private Date dueDate;

    @Schema(description = "任务优先级")
    private Integer priority;

    @Schema(description = "任务分类")
    private String category;

    @Schema(description = "表单Key")
    private String formKey;

    @Schema(description = "父任务ID")
    private String parentTaskId;

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "租户ID")
    private String tenantId;

    @Schema(description = "签收人ID")
    private String claimedBy;

    @Schema(description = "签收时间")
    private Date claimTime;

    @Schema(description = "任务状态")
    private String state;

    @Schema(description = "挂起状态 0=激活 1=挂起")
    private Integer suspensionState;

    @Schema(description = "是否已取消")
    private Boolean isCanceled;

    /**
     * 从 Flowable Task 实体转换为 TaskVO
     */
    public static TaskVO convert(Task task) {
        if (task == null) {
            return null;
        }
        TaskVO vo = new TaskVO();

        // 标准接口字段（不触发懒加载）
        vo.setId(task.getId());
        vo.setName(task.getName());
        vo.setDescription(task.getDescription());
        vo.setTaskDefinitionKey(task.getTaskDefinitionKey());
        vo.setAssignee(task.getAssignee());
        vo.setOwner(task.getOwner());
        vo.setCreateTime(task.getCreateTime());
        vo.setDueDate(task.getDueDate());
        vo.setPriority(task.getPriority());
        vo.setCategory(task.getCategory());
        vo.setFormKey(task.getFormKey());
        vo.setParentTaskId(task.getParentTaskId());
        vo.setProcessInstanceId(task.getProcessInstanceId());
        vo.setTenantId(task.getTenantId());

        // TaskEntityImpl 特有字段
        if (task instanceof TaskEntityImpl taskEntity) {
            vo.setClaimedBy(taskEntity.getClaimedBy());
            vo.setClaimTime(taskEntity.getClaimTime());
            vo.setState(taskEntity.getState());
            vo.setSuspensionState(taskEntity.getSuspensionState());
            vo.setIsCanceled(taskEntity.isCanceled());
        }

        return vo;
    }
}
