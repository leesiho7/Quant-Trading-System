package com.tem.quant.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 기술적 지표 및 리스크 관리 파라미터를 저장하는 전략 엔티티
 */
@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // 컨트롤러에서 빌더 패턴을 쓰기 위해 반드시 필요합니다!
public class Strategy {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;           // 전략 이름 (예: RSI+회귀 돌파)
    
    // === 대상 설정 ===
    private String symbol;         // 대상 코인 (예: BTC/USDT)
    private String candleInterval; // 캔들 주기 (예: 1m, 5m, 1h)
    
    // === 기술적 지표 파라미터 (RSI) ===
    private Integer rsiPeriod;     // RSI 기간
    private Integer rsiOverbought; // 과매수 구간 (70)
    private Integer rsiOversold;   // 과매도 구간 (30)
    
    // === 기술적 지표 파라미터 (선형 회귀) ===
    private Integer regPeriod;     // Look-back 기간
    private Double regThreshold;   // 편차 임계값 (%)
    
    // === 리스크 관리 ===
    private Double stopLoss;       // 손절 비율 (%)
    private Double takeProfit;     // 익절 비율 (%)
    private Double positionSize;   // 포지션 크기 (%)

    @Builder.Default // 빌더 사용 시 기본값으로 현재 시간을 넣기 위함
    private LocalDateTime createdAt = LocalDateTime.now();

    /* * 주의: Member(User) 엔티티가 완성되면 아래 주석을 해제하여 연결하세요.
     * 지금은 단독 테스트를 위해 주석 처리해 둡니다.
     */
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "member_id")
    // private Member member;             
}