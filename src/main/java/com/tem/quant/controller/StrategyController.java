package com.tem.quant.controller;

import com.tem.quant.dto.StrategyRequest;
import com.tem.quant.entity.Strategy;
import com.tem.quant.repository.StrategyRepository;
import com.tem.quant.service.BacktestService; // Service 패키지 경로 확인 필요
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 전략 백테스팅 실행 및 전략 저장을 담당하는 컨트롤러
 */
@RestController
@RequestMapping("/api/strategy")
@RequiredArgsConstructor
public class StrategyController {
    
    private final BacktestService backtestService;
    private final StrategyRepository strategyRepository;

    /**
     * 1. 단순 백테스팅 실행 (DB 저장 안 함)
     * 사용자가 슬라이더를 조절할 때마다 호출하여 메모리 상에서 수익률만 계산합니다.
     */
    @PostMapping("/test")
    public ResponseEntity<?> runTest(@RequestBody StrategyRequest request) {
        // DB를 건드리지 않고 로직만 수행 (비용 절감형)
        var result = backtestService.calculate(request); 
        return ResponseEntity.ok(result);
    }

    /**
     * 2. 전략 확정 저장
     * 테스트 결과가 마음에 들 때 호출하여 DB에 전략 파라미터를 영구 저장합니다.
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveStrategy(@RequestBody StrategyRequest request) {
        // DTO 데이터를 Entity로 변환 (Builder 패턴 사용)
        Strategy strategy = Strategy.builder()
                .name(request.getName())
                .symbol(request.getSymbol())
                .candleInterval(request.getCandleInterval())
                .rsiPeriod(request.getRsiPeriod())
                .rsiOverbought(request.getRsiOverbought())
                .rsiOversold(request.getRsiOversold())
                .regPeriod(request.getRegPeriod())
                .regThreshold(request.getRegThreshold())
                .stopLoss(request.getStopLoss())
                .takeProfit(request.getTakeProfit())
                .positionSize(request.getPositionSize())
                .build();

        strategyRepository.save(strategy);
        return ResponseEntity.ok("전략이 성공적으로 저장되었습니다.");
    }
}