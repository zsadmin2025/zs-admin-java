package com.zs.bpm.model.resolver.impl;

import cn.hutool.core.util.StrUtil;
import com.zs.common.core.enums.ApproveSetTypeEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 指定角色审批人解析器
 * <p>
 * 根据角色ID查询拥有该角色的所有用户作为审批人。
 * 参数格式为逗号分隔的角色ID。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Component
public class SpecifiedRoleResolver extends AbstractApproverResolver {

    @Override
    public Integer getResolverCode() {
        return ApproveSetTypeEnum.SPECIFIED_ROLE.getValue();
    }

    @Override
    public String getResolverName() {
        return ApproveSetTypeEnum.SPECIFIED_ROLE.getLabel();
    }

    @Override
    protected List<String> doResolve(String param, String initiator) {
        if (StrUtil.isBlank(param)) {
            return Collections.emptyList();
        }

        List<String> roleIds = parseParamList(param);
        List<String> allUserIds = new ArrayList<>();

        for (String roleId : roleIds) {
            List<String> userIds = businessOrgService.getUserIdsByRoleId(roleId);
            if (userIds != null && !userIds.isEmpty()) {
                allUserIds.addAll(userIds);
            }
        }

        return allUserIds;
    }
}