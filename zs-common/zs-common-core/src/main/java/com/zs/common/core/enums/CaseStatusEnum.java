package com.zs.common.core.enums;

import lombok.Getter;

/**
 * 案件状态枚举
 *
 * @author zs
 */
@Getter
public enum CaseStatusEnum {

    PROGRESS("进行中", 1),
    CLOSED("结案", 2),
    FILING("归档", 3);

    private final String name;

    private final Integer code;

    CaseStatusEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public static String getName(Integer code) {
        for (CaseStatusEnum value : CaseStatusEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return "";
    }

    public static Integer getCode(String name) {
        for (CaseStatusEnum value : CaseStatusEnum.values()) {
            if (value.getName().equals(name)) {
                return value.getCode();
            }
        }
        return -1;
    }
}
