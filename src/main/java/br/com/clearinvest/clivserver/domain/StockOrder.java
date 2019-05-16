package br.com.clearinvest.clivserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A StockOrder.
 */
@Entity
@Table(name = "stock_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String KIND_TRADE = "T";
    public static final String KIND_STOP_LOSS = "SL";
    public static final String KIND_STOP_GAIN = "SG";
    public static final String KIND_CANCEL = "C";
    public static final String KIND_REPLACE = "R";

    public static final String FIX_SIDE_BUY = "1";
    public static final String FIX_SIDE_SELL = "2";

    public static final String FIX_ORD_TYPE_MARKET = "1";
    public static final String FIX_ORD_TYPE_STOP_LIMIT = "4";

    public static final String FIX_TIME_IN_FORCE_GOOD_TILL_DATE = "6";

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

    // TODO adicionar campo originalOrder ou targetOrder, para armazenar ordem q está sendo alterada ou cancelada

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "day_seq")
    private Long daySeq;

    /** compra/venda, cancelamento, alteração, etc */
    @NotNull
    @Size(max = 10)
    @Pattern(regexp = "T|SL|SG|C|R")
    @Column(name = "kind", length = 10, nullable = false)
    private String kind;

    /** mercado, limite, etc */
    @Column(name = "order_type")
    private String orderType;

    @NotNull
    @Size(max = 1)
    @Column(name = "side", length = 1, nullable = false)
    private String side;

    @Size(max = 1)
    @Column(name = "time_in_force", length = 1)
    private String timeInForce;

    @Column(name = "expire_time")
    private ZonedDateTime expireTime;

    /** day trade ou swing trade */
    @Size(max = 1)
    @Column(name = "operation_type", length = 1)
    private String operationType;

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

    // TODO remover
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "oms_order_id")
    private String omsOrderId;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    // TODO remover, foi movido para StockTrade
    @Column(name = "last_exec_report_time")
    private ZonedDateTime lastExecReportTime;

    // TODO remover, foi movido para StockTrade
    @Column(name = "last_exec_report_descr")
    private String lastExecReportDescr;

    @Size(max = 45)
    @Column(name = "created_by_ip", length = 45)
    private String createdByIp;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Stock stock;

    @ManyToOne
    @JsonIgnoreProperties("")
    private BrokerageAccount brokerageAccount;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User createdBy;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("orders")
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

    public StockOrder createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getDaySeq() {
        return daySeq;
    }

    public StockOrder daySeq(Long daySeq) {
        this.daySeq = daySeq;
        return this;
    }

    public void setDaySeq(Long daySeq) {
        this.daySeq = daySeq;
    }

    public String getKind() {
        return kind;
    }

    public StockOrder kind(String kind) {
        this.kind = kind;
        return this;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getOrderType() {
        return orderType;
    }

    public StockOrder orderType(String orderType) {
        this.orderType = orderType;
        return this;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSide() {
        return side;
    }

    public StockOrder side(String side) {
        this.side = side;
        return this;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getTimeInForce() {
        return timeInForce;
    }

    public StockOrder timeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
        return this;
    }

    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    public ZonedDateTime getExpireTime() {
        return expireTime;
    }

    public StockOrder expireTime(ZonedDateTime expireTime) {
        this.expireTime = expireTime;
        return this;
    }

    public void setExpireTime(ZonedDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public String getOperationType() {
        return operationType;
    }

    public StockOrder operationType(String operationType) {
        this.operationType = operationType;
        return this;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Long getQuantity() {
        return quantity;
    }

    public StockOrder quantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getExecQuantity() {
        return execQuantity;
    }

    public StockOrder execQuantity(Long execQuantity) {
        this.execQuantity = execQuantity;
        return this;
    }

    public void setExecQuantity(Long execQuantity) {
        this.execQuantity = execQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public StockOrder unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getStopPrice() {
        return stopPrice;
    }

    public StockOrder stopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
        return this;
    }

    public void setStopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public StockOrder averagePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
        return this;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public StockOrder totalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOmsOrderId() {
        return omsOrderId;
    }

    public StockOrder omsOrderId(String omsOrderId) {
        this.omsOrderId = omsOrderId;
        return this;
    }

    public void setOmsOrderId(String omsOrderId) {
        this.omsOrderId = omsOrderId;
    }

    public String getStatus() {
        return status;
    }

    public StockOrder status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getLastExecReportTime() {
        return lastExecReportTime;
    }

    public StockOrder lastExecReportTime(ZonedDateTime lastExecReportTime) {
        this.lastExecReportTime = lastExecReportTime;
        return this;
    }

    public void setLastExecReportTime(ZonedDateTime lastExecReportTime) {
        this.lastExecReportTime = lastExecReportTime;
    }

    public String getLastExecReportDescr() {
        return lastExecReportDescr;
    }

    public StockOrder lastExecReportDescr(String lastExecReportDescr) {
        this.lastExecReportDescr = lastExecReportDescr;
        return this;
    }

    public void setLastExecReportDescr(String lastExecReportDescr) {
        this.lastExecReportDescr = lastExecReportDescr;
    }

    public String getCreatedByIp() {
        return createdByIp;
    }

    public StockOrder createdByIp(String createdByIp) {
        this.createdByIp = createdByIp;
        return this;
    }

    public void setCreatedByIp(String createdByIp) {
        this.createdByIp = createdByIp;
    }

    public Stock getStock() {
        return stock;
    }

    public StockOrder stock(Stock stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public BrokerageAccount getBrokerageAccount() {
        return brokerageAccount;
    }

    public StockOrder brokerageAccount(BrokerageAccount brokerageAccount) {
        this.brokerageAccount = brokerageAccount;
        return this;
    }

    public void setBrokerageAccount(BrokerageAccount brokerageAccount) {
        this.brokerageAccount = brokerageAccount;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public StockOrder createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public StockTrade getTrade() {
        return trade;
    }

    public StockOrder trade(StockTrade stockTrade) {
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
        StockOrder stockOrder = (StockOrder) o;
        if (stockOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockOrder{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", daySeq=" + getDaySeq() +
            ", kind='" + getKind() + "'" +
            ", orderType='" + getOrderType() + "'" +
            ", side='" + getSide() + "'" +
            ", timeInForce='" + getTimeInForce() + "'" +
            ", expireTime='" + getExpireTime() + "'" +
            ", operationType='" + getOperationType() + "'" +
            ", quantity=" + getQuantity() +
            ", execQuantity=" + getExecQuantity() +
            ", unitPrice=" + getUnitPrice() +
            ", stopPrice=" + getStopPrice() +
            ", averagePrice=" + getAveragePrice() +
            ", totalPrice=" + getTotalPrice() +
            ", omsOrderId='" + getOmsOrderId() + "'" +
            ", status='" + getStatus() + "'" +
            ", lastExecReportTime='" + getLastExecReportTime() + "'" +
            ", lastExecReportDescr='" + getLastExecReportDescr() + "'" +
            ", createdByIp='" + getCreatedByIp() + "'" +
            "}";
    }
}
