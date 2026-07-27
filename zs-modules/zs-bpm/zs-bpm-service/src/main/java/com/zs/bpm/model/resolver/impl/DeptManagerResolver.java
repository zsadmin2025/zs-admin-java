package com.zs.bpm.model.resolver.impl;

import cn.hutool.core.util.StrUtil;
import com.zs.common.core.enums.ApproveSetTypeEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 部门负责人审批人解析器
 * <p>
 * 根据部门ID查询部门负责人作为审批人。
 * 参数格式为逗号分隔的部门ID。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Component
public class DeptManagerResolver extends AbstractApproverResolver {

    @Override
    public Integer getResolverCode() {
        return ApproveSetTypeEnum.DEPT_MANAGER.getValue();
    }

    @Override
    public String getResolverName() {
        return ApproveSetTypeEnum.DEPT_MANAGER.getLabel();
    }

    @Override
    protected List<String> doResolve(String param, String initiator) {
        if (StrUtil.isBlank(param)) {
            return Collections.emptyList();
        }

        List<String> deptIds = parseParamList(param);
        List<String> userIds = new ArrayList<>();

        for (String deptId : deptIds) {
            String userId = businessOrgService.getDeptHeadUserId(deptId);
            if (StrUtil.isNotBlank(userId)) {
                userIds.add(userId);
            }
        }

        return userIds;
    }
}