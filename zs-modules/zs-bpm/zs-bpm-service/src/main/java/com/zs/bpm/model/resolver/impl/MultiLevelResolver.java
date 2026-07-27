package com.zs.bpm.model.resolver.impl;

import cn.hutool.core.util.StrUtil;
import com.zs.common.core.enums.ApproveSetTypeEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 连续多级审批解析器
 * <p>
 * 上级负责人逐级审批，从发起人的直属上级开始，逐级向上审批。
 * 参数指定审批级数，如"3"表示向上3级审批。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Component
public class MultiLevelResolver extends AbstractApproverResolver {

    @Override
    public Integer getResolverCode() {
        return ApproveSetTypeEnum.MULTI_LEVEL.getValue();
    }

    @Override
    public String getResolverName() {
        return ApproveSetTypeEnum.MULTI_LEVEL.getLabel();
    }

    @Override
    protected List<String> doResolve(String param, String initiator) {
        if (StrUtil.isBlank(initiator)) {
            return Collections.emptyList();
        }

        int levels = 1; // 默认1级
        if (StrUtil.isNotBlank(param)) {
            try {
                levels = Integer.parseInt(param);
            } catch (NumberFormatException e) {
                log.warn("连续多级审批参数格式错误，使用默认1级：param={}", param);
            }
        }

        List<String> approvers = new ArrayList<>();
        String currentUserId = initiator;

        for (int i = 0; i < levels; i++) {
            String leaderUserId = businessOrgService.getLeaderUserId(currentUserId);
            if (StrUtil.isBlank(leaderUserId)) {
                break; // 没有上级了
            }
            approvers.add(leaderUserId);
            currentUserId = leaderUserId;
        }

        return approvers;
    }
}