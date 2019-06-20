package br.com.clearinvest.clivserver.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the StockTrade entity. This class is used in StockTradeResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /stock-trades?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockTradeCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter createdAt;

    private StringFilter createdByIp;

    private StringFilter status;

    private ZonedDateTimeFilter tradeDate;

    private StringFilter kind;

    private StringFilter market;

    private StringFilter side;

    private BooleanFilter manualEntry;

    private ZonedDateTimeFilter expireTime;

    private LongFilter quantity;

    private LongFilter execQuantity;

    private BigDecimalFilter unitPrice;

    private BigDecimalFilter stopPrice;

    private BigDecimalFilter averagePrice;

    private BigDecimalFilter totalPrice;

    private BigDecimalFilter totalPriceActual;

    private BigDecimalFilter feeBrokerage;

    private BigDecimalFilter feeBrokerageIss;

    private BigDecimalFilter feeNegotiation;

    private BigDecimalFilter feeLiquidation;

    private BigDecimalFilter feeRegistry;

    private BigDecimalFilter feeIrrf;

    private ZonedDateTimeFilter lastExecReportTime;

    private StringFilter lastExecReportDescr;

    private LongFilter stockId;

    private LongFilter brokerageAccountId;

    private LongFilter mainOrderId;

    private LongFilter ordersId;

    private LongFilter createdById;

    public StockTradeCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ZonedDateTimeFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTimeFilter createdAt) {
        this.createdAt = createdAt;
    }

    public StringFilter getCreatedByIp() {
        return createdByIp;
    }

    public void setCreatedByIp(StringFilter createdByIp) {
        this.createdByIp = createdByIp;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public ZonedDateTimeFilter getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(ZonedDateTimeFilter tradeDate) {
        this.tradeDate = tradeDate;
    }

    public StringFilter getKind() {
        return kind;
    }

    public void setKind(StringFilter kind) {
        this.kind = kind;
    }

    public StringFilter getMarket() {
        return market;
    }

    public void setMarket(StringFilter market) {
        this.market = market;
    }

    public StringFilter getSide() {
        return side;
    }

    public void setSide(StringFilter side) {
        this.side = side;
    }

    public BooleanFilter getManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(BooleanFilter manualEntry) {
        this.manualEntry = manualEntry;
    }

    public ZonedDateTimeFilter getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(ZonedDateTimeFilter expireTime) {
        this.expireTime = expireTime;
    }

    public LongFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(LongFilter quantity) {
        this.quantity = quantity;
    }

    public LongFilter getExecQuantity() {
        return execQuantity;
    }

    public void setExecQuantity(LongFilter execQuantity) {
        this.execQuantity = execQuantity;
    }

    public BigDecimalFilter getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimalFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimalFilter getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(BigDecimalFilter stopPrice) {
        this.stopPrice = stopPrice;
    }

    public BigDecimalFilter getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimalFilter averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimalFilter getTotalPriceActual() {
        return totalPriceActual;
    }

    public void setTotalPriceActual(BigDecimalFilter totalPriceActual) {
        this.totalPriceActual = totalPriceActual;
    }

    public BigDecimalFilter getFeeBrokerage() {
        return feeBrokerage;
    }

    public void setFeeBrokerage(BigDecimalFilter feeBrokerage) {
        this.feeBrokerage = feeBrokerage;
    }

    public BigDecimalFilter getFeeBrokerageIss() {
        return feeBrokerageIss;
    }

    public void setFeeBrokerageIss(BigDecimalFilter feeBrokerageIss) {
        this.feeBrokerageIss = feeBrokerageIss;
    }

    public BigDecimalFilter getFeeNegotiation() {
        return feeNegotiation;
    }

    public void setFeeNegotiation(BigDecimalFilter feeNegotiation) {
        this.feeNegotiation = feeNegotiation;
    }

    public BigDecimalFilter getFeeLiquidation() {
        return feeLiquidation;
    }

    public void setFeeLiquidation(BigDecimalFilter feeLiquidation) {
        this.feeLiquidation = feeLiquidation;
    }

    public BigDecimalFilter getFeeRegistry() {
        return feeRegistry;
    }

    public void setFeeRegistry(BigDecimalFilter feeRegistry) {
        this.feeRegistry = feeRegistry;
    }

    public BigDecimalFilter getFeeIrrf() {
        return feeIrrf;
    }

    public void setFeeIrrf(BigDecimalFilter feeIrrf) {
        this.feeIrrf = feeIrrf;
    }

    public ZonedDateTimeFilter getLastExecReportTime() {
        return lastExecReportTime;
    }

    public void setLastExecReportTime(ZonedDateTimeFilter lastExecReportTime) {
        this.lastExecReportTime = lastExecReportTime;
    }

    public StringFilter getLastExecReportDescr() {
        return lastExecReportDescr;
    }

    public void setLastExecReportDescr(StringFilter lastExecReportDescr) {
        this.lastExecReportDescr = lastExecReportDescr;
    }

    public LongFilter getStockId() {
        return stockId;
    }

    public void setStockId(LongFilter stockId) {
        this.stockId = stockId;
    }

    public LongFilter getBrokerageAccountId() {
        return brokerageAccountId;
    }

    public void setBrokerageAccountId(LongFilter brokerageAccountId) {
        this.brokerageAccountId = brokerageAccountId;
    }

    public LongFilter getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(LongFilter mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public LongFilter getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(LongFilter ordersId) {
        this.ordersId = ordersId;
    }

    public LongFilter getCreatedById() {
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockTradeCriteria that = (StockTradeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(createdByIp, that.createdByIp) &&
            Objects.equals(status, that.status) &&
            Objects.equals(tradeDate, that.tradeDate) &&
            Objects.equals(kind, that.kind) &&
            Objects.equals(market, that.market) &&
            Objects.equals(side, that.side) &&
            Objects.equals(manualEntry, that.manualEntry) &&
            Objects.equals(expireTime, that.expireTime) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(execQuantity, that.execQuantity) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(stopPrice, that.stopPrice) &&
            Objects.equals(averagePrice, that.averagePrice) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(totalPriceActual, that.totalPriceActual) &&
            Objects.equals(feeBrokerage, that.feeBrokerage) &&
            Objects.equals(feeBrokerageIss, that.feeBrokerageIss) &&
            Objects.equals(feeNegotiation, that.feeNegotiation) &&
            Objects.equals(feeLiquidation, that.feeLiquidation) &&
            Objects.equals(feeRegistry, that.feeRegistry) &&
            Objects.equals(feeIrrf, that.feeIrrf) &&
            Objects.equals(lastExecReportTime, that.lastExecReportTime) &&
            Objects.equals(lastExecReportDescr, that.lastExecReportDescr) &&
            Objects.equals(stockId, that.stockId) &&
            Objects.equals(brokerageAccountId, that.brokerageAccountId) &&
            Objects.equals(mainOrderId, that.mainOrderId) &&
            Objects.equals(ordersId, that.ordersId) &&
            Objects.equals(createdById, that.createdById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        createdAt,
        createdByIp,
        status,
        tradeDate,
        kind,
        market,
        side,
        manualEntry,
        expireTime,
        quantity,
        execQuantity,
        unitPrice,
        stopPrice,
        averagePrice,
        totalPrice,
        totalPriceActual,
        feeBrokerage,
        feeBrokerageIss,
        feeNegotiation,
        feeLiquidation,
        feeRegistry,
        feeIrrf,
        lastExecReportTime,
        lastExecReportDescr,
        stockId,
        brokerageAccountId,
        mainOrderId,
        ordersId,
        createdById
        );
    }

    @Override
    public String toString() {
        return "StockTradeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (createdByIp != null ? "createdByIp=" + createdByIp + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (tradeDate != null ? "tradeDate=" + tradeDate + ", " : "") +
                (kind != null ? "kind=" + kind + ", " : "") +
                (market != null ? "market=" + market + ", " : "") +
                (side != null ? "side=" + side + ", " : "") +
                (manualEntry != null ? "manualEntry=" + manualEntry + ", " : "") +
                (expireTime != null ? "expireTime=" + expireTime + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (execQuantity != null ? "execQuantity=" + execQuantity + ", " : "") +
                (unitPrice != null ? "unitPrice=" + unitPrice + ", " : "") +
                (stopPrice != null ? "stopPrice=" + stopPrice + ", " : "") +
                (averagePrice != null ? "averagePrice=" + averagePrice + ", " : "") +
                (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
                (totalPriceActual != null ? "totalPriceActual=" + totalPriceActual + ", " : "") +
                (feeBrokerage != null ? "feeBrokerage=" + feeBrokerage + ", " : "") +
                (feeBrokerageIss != null ? "feeBrokerageIss=" + feeBrokerageIss + ", " : "") +
                (feeNegotiation != null ? "feeNegotiation=" + feeNegotiation + ", " : "") +
                (feeLiquidation != null ? "feeLiquidation=" + feeLiquidation + ", " : "") +
                (feeRegistry != null ? "feeRegistry=" + feeRegistry + ", " : "") +
                (feeIrrf != null ? "feeIrrf=" + feeIrrf + ", " : "") +
                (lastExecReportTime != null ? "lastExecReportTime=" + lastExecReportTime + ", " : "") +
                (lastExecReportDescr != null ? "lastExecReportDescr=" + lastExecReportDescr + ", " : "") +
                (stockId != null ? "stockId=" + stockId + ", " : "") +
                (brokerageAccountId != null ? "brokerageAccountId=" + brokerageAccountId + ", " : "") +
                (mainOrderId != null ? "mainOrderId=" + mainOrderId + ", " : "") +
                (ordersId != null ? "ordersId=" + ordersId + ", " : "") +
                (createdById != null ? "createdById=" + createdById + ", " : "") +
            "}";
    }

}
