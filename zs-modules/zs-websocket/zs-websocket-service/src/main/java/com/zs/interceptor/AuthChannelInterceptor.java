package com.zs.interceptor;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.utils.JwtUtil;
import com.zs.common.redis.config.RedisUtil;
import com.zs.model.TenantAwarePrincipal;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;


@Component
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(@NotNull Message<?> message, @NotNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) return message;


        // 只处理 CONNECT 命令
        if (!StompCommand.CONNECT.equals(accessor.getCommand())) {
            return message;
        }
        // 获取 Authorization 和 Tenant-ID 头

        String authorization = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        String tenantId = accessor.getFirstNativeHeader(Constants.TENANT_HEADER);

        // 提取 token（去除 "Bearer " 前缀）
        String token = extractToken(authorization);
        if (StringUtils.isBlank(token)) {
            return null; // 无有效 token，拒绝连接
        }

        try {
            LoginUserInfo loginUserInfo = getLoginUserInfo(token);
            if (loginUserInfo == null) return null;

            // 设置用户身份（含租户信息）
            TenantAwarePrincipal principal = new TenantAwarePrincipal(
                    tenantId,
                    loginUserInfo.getSysUserId(),
                    loginUserInfo.getUsername()
            );
            accessor.setUser(principal);


            return message;

        } catch (Exception e) {
            // 日志记录异常（建议使用日志框架）
            // log.warn("STOMP CONNECT authentication failed", e);
            return null; // 认证失败，拒绝连接
        }


    }

    @Nullable
    private LoginUserInfo getLoginUserInfo(String token) {
        Claims claims = jwtUtil.parseToken(token);
        if (claims == null) {
            return null;
        }

        String loginInfo = claims.getSubject();
        if (StringUtils.isBlank(loginInfo)) {
            return null;
        }

        Object jsonLoginUserInfo = redisUtil.get(loginInfo);
        if (jsonLoginUserInfo == null) {
            return null;
        }

        return JSONUtil.toBean(JSONUtil.parseObj(jsonLoginUserInfo), LoginUserInfo.class);
    }


    // 辅助方法：安全提取 Bearer Token
    private String extractToken(String authorization) {
        if (StringUtils.isNotBlank(authorization) && authorization.startsWith(Constants.TOKEN_PREFIX)) {
            return authorization.substring(Constants.TOKEN_PREFIX.length()).trim();
        }
        return null;
    }


}
