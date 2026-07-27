package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 实体层级类型枚举
 * <p>
 * 对应 {@code org.flowable.entitylink.api.HierarchyType} 常量，
 * 定义流程实体（子流程、调用活动等）在层级结构中的位置类型。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.entitylink.api.HierarchyType
 */
@Getter
@AllArgsConstructor
public enum FlowableHierarchyTypeEnum {

    /** 根节点 */
    ROOT("root", "根节点"),

    /** 父节点 */
    PARENT("parent", "父节点"),

    /** 祖父节点 */
    GRAND_PARENT("grandParent", "祖父节点");

    private final String value;
    private final String label;

    public static FlowableHierarchyTypeEnum of(String value) {
        if (value == null) return null;
        for (FlowableHierarchyTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }
}
