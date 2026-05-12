package com.zs.common.core.interceptor;

import com.zs.common.core.constant.Constants;
import com.zs.common.core.exception.ErrorCodeConstants;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.tenant.TenantContext;
import com.zs.common.core.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
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
            "/auth/captcha",
            "/auth/login",
            "/member/auth/captcha",
            "/member/auth/login",
            "/system/sys/config/website",
            "/system/sys/tenant/select",
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

    @Resource
    private  JwtUtil jwtUtil;




    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 1. 如果是静态资源处理器（Spring MVC 自动映射的静态资源）
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        // 判断是否是白名单路径
        if (isWhiteList(request)) {
            return true;
        }

        // 从请求中获取租户ID
        String tenantId = handleRequest(request);
        TenantContext.setTenantId(tenantId);
        return true;
    }



    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清除租户ID，避免线程复用导致的问题
        TenantContext.clear();
    }

    /**
     * 判断是否是白名单路径
     */
    private boolean isWhiteList(HttpServletRequest request) {
        String uri = request.getRequestURI();
        for (String whitePath : WHITE_LIST) {
            if (uri.contains(whitePath)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 从请求中获取租户ID：优先从JWT解析，若没有token则从请求头/参数获取
     */
    public String handleRequest(HttpServletRequest request) {
        // 优先从 JWT 中解析租户ID
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isNotBlank(authorization) && authorization.startsWith(Constants.TOKEN_PREFIX)) {
            try {
                String token = authorization.replace(Constants.TOKEN_PREFIX, "");
                Claims claims = jwtUtil.parseToken(token);
                if (claims != null && claims.get(Constants.TENANT_HEADER) != null) {
                    return claims.get(Constants.TENANT_HEADER).toString();
                }
            } catch (Exception ignored) {
                // 解析失败，降级到其他方式获取
            }
        }

        // 降级：从请求头或参数中获取租户ID
        String tenantId = request.getHeader(Constants.TENANT_HEADER);
        if (StringUtils.isBlank(tenantId)) {
            tenantId = request.getParameter(Constants.TENANT_HEADER);
        }
        if (StringUtils.isBlank(tenantId)) {
            throw new ZsException(ErrorCodeConstants.TENANT_NOT_EXIST);
        }
        return tenantId;
    }

}

