package com.zs.common.aop.annotation;


import com.zs.common.core.enums.OperationTypeEnum;

import java.lang.annotation.*;

/**
 * @author zsadmin
 */
@Target({ElementType.PARAMETER, ElementType.METHOD}) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface Log {

    /**
     * 功能模块
     */
    String module() default "";

    /**
     * 操作类型
     */
    OperationTypeEnum type() default OperationTypeEnum.OTHER;

    /**
     * 功能描述
     */
    String description() default "";

}
