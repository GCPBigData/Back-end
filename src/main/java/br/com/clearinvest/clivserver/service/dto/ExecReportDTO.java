package br.com.clearinvest.clivserver.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the ExecReport entity.
 */
public class ExecReportDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime createdAt;

    @NotNull
    private ZonedDateTime transactTime;

    
    @Lob
    private String execId;

    @NotNull
    @Max(value = 1)
    private String execType;

    @NotNull
    @Max(value = 1)
    private String ordStatus;

    private Integer ordRejReason;

    @Lob
    private String execText;

    private Long lastQty;

    private Long leavesQty;

    private Long cumQty;

    private BigDecimal lastPx;

    private BigDecimal avgPx;

    @Lob
    private String fixMessage;

    private Long orderId;

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

    public ZonedDateTime getTransactTime() {
        return transactTime;
    }

    public void setTransactTime(ZonedDateTime transactTime) {
        this.transactTime = transactTime;
    }

    public String getExecId() {
        return execId;
    }

    public void setExecId(String execId) {
        this.execId = execId;
    }

    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public String getOrdStatus() {
        return ordStatus;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    public Integer getOrdRejReason() {
        return ordRejReason;
    }

    public void setOrdRejReason(Integer ordRejReason) {
        this.ordRejReason = ordRejReason;
    }

    public String getExecText() {
        return execText;
    }

    public void setExecText(String execText) {
        this.execText = execText;
    }

    public Long getLastQty() {
        return lastQty;
    }

    public void setLastQty(Long lastQty) {
        this.lastQty = lastQty;
    }

    public Long getLeavesQty() {
        return leavesQty;
    }

    public void setLeavesQty(Long leavesQty) {
        this.leavesQty = leavesQty;
    }

    public Long getCumQty() {
        return cumQty;
    }

    public void setCumQty(Long cumQty) {
        this.cumQty = cumQty;
    }

    public BigDecimal getLastPx() {
        return lastPx;
    }

    public void setLastPx(BigDecimal lastPx) {
        this.lastPx = lastPx;
    }

    public BigDecimal getAvgPx() {
        return avgPx;
    }

    public void setAvgPx(BigDecimal avgPx) {
        this.avgPx = avgPx;
    }

    public String getFixMessage() {
        return fixMessage;
    }

    public void setFixMessage(String fixMessage) {
        this.fixMessage = fixMessage;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long stockOrderId) {
        this.orderId = stockOrderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExecReportDTO execReportDTO = (ExecReportDTO) o;
        if (execReportDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), execReportDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExecReportDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", transactTime='" + getTransactTime() + "'" +
            ", execId='" + getExecId() + "'" +
            ", execType='" + getExecType() + "'" +
            ", ordStatus='" + getOrdStatus() + "'" +
            ", ordRejReason=" + getOrdRejReason() +
            ", execText='" + getExecText() + "'" +
            ", lastQty=" + getLastQty() +
            ", leavesQty=" + getLeavesQty() +
            ", cumQty=" + getCumQty() +
            ", lastPx=" + getLastPx() +
            ", avgPx=" + getAvgPx() +
            ", fixMessage='" + getFixMessage() + "'" +
            ", order=" + getOrderId() +
            "}";
    }
}
