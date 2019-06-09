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
    private BigDecimal totalPriceActual;

    public StockFlowBalanceDTO(String symbol, Long quantity, BigDecimal totalPrice) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
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

    public BigDecimal getTotalPriceActual() {
        return totalPriceActual;
    }

    public void setTotalPriceActual(BigDecimal totalPriceActual) {
        this.totalPriceActual = totalPriceActual;
    }
}
