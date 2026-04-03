package com.tem.quant.repository;

import com.tem.quant.entity.TechnicalIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 기술적 지표(RSI, 선형회귀 등) 데이터를 DB에서 조회하고 관리하는 레포지토리
 */
@Repository
public interface TechnicalIndicatorRepository extends JpaRepository<TechnicalIndicator, Long> {

    /**
     * 특정 코인 심볼(예: BTC/USDT)에 해당하는 모든 지표 데이터를 가져옵니다.
     * JOIN FETCH를 사용하여 연관된 MarketData 엔티티도 한 번에 가져오므로 성능이 최적화됩니다.
     *
     * @param symbol 코인 심볼 (예: "BTC/USDT")
     * @return 해당 심볼의 지표 데이터 리스트
     */
    @Query("SELECT ti FROM TechnicalIndicator ti JOIN FETCH ti.marketData WHERE ti.marketData.symbol = :symbol")
    List<TechnicalIndicator> findLatestIndicators(@Param("symbol") String symbol);
}