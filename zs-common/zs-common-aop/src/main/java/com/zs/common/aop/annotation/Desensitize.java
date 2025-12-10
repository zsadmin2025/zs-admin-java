package com.zs.common.aop.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zs.common.aop.config.DesensitizeSerializer;
import com.zs.common.core.enums.DesensitizeTypeEnum;

import java.lang.annotation.*;

/**
 * 数据脱敏注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizeSerializer.class)
public @interface Desensitize {

    /** 脱敏类型 */
    DesensitizeTypeEnum type() default DesensitizeTypeEnum.CUSTOM;

    /** 脱敏起始位置 */
    int startIndex() default 0;

    /** 脱敏结束位置 */
    int endIndex() default 0;
}
