package com.tem.quant.config;

import com.tem.quant.handler.MarketDataWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 서버 측 웹소켓 설정 (브라우저 접속 허용)
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final MarketDataWebSocketHandler marketDataWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // "/ws/market" 주소로 웹소켓 연결을 허용합니다.
        registry.addHandler(marketDataWebSocketHandler, "/ws/market")
                .setAllowedOrigins("*"); // 모든 도메인 접속 허용
    }
}
