package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 任务删除原因枚举
 * <p>
 * 对应 {@code TaskEntityImpl} 中定义的 DELETE_REASON_* 常量，
 * 即 {@code HistoricTaskInstance.getDeleteReason()} 的返回值。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.task.service.impl.persistence.entity.TaskEntityImpl#DELETE_REASON_COMPLETED
 * @see org.flowable.task.service.impl.persistence.entity.TaskEntityImpl#DELETE_REASON_DELETED
 */
@Getter
@AllArgsConstructor
public enum FlowableTaskDeleteReasonEnum {

    /**
     * 正常完成
     */
    COMPLETED("completed", "正常完成"),

    /**
     * 被删除
     */
    DELETED("deleted", "已删除");

    private final String value;
    private final String label;

    public static FlowableTaskDeleteReasonEnum of(String value) {
        if (value == null) return null;
        for (FlowableTaskDeleteReasonEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }
}
