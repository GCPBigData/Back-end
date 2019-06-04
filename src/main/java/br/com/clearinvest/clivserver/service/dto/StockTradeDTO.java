package br.com.clearinvest.clivserver.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the StockTrade entity.
 */
public class StockTradeDTO implements Serializable {

    private Long id;

    private ZonedDateTime createdAt;

    @Size(max = 45)
    private String createdByIp;

    private String status;

    @NotNull
    private ZonedDateTime tradeDate;

    @NotNull
    @Size(max = 10)
    @Pattern(regexp = "T|SL|SG")
    private String kind;

    @Size(max = 1)
    private String market;

    @NotNull
    @Size(max = 1)
    private String side;

    @NotNull
    private Boolean manualEntry;

    //@JsonFormat(pattern = "yyyy-MM-dd'T'mm:HH:ss.SSSZZZZZ")
    private ZonedDateTime expireTime;

    @NotNull
    @Min(value = 1L)
    private Long quantity;

    private Long execQuantity;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal unitPrice;

    @DecimalMin(value = "0.01")
    private BigDecimal stopPrice;

    private BigDecimal averagePrice;

    @DecimalMin(value = "0.01")
    private BigDecimal totalPrice;

    @DecimalMin(value = "0.01")
    private BigDecimal totalPriceActual;

    private BigDecimal brokerageFee;

    private BigDecimal brokerageFeeIss;

    private BigDecimal negotiationPerc;

    private BigDecimal liquidationPerc;

    private BigDecimal registryPerc;

    private BigDecimal irrfPerc;

    private ZonedDateTime lastExecReportTime;

    private String lastExecReportDescr;

    private Long stockId;

    private String stockSymbol;

    private Long brokerageAccountId;

    private Long mainOrderId;

    private Long createdById;

    private String createdByLogin;

    // derived fields

    private String statusDescr;
    private boolean canCancel;
    private boolean canEdit;
    private BigDecimal brokerageFeeIssVal;
    private BigDecimal negotiationVal;
    private BigDecimal liquidationVal;
    private BigDecimal registryVal;
    private BigDecimal irrfVal;

    // getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedByIp() {
        return createdByIp;
    }

    public void setCreatedByIp(String createdByIp) {
        this.createdByIp = createdByIp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ZonedDateTime getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(ZonedDateTime tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Boolean isManualEntry() {
        return manualEntry;
    }

    public void setManualEntry(Boolean manualEntry) {
        this.manualEntry = manualEntry;
    }

    public ZonedDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(ZonedDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getExecQuantity() {
        return execQuantity;
    }

    public void setExecQuantity(Long execQuantity) {
        this.execQuantity = execQuantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(BigDecimal stopPrice) {
        this.stopPrice = stopPrice;
    }

    public BigDecimal getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(BigDecimal averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getTotalPriceActual() {
        return totalPriceActual;
    }

    public void setTotalPriceActual(BigDecimal totalPriceActual) {
        this.totalPriceActual = totalPriceActual;
    }

    public BigDecimal getBrokerageFee() {
        return brokerageFee;
    }

    public void setBrokerageFee(BigDecimal brokerageFee) {
        this.brokerageFee = brokerageFee;
    }

    public BigDecimal getBrokerageFeeIss() {
        return brokerageFeeIss;
    }

    public void setBrokerageFeeIss(BigDecimal brokerageFeeIss) {
        this.brokerageFeeIss = brokerageFeeIss;
    }

    public BigDecimal getNegotiationPerc() {
        return negotiationPerc;
    }

    public void setNegotiationPerc(BigDecimal negotiationPerc) {
        this.negotiationPerc = negotiationPerc;
    }

    public BigDecimal getLiquidationPerc() {
        return liquidationPerc;
    }

    public void setLiquidationPerc(BigDecimal liquidationPerc) {
        this.liquidationPerc = liquidationPerc;
    }

    public BigDecimal getRegistryPerc() {
        return registryPerc;
    }

    public void setRegistryPerc(BigDecimal registryPerc) {
        this.registryPerc = registryPerc;
    }

    public BigDecimal getIrrfPerc() {
        return irrfPerc;
    }

    public void setIrrfPerc(BigDecimal irrfPerc) {
        this.irrfPerc = irrfPerc;
    }

    public ZonedDateTime getLastExecReportTime() {
        return lastExecReportTime;
    }

    public void setLastExecReportTime(ZonedDateTime lastExecReportTime) {
        this.lastExecReportTime = lastExecReportTime;
    }

    public String getLastExecReportDescr() {
        return lastExecReportDescr;
    }

    public void setLastExecReportDescr(String lastExecReportDescr) {
        this.lastExecReportDescr = lastExecReportDescr;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Long getBrokerageAccountId() {
        return brokerageAccountId;
    }

    public void setBrokerageAccountId(Long brokerageAccountId) {
        this.brokerageAccountId = brokerageAccountId;
    }

    public Long getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(Long stockOrderId) {
        this.mainOrderId = stockOrderId;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public String getStatusDescr() {
        return statusDescr;
    }

    public void setStatusDescr(String statusDescr) {
        this.statusDescr = statusDescr;
    }

    public boolean isCanCancel() {
        return canCancel;
    }

    public void setCanCancel(boolean canCancel) {
        this.canCancel = canCancel;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public BigDecimal getBrokerageFeeIssVal() {
        return brokerageFeeIssVal;
    }

    public void setBrokerageFeeIssVal(BigDecimal brokerageFeeIssVal) {
        this.brokerageFeeIssVal = brokerageFeeIssVal;
    }

    public BigDecimal getNegotiationVal() {
        return negotiationVal;
    }

    public void setNegotiationVal(BigDecimal negotiationVal) {
        this.negotiationVal = negotiationVal;
    }

    public BigDecimal getLiquidationVal() {
        return liquidationVal;
    }

    public void setLiquidationVal(BigDecimal liquidationVal) {
        this.liquidationVal = liquidationVal;
    }

    public BigDecimal getRegistryVal() {
        return registryVal;
    }

    public void setRegistryVal(BigDecimal registryVal) {
        this.registryVal = registryVal;
    }

    public BigDecimal getIrrfVal() {
        return irrfVal;
    }

    public void setIrrfVal(BigDecimal irrfVal) {
        this.irrfVal = irrfVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockTradeDTO stockTradeDTO = (StockTradeDTO) o;
        if (stockTradeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockTradeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockTradeDTO{" +
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
            ", stock=" + getStockId() +
            ", stock='" + getStockSymbol() + "'" +
            ", brokerageAccount=" + getBrokerageAccountId() +
            ", mainOrder=" + getMainOrderId() +
            ", createdBy=" + getCreatedById() +
            ", createdBy='" + getCreatedByLogin() + "'" +
            "}";
    }
}
