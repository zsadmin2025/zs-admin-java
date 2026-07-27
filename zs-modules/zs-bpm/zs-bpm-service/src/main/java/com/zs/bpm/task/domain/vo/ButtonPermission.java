package com.zs.bpm.task.domain.vo;

import lombok.Data;

/**
 * 按钮权限实体
 * 对应 XML 中的 <flowable:buttonsSetting> 标签或 buttonsSettingJson 中的对象
 */
@Data
public class ButtonPermission {

    /**
     * 按钮的唯一标识，如 "1"
     * 对应 XML 属性: id
     */
    private String id;

    /**
     * 按钮的显示名称，如 "通过"
     * 对应 XML 属性: displayName
     */
    private String displayName;

    /**
     * 按钮是否启用
     * 对应 XML 属性: enable
     */
    private boolean enable;
}
