package com.tem.quant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 백테스팅 리포트에 포함되는 개별 매매 기록 엔티티
 */
@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeLog {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이 거래 기록이 어떤 백테스트 리포트에 속하는지 연결 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private BacktestReport report; 

    private String type;             // 거래 유형 (예: "BUY", "SELL")
    private String symbol;           // 거래 대상 (예: "BTC/USDT")
    private Double entryPrice;       // 진입 가격
    private Double exitPrice;        // 청산 가격
    private Double profitPercentage; // 해당 거래의 수익률 (%)
    
    // 거래가 발생한 시점
    private LocalDateTime tradeDate;
}