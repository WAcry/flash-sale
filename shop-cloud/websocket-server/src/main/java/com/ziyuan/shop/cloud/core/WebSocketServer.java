package com.ziyuan.shop.cloud.core;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * WebSocket
 * ws://localhost:8095/111111
 */
@ServerEndpoint("/{uuid}")
@Component
public class WebSocketServer {

    /**
     *，
     * @param uuid
     * @param session
     */
    @OnOpen
    public void onOpen(@PathParam("uuid") String uuid, Session session) {
        System.out.println("client：" + uuid + "connected");
        WebSocketSessionManager.INSTANCE.put(uuid, session);
    }

    @OnClose
    public void onClose(@PathParam("uuid") String uuid) {
        System.out.println("client：" + uuid + "close connection");
        if (!StringUtils.isEmpty(uuid)) {
            WebSocketSessionManager.INSTANCE.remove(uuid);
        }
    }

    @OnError
    public void onError(@PathParam("uuid") String uuid, Throwable throwable) {
        System.out.println("client：" + uuid + "error");
        throwable.printStackTrace();
    }
}
