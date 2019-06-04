package br.com.clearinvest.clivserver.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the BrokerageFlow entity.
 */
public class BrokerageFlowDTO implements Serializable {

    private Long id;

    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime flowDate;

    @NotNull
    private BigDecimal amount;

    private Boolean manualEntry;

    private Long userId;

    private String userLogin;

    private Long brokerageAccountId;

    private Long tradeId;

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

    public ZonedDateTime getFlowDate() {
        return flowDate;
    }

    public void setFlowDate(ZonedDateTime flowDate) {
        this.flowDate = flowDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BrokerageFlowDTO brokerageFlowDTO = (BrokerageFlowDTO) o;
        if (brokerageFlowDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), brokerageFlowDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BrokerageFlowDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", flowDate='" + getFlowDate() + "'" +
            ", amount=" + getAmount() +
            ", manualEntry='" + isManualEntry() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", brokerageAccount=" + getBrokerageAccountId() +
            ", trade=" + getTradeId() +
            "}";
    }
}
