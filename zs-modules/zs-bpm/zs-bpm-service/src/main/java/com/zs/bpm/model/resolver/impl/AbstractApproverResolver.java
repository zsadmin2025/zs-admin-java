package com.zs.bpm.model.resolver.impl;

import cn.hutool.core.util.StrUtil;
import com.zs.bpm.model.resolver.ApproverResolver;
import com.zs.bpm.org.BusinessOrgService;
import jakarta.annotation.Resource;

import java.util.Collections;
import java.util.List;

/**
 * 审批人解析器基类
 * <p>
 * 提供通用的工具方法和异常处理，子类继承后只需实现具体的解析逻辑。
 * </p>
 *
 * @author zsadmin
 * @since 1.0.0
 */
public abstract class AbstractApproverResolver implements ApproverResolver {

    protected final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    protected BusinessOrgService businessOrgService;

    /**
     * 解析逗号分隔的参数为列表
     * 
     * @param param 逗号分隔的参数字符串
     * @return 参数列表，空值返回空集合
     */
    protected List<String> parseParamList(String param) {
        if (StrUtil.isBlank(param)) {
            return Collections.emptyList();
        }
        return List.of(param.split(","));
    }

    /**
     * 安全地解析参数，处理空值和异常
     * 
     * @param param 参数字符串
     * @param initiator 发起人ID
     * @return 解析结果列表
     */
    @Override
    public List<String> resolve(String param, String initiator) {
        try {
            return doResolve(param, initiator);
        } catch (Exception e) {
            log.error("审批人解析失败，解析器：{}，参数：{}，发起人：{}", getResolverName(), param, initiator, e);
            return Collections.emptyList();
        }
    }

    /**
     * 具体的解析逻辑，由子类实现
     * 
     * @param param 解析参数
     * @param initiator 发起人ID
     * @return 审批人用户ID列表
     */
    protected abstract List<String> doResolve(String param, String initiator);
}