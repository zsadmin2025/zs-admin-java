package com.zs.listener;

import cn.hutool.json.JSONUtil;
import com.zs.common.core.constant.Constants;
import com.zs.common.core.model.LoginUserInfo;
import com.zs.common.core.utils.JwtUtil;
import com.zs.common.redis.config.RedisUtil;
import com.zs.manager.WebSocketSessionManager;
import com.zs.model.TenantAwarePrincipal;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Objects;

@Component
public class WebSocketEventListener {


    @Resource
    private WebSocketSessionManager webSocketSessionManager;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;


    // 用户连接
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        LoginUserInfo loginUserInfo = getUserFromHeaders(accessor); // 从 token 或 principal 获取
        if (loginUserInfo != null) {
            webSocketSessionManager.addUser(accessor.getSessionId(), loginUserInfo.getUserInfo());
        }
    }

    // 用户断开
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Object object = accessor.getHeader("simpUser");
        if (object != null) {
            TenantAwarePrincipal principal = (TenantAwarePrincipal) object;
            webSocketSessionManager.removeUser(accessor.getSessionId(), principal.getTenantId());
        }
    }

    private LoginUserInfo getUserFromHeaders(StompHeaderAccessor headers) {
        // 若使用 Spring Security，可从 simpUser 获取

        List<String> nativeHeader = headers.getNativeHeader("Authorization");

        if (nativeHeader != null && !nativeHeader.isEmpty()) {
            String token = nativeHeader.get(0);

            // 解析 token，获取用户 ID
            token = token.substring(Constants.TOKEN_PREFIX.length()); // 去除 "Bearer " 前缀
            Claims claims = jwtUtil.parseToken(token);
            String loginInfo = Objects.requireNonNull(claims).getSubject();
            Object jsonLoginUserInfo = redisUtil.get(loginInfo);

            return JSONUtil.toBean(JSONUtil.parseObj(jsonLoginUserInfo), LoginUserInfo.class);
        }


        return null;
    }
}
