package com.zs.bpm.model.resolver.impl;

import com.zs.common.core.enums.ApproveSetTypeEnum;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 指定成员审批人解析器
 * <p>
 * 解析指定用户ID列表作为审批人，参数格式为逗号分隔的用户ID。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Component
public class SpecifiedUserResolver extends AbstractApproverResolver {

    @Override
    public Integer getResolverCode() {
        return ApproveSetTypeEnum.SPECIFIED_USER.getValue();
    }

    @Override
    public String getResolverName() {
        return ApproveSetTypeEnum.SPECIFIED_USER.getLabel();
    }

    @Override
    protected List<String> doResolve(String param, String initiator) {
        return parseParamList(param);
    }
}