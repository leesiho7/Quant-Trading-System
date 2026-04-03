package com.tem.quant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tem.quant.entity.MarketData;
import com.tem.quant.repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BybitApiService {

    private final MarketDataRepository marketDataRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 바이비트 V5 K-Line API 주소
    private final String BYBIT_API_URL = "https://api.bybit.com/v5/market/kline?category=linear&symbol=%s&interval=%s&limit=%d";

    public void fetchHistoricalData(String symbol, String interval, int limit) {
        String url = String.format(BYBIT_API_URL, symbol.replace("/", ""), interval, limit);
        
        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode list = root.path("result").path("list");

            List<MarketData> marketDataList = new ArrayList<>();
            for (JsonNode node : list) {
                MarketData data = new MarketData();
                data.setSymbol(symbol);
                data.setOpenPrice(node.get(1).asDouble());
                data.setHighPrice(node.get(2).asDouble());
                data.setLowPrice(node.get(3).asDouble());
                data.setClosePrice(node.get(4).asDouble());
                data.setVolume(node.get(5).asDouble());
                data.setInterval(interval);
                
                // 타임스탬프 변환 (ms -> LocalDateTime)
                long millis = node.get(0).asLong();
                data.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault()));
                
                marketDataList.add(data);
            }
            marketDataRepository.saveAll(marketDataList);
            System.out.println(symbol + " 과거 데이터 저장 완료: " + marketDataList.size() + "건");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}