package com.zs.common.mp.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD}) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface DataScope {

    /**
     * 表别名（多表关联时使用）
     */
    String tableAlias() default "";

    /** 部门限制范围的字段名称 */
    String deptField() default "creator_dept_id";

    /** 用户限制范围的字段名称 */
    String userField() default "creator";

}
