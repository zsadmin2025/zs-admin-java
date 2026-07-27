package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 实体链接类型枚举
 * <p>
 * 对应 {@code org.flowable.entitylink.api.EntityLinkType} 常量，
 * 定义流程实体（子流程、调用活动等）之间的关联关系类型。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.entitylink.api.EntityLinkType
 */
@Getter
@AllArgsConstructor
public enum FlowableEntityLinkTypeEnum {

    /** 父子关系 */
    CHILD("child", "父子关系"),

    /** 关联关系 */
    ASSOCIATION("association", "关联关系");

    private final String value;
    private final String label;

    public static FlowableEntityLinkTypeEnum of(String value) {
        if (value == null) return null;
        for (FlowableEntityLinkTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }
}
