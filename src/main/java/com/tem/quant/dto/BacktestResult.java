package com.tem.quant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 백테스팅 계산 결과를 담아 프론트엔드로 전달하는 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BacktestResult {
    // 요약 통계
    private Double totalReturn;      // 총 수익률 (%)
    private Double winRate;          // 승률 (%)
    private Double mdd;              // 최대 낙폭 (Max Drawdown)
    private Integer totalTrades;     // 총 거래 횟수
    private Double finalBalance;     // 최종 자산 합계

    // 차트 데이터 (시간 흐름에 따른 데이터)
    private List<String> labels;      // X축: 시간 (예: "2024-04-03 20:00")
    private List<Double> equityCurve; // Y축: 자산 변화 곡선
    private List<Double> drawdowns;   // Y축: 낙폭 차트 데이터

    private String message;          // 실행 결과 메시지
}