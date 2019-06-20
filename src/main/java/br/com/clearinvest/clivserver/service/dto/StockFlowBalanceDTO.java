package br.com.clearinvest.clivserver.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the Stock entity.
 */
public class StockFlowBalanceDTO implements Serializable {

    private String symbol;
    private Long quantity;
    private BigDecimal totalPrice;
    private BigDecimal feeBrokerage;
    private BigDecimal feeBrokerageIss;
    private BigDecimal feeNegotiation;
    private BigDecimal feeLiquidation;
    private BigDecimal feeRegistry;
    private BigDecimal feeIrrf;

    public StockFlowBalanceDTO(String symbol, Long quantity, BigDecimal totalPrice, BigDecimal feeBrokerage,
            BigDecimal feeBrokerageIss, BigDecimal feeNegotiation, BigDecimal feeLiquidation, BigDecimal feeRegistry,
            BigDecimal feeIrrf) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.feeBrokerage = feeBrokerage;
        this.feeBrokerageIss = feeBrokerageIss;
        this.feeNegotiation = feeNegotiation;
        this.feeLiquidation = feeLiquidation;
        this.feeRegistry = feeRegistry;
        this.feeIrrf = feeIrrf;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getFeeBrokerage() {
        return feeBrokerage;
    }

    public void setFeeBrokerage(BigDecimal feeBrokerage) {
        this.feeBrokerage = feeBrokerage;
    }

    public BigDecimal getFeeBrokerageIss() {
        return feeBrokerageIss;
    }

    public void setFeeBrokerageIss(BigDecimal feeBrokerageIss) {
        this.feeBrokerageIss = feeBrokerageIss;
    }

    public BigDecimal getFeeNegotiation() {
        return feeNegotiation;
    }

    public void setFeeNegotiation(BigDecimal feeNegotiation) {
        this.feeNegotiation = feeNegotiation;
    }

    public BigDecimal getFeeLiquidation() {
        return feeLiquidation;
    }

    public void setFeeLiquidation(BigDecimal feeLiquidation) {
        this.feeLiquidation = feeLiquidation;
    }

    public BigDecimal getFeeRegistry() {
        return feeRegistry;
    }

    public void setFeeRegistry(BigDecimal feeRegistry) {
        this.feeRegistry = feeRegistry;
    }

    public BigDecimal getFeeIrrf() {
        return feeIrrf;
    }

    public void setFeeIrrf(BigDecimal feeIrrf) {
        this.feeIrrf = feeIrrf;
    }
}
