package com.zs.common.core.enums;

import lombok.Getter;

@Getter
public enum FileSourceEnum {

    OTHER(0), //其他
    CASE_INFO(1), //案件信息
    CASE_HEARING(2), //案件开庭信息
    CASE_CONTRACT(3); //案件合同


    private final int value;

    FileSourceEnum(int value) {
        this.value = value;
    }

    // 根据 value 获取对应的枚举
    public static FileSourceEnum value(int value) {
        for (FileSourceEnum status : FileSourceEnum.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
