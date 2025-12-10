package com.zs.service;

import com.zs.model.TenantAwarePrincipal;

public interface WebsocketService {

    /**
     * 发送消息
     * @param message 消息
     * @param principal 当前用户
     */
    public void sendMessage(String message, TenantAwarePrincipal principal);

    /**
     * 发送消息给指定用户
     * @param receiverId 接收者id
     * @param message 消息
     * @param principal 当前用户
     */
    public void sendMessageToUser(String receiverId, String message, TenantAwarePrincipal principal);
}
