package com.zs.bpm.model.resolver.impl;

import cn.hutool.core.util.StrUtil;
import com.zs.common.core.enums.ApproveSetTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 发起人的部门负责人审批人解析器
 * <p>
 * 查询发起人所在部门的部门负责人作为审批人。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Component
public class InitiatorDeptManagerResolver extends AbstractApproverResolver {

    @Override
    public Integer getResolverCode() {
        return ApproveSetTypeEnum.INITIATOR_DEPT_MANAGER.getValue();
    }

    @Override
    public String getResolverName() {
        return ApproveSetTypeEnum.INITIATOR_DEPT_MANAGER.getLabel();
    }

    @Override
    protected List<String> doResolve(String param, String initiator) {
        if (StrUtil.isBlank(initiator)) {
            return Collections.emptyList();
        }

        // 查询发起人的部门ID，然后查询部门负责人
        // 这里需要根据实际业务逻辑实现，暂时返回空集合
        // 实际实现需要调用组织架构服务获取发起人的部门ID
        log.warn("发起人的部门负责人解析器需要根据业务实现：initiator={}", initiator);
        return Collections.emptyList();
    }
}