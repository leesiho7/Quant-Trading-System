package com.tem.quant.repository;

import com.tem.quant.entity.BacktestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BacktestReportRepository extends JpaRepository<BacktestReport, Long> {
}