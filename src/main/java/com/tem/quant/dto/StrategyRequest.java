package com.tem.quant.dto;

import lombok.Data;

@Data
public class StrategyRequest {
    private String name = "나의 전략";
    private String symbol = "BTC/USDT";
    private String candleInterval = "1m";

    // 필드 선언 시 기본값을 할당해두면 "권장값" 역할을 합니다.
    private Integer rsiPeriod = 14;
    private Integer rsiOverbought = 70;
    private Integer rsiOversold = 30;
    
    private Integer regPeriod = 50;
    private Double regThreshold = 2.5;
    
    private Double stopLoss = 5.0;
    private Double takeProfit = 10.0;
    private Double positionSize = 25.0;
}