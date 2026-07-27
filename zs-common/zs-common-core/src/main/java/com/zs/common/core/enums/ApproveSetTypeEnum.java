package com.zs.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审批人类型枚举（对应前端 settype 字段）
 *
 * @author zsadmin
 */
@Getter
@AllArgsConstructor
public enum ApproveSetTypeEnum {

    /** 指定成员 */
    SPECIFIED_USER(1, "指定成员"),
    /** 指定角色 */
    SPECIFIED_ROLE(2, "指定角色"),
    /** 指定岗位 */
    SPECIFIED_POST(3, "指定岗位"),
    /** 部门负责人 */
    DEPT_MANAGER(4, "部门负责人"),
    /** 发起人自选 */
    SELF_SELECT(5, "发起人自选"),
    /** 发起人本人 */
    INITIATOR(6, "发起人本人"),
    /** 发起人的部门负责人 */
    INITIATOR_DEPT_MANAGER(7, "发起人的部门负责人"),
    /** 连续多级审批（上级负责人逐级审批） */
    MULTI_LEVEL(8, "连续多级审批"),
    /** 表单内的人（表单字段指定） */
    FORM_USER(9, "表单内的人"),
    /** 发起人的部门领导 */
    INITIATOR_DEPT_LEADER(10, "发起人的部门领导");

    private final int value;
    private final String label;

    public static ApproveSetTypeEnum of(int value) {
        for (ApproveSetTypeEnum e : values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }
}
