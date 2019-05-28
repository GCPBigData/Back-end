package br.com.clearinvest.clivserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A ExecutionReport.
 */
@Entity
@Table(name = "execution_report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExecutionReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    
    @Lob
    @Column(name = "exec_id", nullable = false)
    private String execId;

    @NotNull
    @Max(value = 1)
    @Column(name = "exec_type", nullable = false)
    private String execType;

    @NotNull
    @Max(value = 1)
    @Column(name = "ord_status", nullable = false)
    private String ordStatus;

    @Column(name = "ord_rej_reason")
    private Integer ordRejReason;

    @Column(name = "last_qty")
    private Long lastQty;

    @Column(name = "last_px", precision = 10, scale = 2)
    private BigDecimal lastPx;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private StockOrder order;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ExecutionReport createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getExecId() {
        return execId;
    }

    public ExecutionReport execId(String execId) {
        this.execId = execId;
        return this;
    }

    public void setExecId(String execId) {
        this.execId = execId;
    }

    public String getExecType() {
        return execType;
    }

    public ExecutionReport execType(String execType) {
        this.execType = execType;
        return this;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public String getOrdStatus() {
        return ordStatus;
    }

    public ExecutionReport ordStatus(String ordStatus) {
        this.ordStatus = ordStatus;
        return this;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    public Integer getOrdRejReason() {
        return ordRejReason;
    }

    public ExecutionReport ordRejReason(Integer ordRejReason) {
        this.ordRejReason = ordRejReason;
        return this;
    }

    public void setOrdRejReason(Integer ordRejReason) {
        this.ordRejReason = ordRejReason;
    }

    public Long getLastQty() {
        return lastQty;
    }

    public ExecutionReport lastQty(Long lastQty) {
        this.lastQty = lastQty;
        return this;
    }

    public void setLastQty(Long lastQty) {
        this.lastQty = lastQty;
    }

    public BigDecimal getLastPx() {
        return lastPx;
    }

    public ExecutionReport lastPx(BigDecimal lastPx) {
        this.lastPx = lastPx;
        return this;
    }

    public void setLastPx(BigDecimal lastPx) {
        this.lastPx = lastPx;
    }

    public StockOrder getOrder() {
        return order;
    }

    public ExecutionReport order(StockOrder stockOrder) {
        this.order = stockOrder;
        return this;
    }

    public void setOrder(StockOrder stockOrder) {
        this.order = stockOrder;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExecutionReport executionReport = (ExecutionReport) o;
        if (executionReport.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), executionReport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExecutionReport{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", execId='" + getExecId() + "'" +
            ", execType='" + getExecType() + "'" +
            ", ordStatus='" + getOrdStatus() + "'" +
            ", ordRejReason=" + getOrdRejReason() +
            ", lastQty=" + getLastQty() +
            ", lastPx=" + getLastPx() +
            "}";
    }
}
