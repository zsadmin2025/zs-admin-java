package com.zs.bpm.task.domain.vo;

import lombok.Data;

/**
 * 字段权限实体
 * 对应 XML 中的 <flowable:fieldsPermission> 标签或 fieldsPermissionJson 中的对象
 */
@Data
public class FieldPermission {

    /**
     * 表单字段的唯一ID，如 "Fiacmqup0l6labc"
     * 对应 XML 属性: field
     */
    private String field;

    /**
     * 表单字段的显示名称，如 "请假类型"
     * 对应 XML 属性: title
     */
    private String title;

    /**
     * 权限值，如 "1" (通常代表可编辑)
     * 对应 XML 属性: permission
     */
    private String permission;
}
