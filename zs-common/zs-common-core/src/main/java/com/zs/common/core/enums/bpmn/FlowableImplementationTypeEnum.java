package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable BPMN 实现类型枚举
 * <p>
 * 对应 {@code org.flowable.bpmn.model.ImplementationType} 常量，
 * 定义 BPMN 元素（ServiceTask、Listener 等）的实现方式。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.bpmn.model.ImplementationType
 */
@Getter
@AllArgsConstructor
public enum FlowableImplementationTypeEnum {

    /** Java 类 */
    CLASS("class", "Java类"),

    /** UEL 表达式 */
    EXPRESSION("expression", "UEL表达式"),

    /** Spring Bean 委托表达式 */
    DELEGATE_EXPRESSION("delegateExpression", "Spring Bean委托"),

    /** 实例 */
    INSTANCE("instance", "实例"),

    /** 脚本 */
    SCRIPT("script", "脚本"),

    /** 抛出信号事件 */
    THROW_SIGNAL_EVENT("throwSignalEvent", "抛出信号事件"),

    /** 抛出全局信号事件 */
    THROW_GLOBAL_SIGNAL_EVENT("throwGlobalSignalEvent", "抛出全局信号事件"),

    /** 抛出消息事件 */
    THROW_MESSAGE_EVENT("throwMessageEvent", "抛出消息事件"),

    /** 抛出错误事件 */
    THROW_ERROR_EVENT("throwErrorEvent", "抛出错误事件"),

    /** WebService */
    WEB_SERVICE("##WebService", "WebService"),

    /** 无效抛出事件 */
    INVALID_THROW_EVENT("invalidThrowEvent", "无效抛出事件");

    private final String value;
    private final String label;

    public static FlowableImplementationTypeEnum of(String value) {
        if (value == null) return null;
        for (FlowableImplementationTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }

    /**
     * 是否为委托/类调用类型
     */
    public boolean isDelegate() {
        return this == CLASS
            || this == EXPRESSION
            || this == DELEGATE_EXPRESSION
            || this == INSTANCE;
    }

    /**
     * 是否为事件抛出类型
     */
    public boolean isThrowEvent() {
        return this == THROW_SIGNAL_EVENT
            || this == THROW_GLOBAL_SIGNAL_EVENT
            || this == THROW_MESSAGE_EVENT
            || this == THROW_ERROR_EVENT;
    }
}
