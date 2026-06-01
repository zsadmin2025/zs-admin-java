package com.zs.common.core.events;

import org.springframework.context.ApplicationEvent;

import java.util.Set;


public class DataPermissionChangedEvent extends ApplicationEvent {

    /**
     * 数据权限变更类型
     */
    public enum ChangeType {
        /** 角色 dataScope 字段变更 */
        ROLE_UPDATED,
        /** 角色删除 */
        ROLE_DELETED,
        /** 角色自定义部门变更 */
        ROLE_DEPT_CHANGED,
        /** 部门结构变更（影响 DEPT_AND_CHILD 的递归子部门） */
        DEPT_CHANGED,
        /** 用户-角色关联变更 */
        USER_ROLE_CHANGED,
    }

    private final ChangeType changeType;
    private final Set<Long> affectedUserIds;

    public DataPermissionChangedEvent(Object source, ChangeType changeType, Set<Long> affectedUserIds) {
        super(source);
        this.changeType = changeType;
        this.affectedUserIds = affectedUserIds;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public Set<Long> getAffectedUserIds() {
        return affectedUserIds;
    }
}
