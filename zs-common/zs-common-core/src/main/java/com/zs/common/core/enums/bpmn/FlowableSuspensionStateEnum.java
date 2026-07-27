package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 挂起状态枚举
 * <p>
 * 对应 {@code org.flowable.common.engine.impl.db.SuspensionState} 接口常量，
 * 适用于 ProcessDefinition / ProcessInstance / Task 的 suspensionState 字段。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.common.engine.impl.db.SuspensionState
 */
@Getter
@AllArgsConstructor
public enum FlowableSuspensionStateEnum {

    /**
     * 正常激活
     */
    ACTIVE(1, "激活"),

    /**
     * 挂起冻结
     */
    SUSPENDED(2, "挂起");

    private final Integer value;
    private final String label;

    public static FlowableSuspensionStateEnum of(Integer value) {
        if (value == null) return null;
        for (FlowableSuspensionStateEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }

    /**
     * 根据 stateCode 匹配
     */
    public static FlowableSuspensionStateEnum ofCode(int stateCode) {
        for (FlowableSuspensionStateEnum e : values()) {
            if (e.value == stateCode) return e;
        }
        return null;
    }
}
