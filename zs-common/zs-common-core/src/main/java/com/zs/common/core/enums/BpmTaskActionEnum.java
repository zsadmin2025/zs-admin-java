package com.zs.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务审批动作枚举
 *
 * @author zsadmin
 */
@Getter
@AllArgsConstructor
public enum BpmTaskActionEnum {

    COMPLETE("COMPLETE", "通过"),
    REJECT("REJECT", "驳回"),
    RETURN("RETURN", "退回"),
    TRANSFER("TRANSFER", "转办"),
    DELEGATE("DELEGATE", "委派"),
    RESOLVE("RESOLVE", "完成委派"),
    CLAIM("CLAIM", "认领"),
    UNCLAIM("UNCLAIM", "取消认领"),
    REVOKE("REVOKE", "撤销"),
    CANCEL("CANCEL", "取消");

    private final String value;
    private final String label;

    public static BpmTaskActionEnum of(String value) {
        if (value == null) return null;
        for (BpmTaskActionEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }
}
