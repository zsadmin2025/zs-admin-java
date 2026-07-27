package com.zs.bpm.model.resolver.impl;

import cn.hutool.core.util.StrUtil;
import com.zs.common.core.enums.ApproveSetTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 发起人本人审批人解析器
 * <p>
 * 发起人本人作为审批人，即发起人自己审批自己的流程。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Component
public class InitiatorResolver extends AbstractApproverResolver {

    @Override
    public Integer getResolverCode() {
        return ApproveSetTypeEnum.INITIATOR.getValue();
    }

    @Override
    public String getResolverName() {
        return ApproveSetTypeEnum.INITIATOR.getLabel();
    }

    @Override
    protected List<String> doResolve(String param, String initiator) {
        if (StrUtil.isBlank(initiator)) {
            return Collections.emptyList();
        }
        return Collections.singletonList(initiator);
    }
}