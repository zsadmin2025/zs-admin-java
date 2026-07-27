package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 引擎事件类型枚举
 * <p>
 * 对应 {@code org.flowable.common.engine.api.delegate.event.FlowableEngineEventType} 枚举，
 * 覆盖流程实例、活动节点、任务、Job 等维度的关键事件类型。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.common.engine.api.delegate.event.FlowableEngineEventType
 */
@Getter
@AllArgsConstructor
public enum FlowableEngineEventTypeEnum {

    // ==================== 流程实例 ====================
    /** 流程实例已创建 */
    PROCESS_CREATED("PROCESS_CREATED", "流程创建"),

    /** 流程实例已启动 */
    PROCESS_STARTED("PROCESS_STARTED", "流程启动"),

    /** 流程正常完成 */
    PROCESS_COMPLETED("PROCESS_COMPLETED", "流程完成"),

    /** 流程通过终止结束事件完成 */
    PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT("PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT", "流程终止结束"),

    /** 流程通过错误结束事件完成 */
    PROCESS_COMPLETED_WITH_ERROR_END_EVENT("PROCESS_COMPLETED_WITH_ERROR_END_EVENT", "流程错误结束"),

    /** 流程通过升级结束事件完成 */
    PROCESS_COMPLETED_WITH_ESCALATION_END_EVENT("PROCESS_COMPLETED_WITH_ESCALATION_END_EVENT", "流程升级结束"),

    /** 流程已取消 */
    PROCESS_CANCELLED("PROCESS_CANCELLED", "流程取消"),

    // ==================== 活动节点 ====================
    /** 活动开始执行 */
    ACTIVITY_STARTED("ACTIVITY_STARTED", "活动开始"),

    /** 活动完成 */
    ACTIVITY_COMPLETED("ACTIVITY_COMPLETED", "活动完成"),

    /** 活动被取消 */
    ACTIVITY_CANCELLED("ACTIVITY_CANCELLED", "活动取消"),

    /** 活动等待信号 */
    ACTIVITY_SIGNAL_WAITING("ACTIVITY_SIGNAL_WAITING", "活动等待信号"),

    /** 活动收到信号 */
    ACTIVITY_SIGNALED("ACTIVITY_SIGNALED", "活动收到信号"),

    /** 活动等待消息 */
    ACTIVITY_MESSAGE_WAITING("ACTIVITY_MESSAGE_WAITING", "活动等待消息"),

    /** 活动收到消息 */
    ACTIVITY_MESSAGE_RECEIVED("ACTIVITY_MESSAGE_RECEIVED", "活动收到消息"),

    /** 活动消息取消 */
    ACTIVITY_MESSAGE_CANCELLED("ACTIVITY_MESSAGE_CANCELLED", "活动消息取消"),

    /** 活动收到错误 */
    ACTIVITY_ERROR_RECEIVED("ACTIVITY_ERROR_RECEIVED", "活动收到错误"),

    /** 活动补偿 */
    ACTIVITY_COMPENSATE("ACTIVITY_COMPENSATE", "活动补偿"),

    /** 活动等待条件 */
    ACTIVITY_CONDITIONAL_WAITING("ACTIVITY_CONDITIONAL_WAITING", "活动等待条件"),

    /** 活动条件满足 */
    ACTIVITY_CONDITIONAL_RECEIVED("ACTIVITY_CONDITIONAL_RECEIVED", "活动条件满足"),

    /** 活动等待升级 */
    ACTIVITY_ESCALATION_WAITING("ACTIVITY_ESCALATION_WAITING", "活动等待升级"),

    /** 活动收到升级 */
    ACTIVITY_ESCALATION_RECEIVED("ACTIVITY_ESCALATION_RECEIVED", "活动收到升级"),

    // ==================== 多实例 ====================
    /** 多实例活动开始 */
    MULTI_INSTANCE_ACTIVITY_STARTED("MULTI_INSTANCE_ACTIVITY_STARTED", "多实例活动开始"),

    /** 多实例活动完成 */
    MULTI_INSTANCE_ACTIVITY_COMPLETED("MULTI_INSTANCE_ACTIVITY_COMPLETED", "多实例活动完成"),

    /** 多实例活动条件满足完成 */
    MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION("MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION", "多实例条件完成"),

    /** 多实例活动取消 */
    MULTI_INSTANCE_ACTIVITY_CANCELLED("MULTI_INSTANCE_ACTIVITY_CANCELLED", "多实例活动取消"),

    // ==================== 任务 ====================
    /** 任务创建 */
    TASK_CREATED("TASK_CREATED", "任务创建"),

    /** 任务分配处理人 */
    TASK_ASSIGNED("TASK_ASSIGNED", "任务分配"),

    /** 任务完成 */
    TASK_COMPLETED("TASK_COMPLETED", "任务完成"),

    /** 任务负责人变更 */
    TASK_OWNER_CHANGED("TASK_OWNER_CHANGED", "负责人变更"),

