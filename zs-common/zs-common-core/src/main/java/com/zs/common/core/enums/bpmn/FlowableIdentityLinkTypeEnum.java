package com.zs.common.core.enums.bpmn;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Flowable 身份链接类型枚举
 * <p>
 * 对应 {@code org.flowable.identitylink.api.IdentityLinkType} 常量，
 * 定义用户/组与流程对象（Task/ProcessInstance 等）的关联关系类型。
 * </p>
 *
 * @author zsadmin
 * @see org.flowable.identitylink.api.IdentityLinkType
 */
@Getter
@AllArgsConstructor
public enum FlowableIdentityLinkTypeEnum {

    /** 经办人（当前处理人） */
    ASSIGNEE("assignee", "经办人"),

    /** 候选人（待认领） */
    CANDIDATE("candidate", "候选人"),

    /** 负责人（任务所有者） */
    OWNER("owner", "负责人"),

    /** 发起人（流程启动者） */
    STARTER("starter", "发起人"),

    /** 参与者（已参与过流程） */
    PARTICIPANT("participant", "参与者"),

    /** 重新激活人 */
    REACTIVATOR("reactivator", "重新激活人");

    private final String value;
    private final String label;

    public static FlowableIdentityLinkTypeEnum of(String value) {
        if (value == null) return null;
        for (FlowableIdentityLinkTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        return null;
    }

    /**
     * 是否为直接处理人类型
     */
    public boolean isDirectAssignee() {
        return this == ASSIGNEE || this == OWNER;
    }
}
