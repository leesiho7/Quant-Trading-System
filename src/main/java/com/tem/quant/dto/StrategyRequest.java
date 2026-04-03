package com.tem.quant.dto;

import lombok.Data;

@Data
public class StrategyRequest {
    private String name = "나의 전략";

    // 파이썬 API 연동용 필드
    private String ticker = "BTCUSDT";
    private String startDate;
    private String endDate;
    private String interval = "1d"; // 5m, 15m, 60m, 1d
    private String signalMode = "COMBINED"; // RSI_ONLY, LR_ONLY, COMBINED
    private Double initialCapital = 10000.0;

    // 레거시 필드 (전략 저장용)
    private String symbol = "BTC/USDT";
    private String candleInterval = "1d";

    private Integer rsiPeriod = 14;
    private Integer rsiOverbought = 70;
    private Integer rsiOversold = 30;

    private Integer regPeriod = 20;
    private Double regThreshold = 2.5;

    private Double stopLoss = 5.0;
    private Double takeProfit = 10.0;
    private Double positionSize = 100.0;
}
