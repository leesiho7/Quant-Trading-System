package com.tem.quant.repository;

import com.tem.quant.entity.TradeLog;
import com.tem.quant.entity.BacktestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TradeLogRepository extends JpaRepository<TradeLog, Long> {
    // 특정 리포트 아이디에 해당하는 거래 내역만 가져오는 메서드 추가 가능
    List<TradeLog> findByReport(BacktestReport report);
}