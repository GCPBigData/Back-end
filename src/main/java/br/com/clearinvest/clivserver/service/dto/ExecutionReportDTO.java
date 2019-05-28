package br.com.clearinvest.clivserver.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the ExecutionReport entity.
 */
public class ExecutionReportDTO implements Serializable {

    private Long id;

    @NotNull
    private ZonedDateTime createdAt;

    
    @Lob
    private String execId;

    @NotNull
    @Max(value = 1)
    private String execType;

    @NotNull
    @Max(value = 1)
    private String ordStatus;

    private Integer ordRejReason;

    private Long lastQty;

    private BigDecimal lastPx;

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

    public Long getLastQty() {
        return lastQty;
    }

    public void setLastQty(Long lastQty) {
        this.lastQty = lastQty;
    }

    public BigDecimal getLastPx() {
        return lastPx;
    }

    public void setLastPx(BigDecimal lastPx) {
        this.lastPx = lastPx;
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

        ExecutionReportDTO executionReportDTO = (ExecutionReportDTO) o;
        if (executionReportDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), executionReportDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExecutionReportDTO{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", execId='" + getExecId() + "'" +
            ", execType='" + getExecType() + "'" +
            ", ordStatus='" + getOrdStatus() + "'" +
            ", ordRejReason=" + getOrdRejReason() +
            ", lastQty=" + getLastQty() +
            ", lastPx=" + getLastPx() +
            ", order=" + getOrderId() +
            "}";
    }
}
