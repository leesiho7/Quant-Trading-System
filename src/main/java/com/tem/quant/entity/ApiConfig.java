package com.tem.quant.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 자동매매 봇 연동을 위한 거래소 API 설정 엔티티
 */
@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiConfig {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 거래소 이름 (예: Bybit, Binance, OKX)
    private String exchangeName;   

    // 거래소 발급 API Key
    private String apiKey;

    // 거래소 발급 API Secret (실제 서비스 시 암호화 권장)
    private String apiSecret;      

    // 거래 페어 (예: BTC/USDT)
    private String tradingPair;    
    
    // 봇 활성화 여부 (기본값 false)
    private Boolean isActive = false; 
}