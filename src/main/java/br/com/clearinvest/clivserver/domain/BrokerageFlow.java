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
 * A BrokerageFlow.
 */
@Entity
@Table(name = "brokerage_flow")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BrokerageFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "flow_date", nullable = false)
    private ZonedDateTime flowDate;

    @NotNull
    @Column(name = "jhi_value", precision = 10, scale = 2, nullable = false)
    private BigDecimal value;

    @NotNull
    @Column(name = "manual_entry", nullable = false)
    private Boolean manualEntry;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("")
    private BrokerageAccount brokerageAccount;

    @ManyToOne
    @JsonIgnoreProperties("")
    private StockTrade trade;

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

    public BrokerageFlow createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getFlowDate() {
        return flowDate;
    }

    public BrokerageFlow flowDate(ZonedDateTime flowDate) {
        this.flowDate = flowDate;
        return this;
    }

    public void setFlowDate(ZonedDateTime flowDate) {
        this.flowDate = flowDate;
    }

    public BigDecimal getValue() {
        return value;
    }

    public BrokerageFlow value(BigDecimal value) {
        this.value = value;
        return this;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean isManualEntry() {
        return manualEntry;
    }

    public BrokerageFlow manualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
        return this;
    }

    public void setManualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public User getUser() {
        return user;
    }

    public BrokerageFlow user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BrokerageAccount getBrokerageAccount() {
        return brokerageAccount;
    }

    public BrokerageFlow brokerageAccount(BrokerageAccount brokerageAccount) {
        this.brokerageAccount = brokerageAccount;
        return this;
    }

    public void setBrokerageAccount(BrokerageAccount brokerageAccount) {
        this.brokerageAccount = brokerageAccount;
    }

    public StockTrade getTrade() {
        return trade;
    }

    public BrokerageFlow trade(StockTrade stockTrade) {
        this.trade = stockTrade;
        return this;
    }

    public void setTrade(StockTrade stockTrade) {
        this.trade = stockTrade;
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
        BrokerageFlow brokerageFlow = (BrokerageFlow) o;
        if (brokerageFlow.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), brokerageFlow.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BrokerageFlow{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", flowDate='" + getFlowDate() + "'" +
            ", value=" + getValue() +
            ", manualEntry='" + isManualEntry() + "'" +
            "}";
    }
}
