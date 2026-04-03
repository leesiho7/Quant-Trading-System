package com.tem.quant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PyBacktestRequest {

    @JsonProperty("data_provider") private DataProviderConfig dataProvider;
    private StrategyConfig strategy;
    private BacktestConfig backtest;

    @Data @Builder
    public static class DataProviderConfig {
        private String ticker;
        @JsonProperty("start_date") private String startDate;
        @JsonProperty("end_date")   private String endDate;
        private String interval;
    }

    @Data @Builder
    public static class StrategyConfig {
        @JsonProperty("signal_mode") private String signalMode;
        private RsiConfig rsi;
        @JsonProperty("linear_regression") private LinearRegressionConfig linearRegression;
        @JsonProperty("lr_v2") private LrV2Config lrV2;
    }

    @Data @Builder
    public static class LrV2Config {
        @JsonProperty("window_size")   private Integer windowSize;
        @JsonProperty("multiplier")    private Double  multiplier;
        @JsonProperty("rsi_filter")    private Boolean rsiFilter;
        @JsonProperty("rsi_threshold") private Double  rsiThreshold;
    }

    @Data @Builder
    public static class RsiConfig {
        private Integer period;
        private Double oversold;
        private Double overbought;
    }

    @Data @Builder
    public static class LinearRegressionConfig {
        private Integer period;
        @JsonProperty("slope_threshold") private Double slopeThreshold;
    }

    @Data @Builder
    public static class BacktestConfig {
        @JsonProperty("initial_capital")   private Double initialCapital;
        @JsonProperty("position_size_pct") private Double positionSizePct;
        @JsonProperty("commission_pct")    private Double commissionPct;
        @JsonProperty("slippage_pct")      private Double slippagePct;
    }
}
