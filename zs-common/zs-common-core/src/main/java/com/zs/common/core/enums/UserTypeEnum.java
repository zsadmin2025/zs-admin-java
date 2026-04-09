package com.zs.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {

    PLATFORM("platform", "平台端"),
    MEMBER("member", "会员端"),
    COMPANION("companion", "陪诊师端");

    private final String code;
    private final String desc;

    public static UserTypeEnum fromCode(String code) {
        for (UserTypeEnum type : values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown user type: " + code);
    }
}
