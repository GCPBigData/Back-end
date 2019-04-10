package br.com.clearinvest.clivserver.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the StockOrder entity.
 */
public class StockOrderDTO implements Serializable {

    private Long id;

    private ZonedDateTime createdAt;

    private Long daySeq;

    private String orderType;

    @NotNull
    @Size(max = 1)
    private String side;

    @Size(max = 1)
    private String timeInForce;

    @Size(max = 1)
    private String operationType;

    @NotNull
    @Min(value = 1L)
    private Long quantity;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private String omsOrderId;

    private String status;

    private ZonedDateTime lastExecReportTime;

    private String lastExecReportDescr;

    private Long stockId;

    private String stockSymbol;

    @NotNull
    private Long brokerageAccountId;

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

    public Long getDaySeq() {
        return daySeq;
    }

    public void setDaySeq(Long daySeq) {
        this.daySeq = daySeq;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
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

    public String getOmsOrderId() {
        return omsOrderId;
    }

    public void setOmsOrderId(String omsOrderId) {
        this.omsOrderId = omsOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockOrderDTO stockOrderDTO = (StockOrderDTO) o;
        if (stockOrderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockOrderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockOrderDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", daySeq=" + getDaySeq() +
            ", orderType='" + getOrderType() + "'" +
            ", side='" + getSide() + "'" +
            ", timeInForce='" + getTimeInForce() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", totalPrice=" + getTotalPrice() +
            ", omsOrderId='" + getOmsOrderId() + "'" +
            ", status='" + getStatus() + "'" +
            ", lastExecReportTime='" + getLastExecReportTime() + "'" +
            ", lastExecReportDescr='" + getLastExecReportDescr() + "'" +
            ", stock=" + getStockId() +
            ", stock='" + getStockSymbol() + "'" +
            ", brokerageAccount=" + getBrokerageAccountId() +
            "}";
    }
}
