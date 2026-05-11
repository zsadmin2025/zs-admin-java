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
import org.springframework.security.authentication.CredentialsExpiredException;
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


        // 1. 白名单检查
        if (isWhiteUrl(request.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }


        // 2. 获取Token
         String token = extractToken(request);
        if (!StringUtils.hasText(request.getHeader(HttpHeaders.AUTHORIZATION))) {
            chain.doFilter(request, response);
            return;
        }
       // 3. 解析JWT
        Claims claims = jwtUtil.parseToken(token);

        // 4. 获取Redis Key（根据用户类型）
        String redisKey = jwtUtil.getRedisKey(claims);

        // 5. 从Redis获取用户信息
        Object jsonLoginUserInfo = redisUtil.get(redisKey);
        if (jsonLoginUserInfo == null) {
            chain.doFilter(request, response);
            return;
        }
        LoginUserInfo loginUserInfo = JSONUtil.toBean(JSONUtil.parseObj(jsonLoginUserInfo), LoginUserInfo.class);

        if (loginUserInfo == null) {
            throw new CredentialsExpiredException("登录已过期，请重新登录");
        }


        
       // 6. 设置认证信息
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUserInfo, null, loginUserInfo.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 放行
        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
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
        return token;
    }


    private void setAuthentication(@NotNull LoginUserInfo loginUserInfo) {

    }

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private boolean isWhiteUrl(String requestPath) {
        for (String whiteUrl : whiteUrlProperties.getUrl()) {
            if (antPathMatcher.match(whiteUrl, requestPath)) {
                return true;
            }
        }
        return false;
    }

}
