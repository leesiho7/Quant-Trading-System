package com.tem.quant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class PyBacktestResponse {
    private String ticker;
    @JsonProperty("start_date")     private String startDate;
    @JsonProperty("end_date")       private String endDate;
    @JsonProperty("signal_mode")    private String signalMode;

    @JsonProperty("initial_capital")  private Double initialCapital;
    @JsonProperty("final_capital")    private Double finalCapital;
    @JsonProperty("total_return_pct") private Double totalReturnPct;

    @JsonProperty("mdd_pct")   private Double mddPct;
    @JsonProperty("mdd_start") private String mddStart;
    @JsonProperty("mdd_end")   private String mddEnd;
    @JsonProperty("sharpe_ratio") private Double sharpeRatio;

    @JsonProperty("total_trades")   private Integer totalTrades;
    @JsonProperty("winning_trades") private Integer winningTrades;
    @JsonProperty("losing_trades")  private Integer losingTrades;
    @JsonProperty("win_rate_pct")   private Double  winRatePct;
    @JsonProperty("avg_profit")     private Double  avgProfit;
    @JsonProperty("avg_loss")       private Double  avgLoss;
    @JsonProperty("profit_factor")  private Double  profitFactor;

    private List<String> labels;
    @JsonProperty("equity_curve")    private List<Double> equityCurve;
    @JsonProperty("drawdown_curve")  private List<Double> drawdownCurve;
    @JsonProperty("upper_band")      private List<Double> upperBand;
    @JsonProperty("lower_band")      private List<Double> lowerBand;
    @JsonProperty("lr_center")       private List<Double> lrCenter;
    @JsonProperty("trade_log")       private List<PyTradeLog> tradeLog;
}
