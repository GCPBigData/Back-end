package br.com.clearinvest.clivserver.service.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the StockBalance entity.
 */
public class StockBalanceDTO implements Serializable {

    private Long id;

    private ZonedDateTime createdAt;

    @NotNull
    @Size(max = 8)
    private String symbol;

    @NotNull
    private LocalDate day;

    @NotNull
    private BigDecimal balance;

    private Long userId;

    private String userLogin;

    private Long lastFlowEntryId;

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

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public Long getLastFlowEntryId() {
        return lastFlowEntryId;
    }

    public void setLastFlowEntryId(Long stockFlowId) {
        this.lastFlowEntryId = stockFlowId;
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

        StockBalanceDTO stockBalanceDTO = (StockBalanceDTO) o;
        if (stockBalanceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockBalanceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockBalanceDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", day='" + getDay() + "'" +
            ", balance=" + getBalance() +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", lastFlowEntry=" + getLastFlowEntryId() +
            ", stock=" + getStockId() +
            ", stock='" + getStockSymbol() + "'" +
            "}";
    }
}
