package com.zs.common.core.crypto.annotation;

import com.zs.common.core.enums.CryptoTypeEnum;

import java.lang.annotation.*;

/**
 * 解密注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decryption {

    CryptoTypeEnum value() default CryptoTypeEnum.SM4;
}
