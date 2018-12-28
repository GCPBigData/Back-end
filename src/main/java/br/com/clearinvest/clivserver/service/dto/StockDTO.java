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
    @Size(max = 8)
    private String symbol;

    @NotNull
    @Size(max = 200)
    private String company;

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
            ", marketSector=" + getMarketSectorId() +
            ", marketSector='" + getMarketSectorName() + "'" +
            "}";
    }
}
