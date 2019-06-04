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
 * Criteria class for the StockFlow entity. This class is used in StockFlowResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /stock-flows?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StockFlowCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter flowDate;

    private StringFilter side;

    private StringFilter symbol;

    private LongFilter quantity;

    private BigDecimalFilter unitPrice;

    private BigDecimalFilter totalPrice;

    private BooleanFilter manualEntry;

    private LongFilter userId;

    private LongFilter brokerageAccountId;

    private LongFilter tradeId;

    private LongFilter execReportId;

    private LongFilter stockId;

    public StockFlowCriteria() {
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

    public ZonedDateTimeFilter getFlowDate() {
        return flowDate;
    }

    public void setFlowDate(ZonedDateTimeFilter flowDate) {
        this.flowDate = flowDate;
    }

    public StringFilter getSide() {
        return side;
    }

    public void setSide(StringFilter side) {
        this.side = side;
    }

    public StringFilter getSymbol() {
        return symbol;
    }

    public void setSymbol(StringFilter symbol) {
        this.symbol = symbol;
    }

    public LongFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(LongFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimalFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BooleanFilter getManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(BooleanFilter manualEntry) {
        this.manualEntry = manualEntry;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getBrokerageAccountId() {
        return brokerageAccountId;
    }

    public void setBrokerageAccountId(LongFilter brokerageAccountId) {
        this.brokerageAccountId = brokerageAccountId;
    }

    public LongFilter getTradeId() {
        return tradeId;
    }

    public void setTradeId(LongFilter tradeId) {
        this.tradeId = tradeId;
    }

    public LongFilter getExecReportId() {
        return execReportId;
    }

    public void setExecReportId(LongFilter execReportId) {
        this.execReportId = execReportId;
    }

    public LongFilter getStockId() {
        return stockId;
    }

    public void setStockId(LongFilter stockId) {
        this.stockId = stockId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StockFlowCriteria that = (StockFlowCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(flowDate, that.flowDate) &&
            Objects.equals(side, that.side) &&
            Objects.equals(symbol, that.symbol) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(manualEntry, that.manualEntry) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(brokerageAccountId, that.brokerageAccountId) &&
            Objects.equals(tradeId, that.tradeId) &&
            Objects.equals(execReportId, that.execReportId) &&
            Objects.equals(stockId, that.stockId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        createdAt,
        flowDate,
        side,
        symbol,
        quantity,
        unitPrice,
        totalPrice,
        manualEntry,
        userId,
        brokerageAccountId,
        tradeId,
        execReportId,
        stockId
        );
    }

    @Override
    public String toString() {
        return "StockFlowCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (flowDate != null ? "flowDate=" + flowDate + ", " : "") +
                (side != null ? "side=" + side + ", " : "") +
                (symbol != null ? "symbol=" + symbol + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (unitPrice != null ? "unitPrice=" + unitPrice + ", " : "") +
                (totalPrice != null ? "totalPrice=" + totalPrice + ", " : "") +
                (manualEntry != null ? "manualEntry=" + manualEntry + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (brokerageAccountId != null ? "brokerageAccountId=" + brokerageAccountId + ", " : "") +
                (tradeId != null ? "tradeId=" + tradeId + ", " : "") +
                (execReportId != null ? "execReportId=" + execReportId + ", " : "") +
                (stockId != null ? "stockId=" + stockId + ", " : "") +
            "}";
    }

}
