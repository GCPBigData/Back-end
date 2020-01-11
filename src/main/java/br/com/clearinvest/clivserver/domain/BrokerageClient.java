package br.com.clearinvest.clivserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A BrokerageClient.
 */
@Entity
@Table(name = "brokerage_client")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BrokerageClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "brokerageClients")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Brokerage> brokerages = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BrokerageClient name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Brokerage> getBrokerages() {
        return brokerages;
    }

    public BrokerageClient brokerages(Set<Brokerage> brokerages) {
        this.brokerages = brokerages;
        return this;
    }

    public BrokerageClient addBrokerage(Brokerage brokerage) {
        this.brokerages.add(brokerage);
        brokerage.getBrokerageClients().add(this);
        return this;
    }

    public BrokerageClient removeBrokerage(Brokerage brokerage) {
        this.brokerages.remove(brokerage);
        brokerage.getBrokerageClients().remove(this);
        return this;
    }

    public void setBrokerages(Set<Brokerage> brokerages) {
        this.brokerages = brokerages;
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
        BrokerageClient brokerageClient = (BrokerageClient) o;
        if (brokerageClient.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), brokerageClient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BrokerageClient{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
