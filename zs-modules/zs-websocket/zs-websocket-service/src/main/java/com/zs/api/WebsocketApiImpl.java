package com.zs.api;

import org.springframework.stereotype.Service;
import websocket.WebsocketApi;

@Service
public class WebsocketApiImpl implements WebsocketApi {

    @Override
    public void sendMessage(String message) {
    }

    @Override
    public void sendMessageToUser(String receiverId, String message) {

    }
}
