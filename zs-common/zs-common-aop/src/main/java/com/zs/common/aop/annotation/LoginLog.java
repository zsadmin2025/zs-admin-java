package com.zs.common.aop.annotation;

import java.lang.annotation.*;

/**
 * @author zsadmin
 */
@Target({ElementType.PARAMETER, ElementType.METHOD}) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface LoginLog {

    /**
     * 登录状态 1 成功 2 失败
     * 默认为1-成功
     */
    int value() default 1;
}
