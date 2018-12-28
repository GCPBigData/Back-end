package br.com.clearinvest.clivserver.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the MarketSector entity.
 */
public class MarketSectorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    private List<StockDTO> stocks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StockDTO> getStocks() {
        return stocks;
    }

    public void setStocks(List<StockDTO> stocks) {
        this.stocks = stocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MarketSectorDTO marketSectorDTO = (MarketSectorDTO) o;
        if (marketSectorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), marketSectorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MarketSectorDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
