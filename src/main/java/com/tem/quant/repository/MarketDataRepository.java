package com.tem.quant.repository;

import com.tem.quant.entity.MarketData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 실시간 시세 및 캔들 데이터를 DB에서 조회하고 관리하는 레포지토리
 */
@Repository
public interface MarketDataRepository extends JpaRepository<MarketData, Long> {

    /**
     * 특정 코인의 최신 데이터를 지정된 개수(100개)만큼 가져옵니다.
     * 차트의 초기 로딩 시 마지막 100개의 캔들을 그릴 때 사용합니다.
     * * @param symbol   코인 심볼 (예: "BTC/USDT")
     * @param interval 캔들 주기 (예: "1m", "5m", "1h")
     * @return 최신순으로 정렬된 마켓 데이터 리스트
     */
    List<MarketData> findTop100BySymbolAndIntervalOrderByTimestampDesc(String symbol, String interval);

    /**
     * 특정 시간 이후에 생성된 데이터만 가져옵니다.
     * 실시간 차트 업데이트 시, 마지막으로 받은 데이터 이후의 새로운 캔들만 추가할 때 유용합니다.
     * * @param symbol 코인 심볼 (예: "BTC/USDT")
     * @param time   기준 시간
     * @return 기준 시간 이후의 데이터 리스트
     */
    List<MarketData> findBySymbolAndTimestampAfter(String symbol, LocalDateTime time);
    
    // 추가 팁: 특정 기간 사이의 데이터를 가져오고 싶을 때 사용
    List<MarketData> findBySymbolAndTimestampBetween(String symbol, LocalDateTime start, LocalDateTime end);
}