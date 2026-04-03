package com.tem.quant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PyTradeLog {
    @JsonProperty("trade_no")    private Integer tradeNo;
    @JsonProperty("entry_date")  private String  entryDate;
    @JsonProperty("entry_price") private Double  entryPrice;
    @JsonProperty("exit_date")   private String  exitDate;
    @JsonProperty("exit_price")  private Double  exitPrice;
    @JsonProperty("exit_type")   private String  exitType;
    @JsonProperty("return_pct")  private Double  returnPct;
    @JsonProperty("net_pnl")     private Double  netPnl;
    @JsonProperty("is_winner")   private Boolean isWinner;
    @JsonProperty("holding_bars") private Integer holdingBars;
}
