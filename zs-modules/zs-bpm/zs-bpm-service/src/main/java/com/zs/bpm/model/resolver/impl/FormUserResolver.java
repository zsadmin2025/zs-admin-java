package com.zs.bpm.model.resolver.impl;

import cn.hutool.core.util.StrUtil;
import com.zs.common.core.enums.ApproveSetTypeEnum;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 表单内的人审批人解析器
 * <p>
 * 根据表单字段中指定的用户ID作为审批人。
 * 参数格式为表单字段名，如"managerUserId"。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Component
public class FormUserResolver extends AbstractApproverResolver {

    @Override
    public Integer getResolverCode() {
        return ApproveSetTypeEnum.FORM_USER.getValue();
    }

    @Override
    public String getResolverName() {
        return ApproveSetTypeEnum.FORM_USER.getLabel();
    }

    @Override
    protected List<String> doResolve(String param, String initiator) {
        if (StrUtil.isBlank(param)) {
            return Collections.emptyList();
        }

        // 表单内的人类型，参数为表单字段名
        // 实际实现需要从流程变量中获取表单数据，然后根据字段名获取用户ID
        // 这里返回空集合，实际审批人由流程变量动态设置
        log.warn("表单内的人解析器需要根据业务实现：param={}", param);
        return Collections.emptyList();
    }
}