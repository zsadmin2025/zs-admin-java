package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable Scope 类型枚举
 * <p>
 * 对应 {@code org.flowable.common.engine.api.scope.ScopeTypes} 接口常量，
 * 标识引擎中不同模块的作用域类型。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.common.engine.api.scope.ScopeTypes
 */
@Getter
@AllArgsConstructor
public enum FlowableScopeTypeEnum {

    /** BPMN 流程 */
    BPMN("bpmn", "BPMN流程"),

    /** CMMN 案例 */
    CMMN("cmmn", "CMMN案例"),

    /** DMN 决策 */
    DMN("dmn", "DMN决策"),

    /** 任务 */
    TASK("task", "任务"),

    /** 应用 */
    APP("app", "应用"),

    /** 事件注册 */
    EVENT_REGISTRY("eventRegistry", "事件注册"),

    /** 表单 */
    FORM("form", "表单"),

    /** 计划项 */
    PLAN_ITEM("planItem", "计划项"),

    /** 外部 Worker */
    EXTERNAL_WORKER("externalWorker", "外部Worker");

    private final String value;
    private final String label;

    public static FlowableScopeTypeEnum of(String value) {
        if (value == null) return null;
        for (FlowableScopeTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }
}
