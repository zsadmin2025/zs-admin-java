package com.zs.common.core.enums;

import lombok.Getter;

@Getter
public enum DataScopeEnum {

    ALL(1),           // 所有数据权限
    CUSTOM(2),         // 自定义数据权限
    DEPT(3),          // 部门数据权限
    DEPT_AND_CHILD(4), // 部门及子部门数据权限
    SELF(5);         // 仅本人数据权限


    private final int value;

    DataScopeEnum(int value) {
        this.value = value;
    }

    // 根据 value 获取对应的枚举
    public static DataScopeEnum value(int value) {
        for (DataScopeEnum status : DataScopeEnum.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
