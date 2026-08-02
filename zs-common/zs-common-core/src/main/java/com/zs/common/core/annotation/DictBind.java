package com.zs.common.core.annotation;

import java.lang.annotation.*;

/**
 * <p>数据字典绑定注解</p>
 * <p>标注在 Label 字段上，自动根据同 Bean 内 sourceField 指定的字段值查询字典并填充。</p>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * private Long partnerType;
 *
 * @DictBind(dictCode = "partner_type", sourceField = "partnerType", defaultValue = "未知")
 * private String partnerTypeLabel;
 * }</pre>
 *
 * <h3>属性说明</h3>
 * <ul>
 *   <li><b>dictCode</b>：字典编码，对应 sys_dict_data.dict_type</li>
 *   <li><b>sourceField</b>：同 Bean 内的源值字段名，该字段的值用于匹配字典</li>
 *   <li><b>defaultValue</b>：字典查询无匹配时的默认回填值</li>
 * </ul>
 *
 * <p><b>性能优化点</b>：注解仅用于标记元数据，填充动作由 {@code DictFillUtil} 批量完成，避免逐字段反射。</p>
 *
 * @author zsadmin
 * @see com.zs.common.core.utils.DictFillUtil
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DictBind {

    /** 字典编码，如 "partner_type" */
    String dictCode();

    /** 源值字段名，同 Bean 内用于取值查询字典的字段名 */
    String sourceField();

    /** 字典无匹配时的默认值，默认为空字符串 */
    String defaultValue() default "";
}
