package com.zs.bpm.model.resolver.impl;

import com.zs.common.core.enums.ApproveSetTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 发起人自选审批人解析器
 * <p>
 * 发起人自选审批人，此时审批人由发起人在流程发起时选择。
 * 解析器返回空集合，实际审批人由流程变量动态设置。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Component
public class SelfSelectResolver extends AbstractApproverResolver {

    @Override
    public Integer getResolverCode() {
        return ApproveSetTypeEnum.SELF_SELECT.getValue();
    }

    @Override
    public String getResolverName() {
        return ApproveSetTypeEnum.SELF_SELECT.getLabel();
    }

    @Override
    protected List<String> doResolve(String param, String initiator) {
        // 发起人自选类型，返回空集合，实际审批人由流程变量动态设置
        return Collections.emptyList();
    }
}