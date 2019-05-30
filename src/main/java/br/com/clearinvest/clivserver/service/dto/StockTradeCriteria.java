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

    private ZonedDateTimeFilter lastExecReportTime;

    private StringFilter lastExecReportDescr;

    private StringFilter createdByIp;

    private StringFilter kind;

    private StringFilter side;

    private ZonedDateTimeFilter expireTime;

    private LongFilter quantity;

    private LongFilter execQuantity;

    private BigDecimalFilter unitPrice;

    private BigDecimalFilter stopPrice;

    private BigDecimalFilter averagePrice;

    private BigDecimalFilter stockTotalPrice;

    private BigDecimalFilter totalPrice;

    private StringFilter status;

    private LongFilter stockId;

    private LongFilter brokerageAccountId;

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

    public StringFilter getCreatedByIp() {
        return createdByIp;
    }

    public void setCreatedByIp(StringFilter createdByIp) {
        this.createdByIp = createdByIp;
    }

    public StringFilter getKind() {
        return kind;
    }

    public void setKind(StringFilter kind) {
        this.kind = kind;
    }

    public StringFilter getSide() {
        return side;
    }

    public void setSide(StringFilter side) {
        this.side = side;
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

    public BigDecimalFilter getStockTotalPrice() {
        return stockTotalPrice;
    }

    public void setStockTotalPrice(BigDecimalFilter stockTotalPrice) {
        this.stockTotalPrice = stockTotalPrice;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
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
            Objects.equals(lastExecReportTime, that.lastExecReportTime) &&
            Objects.equals(lastExecReportDescr, that.lastExecReportDescr) &&
            Objects.equals(createdByIp, that.createdByIp) &&
            Objects.equals(kind, that.kind) &&
            Objects.equals(side, that.side) &&
            Objects.equals(expireTime, that.expireTime) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(execQuantity, that.execQuantity) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(stopPrice, that.stopPrice) &&
            Objects.equals(averagePrice, that.averagePrice) &&
            Objects.equals(stockTotalPrice, that.stockTotalPrice) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(status, that.status) &&
            Objects.equals(stockId, that.stockId) &&
            Objects.equals(brokerageAccountId, that.brokerageAccountId) &&
            Objects.equals(ordersId, that.ordersId) &&
            Objects.equals(createdById, that.createdById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        createdAt,
        lastExecReportTime,
        lastExecReportDescr,
        createdByIp,
        kind,
        side,
        expireTime,
        quantity,
        execQuantity,
        unitPrice,
        stopPrice,
        averagePrice,
        stockTotalPrice,
        totalPrice,
        status,
        stockId,
        brokerageAccountId,
        ordersId,
        createdById
        );
    }

    @Override
    public String toString() {
        return "StockTradeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (lastExecReportTime != null ? "lastExecReportTime=" + lastExecReportTime + ", " : "") +
                (lastExecReportDescr != null ? "lastExecReportDescr=" + lastExecReportDescr + ", " : "") +
                (createdByIp != null ? "createdByIp=" + createdByIp + ", " : "") +
                (kind != null ? "kind=" + kind + ", " : "") +
                (side != null ? "side=" + side + ", " : "") +
                (expireTime != null ? "expireTime=" + expireTime + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (execQuantity != null ? "execQuantity=" + execQuantity + ", " : "") +
                (unitPrice != null ? "unitPrice=" + unitPrice + ", " : "") +
                (stopPrice != null ? "stopPrice=" + stopPrice + ", " : "") +
                (averagePrice != null ? "averagePrice=" + averagePrice + ", " : "") +
                (stockTotalPrice != null ? "stockTotalPrice=" + stockTotalPrice + ", " : "") +
                (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (stockId != null ? "stockId=" + stockId + ", " : "") +
                (brokerageAccountId != null ? "brokerageAccountId=" + brokerageAccountId + ", " : "") +
                (ordersId != null ? "ordersId=" + ordersId + ", " : "") +
                (createdById != null ? "createdById=" + createdById + ", " : "") +
            "}";
    }

}
