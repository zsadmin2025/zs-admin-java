package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 流程实例删除原因枚举
 * <p>
 * 对应 {@code org.flowable.engine.history.DeleteReason} 接口常量，
 * 即 {@code HistoricProcessInstance.getDeleteReason()} / {@code HistoricActivityInstance.getDeleteReason()} 的返回值。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.engine.history.DeleteReason
 */
@Getter
@AllArgsConstructor
public enum FlowableDeleteReasonEnum {

    /** 流程实例被删除 */
    PROCESS_INSTANCE_DELETED("process instance deleted", "流程实例删除"),

    /** 终止结束事件触发 */
    TERMINATE_END_EVENT("terminate end event", "终止结束事件"),

    /** 边界中断事件触发 */
    BOUNDARY_EVENT_INTERRUPTING("boundary event", "边界中断事件"),

    /** 事件子流程中断触发 */
    EVENT_SUBPROCESS_INTERRUPTING("event subprocess", "事件子流程中断"),

    /** 事件网关取消 */
    EVENT_BASED_GATEWAY_CANCEL("event based gateway cancel", "事件网关取消"),

    /** 事务取消 */
    TRANSACTION_CANCELED("transaction canceled", "事务取消");

    private final String value;
    private final String label;

    public static FlowableDeleteReasonEnum of(String value) {
        if (value == null) return null;
        for (FlowableDeleteReasonEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }
}
