package br.com.clearinvest.clivserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A StockTrade. Represents the current data of the negotiation.
 */
@Entity
@Table(name = "stock_trade")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockTrade implements Serializable {

    public static final String KIND_TRADE = "T";
    public static final String KIND_STOP_LOSS = "SL";
    public static final String KIND_STOP_GAIN = "SG";

    private static final long serialVersionUID = 1L;

    /** Created in this application */
    public static final String STATUS_LOCAL_NEW = "LN";

    public static final String STATUS_FIX_NEW = "0";
    public static final String STATUS_FIX_PARTIALLY_FILLED = "1";
    public static final String STATUS_FIX_FILLED = "2";
    public static final String STATUS_FIX_CANCELED = "4";
    public static final String STATUS_FIX_REPLACED = "5";
    public static final String STATUS_FIX_PENDING_CANCEL = "6";
    public static final String STATUS_FIX_REJECTED = "8";
    public static final String STATUS_FIX_SUSPENDED = "9";
    public static final String STATUS_FIX_PENDING_NEW = "A";
    public static final String STATUS_FIX_EXPIRED = "C";
    public static final String STATUS_FIX_PENDING_REPLACE = "E";
    public static final String STATUS_FIX_RECEIVED = "R";

    public static final String MARKET_SPOT = "S";
    public static final String MARKET_FUTURES = "F";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Size(max = 45)
    @Column(name = "created_by_ip", length = 45)
    private String createdByIp;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "trade_date", nullable = false)
    private ZonedDateTime tradeDate;

    @NotNull
    @Size(max = 10)
    @Pattern(regexp = "T|SL|SG")
    @Column(name = "kind", length = 10, nullable = false)
    private String kind;

    @NotNull
    @Size(max = 1)
    @Column(name = "market", length = 1, nullable = false)
    private String market;

    @NotNull
    @Size(max = 1)
    @Column(name = "side", length = 1, nullable = false)
    private String side;

    @NotNull
    @Column(name = "manual_entry", nullable = false)
    private Boolean manualEntry;

    @Column(name = "expire_time")
    private ZonedDateTime expireTime;

    @NotNull
    @Min(value = 1L)
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "exec_quantity")
    private Long execQuantity;

    @NotNull
    @DecimalMin(value = "0.01")
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.01")
    @Column(name = "stop_price", precision = 10, scale = 2)
    private BigDecimal stopPrice;

    @Column(name = "average_price", precision = 10, scale = 2)
    private BigDecimal averagePrice;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "total_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "total_price_actual", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalPriceActual;

    @Column(name = "brokerage_fee", precision = 10, scale = 2)
    private BigDecimal brokerageFee;

    @Column(name = "brokerage_fee_iss", precision = 10, scale = 2)
    private BigDecimal brokerageFeeIss;

    @Column(name = "negotiation_perc", precision = 10, scale = 2)
    private BigDecimal negotiationPerc;

    @Column(name = "liquidation_perc", precision = 10, scale = 2)
    private BigDecimal liquidationPerc;

    @Column(name = "registry_perc", precision = 10, scale = 2)
    private BigDecimal registryPerc;

    @Column(name = "irrf_perc", precision = 10, scale = 2)
    private BigDecimal irrfPerc;

    @Column(name = "last_exec_report_time")
    private ZonedDateTime lastExecReportTime;

    @Column(name = "last_exec_report_descr")
    private String lastExecReportDescr;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Stock stock;

    @ManyToOne
    @JsonIgnoreProperties("")
    private BrokerageAccount brokerageAccount;

    @ManyToOne
    @JsonIgnoreProperties("")
    private StockOrder mainOrder;

    @OneToMany(mappedBy = "trade")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<StockOrder> orders = new HashSet<>();
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User createdBy;

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

    public StockTrade createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedByIp() {
        return createdByIp;
    }

    public StockTrade createdByIp(String createdByIp) {
        this.createdByIp = createdByIp;
        return this;
    }

    public void setCreatedByIp(String createdByIp) {
        this.createdByIp = createdByIp;
    }

    public String getStatus() {
        return status;
    }

    public StockTrade status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getTradeDate() {
        return tradeDate;
    }

    public StockTrade tradeDate(ZonedDateTime tradeDate) {
        this.tradeDate = tradeDate;
        return this;
    }

    public void setTradeDate(ZonedDateTime tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getKind() {
        return kind;
    }

    public StockTrade kind(String kind) {
        this.kind = kind;
        return this;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMarket() {
        return market;
    }

    public StockTrade market(String market) {
        this.market = market;
        return this;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getSide() {
        return side;
    }

    public StockTrade side(String side) {
        this.side = side;
        return this;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Boolean isManualEntry() {
        return manualEntry;
    }

    public StockTrade manualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
        return this;
    }

    public void setManualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public ZonedDateTime getExpireTime() {
        return expireTime;
    }

    public StockTrade expireTime(ZonedDateTime expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public void setExpireTime(ZonedDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public Long getQuantity() {
        return quantity;
    }

    public StockTrade quantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getExecQuantity() {
        return execQuantity;
    }

    public StockTrade execQuantity(Long execQuantity) {
        this.execQuantity = execQuantity;
        return this;
    }

    public void setExecQuantity(Long execQuantity) {
        this.execQuantity = execQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public StockTrade unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getStopPrice() {
        return stopPrice;
    }

    public StockTrade stopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
        return this;
    }

    public void setStopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public StockTrade averagePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
        return this;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public StockTrade totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPriceActual() {
        return totalPriceActual;
    }

    public StockTrade totalPriceActual(BigDecimal totalPriceActual) {
        this.totalPriceActual = totalPriceActual;
        return this;
    }

    public void setTotalPriceActual(BigDecimal totalPriceActual) {
        this.totalPriceActual = totalPriceActual;
    }

    public BigDecimal getBrokerageFee() {
        return brokerageFee;
    }

    public StockTrade brokerageFee(BigDecimal brokerageFee) {
        this.brokerageFee = brokerageFee;
        return this;
    }

    public void setBrokerageFee(BigDecimal brokerageFee) {
        this.brokerageFee = brokerageFee;
    }

    public BigDecimal getBrokerageFeeIss() {
        return brokerageFeeIss;
    }

    public StockTrade brokerageFeeIss(BigDecimal brokerageFeeIss) {
        this.brokerageFeeIss = brokerageFeeIss;
        return this;
    }

    public void setBrokerageFeeIss(BigDecimal brokerageFeeIss) {
        this.brokerageFeeIss = brokerageFeeIss;
    }

    public BigDecimal getNegotiationPerc() {
        return negotiationPerc;
    }

    public StockTrade negotiationPerc(BigDecimal negotiationPerc) {
        this.negotiationPerc = negotiationPerc;
        return this;
    }

    public void setNegotiationPerc(BigDecimal negotiationPerc) {
        this.negotiationPerc = negotiationPerc;
    }

    public BigDecimal getLiquidationPerc() {
        return liquidationPerc;
    }

    public StockTrade liquidationPerc(BigDecimal liquidationPerc) {
        this.liquidationPerc = liquidationPerc;
        return this;
    }

    public void setLiquidationPerc(BigDecimal liquidationPerc) {
        this.liquidationPerc = liquidationPerc;
    }

    public BigDecimal getRegistryPerc() {
        return registryPerc;
    }

    public StockTrade registryPerc(BigDecimal registryPerc) {
        this.registryPerc = registryPerc;
        return this;
    }

    public void setRegistryPerc(BigDecimal registryPerc) {
        this.registryPerc = registryPerc;
    }

    public BigDecimal getIrrfPerc() {
        return irrfPerc;
    }

    public StockTrade irrfPerc(BigDecimal irrfPerc) {
        this.irrfPerc = irrfPerc;
        return this;
    }

    public void setIrrfPerc(BigDecimal irrfPerc) {
        this.irrfPerc = irrfPerc;
    }

    public ZonedDateTime getLastExecReportTime() {
        return lastExecReportTime;
    }

    public StockTrade lastExecReportTime(ZonedDateTime lastExecReportTime) {
        this.lastExecReportTime = lastExecReportTime;
        return this;
    }

    public void setLastExecReportTime(ZonedDateTime lastExecReportTime) {
        this.lastExecReportTime = lastExecReportTime;
    }

    public String getLastExecReportDescr() {
        return lastExecReportDescr;
    }

    public StockTrade lastExecReportDescr(String lastExecReportDescr) {
        this.lastExecReportDescr = lastExecReportDescr;
        return this;
    }

    public void setLastExecReportDescr(String lastExecReportDescr) {
        this.lastExecReportDescr = lastExecReportDescr;
    }

    public Stock getStock() {
        return stock;
    }

    public StockTrade stock(Stock stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public BrokerageAccount getBrokerageAccount() {
        return brokerageAccount;
    }

    public StockTrade brokerageAccount(BrokerageAccount brokerageAccount) {
        this.brokerageAccount = brokerageAccount;
        return this;
    }

    public void setBrokerageAccount(BrokerageAccount brokerageAccount) {
        this.brokerageAccount = brokerageAccount;
    }

    public StockOrder getMainOrder() {
        return mainOrder;
    }

    public StockTrade mainOrder(StockOrder stockOrder) {
        this.mainOrder = stockOrder;
        return this;
    }

    public void setMainOrder(StockOrder stockOrder) {
        this.mainOrder = stockOrder;
    }

    public Set<StockOrder> getOrders() {
        return orders;
    }

    public StockTrade orders(Set<StockOrder> stockOrders) {
        this.orders = stockOrders;
        return this;
    }

    public StockTrade addOrders(StockOrder stockOrder) {
        this.orders.add(stockOrder);
        stockOrder.setTrade(this);
        return this;
    }

    public StockTrade removeOrders(StockOrder stockOrder) {
        this.orders.remove(stockOrder);
        stockOrder.setTrade(null);
        return this;
    }

    public void setOrders(Set<StockOrder> stockOrders) {
        this.orders = stockOrders;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public StockTrade createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
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
        StockTrade stockTrade = (StockTrade) o;
        if (stockTrade.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockTrade.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockTrade{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdByIp='" + getCreatedByIp() + "'" +
            ", status='" + getStatus() + "'" +
            ", tradeDate='" + getTradeDate() + "'" +
            ", kind='" + getKind() + "'" +
            ", market='" + getMarket() + "'" +
            ", side='" + getSide() + "'" +
            ", manualEntry='" + isManualEntry() + "'" +
            ", expireTime='" + getExpireTime() + "'" +
            ", quantity=" + getQuantity() +
            ", execQuantity=" + getExecQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", stopPrice=" + getStopPrice() +
            ", averagePrice=" + getAveragePrice() +
            ", totalPrice=" + getTotalPrice() +
            ", totalPriceActual=" + getTotalPriceActual() +
            ", brokerageFee=" + getBrokerageFee() +
            ", brokerageFeeIss=" + getBrokerageFeeIss() +
            ", negotiationPerc=" + getNegotiationPerc() +
            ", liquidationPerc=" + getLiquidationPerc() +
            ", registryPerc=" + getRegistryPerc() +
            ", irrfPerc=" + getIrrfPerc() +
            ", lastExecReportTime='" + getLastExecReportTime() + "'" +
            ", lastExecReportDescr='" + getLastExecReportDescr() + "'" +
            "}";
    }
}
