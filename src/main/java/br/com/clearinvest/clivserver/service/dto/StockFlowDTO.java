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
    @Size(max = 8)
    private String symbol;

    @NotNull
    private Long quantity;

    @NotNull
    private BigDecimal totalValue;

    private Long userId;

    private String userLogin;

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

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
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
            ", symbol='" + getSymbol() + "'" +
            ", quantity=" + getQuantity() +
            ", totalValue=" + getTotalValue() +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", execReport=" + getExecReportId() +
            ", execReport='" + getExecReportExecId() + "'" +
            ", stock=" + getStockId() +
            ", stock='" + getStockSymbol() + "'" +
            "}";
    }
}
