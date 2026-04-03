package com.tem.quant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 백테스팅 실행 결과 요약 데이터를 담는 엔티티
 */
@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BacktestReport {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 전략 설정을 사용하여 백테스트를 수행했는지 연결 (N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id")
    private Strategy strategy;     

    private Double totalReturn;    // 총 수익률 (예: 15.5)
    private Double maxDrawdown;    // 최대 낙폭 (MDD, 예: -5.2)
    private Double winRate;        // 승률 (예: 65.0)
    private Integer totalTrades;   // 총 거래 횟수 (예: 120)
    private Double sharpeRatio;    // 샤프 지수 (위험 대비 수익 지표)
    
    // 백테스트가 실행된 시간 (기본값 현재시간)
    private LocalDateTime executedAt = LocalDateTime.now();
}