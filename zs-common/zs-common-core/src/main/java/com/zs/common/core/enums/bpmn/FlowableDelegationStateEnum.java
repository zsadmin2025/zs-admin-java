package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 任务委托状态枚举
 * <p>
 * 对应 {@code org.flowable.task.api.DelegationState} 枚举，
 * 描述任务在委托代办生命周期中的状态。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.task.api.DelegationState
 */
@Getter
@AllArgsConstructor
public enum FlowableDelegationStateEnum {

    /**
     * 已委托待办：owner 将任务委托给 assignee，等待 assignee 处理完成归还
     */
    PENDING("PENDING", "已委托待办"),

    /**
     * 代办完成已归还：assignee 已处理完毕，owner 重新成为 assignee 可 review 后完成
     */
    RESOLVED("RESOLVED", "代办完成已归还");

    private final String value;
    private final String label;

    public static FlowableDelegationStateEnum of(String value) {
        if (value == null) return null;
        for (FlowableDelegationStateEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }
}
