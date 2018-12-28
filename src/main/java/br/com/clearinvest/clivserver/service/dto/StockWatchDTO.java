package br.com.clearinvest.clivserver.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the StockWatch entity.
 */
public class StockWatchDTO implements Serializable {

    private Long id;

    private Long stockId;

    private String stockCompany;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStockId() {
        return stockId;
    }

    public void setStockId(Long stockId) {
        this.stockId = stockId;
    }

    public String getStockCompany() {
        return stockCompany;
    }

    public void setStockCompany(String stockCompany) {
        this.stockCompany = stockCompany;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StockWatchDTO stockWatchDTO = (StockWatchDTO) o;
        if (stockWatchDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockWatchDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StockWatchDTO{" +
            "id=" + getId() +
            ", stock=" + getStockId() +
            ", stock='" + getStockCompany() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}
