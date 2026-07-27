package com.zs.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BpmModelStatusEnum {

    DISABLED(0, "已停用"),
    ACTIVE(1, "已启用");

    private final Integer value;
    private final String label;

    public static BpmModelStatusEnum of(Integer value) {
        if (value == null) return null;
        for (BpmModelStatusEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }

}
