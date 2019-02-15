package br.com.clearinvest.clivserver.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Brokerage.
 */
@Entity
@Table(name = "brokerage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Brokerage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Size(min = 14, max = 14)
    @Column(name = "cnpj", length = 14, nullable = false, unique = true)
    private String cnpj;

    @NotNull
    @Size(max = 100)
    @Column(name = "address", length = 100, nullable = false)
    private String address;

    @Size(max = 100)
    @Column(name = "address_neighborhood", length = 100)
    private String addressNeighborhood;

    @NotNull
    @Size(max = 100)
    @Column(name = "address_city", length = 100, nullable = false)
    private String addressCity;

    @NotNull
    @Size(min = 2, max = 2)
    @Column(name = "address_state", length = 2, nullable = false)
    private String addressState;

    @NotNull
    @Column(name = "swing_trade", nullable = false)
    private Boolean swingTrade;

    @NotNull
    @Column(name = "day_trade", nullable = false)
    private Boolean dayTrade;

    @NotNull
    @Column(name = "fee_brokerage", precision = 10, scale = 2, nullable = false)
    private BigDecimal feeBrokerage;

    @NotNull
    @Column(name = "fee_iss", precision = 10, scale = 2, nullable = false)
    private BigDecimal feeIss;

    @NotNull
    @Column(name = "login_email", nullable = false)
    private Boolean loginEmail;

    @NotNull
    @Column(name = "login_access_code", nullable = false)
    private Boolean loginAccessCode;

    @NotNull
    @Column(name = "login_cpf", nullable = false)
    private Boolean loginCpf;

    @NotNull
    @Column(name = "login_password", nullable = false)
    private Boolean loginPassword;

    @NotNull
    @Column(name = "login_token", nullable = false)
    private Boolean loginToken;

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

    public Brokerage name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public Brokerage cnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getAddress() {
        return address;
    }

    public Brokerage address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressNeighborhood() {
        return addressNeighborhood;
    }

    public Brokerage addressNeighborhood(String addressNeighborhood) {
        this.addressNeighborhood = addressNeighborhood;
        return this;
    }

    public void setAddressNeighborhood(String addressNeighborhood) {
        this.addressNeighborhood = addressNeighborhood;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public Brokerage addressCity(String addressCity) {
        this.addressCity = addressCity;
        return this;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public Brokerage addressState(String addressState) {
        this.addressState = addressState;
        return this;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public Boolean isSwingTrade() {
        return swingTrade;
    }

    public Brokerage swingTrade(Boolean swingTrade) {
        this.swingTrade = swingTrade;
        return this;
    }

    public void setSwingTrade(Boolean swingTrade) {
        this.swingTrade = swingTrade;
    }

    public Boolean isDayTrade() {
        return dayTrade;
    }

    public Brokerage dayTrade(Boolean dayTrade) {
        this.dayTrade = dayTrade;
        return this;
    }

    public void setDayTrade(Boolean dayTrade) {
        this.dayTrade = dayTrade;
    }

    public BigDecimal getFeeBrokerage() {
        return feeBrokerage;
    }

    public Brokerage feeBrokerage(BigDecimal feeBrokerage) {
        this.feeBrokerage = feeBrokerage;
        return this;
    }

    public void setFeeBrokerage(BigDecimal feeBrokerage) {
        this.feeBrokerage = feeBrokerage;
    }

    public BigDecimal getFeeIss() {
        return feeIss;
    }

    public Brokerage feeIss(BigDecimal feeIss) {
        this.feeIss = feeIss;
        return this;
    }

    public void setFeeIss(BigDecimal feeIss) {
        this.feeIss = feeIss;
    }

    public Boolean isLoginEmail() {
        return loginEmail;
    }

    public Brokerage loginEmail(Boolean loginEmail) {
        this.loginEmail = loginEmail;
        return this;
    }

    public void setLoginEmail(Boolean loginEmail) {
        this.loginEmail = loginEmail;
    }

    public Boolean isLoginAccessCode() {
        return loginAccessCode;
    }

    public Brokerage loginAccessCode(Boolean loginAccessCode) {
        this.loginAccessCode = loginAccessCode;
        return this;
    }

    public void setLoginAccessCode(Boolean loginAccessCode) {
        this.loginAccessCode = loginAccessCode;
    }

    public Boolean isLoginCpf() {
        return loginCpf;
    }

    public Brokerage loginCpf(Boolean loginCpf) {
        this.loginCpf = loginCpf;
        return this;
    }

    public void setLoginCpf(Boolean loginCpf) {
        this.loginCpf = loginCpf;
    }

    public Boolean isLoginPassword() {
        return loginPassword;
    }

    public Brokerage loginPassword(Boolean loginPassword) {
        this.loginPassword = loginPassword;
        return this;
    }

    public void setLoginPassword(Boolean loginPassword) {
        this.loginPassword = loginPassword;
    }

    public Boolean isLoginToken() {
        return loginToken;
    }

    public Brokerage loginToken(Boolean loginToken) {
        this.loginToken = loginToken;
        return this;
    }

    public void setLoginToken(Boolean loginToken) {
        this.loginToken = loginToken;
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
        Brokerage brokerage = (Brokerage) o;
        if (brokerage.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), brokerage.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Brokerage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cnpj='" + getCnpj() + "'" +
            ", address='" + getAddress() + "'" +
            ", addressNeighborhood='" + getAddressNeighborhood() + "'" +
            ", addressCity='" + getAddressCity() + "'" +
            ", addressState='" + getAddressState() + "'" +
            ", swingTrade='" + isSwingTrade() + "'" +
            ", dayTrade='" + isDayTrade() + "'" +
            ", feeBrokerage=" + getFeeBrokerage() +
            ", feeIss=" + getFeeIss() +
            ", loginEmail='" + isLoginEmail() + "'" +
            ", loginAccessCode='" + isLoginAccessCode() + "'" +
            ", loginCpf='" + isLoginCpf() + "'" +
            ", loginPassword='" + isLoginPassword() + "'" +
            ", loginToken='" + isLoginToken() + "'" +
            "}";
    }
}
