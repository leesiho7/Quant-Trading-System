package com.tem.quant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tem.quant.entity.MarketData;
import com.tem.quant.repository.MarketDataRepository;
import com.tem.quant.handler.MarketDataWebSocketHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 바이비트 웹소켓을 통해 1분봉(Kline)과 실시간 시세(Ticker)를 동시에 수신합니다.
 */
@Service
@RequiredArgsConstructor
public class BybitDataService extends TextWebSocketHandler {

    private final MarketDataRepository marketDataRepository;
    private final MarketDataWebSocketHandler marketDataWebSocketHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void connect() {
        try {
            StandardWebSocketClient client = new StandardWebSocketClient();
            String url = "wss://stream.bybit.com/v5/public/linear";
            client.execute(this, url);
        } catch (Exception e) {
            System.err.println("바이비트 웹소켓 연결 실패: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 여러 주기를 한 번에 구독합니다 (1, 5, 15, 60, 240, D)
        String subMsg = "{\"op\": \"subscribe\", \"args\": [" +
                "\"kline.1.BTCUSDT\", \"kline.5.BTCUSDT\", \"kline.15.BTCUSDT\", " +
                "\"kline.60.BTCUSDT\", \"kline.240.BTCUSDT\", \"kline.D.BTCUSDT\", " +
                "\"ticker.BTCUSDT\"]}";
        session.sendMessage(new TextMessage(subMsg));
        System.out.println("Bybit WebSocket 구독 확장: 1m, 5m, 15m, 1h, 4h, 1d & Ticker");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        marketDataWebSocketHandler.broadcast(payload);

        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String topic = jsonNode.path("topic").asText();

            // kline 데이터 처리 및 DB 저장
            if (topic.startsWith("kline") && jsonNode.has("data")) {
                // 토픽명에서 인터벌 추출 (예: kline.5.BTCUSDT -> 5)
                String[] parts = topic.split("\\.");
                String interval = parts[1]; 
                
                JsonNode dataArray = jsonNode.get("data");
                for (JsonNode node : dataArray) {
                    saveMarketData(node, interval);
                }
            }
        } catch (Exception e) {
            // 에러 로그 생략
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
            marketData.setInterval(interval); // 주기 저장
            
            long millis = node.get("start").asLong();
            marketData.setTimestamp(LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(millis), ZoneId.systemDefault()
            ));

            marketDataRepository.save(marketData);
        } catch (Exception e) {
        }
    }
}