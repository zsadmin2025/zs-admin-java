package com.zs.bpm.model.resolver.impl;

import cn.hutool.core.util.StrUtil;
import com.zs.common.core.enums.ApproveSetTypeEnum;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 指定岗位审批人解析器
 * <p>
 * 根据岗位ID查询拥有该岗位的所有用户作为审批人。
 * 参数格式为逗号分隔的岗位ID。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Component
public class SpecifiedPostResolver extends AbstractApproverResolver {

    @Override
    public Integer getResolverCode() {
        return ApproveSetTypeEnum.SPECIFIED_POST.getValue();
    }

    @Override
    public String getResolverName() {
        return ApproveSetTypeEnum.SPECIFIED_POST.getLabel();
    }

    @Override
    protected List<String> doResolve(String param, String initiator) {
        if (StrUtil.isBlank(param)) {
            return Collections.emptyList();
        }

        List<String> postIds = parseParamList(param);
        List<String> allUserIds = new ArrayList<>();

        for (String postId : postIds) {
            List<String> userIds = businessOrgService.getUserIdsByPostId(postId);
            if (userIds != null && !userIds.isEmpty()) {
                allUserIds.addAll(userIds);
            }
        }

        return allUserIds;
    }
}