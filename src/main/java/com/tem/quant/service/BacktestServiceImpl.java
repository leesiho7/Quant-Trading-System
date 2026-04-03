package com.tem.quant.service;

import com.tem.quant.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class BacktestServiceImpl implements BacktestService {

    private final RestTemplate restTemplate;

    @Value("${python.api.url}")
    private String pythonApiUrl;

    @Override
    public BacktestResult calculate(StrategyRequest req) {
        try {
            PyBacktestRequest pyReq = PyBacktestRequest.builder()
                .dataProvider(PyBacktestRequest.DataProviderConfig.builder()
                    .ticker(req.getTicker())
                    .startDate(req.getStartDate())
                    .endDate(req.getEndDate())
                    .interval(req.getInterval())
                    .build())
                .strategy(PyBacktestRequest.StrategyConfig.builder()
                    .signalMode(req.getSignalMode())
                    .rsi(PyBacktestRequest.RsiConfig.builder()
                        .period(req.getRsiPeriod())
                        .oversold(req.getRsiOversold().doubleValue())
                        .overbought(req.getRsiOverbought().doubleValue())
                        .build())
                    .linearRegression(PyBacktestRequest.LinearRegressionConfig.builder()
                        .period(req.getRegPeriod())
                        .slopeThreshold(0.0)
                        .build())
                    .lrV2(PyBacktestRequest.LrV2Config.builder()
                        .windowSize(req.getLrV2WindowSize())
                        .multiplier(req.getLrV2Multiplier())
                        .rsiFilter(req.getLrV2RsiFilter())
                        .rsiThreshold(70.0)
                        .build())
                    .build())
                .backtest(PyBacktestRequest.BacktestConfig.builder()
                    .initialCapital(req.getInitialCapital())
                    .positionSizePct(req.getPositionSize() / 100.0)
                    .commissionPct(0.001)
                    .slippagePct(0.0)
                    .takeProfitPct(req.getTakeProfit() != null ? req.getTakeProfit() / 100.0 : 0.0)
                    .stopLossPct(req.getStopLoss()    != null ? req.getStopLoss()    / 100.0 : 0.0)
                    .build())
                .build();

            log.info("파이썬 백테스팅 API 호출: {} {}", req.getTicker(), req.getSignalMode());

            ResponseEntity<PyBacktestResponse> resp = restTemplate.postForEntity(
                pythonApiUrl + "/api/v1/backtest",
                pyReq,
                PyBacktestResponse.class
            );

            PyBacktestResponse py = resp.getBody();
            if (py == null) {
                return errorResult("파이썬 API에서 빈 응답을 반환했습니다.");
            }

            return BacktestResult.builder()
                .totalReturn(py.getTotalReturnPct())
                .winRate(py.getWinRatePct())
                .mdd(py.getMddPct())
                .totalTrades(py.getTotalTrades())
                .finalBalance(py.getFinalCapital())
                .sharpeRatio(py.getSharpeRatio())
                .avgProfit(py.getAvgProfit())
                .avgLoss(py.getAvgLoss())
                .winningTrades(py.getWinningTrades())
                .losingTrades(py.getLosingTrades())
                .labels(py.getLabels())
                .equityCurve(py.getEquityCurve())
                .drawdowns(py.getDrawdownCurve())
                .upperBand(py.getUpperBand())
                .lowerBand(py.getLowerBand())
                .lrCenter(py.getLrCenter())
                .tradeLog(py.getTradeLog())
                .message("백테스팅이 성공적으로 완료되었습니다.")
                .build();

        } catch (HttpClientErrorException e) {
            log.error("파이썬 API 오류: {} {}", e.getStatusCode(), e.getResponseBodyAsString());
            return errorResult("데이터 오류: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("백테스팅 실패: {}", e.getMessage());
            return errorResult("파이썬 API 연결 실패: " + e.getMessage());
        }
    }

    private BacktestResult errorResult(String msg) {
        return BacktestResult.builder().error(msg).build();
    }
}
