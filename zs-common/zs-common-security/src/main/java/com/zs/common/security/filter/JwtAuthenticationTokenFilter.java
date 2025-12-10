package com.zs.common.security.filter;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.exception.ZsException;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.utils.JwtUtil;
import com.zs.common.redis.config.RedisUtil;
import com.zs.common.security.propetties.WhiteUrlProperties;
import io.jsonwebtoken.Claims;
import io.micrometer.common.lang.NonNullApi;
import jakarta.annotation.Nullable;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * token认证过滤器
 *
 * @author zsadmin
 */
@Component
@NonNullApi
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private WhiteUrlProperties whiteUrlProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

//        // 自定义方法，判断是否为静态资源请求
//        if (isStaticResourceRequest(request)) {
//            chain.doFilter(request, response); // 直接传递给下一个过滤器
//            return;
//        }
        // 检查URL是否在白名单内
        if (isWhiteUrl(request.getServletPath(), whiteUrlProperties.getUrl())) {
            chain.doFilter(request, response);
            return;
        }


        // 验证授权信息
        if (!StringUtils.hasText(request.getHeader(HttpHeaders.AUTHORIZATION))) {
            chain.doFilter(request, response);
            return;
        }

        // 解析Token并验证用户信息
        LoginUserInfo loginUserInfo = authenticate(request);

        // 设置认证信息
        setAuthentication(Objects.requireNonNull(loginUserInfo));

        // 放行
        chain.doFilter(request, response);
    }

    /**
     * 从 Authorization Header 或 URL 参数中提取 Token 并解析用户信息
     */
    @Nullable
    private LoginUserInfo authenticate(@NotNull HttpServletRequest request) {
        // 优先从 Authorization Header 获取
        String token = null;
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith(Constants.TOKEN_PREFIX)) {
            token = authorization.substring(Constants.TOKEN_PREFIX.length()); // 去除 "Bearer " 前缀
        }
        // 如果 Header 中没有，则尝试从 URL 参数获取（如 ?access_token=xxx）
        if (!StringUtils.hasText(token)) {
            token = request.getParameter(Constants.ACCESS_TOKEN);
        }
        // 如果仍无 token，返回 null（不认证）
        if (!StringUtils.hasText(token)) {
            return null;
        }

        Claims claims = jwtUtil.parseToken(token);
        if (Objects.isNull(claims)) {
            throw new ZsException("Invalid token");
        }
        String loginInfo = claims.getSubject();
        Object jsonLoginUserInfo = redisUtil.get(loginInfo);

        return JSONUtil.toBean(JSONUtil.parseObj(jsonLoginUserInfo), LoginUserInfo.class);
    }

    private void setAuthentication(@NotNull LoginUserInfo loginUserInfo) {
        // 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserInfo, null, loginUserInfo.getAuthorities());
        // 存入SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private boolean isWhiteUrl(String requestPath, List<String> whiteUrls) {
        for (String whiteUrl : whiteUrls) {
            if (antPathMatcher.match(whiteUrl, requestPath)) {
                return true;
            }
        }
        return false;
    }

}
