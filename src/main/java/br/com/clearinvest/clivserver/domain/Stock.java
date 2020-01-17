package br.com.clearinvest.clivserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "symbol", length = 100, nullable = false)
    private String symbol;

    @NotNull
    @Size(max = 200)
    @Column(name = "company", length = 200, nullable = false)
    private String company;

    @NotNull
    @Size(max = 30)
    @Column(name = "bdr", length = 30, nullable = false)
    private String bdr;

    @Size(min = 14, max = 18)
    @Column(name = "cnpj", length = 18)
    private String cnpj;

    @Size(max = 300)
    @Column(name = "main_activity", length = 300)
    private String main_activity;

    @Size(max = 200)
    @Column(name = "market_sector", length = 200)
    private String market_sector;

    @Size(max = 100)
    @Column(name = "website", length = 100)
    private String website;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private MarketSector marketSector;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public Stock symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompany() {
        return company;
    }

    public Stock company(String company) {
        this.company = company;
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBdr() {
        return bdr;
    }

    public Stock bdr(String bdr) {
        this.bdr = bdr;
        return this;
    }

    public void setBdr(String bdr) {
        this.bdr = bdr;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Stock cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getMain_activity() {
        return main_activity;
    }

    public Stock main_activity(String main_activity) {
        this.main_activity = main_activity;
        return this;
    }

    public void setMain_activity(String main_activity) {
        this.main_activity = main_activity;
    }

    public String getMarket_sector() {
        return market_sector;
    }

    public Stock market_sector(String market_sector) {
        this.market_sector = market_sector;
        return this;
    }

    public void setMarket_sector(String market_sector) {
        this.market_sector = market_sector;
    }

    public String getWebsite() {
        return website;
    }

    public Stock website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public MarketSector getMarketSector() {
        return marketSector;
    }

    public Stock marketSector(MarketSector marketSector) {
        this.marketSector = marketSector;
        return this;
    }

    public void setMarketSector(MarketSector marketSector) {
        this.marketSector = marketSector;
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
        Stock stock = (Stock) o;
        if (stock.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stock.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", symbol='" + getSymbol() + "'" +
            ", company='" + getCompany() + "'" +
            ", bdr='" + getBdr() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", main_activity='" + getMain_activity() + "'" +
            ", market_sector='" + getMarket_sector() + "'" +
            ", website='" + getWebsite() + "'" +
            "}";
    }
}
