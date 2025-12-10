package com.zs.controller;

import com.zs.common.core.core.Result;
import com.zs.model.TenantAwarePrincipal;
import jakarta.annotation.Resource;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
public class WebsocketController {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;
    @Resource
    private SimpUserRegistry simpUserRegistry;

    /**
     * 群发
     * @param message 消息
     *  1. 订阅者订阅地址：/topic/message
     *  2. 发送者发送地址：/app/sendToAll
     */
    @MessageMapping("/sendToAll")
    public void sendToAll(@Payload String message, TenantAwarePrincipal principal) {
        String tenantId = principal.getTenantId();
        simpMessagingTemplate.convertAndSend("/topic/" + tenantId +"/message", message);
    }


    /**
     * 单发
     * @param message 消息
     * @param receiverId 接收者id
     * @param principal 当前用户
     *  1. 订阅者订阅地址：/user/queue/message
     *  2. 订阅者发送地址：/app/sendToUser
     */
    @MessageMapping("/sendToUser/{receiverId}")
    public void sendToUser(@DestinationVariable String receiverId, @Payload String message, TenantAwarePrincipal principal) {

        String tenantId = principal.getTenantId();

        String fullReceiverId = tenantId + ":" + receiverId;

        simpMessagingTemplate.convertAndSendToUser(fullReceiverId, "/queue/" + tenantId + "/message", message);
    }


    // 3. 订阅时返回在线用户
    @SubscribeMapping("/init")
    public Result<?> onSubscribe(Principal user) {
        Set<SimpUser> simpUsers = simpUserRegistry.getUsers();
        for (SimpUser simpUser : simpUsers) {
            System.out.println(simpUser.getName());
        }
        System.out.println(user.getName());
        String str = "这是初始化数据";
        return new Result<>().ok(str);
    }
}
