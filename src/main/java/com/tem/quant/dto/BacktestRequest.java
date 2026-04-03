package com.tem.quant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 사용자가 요청한 백테스팅 파라미터를 담는 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BacktestRequest {
    private String symbol;         // 대상 코인 (예: BTC/USDT)
    private String candleInterval; // 캔들 주기 (예: 1m, 5m, 1h)

    // RSI 파라미터
    private Integer rsiPeriod;
    private Integer rsiOverbought;
    private Integer rsiOversold;

    // 선형 회귀 파라미터
    private Integer regPeriod;
    private Double regThreshold;

    // 리스크 관리
    private Double stopLoss;
    private Double takeProfit;
    private Double positionSize;
}