package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 流程节点/审批人展示状态枚举
 * <p>
 * 用于 FlowNode.status 和 NodeApprover.status 字段，
 * 表示审批轨迹中节点或审批人的展示状态。
 * </p>
 *
 * @author zsadmin
 */
@Getter
@AllArgsConstructor
public enum FlowNodeStatusEnum {

    /** 已完成 */
    COMPLETED("COMPLETED", "已完成"),

    /** 进行中 */
    IN_PROGRESS("IN_PROGRESS", "进行中"),

    /** 未开始 */
    NOT_STARTED("NOT_STARTED", "未开始");

    private final String value;
    private final String label;

    public static FlowNodeStatusEnum of(String value) {
        if (value == null) return null;
        for (FlowNodeStatusEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }

    /**
     * 是否为结束状态
     */
    public boolean isEnd() {
        return this == COMPLETED;
    }
}
