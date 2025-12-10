package com.zs.common.core.enums;

import lombok.Getter;

@Getter
public enum AdminEnum {

    Admin(1), // 管理员
    Normal(0) // 普通用户
    ;


    private final int value;

    AdminEnum(int value) {
        this.value = value;
    }

    // 根据 value 获取对应的枚举
    public static AdminEnum value(int value) {
        for (AdminEnum status : AdminEnum.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
