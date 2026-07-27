package com.zs.common.core.enums.bpmn;

/**
 * Flowable Task 任务状态枚举
 * 对应 Task 接口内置状态常量
 */
public enum TaskStatusEnum {

    /** 已创建：未认领、无处理人 */
    CREATED("created", "已创建"),
    /** 已认领：分配处理人，待处理 */
    CLAIMED("claimed", "已认领"),
    /** 处理中 */
    IN_PROGRESS("inProgress", "处理中"),
    /** 已挂起：暂停不可操作 */
    SUSPENDED("suspended", "已挂起"),
    /** 正常完成 */
    COMPLETED("completed", "正常完成"),
    /** 终止：驳回、撤销、流程作废等非正常结束 */
    TERMINATED("terminated", "已终止");

    /** 引擎原始标识（与Task常量一致） */
    private final String code;
    /** 中文描述 */
    private final String desc;

    TaskStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据引擎code匹配枚举
     */
    public static TaskStatusEnum getByCode(String code) {
        if (code == null) {
            return null;
        }
        for (TaskStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    // ===================== 业务判断工具方法 =====================
    /** 是否待办可处理 */
    public boolean isTodo() {
        return CREATED == this || CLAIMED == this || IN_PROGRESS == this;
    }

    /** 是否已结束（完成/终止） */
    public boolean isEnd() {
        return COMPLETED == this || TERMINATED == this;
    }

    /** 是否挂起冻结 */
    public boolean isSuspend() {
        return SUSPENDED == this;
    }
}

