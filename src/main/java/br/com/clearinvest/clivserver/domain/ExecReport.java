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
 * A ExecReport.
 */
@Entity
@Table(name = "exec_report")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ExecReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "transact_time", nullable = false)
    private ZonedDateTime transactTime;

    
    @Lob
    @Column(name = "exec_id", nullable = false)
    private String execId;

    @NotNull
    @Column(name = "exec_type", nullable = false)
    private String execType;

    @NotNull
    @Column(name = "ord_status", nullable = false)
    private String ordStatus;

    @Column(name = "ord_rej_reason")
    private Integer ordRejReason;

    @Lob
    @Column(name = "exec_text")
    private String execText;

    @Column(name = "last_qty")
    private Long lastQty;

    @Column(name = "leaves_qty")
    private Long leavesQty;

    @Column(name = "cum_qty")
    private Long cumQty;

    @Column(name = "last_px", precision = 10, scale = 2)
    private BigDecimal lastPx;

    @Column(name = "avg_px", precision = 10, scale = 2)
    private BigDecimal avgPx;

    @Lob
    @Column(name = "fix_message")
    private String fixMessage;

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

    public ExecReport createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getTransactTime() {
        return transactTime;
    }

    public ExecReport transactTime(ZonedDateTime transactTime) {
        this.transactTime = transactTime;
        return this;
    }

    public void setTransactTime(ZonedDateTime transactTime) {
        this.transactTime = transactTime;
    }

    public String getExecId() {
        return execId;
    }

    public ExecReport execId(String execId) {
        this.execId = execId;
        return this;
    }

    public void setExecId(String execId) {
        this.execId = execId;
    }

    public String getExecType() {
        return execType;
    }

    public ExecReport execType(String execType) {
        this.execType = execType;
        return this;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public String getOrdStatus() {
        return ordStatus;
    }

    public ExecReport ordStatus(String ordStatus) {
        this.ordStatus = ordStatus;
        return this;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    public Integer getOrdRejReason() {
        return ordRejReason;
    }

    public ExecReport ordRejReason(Integer ordRejReason) {
        this.ordRejReason = ordRejReason;
        return this;
    }

    public void setOrdRejReason(Integer ordRejReason) {
        this.ordRejReason = ordRejReason;
    }

    public String getExecText() {
        return execText;
    }

    public ExecReport execText(String execText) {
        this.execText = execText;
        return this;
    }

    public void setExecText(String execText) {
        this.execText = execText;
    }

    public Long getLastQty() {
        return lastQty;
    }

    public ExecReport lastQty(Long lastQty) {
        this.lastQty = lastQty;
        return this;
    }

    public void setLastQty(Long lastQty) {
        this.lastQty = lastQty;
    }

    public Long getLeavesQty() {
        return leavesQty;
    }

    public ExecReport leavesQty(Long leavesQty) {
        this.leavesQty = leavesQty;
        return this;
    }

    public void setLeavesQty(Long leavesQty) {
        this.leavesQty = leavesQty;
    }

    public Long getCumQty() {
        return cumQty;
    }

    public ExecReport cumQty(Long cumQty) {
        this.cumQty = cumQty;
        return this;
    }

    public void setCumQty(Long cumQty) {
        this.cumQty = cumQty;
    }

    public BigDecimal getLastPx() {
        return lastPx;
    }

    public ExecReport lastPx(BigDecimal lastPx) {
        this.lastPx = lastPx;
        return this;
    }

    public void setLastPx(BigDecimal lastPx) {
        this.lastPx = lastPx;
    }

    public BigDecimal getAvgPx() {
        return avgPx;
    }

    public ExecReport avgPx(BigDecimal avgPx) {
        this.avgPx = avgPx;
        return this;
    }

    public void setAvgPx(BigDecimal avgPx) {
        this.avgPx = avgPx;
    }

    public String getFixMessage() {
        return fixMessage;
    }

    public ExecReport fixMessage(String fixMessage) {
        this.fixMessage = fixMessage;
        return this;
    }

    public void setFixMessage(String fixMessage) {
        this.fixMessage = fixMessage;
    }

    public StockOrder getOrder() {
        return order;
    }

    public ExecReport order(StockOrder stockOrder) {
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
        ExecReport execReport = (ExecReport) o;
        if (execReport.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), execReport.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ExecReport{" +
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
            "}";
    }
}
