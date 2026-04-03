package com.tem.quant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * 실시간 시세 및 캔들(OHLCV) 데이터를 저장하는 엔티티
 */
@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "market_data", indexes = {
    @Index(name = "idx_symbol_time", columnList = "symbol, timestamp")
})
public class MarketData {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;          // 거래 페어 (예: BTC/USDT)
    
    private Double openPrice;       // 시가
    
    private Double highPrice;       // 고가
    
    private Double lowPrice;        // 저가
    
    private Double closePrice;      // 종가 (현재가)
    
    private Double volume;          // 거래량
    
    private LocalDateTime timestamp; // 데이터 시간 (캔들 생성 시각)
    
    @Column(name = "candle_interval") // interval은 SQL 예약어일 수 있어 컬럼명 지정 권장
    private String interval;        // 캔들 주기 (1m, 5m, 1h, 1d)
}