    /** 任务优先级变更 */
    TASK_PRIORITY_CHANGED("TASK_PRIORITY_CHANGED", "优先级变更"),

    /** 任务截止时间变更 */
    TASK_DUEDATE_CHANGED("TASK_DUEDATE_CHANGED", "截止时间变更"),

    /** 任务名称变更 */
    TASK_NAME_CHANGED("TASK_NAME_CHANGED", "名称变更"),

    // ==================== Job/定时器 ====================
    /** 定时器已调度 */
    TIMER_SCHEDULED("TIMER_SCHEDULED", "定时器调度"),

    /** 定时器触发 */
    TIMER_FIRED("TIMER_FIRED", "定时器触发"),

    /** Job 取消 */
    JOB_CANCELED("JOB_CANCELED", "Job取消"),

    /** Job 执行成功 */
    JOB_EXECUTION_SUCCESS("JOB_EXECUTION_SUCCESS", "Job执行成功"),

    /** Job 执行失败 */
    JOB_EXECUTION_FAILURE("JOB_EXECUTION_FAILURE", "Job执行失败"),

    /** Job 重试次数减少 */
    JOB_RETRIES_DECREMENTED("JOB_RETRIES_DECREMENTED", "Job重试递减"),

    /** Job 被拒绝（队列满） */
    JOB_REJECTED("JOB_REJECTED", "Job被拒绝"),

    /** Job 重新调度 */
    JOB_RESCHEDULED("JOB_RESCHEDULED", "Job重新调度"),

    /** Job 移入死信队列 */
    JOB_MOVED_TO_DEADLETTER("JOB_MOVED_TO_DEADLETTER", "Job移入死信"),

    // ==================== 实体生命周期 ====================
    /** 实体创建 */
    ENTITY_CREATED("ENTITY_CREATED", "实体创建"),

    /** 实体初始化完成 */
    ENTITY_INITIALIZED("ENTITY_INITIALIZED", "实体初始化"),

    /** 实体更新 */
    ENTITY_UPDATED("ENTITY_UPDATED", "实体更新"),

    /** 实体删除 */
    ENTITY_DELETED("ENTITY_DELETED", "实体删除"),

    /** 实体挂起 */
    ENTITY_SUSPENDED("ENTITY_SUSPENDED", "实体挂起"),

    /** 实体激活 */
    ENTITY_ACTIVATED("ENTITY_ACTIVATED", "实体激活"),

    // ==================== 其他 ====================
    /** 顺序流被走过 */
    SEQUENCEFLOW_TAKEN("SEQUENCEFLOW_TAKEN", "顺序流走过"),

    /** 变量创建 */
    VARIABLE_CREATED("VARIABLE_CREATED", "变量创建"),

    /** 变量更新 */
    VARIABLE_UPDATED("VARIABLE_UPDATED", "变量更新"),

    /** 变量删除 */
    VARIABLE_DELETED("VARIABLE_DELETED", "变量删除"),

    /** 历史活动实例创建 */
    HISTORIC_ACTIVITY_INSTANCE_CREATED("HISTORIC_ACTIVITY_INSTANCE_CREATED", "历史活动创建"),

    /** 历史活动实例结束 */
    HISTORIC_ACTIVITY_INSTANCE_ENDED("HISTORIC_ACTIVITY_INSTANCE_ENDED", "历史活动结束"),

    /** 历史流程实例创建 */
    HISTORIC_PROCESS_INSTANCE_CREATED("HISTORIC_PROCESS_INSTANCE_CREATED", "历史流程创建"),

    /** 历史流程实例结束 */
    HISTORIC_PROCESS_INSTANCE_ENDED("HISTORIC_PROCESS_INSTANCE_ENDED", "历史流程结束"),

    /** 引擎创建 */
    ENGINE_CREATED("ENGINE_CREATED", "引擎创建"),

    /** 引擎关闭 */
    ENGINE_CLOSED("ENGINE_CLOSED", "引擎关闭"),

    /** 自定义事件 */
    CUSTOM("CUSTOM", "自定义事件");

    private final String value;
    private final String label;

    public static FlowableEngineEventTypeEnum of(String value) {
        if (value == null) return null;
        for (FlowableEngineEventTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }

    /**
     * 是否为流程级事件
     */
    public boolean isProcessEvent() {
        return this.name().startsWith("PROCESS_");
    }

    /**
     * 是否为任务级事件
     */
    public boolean isTaskEvent() {
        return this.name().startsWith("TASK_");
    }

    /**
     * 是否为活动级事件
     */
    public boolean isActivityEvent() {
        return this.name().startsWith("ACTIVITY_") || this.name().startsWith("MULTI_INSTANCE_");
    }

    /**
     * 是否为 Job 级事件
     */
    public boolean isJobEvent() {
        return this.name().startsWith("JOB_") || this.name().startsWith("TIMER_");
    }
}
