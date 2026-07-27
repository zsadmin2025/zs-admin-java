package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable BPMN 活动节点类型枚举
 * <p>
 * 对应 {@code HistoricActivityInstance.getActivityType()} 返回值，
 * 即 BPMN 2.0 规范的 XML 元素标签名。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.engine.history.HistoricActivityInstance#getActivityType()
 */
@Getter
@AllArgsConstructor
public enum FlowableActivityTypeEnum {

    // ==================== 事件 ====================
    /** 开始事件 */
    START_EVENT("startEvent", "开始事件"),

    /** 结束事件 */
    END_EVENT("endEvent", "结束事件"),

    /** 边界事件 */
    BOUNDARY_EVENT("boundaryEvent", "边界事件"),

    /** 中间捕获事件 */
    INTERMEDIATE_CATCH_EVENT("intermediateCatchEvent", "中间捕获事件"),

    /** 中间抛出事件 */
    INTERMEDIATE_THROW_EVENT("intermediateThrowEvent", "中间抛出事件"),

    // ==================== 任务 ====================
    /** 用户任务（审批节点） */
    USER_TASK("userTask", "用户任务"),

    /** 服务任务 */
    SERVICE_TASK("serviceTask", "服务任务"),

    /** 发送任务 */
    SEND_TASK("sendTask", "发送任务"),

    /** 接收任务 */
    RECEIVE_TASK("receiveTask", "接收任务"),

    /** 手动任务 */
    MANUAL_TASK("manualTask", "手动任务"),

    /** 业务规则任务 */
    BUSINESS_RULE_TASK("businessRuleTask", "业务规则任务"),

    /** 脚本任务 */
    SCRIPT_TASK("scriptTask", "脚本任务"),

    // ==================== 网关 ====================
    /** 排他网关 */
    EXCLUSIVE_GATEWAY("exclusiveGateway", "排他网关"),

    /** 并行网关 */
    PARALLEL_GATEWAY("parallelGateway", "并行网关"),

    /** 包容网关 */
    INCLUSIVE_GATEWAY("inclusiveGateway", "包容网关"),

    /** 复杂网关 */
    COMPLEX_GATEWAY("complexGateway", "复杂网关"),

    /** 事件网关 */
    EVENT_GATEWAY("eventGateway", "事件网关"),

    /** 事件驱动网关 */
    EVENT_BASED_GATEWAY("eventBasedGateway", "事件驱动网关"),

    // ==================== 子流程/调用 ====================
    /** 调用活动（子流程引用） */
    CALL_ACTIVITY("callActivity", "调用活动"),

    /** 子流程 */
    SUB_PROCESS("subProcess", "子流程"),

    /** 临时子流程 */
    ADHOC_SUB_PROCESS("adhocSubProcess", "临时子流程"),

    /** 事务 */
    TRANSACTION("transaction", "事务"),

    // ==================== 连线 ====================
    /** 顺序流/连线 */
    SEQUENCE_FLOW("sequenceFlow", "顺序流");

    private final String value;
    private final String label;

    public static FlowableActivityTypeEnum of(String value) {
        if (value == null) return null;
        for (FlowableActivityTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }

    /**
     * 判断是否为网关类型
     */
    public boolean isGateway() {
        return this == EXCLUSIVE_GATEWAY
            || this == PARALLEL_GATEWAY
            || this == INCLUSIVE_GATEWAY
            || this == COMPLEX_GATEWAY
            || this == EVENT_GATEWAY
            || this == EVENT_BASED_GATEWAY;
    }

    /**
     * 判断是否为任务类型
     */
    public boolean isTask() {
        return this == USER_TASK
            || this == SERVICE_TASK
            || this == SEND_TASK
            || this == RECEIVE_TASK
            || this == MANUAL_TASK
            || this == BUSINESS_RULE_TASK
            || this == SCRIPT_TASK;
    }

    /**
     * 判断是否为事件类型
     */
    public boolean isEvent() {
        return this == START_EVENT
            || this == END_EVENT
            || this == BOUNDARY_EVENT
            || this == INTERMEDIATE_CATCH_EVENT
            || this == INTERMEDIATE_THROW_EVENT;
    }
}
