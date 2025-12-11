package com.zs.common.core.interceptor;

import com.zs.common.core.constant.Constants;
import com.zs.common.core.exception.ErrorCodeConstants;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.tenant.TenantContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 * 租户拦截器，从请求头中获取租户ID并设置到上下文
 */
@Component
public class TenantInterceptor implements HandlerInterceptor {
    // 白名单路径
    private static final String[] WHITE_LIST = {
            "/system/sys/tenant/select",
            "/auth/captcha",
            // 🔹 报表
            "/jmreport",

            // 🔹 接口文档
            "/doc.html",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/webjars",
            "/webjars/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-ui/index.html",

            // 🔹 监控类
            "/druid/",
            "/actuator/"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 1. 如果是静态资源处理器（Spring MVC 自动映射的静态资源）
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        // 判断是否是静态资源
        if (isStaticResource(request)) {
            return true;
        }
        // 从请求头获取租户ID
        String tenantId = request.getHeader(Constants.TENANT_HEADER);
        if (StringUtils.isBlank(tenantId)) {
            tenantId = request.getParameter(Constants.TENANT_HEADER);
        }
        if (StringUtils.isBlank(tenantId)) {
            throw new ZsException(ErrorCodeConstants.TENANT_NOT_EXIST);
        }
        TenantContext.setTenantId(tenantId);
        return true;
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清除租户ID，避免线程复用导致的问题
        TenantContext.clear();
    }

    /**
     * 判断是否是静态资源请求
     *
     * @param request 请求
     * @return 是否是静态资源请求
     */
    private boolean isStaticResource(HttpServletRequest request) {
        String uri = request.getRequestURI();

        for (String whitePath : WHITE_LIST) {
            if (uri.contains(whitePath)) {
                return true;
            }
        }
        return false;
    }

}

