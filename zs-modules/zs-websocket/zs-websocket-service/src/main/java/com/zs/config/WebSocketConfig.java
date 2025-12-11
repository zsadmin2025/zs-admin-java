package com.zs.config;

import com.zs.interceptor.AuthChannelInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Resource
    private AuthChannelInterceptor authChannelInterceptor;

    /*
     * 1. 将 /serviceName/ws 路径注册为STOMP的端点，
     *    用户连接了这个端点后就可以进行websocket通讯，支持socketJs
     * 2. setAllowedOrigins("*")表示可以跨域
     * 3. withSockJS()表示支持socktJS访问
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 前端连接地址：/ws?access_token=xxx（或通过 Cookie/Spring Security 自动认证）
        registry.addEndpoint("/api/ws") // 端点
                .setAllowedOriginPatterns("*") // 允许跨域
//                .withSockJS()
        ; // 启用SockJS，兼容性更好
    }

    @Override
    public void configureMessageBroker(org.springframework.messaging.simp.config.MessageBrokerRegistry registry) {
        // 配置消息代理，用于处理客户端发送的消息,/topic是广播，/queue是点对点
        registry.enableSimpleBroker("/topic", "/queue");
        // 配置客户端订阅的广播消息前缀，表示所有以/app 开头的客户端消息或请求都会路由到带有@MessageMapping 注解的方法中
        registry.setApplicationDestinationPrefixes("/app");
        // 配置客户端订阅的点对点消息前缀，用于站内信
        registry.setUserDestinationPrefix("/user");
    }

    /**
     * 拦截器方式2
     *
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authChannelInterceptor);
    }
}
