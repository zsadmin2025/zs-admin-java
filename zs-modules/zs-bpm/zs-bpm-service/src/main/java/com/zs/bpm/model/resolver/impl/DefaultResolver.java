package com.zs.bpm.model.resolver.impl;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 默认审批人解析器
 * <p>
 * 当找不到对应的解析器时使用，返回空集合。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
@Component
public class DefaultResolver extends AbstractApproverResolver {

    @Override
    public Integer getResolverCode() {
        return -1; // 默认解析器编码
    }

    @Override
    public String getResolverName() {
        return "默认解析器";
    }

    @Override
    protected List<String> doResolve(String param, String initiator) {
        log.warn("使用默认解析器，未找到对应的审批人解析器：param={}", param);
        return Collections.emptyList();
    }
}