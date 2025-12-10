package com.zs.common.core.enums;

import lombok.Getter;

/**
 *
 */

@Getter
public enum SmsTypeEnum {

     ALIYUN(1),TENCENT(2);

    private final int value;

    SmsTypeEnum(int value) {
        this.value = value;
    }

    public static SmsTypeEnum getEnum(int value) {
        // 根据value获取UploadTypeEnum
        for (SmsTypeEnum type : SmsTypeEnum.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

}
