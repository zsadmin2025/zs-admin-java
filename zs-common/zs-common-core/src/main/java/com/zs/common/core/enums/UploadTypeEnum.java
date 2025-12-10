package com.zs.common.core.enums;

import lombok.Getter;

/**
 *
 */

@Getter
public enum UploadTypeEnum {

    LOCAL(1), ALIYUN(2),TENCENT(3),HUAWEI(4);

    private final int value;

    UploadTypeEnum(int value) {
        this.value = value;
    }

    public static UploadTypeEnum getEnum(int value) {
        // 根据value获取UploadTypeEnum
        for (UploadTypeEnum type : UploadTypeEnum.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }

}
