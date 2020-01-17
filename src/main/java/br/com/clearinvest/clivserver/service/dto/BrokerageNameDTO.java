package br.com.clearinvest.clivserver.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A DTO for the Brokerage entity (names).
 */
public class BrokerageNameDTO implements Serializable {

    private final Long id;

    @NotNull
    @Size(max = 100)
    private final String name;

    public BrokerageNameDTO(Long id, @NotNull @Size(max = 100) String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BrokerageNameDTO brokerageDTO = (BrokerageNameDTO) o;
        if (brokerageDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), brokerageDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BrokerageNameDTO{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                "}";
    }
}
