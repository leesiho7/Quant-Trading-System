package com.tem.quant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 특정 시점의 시장 데이터를 분석한 기술적 지표 값을 저장하는 엔티티
 */
@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalIndicator {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 시점의 시장 데이터(캔들)를 기반으로 계산되었는지 연결 (1:1 관계)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_data_id")
    private MarketData marketData; 

    private Double rsiValue;        // RSI(Relative Strength Index) 값 (0~100)
    
    private Double regValue;        // 선형 회귀(Linear Regression) 예측 값
    
    private Double macdValue;       // MACD(Moving Average Convergence Divergence) 값
    
    // 분석 결과에 따른 매매 신호 (예: "STRONG_BUY", "BUY", "NEUTRAL", "SELL", "STRONG_SELL")
    private String signal;          
}