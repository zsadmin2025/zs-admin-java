//package com.zs.common.security.filter;
//
//import cn.hutool.json.JSONUtil;
//import com.zs.common.core.core.Result;
//import com.zs.common.core.exception.ErrorCodeConstants;
//import com.zs.common.core.exception.ZsException;
//import com.zs.common.core.tenant.TenantContext;
//import com.zs.common.core.utils.StrUtils;
//import com.zs.common.security.propetties.WhiteUrlProperties;
//import jakarta.annotation.Resource;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
///**
// * 多租户过滤器
// * @author zsadmin
// */
//@Component
//public class TenantIdAuthenticationFilter extends OncePerRequestFilter {
//
//    @Resource
//    private WhiteUrlProperties whiteUrlProperties;
//
//    // 存储租户ID的请求头名称
//    public static final String TENANT_HEADER = "X-Tenant-Id";
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        // 白名单路径跳过校验
//        if (StrUtils.isMatch(whiteUrlProperties.getUrl(), request.getServletPath())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//
//        // 从请求头获取租户ID
//        String tenantId = request.getHeader(TENANT_HEADER);
//
//        if (StringUtils.isBlank(tenantId)) {
//            // 直接构建错误响应，不抛出异常
//            response.setContentType("application/json;charset=UTF-8");
//            String errorResult = JSONUtil.toJsonStr(new Result<>().error(ErrorCodeConstants.TENANT_NOT_EXIST.getCode(), ErrorCodeConstants.TENANT_NOT_EXIST.getMsg()));
//            response.getWriter().write(errorResult);
//            return;
//        }
//        // 设置租户ID
//        TenantContext.setTenantId(tenantId);
//
//        try {
//            filterChain.doFilter(request, response);
//        } finally {
//            // 清除ThreadLocal，避免内存泄漏
//            TenantContext.clear();
//        }
//    }
//}
