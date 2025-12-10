package com.zs.common.core.crypto.annotation;

import com.zs.common.core.enums.CryptoTypeEnum;

import java.lang.annotation.*;

/**
 * 加密注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encryption {

    CryptoTypeEnum value() default CryptoTypeEnum.SM4;
}
