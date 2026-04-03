package com.tem.quant.service;

import com.tem.quant.dto.StrategyRequest;
import com.tem.quant.dto.BacktestResult;

public interface BacktestService {
    /**
     * 사용자의 전략 파라미터를 받아 과거 데이터를 기반으로 수익률을 계산합니다.
     */
    BacktestResult calculate(StrategyRequest request);
}