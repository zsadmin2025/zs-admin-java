package com.zs.service.impl;

import com.zs.model.TenantAwarePrincipal;
import com.zs.service.WebsocketService;
import jakarta.annotation.Resource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebsocketServiceImpl implements WebsocketService {

    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void sendMessage(String message, TenantAwarePrincipal principal) {
        String tenantId = principal.getTenantId();
        simpMessagingTemplate.convertAndSend("/topic/" + tenantId +"/message", message);
    }

    @Override
    public void sendMessageToUser(String receiverId, String message, TenantAwarePrincipal principal) {
        String tenantId = principal.getTenantId();

        String fullReceiverId = tenantId + ":" + receiverId;

        simpMessagingTemplate.convertAndSendToUser(fullReceiverId, "/queue/" + tenantId + "/message", message);
    }
}
