package com.zs.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务任务状态枚举
 *
 * @author zsadmin
 */
@Getter
@AllArgsConstructor
public enum BpmTaskStatusEnum {

    PENDING("PENDING", "待审批"),
    APPROVED("APPROVED", "已同意"),
    REJECTED("REJECTED", "已驳回"),
    TRANSFERRED("TRANSFERRED", "已转办"),
    DELEGATED("DELEGATED", "已委派"),
    WITHDRAWN("WITHDRAWN", "已撤回"),
    CANCELLED("CANCELLED", "已取消"),
    AUTO_EXECUTED("AUTO_EXECUTED", "自动执行");

    private final String value;
    private final String label;
}
