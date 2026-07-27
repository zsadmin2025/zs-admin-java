package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 流程实例回调状态枚举
 * <p>
 * 对应 {@code org.flowable.engine.impl.runtime.callback.ProcessInstanceState} 接口常量，
 * 用于流程实例状态变更回调（小写风格，与 Flowable 引擎内部保持一致）。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.engine.impl.runtime.callback.ProcessInstanceState
 */
@Getter
@AllArgsConstructor
public enum ProcessInstanceStateEnum {

    /** 运行中 */
    RUNNING("running", "运行中"),

    /** 已完成 */
    COMPLETED("completed", "已完成"),

    /** 已取消 */
    CANCELLED("cancelled", "已取消");

    private final String value;
    private final String label;

    public static ProcessInstanceStateEnum of(String value) {
        if (value == null) return null;
        for (ProcessInstanceStateEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }

    public boolean isEnd() {
        return this == COMPLETED || this == CANCELLED;
    }
}
