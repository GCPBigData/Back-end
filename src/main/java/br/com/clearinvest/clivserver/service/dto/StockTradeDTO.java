package br.com.clearinvest.clivserver.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the StockTrade entity.
 */
public class StockTradeDTO implements Serializable {

    private Long id;

    private ZonedDateTime createdAt;

    private ZonedDateTime lastExecReportTime;

    private String lastExecReportDescr;

    @Size(max = 45)
    private String createdByIp;

    @NotNull
    @Size(max = 1)
    private String side;

    private ZonedDateTime expireTime;

    @NotNull
    @Min(value = 1L)
    private Long quantity;

    private Long execQuantity;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.01")
    private BigDecimal stopPrice;

    private BigDecimal averagePrice;

    private BigDecimal stockTotalPrice;

    private BigDecimal totalPrice;

    private String status;

    @NotNull
    @Size(max = 2)
    @Pattern(regexp = "N|SL|SG")
    private String type;

    private Long stockId;

    private String stockSymbol;

    private Long brokerageAccountId;

    private Long createdById;

    private String createdByLogin;

    // derived fields

    private String statusDescr;
    private boolean canCancel;
    private boolean canEdit;

    // getters and setters

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

    public ZonedDateTime getLastExecReportTime() {
        return lastExecReportTime;
    }

    public void setLastExecReportTime(ZonedDateTime lastExecReportTime) {
        this.lastExecReportTime = lastExecReportTime;
    }

    public String getLastExecReportDescr() {
        return lastExecReportDescr;
    }

    public void setLastExecReportDescr(String lastExecReportDescr) {
        this.lastExecReportDescr = lastExecReportDescr;
    }

    public String getCreatedByIp() {
        return createdByIp;
    }

    public void setCreatedByIp(String createdByIp) {
        this.createdByIp = createdByIp;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public ZonedDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(ZonedDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getExecQuantity() {
        return execQuantity;
    }

    public void setExecQuantity(Long execQuantity) {
        this.execQuantity = execQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getStockTotalPrice() {
        return stockTotalPrice;
    }

    public void setStockTotalPrice(BigDecimal stockTotalPrice) {
        this.stockTotalPrice = stockTotalPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Long getBrokerageAccountId() {
        return brokerageAccountId;
    }

    public void setBrokerageAccountId(Long brokerageAccountId) {
        this.brokerageAccountId = brokerageAccountId;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public String getStatusDescr() {
        return statusDescr;
    }

    public void setStatusDescr(String statusDescr) {
        this.statusDescr = statusDescr;
    }

    public boolean isCanCancel() {
        return canCancel;
    }

    public void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockTradeDTO stockTradeDTO = (StockTradeDTO) o;
        if (stockTradeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockTradeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockTradeDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", lastExecReportTime='" + getLastExecReportTime() + "'" +
            ", lastExecReportDescr='" + getLastExecReportDescr() + "'" +
            ", createdByIp='" + getCreatedByIp() + "'" +
            ", side='" + getSide() + "'" +
            ", expireTime='" + getExpireTime() + "'" +
            ", quantity=" + getQuantity() +
            ", execQuantity=" + getExecQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", stopPrice=" + getStopPrice() +
            ", averagePrice=" + getAveragePrice() +
            ", stockTotalPrice=" + getStockTotalPrice() +
            ", totalPrice=" + getTotalPrice() +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", stock=" + getStockId() +
            ", stock='" + getStockSymbol() + "'" +
            ", brokerageAccount=" + getBrokerageAccountId() +
            ", createdBy=" + getCreatedById() +
            ", createdBy='" + getCreatedByLogin() + "'" +
            "}";
    }
}
