package com.tem.quant.controller;

import com.tem.quant.entity.MarketData;
import com.tem.quant.repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketRestController {

    private final MarketDataRepository marketDataRepository;

    /**
     * 차트 초기 로딩을 위한 최근 캔들 데이터 조회
     */
    @GetMapping("/candles")
    public List<MarketData> getCandles(
            @RequestParam(defaultValue = "BTC/USDT") String symbol,
            @RequestParam(defaultValue = "1") String interval) {
        
        // 최신 100개 데이터를 가져와서 시간순(오름차순)으로 반환해야 차트가 왼쪽에서 오른쪽으로 그려집니다.
        List<MarketData> data = marketDataRepository.findTop100BySymbolAndIntervalOrderByTimestampDesc(symbol, interval);
        
        // 리스트를 뒤집어서 시간순으로 정렬
        java.util.Collections.reverse(data);
        return data;
    }
}
