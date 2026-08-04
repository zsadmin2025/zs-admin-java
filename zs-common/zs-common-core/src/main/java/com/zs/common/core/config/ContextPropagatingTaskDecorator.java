package com.zs.common.core.config;

import org.springframework.core.task.TaskDecorator;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.zs.common.core.constant.Constants;
import com.zs.common.core.tenant.TenantContext;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步线程上下文传递装饰器
 * <p>
 * 将提交任务线程（主线程）的租户上下文、请求上下文、安全上下文快照传递到异步执行线程，
 * 并在任务执行完毕后恢复/清理，避免线程池复用导致上下文串号。
 * 用于解决 @Async 异步方法中 ThreadLocal（TenantContext、RequestContextHolder、SecurityContextHolder）
 * 无法跨线程传递的问题。
 */
@Slf4j
public class ContextPropagatingTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 主线程上下文快照
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String tenantId = getTenantId(requestAttributes);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        // 复制安全上下文，避免跨线程共享同一个 SecurityContext 实例
        SecurityContext copyContext = SecurityContextHolder.createEmptyContext();
        if (securityContext != null && securityContext.getAuthentication() != null) {
            copyContext.setAuthentication(securityContext.getAuthentication());
        }

        return () -> {
            // 记录执行线程原有请求上下文，用于执行后恢复
            RequestAttributes originalRequestAttributes = RequestContextHolder.getRequestAttributes();
            try {
                if (tenantId != null) {
                    TenantContext.setTenantId(tenantId);
                }
                if (requestAttributes != null) {
                    RequestContextHolder.setRequestAttributes(requestAttributes, true);
                }
                // 内部自动设置并恢复安全上下文
                new DelegatingSecurityContextRunnable(runnable, copyContext).run();
            } finally {
                TenantContext.clear();
                if (originalRequestAttributes != null) {
                    RequestContextHolder.setRequestAttributes(originalRequestAttributes, true);
                } else {
                    RequestContextHolder.resetRequestAttributes();
                }
            }
        };
    }

    /**
     * 获取租户ID：优先从租户上下文，其次从请求头/参数（白名单接口等未设置租户上下文的场景）
     */
    private String getTenantId(RequestAttributes requestAttributes) {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null && requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            tenantId = request.getHeader(Constants.TENANT_HEADER);
            if (tenantId == null) {
                tenantId = request.getParameter(Constants.TENANT_HEADER);
            }
        }
        return tenantId;
    }
}
