package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 前端流程设计器 - 节点类型枚举
 *
 * @author zsadmin
 */
@Getter
@AllArgsConstructor
public enum FlowNodeTypeEnum {

    /** 发起人 */
    START(0, "发起人"),
    /** 审批人 */
    APPROVAL(1, "审批人"),
    /** 抄送人 */
    CC(2, "抄送人"),
    /** 条件分支 */
    CONDITION(3, "条件"),
    /** 路由（网关） */
    ROUTER(4, "路由");

    private final int value;
    private final String label;

    public static FlowNodeTypeEnum of(int value) {
        for (FlowNodeTypeEnum e : values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }
}
