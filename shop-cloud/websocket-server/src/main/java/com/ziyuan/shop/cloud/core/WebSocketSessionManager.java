package com.ziyuan.shop.cloud.core;

import com.ziyuan.shop.cloud.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public enum WebSocketSessionManager {

    INSTANCE;

    private final ConcurrentHashMap<String, Session> CLIENT_SESSION_MAP = new ConcurrentHashMap<>();

    public boolean put(String uuid, Session session) {
        return CLIENT_SESSION_MAP.put(uuid, session) != null;
    }

    public boolean remove(String uuid) {
        return CLIENT_SESSION_MAP.remove(uuid) != null;
    }

    public boolean sendMsg(String uuid, Object msg) {
        Session session = CLIENT_SESSION_MAP.get(uuid);

        if (session == null) {
            return false;
        }

        try {
            
            String result = JSONUtil.toJSONString(msg);
            session.getBasicRemote().sendText(result);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
