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
 * A StockFlow.
 */
@Entity
@Table(name = "stock_flow")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @NotNull
    @Column(name = "trade_date", nullable = false)
    private ZonedDateTime tradeDate;

    @NotNull
    @Size(max = 1)
    @Column(name = "side", length = 1, nullable = false)
    private String side;

    @NotNull
    @Size(max = 8)
    @Column(name = "symbol", length = 8, nullable = false)
    private String symbol;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @NotNull
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @NotNull
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

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

    @ManyToOne
    @JsonIgnoreProperties("")
    private ExecReport execReport;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Stock stock;

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

    public StockFlow createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getTradeDate() {
        return tradeDate;
    }

    public StockFlow tradeDate(ZonedDateTime tradeDate) {
        this.tradeDate = tradeDate;
        return this;
    }

    public void setTradeDate(ZonedDateTime tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getSide() {
        return side;
    }

    public StockFlow side(String side) {
        this.side = side;
        return this;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSymbol() {
        return symbol;
    }

    public StockFlow symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getQuantity() {
        return quantity;
    }

    public StockFlow quantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public StockFlow unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public StockFlow totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean isManualEntry() {
        return manualEntry;
    }

    public StockFlow manualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
        return this;
    }

    public void setManualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public User getUser() {
        return user;
    }

    public StockFlow user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BrokerageAccount getBrokerageAccount() {
        return brokerageAccount;
    }

    public StockFlow brokerageAccount(BrokerageAccount brokerageAccount) {
        this.brokerageAccount = brokerageAccount;
        return this;
    }

    public void setBrokerageAccount(BrokerageAccount brokerageAccount) {
        this.brokerageAccount = brokerageAccount;
    }

    public StockTrade getTrade() {
        return trade;
    }

    public StockFlow trade(StockTrade stockTrade) {
        this.trade = stockTrade;
        return this;
    }

    public void setTrade(StockTrade stockTrade) {
        this.trade = stockTrade;
    }

    public ExecReport getExecReport() {
        return execReport;
    }

    public StockFlow execReport(ExecReport execReport) {
        this.execReport = execReport;
        return this;
    }

    public void setExecReport(ExecReport execReport) {
        this.execReport = execReport;
    }

    public Stock getStock() {
        return stock;
    }

    public StockFlow stock(Stock stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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
        StockFlow stockFlow = (StockFlow) o;
        if (stockFlow.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockFlow.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockFlow{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", tradeDate='" + getTradeDate() + "'" +
            ", side='" + getSide() + "'" +
            ", symbol='" + getSymbol() + "'" +
            ", quantity=" + getQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", totalPrice=" + getTotalPrice() +
            ", manualEntry='" + isManualEntry() + "'" +
            "}";
    }
}
