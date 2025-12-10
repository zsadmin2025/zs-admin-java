package com.zs.common.core.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

    NORMAL(1),
    DISABLED(0);

    private final int value;

    StatusEnum(int value) {
        this.value = value;
    }

    // 根据 value 获取对应的枚举
    public static StatusEnum fromValue(int value) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }


}
