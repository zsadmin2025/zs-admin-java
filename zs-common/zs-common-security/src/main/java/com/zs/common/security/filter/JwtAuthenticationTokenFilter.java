package com.zs.common.security.filter;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.core.HttpEnum;
import com.zs.common.core.core.Result;
import com.zs.common.core.enums.UserTypeEnum;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.propetties.WhiteUrlProperties;
import com.zs.common.core.utils.JwtUtil;
import com.zs.common.redis.config.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * token认证过滤器
 *
 * @author zsadmin
 */
@Component
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
        if (!StringUtils.hasText(token)) {
            chain.doFilter(request, response);
            return;
        }

        // 3. 解析JWT
        Claims claims = jwtUtil.parseToken(token);
        if (claims == null) {
            writeError(response, HttpEnum.UNAUTHORIZED, "token无效或已过期");
            return;
        }

        // 4. 用户类型隔离：token中的用户类型必须与请求路径匹配
        String tokenUserType = claims.get("userType", String.class);
        if (!isUserTypeMatchRequest(tokenUserType, request.getRequestURI())) {
            writeError(response, HttpEnum.FORBIDDEN, "无权访问该端口");
            return;
        }

        // 5. 获取Redis Key（根据用户类型）
        String redisKey = jwtUtil.getRedisKey(claims);

        // 6. 从Redis获取用户信息
        Object jsonLoginUserInfo = redisUtil.get(redisKey);
        if (jsonLoginUserInfo == null) {
            writeError(response, HttpEnum.UNAUTHORIZED, "登录已过期，请重新登录");
            return;
        }
        LoginUserInfo loginUserInfo;
        if (jsonLoginUserInfo instanceof LoginUserInfo) {
            loginUserInfo = (LoginUserInfo) jsonLoginUserInfo;
        } else {
            writeError(response, HttpEnum.UNAUTHORIZED, "登录信息格式错误");
            return;
        }

        if (!loginUserInfo.isEnabled()) {
            writeError(response, HttpEnum.UNAUTHORIZED, "登录已过期或账号已被禁用");
            return;
        }

        // 7. 设置认证信息
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(loginUserInfo, null, loginUserInfo.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 放行
        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String token = null;
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith(Constants.TOKEN_PREFIX)) {
            token = authorization.substring(Constants.TOKEN_PREFIX.length());
        }
        if (!StringUtils.hasText(token)) {
            token = request.getParameter(Constants.ACCESS_TOKEN);
        }
        if (!StringUtils.hasText(token)) {
            return null;
        }
        return token;
    }

    /**
     * 判断token中的用户类型是否与请求路径匹配
     * /member/** → 仅允许 MEMBER 类型
     * 其他路径 → 仅允许 PLATFORM 类型
     */
    private boolean isUserTypeMatchRequest(String tokenUserType, String requestUri) {
        boolean isMemberPath = requestUri.startsWith("/member/");
        if (isMemberPath) {
            return UserTypeEnum.MEMBER.getCode().equals(tokenUserType);
        } else {
            return UserTypeEnum.PLATFORM.getCode().equals(tokenUserType);
        }
    }

    private void writeError(HttpServletResponse response, HttpEnum httpEnum, String message) throws IOException {
        response.setStatus(httpEnum.getCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONUtil.toJsonStr(new Result<>().error(httpEnum, message)));
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
