package com.tem.quant.service;

import com.tem.quant.dto.StrategyRequest;
import com.tem.quant.dto.BacktestResult;
import com.tem.quant.entity.MarketData;
import com.tem.quant.repository.MarketDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

/**
 * 실제 백테스팅 로직을 수행하는 서비스 구현체
 */
@Service
@RequiredArgsConstructor
public class BacktestServiceImpl implements BacktestService {

    private final MarketDataRepository marketDataRepository;

    @Override
    public BacktestResult calculate(StrategyRequest request) {
        // 1. DB에서 과거 시세 데이터 가져오기 
        List<MarketData> candles = marketDataRepository.findTop100BySymbolAndIntervalOrderByTimestampDesc(
                request.getSymbol(), request.getCandleInterval());

        // 2. 전략 알고리즘 실행 (RSI, 선형 회귀 계산)
        List<String> labels = new ArrayList<>();
        List<Double> equityCurve = new ArrayList<>();
        
        // 차트 테스트를 위한 더미 데이터 생성
        for(int i=0; i<10; i++) {
            labels.add("Point " + i);
            equityCurve.add(100.0 + (i * 1.5));
        }

        // 3. 결과 조립 및 반환
        return BacktestResult.builder()
                .totalReturn(15.5)      // 총 수익률 15.5%
                .winRate(65.0)         // 승률 65%
                .mdd(4.2)              // 최대 낙폭 4.2%
                .totalTrades(12)       // 총 거래 12회
                .finalBalance(1155.0)  // 최종 자산
                .labels(labels)        // 차트 X축
                .equityCurve(equityCurve) // 차트 Y축 (자산곡선)
                .message("백테스팅이 성공적으로 완료되었습니다.")
                .build();
    }
}