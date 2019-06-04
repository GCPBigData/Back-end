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
 * Criteria class for the BrokerageFlow entity. This class is used in BrokerageFlowResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /brokerage-flows?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BrokerageFlowCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter createdAt;

    private ZonedDateTimeFilter flowDate;

    private BigDecimalFilter value;

    private BooleanFilter manualEntry;

    private LongFilter userId;

    private LongFilter brokerageAccountId;

    private LongFilter tradeId;

    public BrokerageFlowCriteria() {
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

    public BigDecimalFilter getValue() {
        return value;
    }

    public void setValue(BigDecimalFilter value) {
        this.value = value;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BrokerageFlowCriteria that = (BrokerageFlowCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(flowDate, that.flowDate) &&
            Objects.equals(value, that.value) &&
            Objects.equals(manualEntry, that.manualEntry) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(brokerageAccountId, that.brokerageAccountId) &&
            Objects.equals(tradeId, that.tradeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        createdAt,
        flowDate,
        value,
        manualEntry,
        userId,
        brokerageAccountId,
        tradeId
        );
    }

    @Override
    public String toString() {
        return "BrokerageFlowCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (flowDate != null ? "flowDate=" + flowDate + ", " : "") +
                (value != null ? "value=" + value + ", " : "") +
                (manualEntry != null ? "manualEntry=" + manualEntry + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (brokerageAccountId != null ? "brokerageAccountId=" + brokerageAccountId + ", " : "") +
                (tradeId != null ? "tradeId=" + tradeId + ", " : "") +
            "}";
    }

}
