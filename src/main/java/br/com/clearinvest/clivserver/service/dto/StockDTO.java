package br.com.clearinvest.clivserver.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Stock entity.
 */
public class StockDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String symbol;

    @NotNull
    @Size(max = 200)
    private String company;

    @NotNull
    @Size(max = 30)
    private String bdr;

    @Size(min = 14, max = 18)
    private String cnpj;

    @Size(max = 300)
    private String main_activity;

    @Size(max = 200)
    private String market_sector;

    @Size(max = 100)
    private String website;

    @NotNull
    private Boolean enabled;

    private Boolean watch;

    private Long marketSectorId;

    private String marketSectorName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBdr() {
        return bdr;
    }

    public void setBdr(String bdr) {
        this.bdr = bdr;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getMain_activity() {
        return main_activity;
    }

    public void setMain_activity(String main_activity) {
        this.main_activity = main_activity;
    }

    public String getMarket_sector() {
        return market_sector;
    }

    public void setMarket_sector(String market_sector) {
        this.market_sector = market_sector;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getWatch() {
        return watch;
    }

    public void setWatch(Boolean watch) {
        this.watch = watch;
    }

    public Long getMarketSectorId() {
        return marketSectorId;
    }

    public void setMarketSectorId(Long marketSectorId) {
        this.marketSectorId = marketSectorId;
    }

    public String getMarketSectorName() {
        return marketSectorName;
    }

    public void setMarketSectorName(String marketSectorName) {
        this.marketSectorName = marketSectorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockDTO stockDTO = (StockDTO) o;
        if (stockDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockDTO{" +
            "id=" + getId() +
            ", symbol='" + getSymbol() + "'" +
            ", company='" + getCompany() + "'" +
            ", bdr='" + getBdr() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", main_activity='" + getMain_activity() + "'" +
            ", market_sector='" + getMarket_sector() + "'" +
            ", website='" + getWebsite() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", marketSector=" + getMarketSectorId() +
            ", marketSector='" + getMarketSectorName() + "'" +
            ", watch='" + getWatch() + "'" +
            "}";
    }
}
