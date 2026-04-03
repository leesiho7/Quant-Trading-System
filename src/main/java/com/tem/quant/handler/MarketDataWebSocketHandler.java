package com.tem.quant.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 브라우저(클라이언트)들과의 웹소켓 연결을 관리하고 데이터를 전송하는 핸들러
 */
@Component
public class MarketDataWebSocketHandler extends TextWebSocketHandler {

    // 연결된 모든 브라우저 세션 저장소
    private static final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("브라우저 연결 성공: " + session.getId() + " | 현재 접속자 수: " + sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("브라우저 연결 종료: " + session.getId());
    }

    /**
     * 외부(BybitDataService 등)에서 받은 데이터를 모든 브라우저에 전송
     */
    public void broadcast(String message) {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    System.err.println("메시지 전송 오류: " + e.getMessage());
                }
            }
        }
    }
}
