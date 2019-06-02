package br.com.clearinvest.clivserver.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the StockFlow entity.
 */
public class StockFlowDTO implements Serializable {

    private Long id;

    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime tradeDate;

    @NotNull
    @Size(max = 1)
    private String side;

    @NotNull
    @Size(max = 8)
    private String symbol;

    @NotNull
    private Long quantity;

    @NotNull
    private BigDecimal unitPrice;

    @NotNull
    private BigDecimal totalPrice;

    @NotNull
    private Boolean manualEntry;

    private Long userId;

    private String userLogin;

    private Long brokerageAccountId;

    private Long tradeId;

    private Long execReportId;

    private String execReportExecId;

    private Long stockId;

    private String stockSymbol;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(ZonedDateTime tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean isManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getBrokerageAccountId() {
        return brokerageAccountId;
    }

    public void setBrokerageAccountId(Long brokerageAccountId) {
        this.brokerageAccountId = brokerageAccountId;
    }

    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long stockTradeId) {
        this.tradeId = stockTradeId;
    }

    public Long getExecReportId() {
        return execReportId;
    }

    public void setExecReportId(Long executionReportId) {
        this.execReportId = executionReportId;
    }

    public String getExecReportExecId() {
        return execReportExecId;
    }

    public void setExecReportExecId(String executionReportExecId) {
        this.execReportExecId = executionReportExecId;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockFlowDTO stockFlowDTO = (StockFlowDTO) o;
        if (stockFlowDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockFlowDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockFlowDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", tradeDate='" + getTradeDate() + "'" +
            ", side='" + getSide() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", totalPrice=" + getTotalPrice() +
            ", manualEntry='" + isManualEntry() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", brokerageAccount=" + getBrokerageAccountId() +
            ", trade=" + getTradeId() +
            ", execReport=" + getExecReportId() +
            ", execReport='" + getExecReportExecId() + "'" +
            ", stock=" + getStockId() +
            ", stock='" + getStockSymbol() + "'" +
            "}";
    }
}
