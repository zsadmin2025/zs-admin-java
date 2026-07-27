package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 历史任务日志类型枚举
 * <p>
 * 对应 {@code org.flowable.task.api.history.HistoricTaskLogEntryType} 枚举，
 * 记录任务生命周期中的各类变更事件。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.task.api.history.HistoricTaskLogEntryType
 */
@Getter
@AllArgsConstructor
public enum FlowableTaskLogEntryTypeEnum {

    /** 任务完成 */
    USER_TASK_COMPLETED("USER_TASK_COMPLETED", "任务完成"),

    /** 处理人变更 */
    USER_TASK_ASSIGNEE_CHANGED("USER_TASK_ASSIGNEE_CHANGED", "处理人变更"),

    /** 任务创建 */
    USER_TASK_CREATED("USER_TASK_CREATED", "任务创建"),

    /** 负责人变更 */
    USER_TASK_OWNER_CHANGED("USER_TASK_OWNER_CHANGED", "负责人变更"),

    /** 优先级变更 */
    USER_TASK_PRIORITY_CHANGED("USER_TASK_PRIORITY_CHANGED", "优先级变更"),

    /** 截止时间变更 */
    USER_TASK_DUEDATE_CHANGED("USER_TASK_DUEDATE_CHANGED", "截止时间变更"),

    /** 任务名变更 */
    USER_TASK_NAME_CHANGED("USER_TASK_NAME_CHANGED", "任务名变更"),

    /** 挂起状态变更 */
    USER_TASK_SUSPENSIONSTATE_CHANGED("USER_TASK_SUSPENSIONSTATE_CHANGED", "挂起状态变更"),

    /** 身份链接新增（添加候选人/候选组） */
    USER_TASK_IDENTITY_LINK_ADDED("USER_TASK_IDENTITY_LINK_ADDED", "身份链接新增"),

    /** 身份链接移除 */
    USER_TASK_IDENTITY_LINK_REMOVED("USER_TASK_IDENTITY_LINK_REMOVED", "身份链接移除");

    private final String value;
    private final String label;

    public static FlowableTaskLogEntryTypeEnum of(String value) {
        if (value == null) return null;
        for (FlowableTaskLogEntryTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }
}
