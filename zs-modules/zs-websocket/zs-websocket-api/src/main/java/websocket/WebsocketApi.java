package websocket;

public interface WebsocketApi {


    /**
     * 给所有人发送消息
     * @param message 消息
     */
    void sendMessage(String message);


    /**
     * 给某个用户发送消息
     * @param receiverId 接收者id
     * @param message 消息
     */
    void sendMessageToUser(String receiverId, String message);
}
