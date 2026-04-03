package com.tem.quant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tem.quant.entity.MarketData;
import com.tem.quant.repository.MarketDataRepository;
import com.tem.quant.handler.MarketDataWebSocketHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 바이비트 웹소켓을 통해 1분봉(Kline)과 실시간 시세(Ticker)를 동시에 수신합니다.
 */
@Service
@RequiredArgsConstructor
public class BybitDataService extends TextWebSocketHandler {

    private final MarketDataRepository marketDataRepository;
    private final MarketDataWebSocketHandler marketDataWebSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private volatile WebSocketSession currentSession;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final String BYBIT_WS_URL = "wss://stream.bybit.com/v5/public/linear";

    @PostConstruct
    public void connect() {
        connectToBybit();
    }

    private void connectToBybit() {
        try {
            StandardWebSocketClient client = new StandardWebSocketClient();
            client.execute(this, BYBIT_WS_URL);
        } catch (Exception e) {
            System.err.println("바이비트 웹소켓 연결 실패: " + e.getMessage());
            scheduleReconnect();
        }
    }

    private void scheduleReconnect() {
        System.out.println("30초 후 재연결 시도...");
        scheduler.schedule(this::connectToBybit, 30, TimeUnit.SECONDS);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.currentSession = session;
        String subMsg = "{\"op\": \"subscribe\", \"args\": [" +
                "\"kline.1.BTCUSDT\", \"kline.5.BTCUSDT\", \"kline.15.BTCUSDT\", " +
                "\"kline.60.BTCUSDT\", \"kline.240.BTCUSDT\", \"kline.D.BTCUSDT\", " +
                "\"ticker.BTCUSDT\"]}";
        session.sendMessage(new TextMessage(subMsg));
        System.out.println("Bybit WebSocket 연결 성공: 1m, 5m, 15m, 1h, 4h, 1d & Ticker 구독");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.err.println("Bybit WebSocket 연결 종료 (code=" + status.getCode() + "). 재연결 예정.");
        this.currentSession = null;
        scheduleReconnect();
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.err.println("Bybit WebSocket 오류: " + exception.getMessage());
        if (session.isOpen()) {
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        marketDataWebSocketHandler.broadcast(payload);

        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String topic = jsonNode.path("topic").asText();

            // 확정된 캔들(confirm=true)만 DB에 저장해 중복 저장 방지
            if (topic.startsWith("kline") && jsonNode.has("data")) {
                String[] parts = topic.split("\\.");
                String interval = parts[1];

                JsonNode dataArray = jsonNode.get("data");
                for (JsonNode node : dataArray) {
                    // confirm 필드가 true일 때만 저장 (완성된 캔들)
                    if (node.path("confirm").asBoolean(false)) {
                        saveMarketData(node, interval);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("메시지 처리 오류: " + e.getMessage());
        }
    }

    private void saveMarketData(JsonNode node, String interval) {
        try {
            MarketData marketData = new MarketData();
            marketData.setSymbol("BTC/USDT");
            marketData.setOpenPrice(node.get("open").asDouble());
            marketData.setHighPrice(node.get("high").asDouble());
            marketData.setLowPrice(node.get("low").asDouble());
            marketData.setClosePrice(node.get("close").asDouble());
            marketData.setVolume(node.get("volume").asDouble());
            marketData.setInterval(interval);

            long millis = node.get("start").asLong();
            marketData.setTimestamp(LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(millis), ZoneId.systemDefault()
            ));

            marketDataRepository.save(marketData);
        } catch (Exception e) {
            System.err.println("MarketData 저장 오류: " + e.getMessage());
        }
    }

    /**
     * 매일 새벽 3시에 오래된 데이터 정리 (최근 7일치만 유지)
     * Railway 무료 MySQL 용량 초과 방지
     */
    @Scheduled(cron = "0 0 3 * * *")
    public void cleanOldData() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(7);
        marketDataRepository.deleteByTimestampBefore(cutoff);
        System.out.println("오래된 MarketData 정리 완료 (7일 이전 데이터 삭제)");
    }
}
