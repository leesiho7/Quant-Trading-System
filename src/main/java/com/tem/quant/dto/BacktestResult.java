package com.tem.quant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BacktestResult {
    // 요약 통계
    private Double totalReturn;
    private Double winRate;
    private Double mdd;
    private Integer totalTrades;
    private Double finalBalance;

    // 추가 성과 지표
    private Double sharpeRatio;
    private Double avgProfit;
    private Double avgLoss;
    private Integer winningTrades;
    private Integer losingTrades;

    // 차트 데이터
    private List<String> labels;
    private List<Double> equityCurve;
    private List<Double> drawdowns;

    // 거래 내역
    private List<PyTradeLog> tradeLog;

    private String message;
    private String error;
}
