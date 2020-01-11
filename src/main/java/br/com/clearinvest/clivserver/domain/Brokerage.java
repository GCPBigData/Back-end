package br.com.clearinvest.clivserver.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
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

    @Size(min = 14, max = 18)
    @Column(name = "cnpj", length = 18, unique = true)
    private String cnpj;

    @Size(max = 100)
    @Column(name = "address", length = 100)
    private String address;

    @Size(max = 100)
    @Column(name = "address_neighborhood", length = 100)
    private String addressNeighborhood;

    @Size(max = 100)
    @Column(name = "address_city", length = 100)
    private String addressCity;

    @Size(max = 50)
    @Column(name = "address_state", length = 50)
    private String addressState;

    @NotNull
    @Column(name = "swing_trade", nullable = false)
    private Boolean swingTrade;

    @NotNull
    @Column(name = "day_trade", nullable = false)
    private Boolean dayTrade;

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

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "fee", precision = 10, scale = 2, nullable = false)
    private BigDecimal fee;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100.00")
    @Column(name = "iss", precision = 10, scale = 2)
    private BigDecimal iss;

    @Size(max = 50)
    @Column(name = "phone", length = 50)
    private String phone;

    @Size(max = 100)
    @Column(name = "website", length = 100)
    private String website;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "brokerage_brokerage_client",
               joinColumns = @JoinColumn(name = "brokerages_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "brokerage_clients_id", referencedColumnName = "id"))
    private Set<BrokerageClient> brokerageClients = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "brokerage_brokerage_product",
               joinColumns = @JoinColumn(name = "brokerages_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "brokerage_products_id", referencedColumnName = "id"))
    private Set<BrokerageProduct> brokerageProducts = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "brokerage_brokerage_assistance",
               joinColumns = @JoinColumn(name = "brokerages_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "brokerage_assistances_id", referencedColumnName = "id"))
    private Set<BrokerageAssistance> brokerageAssistances = new HashSet<>();

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

    public BigDecimal getFee() {
        return fee;
    }

    public Brokerage fee(BigDecimal fee) {
        this.fee = fee;
        return this;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getIss() {
        return iss;
    }

    public Brokerage iss(BigDecimal iss) {
        this.iss = iss;
        return this;
    }

    public void setIss(BigDecimal iss) {
        this.iss = iss;
    }

    public String getPhone() {
        return phone;
    }

    public Brokerage phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public Brokerage website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public Brokerage email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<BrokerageClient> getBrokerageClients() {
        return brokerageClients;
    }

    public Brokerage brokerageClients(Set<BrokerageClient> brokerageClients) {
        this.brokerageClients = brokerageClients;
        return this;
    }

    public Brokerage addBrokerageClient(BrokerageClient brokerageClient) {
        this.brokerageClients.add(brokerageClient);
        brokerageClient.getBrokerages().add(this);
        return this;
    }

    public Brokerage removeBrokerageClient(BrokerageClient brokerageClient) {
        this.brokerageClients.remove(brokerageClient);
        brokerageClient.getBrokerages().remove(this);
        return this;
    }

    public void setBrokerageClients(Set<BrokerageClient> brokerageClients) {
        this.brokerageClients = brokerageClients;
    }

    public Set<BrokerageProduct> getBrokerageProducts() {
        return brokerageProducts;
    }

    public Brokerage brokerageProducts(Set<BrokerageProduct> brokerageProducts) {
        this.brokerageProducts = brokerageProducts;
        return this;
    }

    public Brokerage addBrokerageProduct(BrokerageProduct brokerageProduct) {
        this.brokerageProducts.add(brokerageProduct);
        brokerageProduct.getBrokerages().add(this);
        return this;
    }

    public Brokerage removeBrokerageProduct(BrokerageProduct brokerageProduct) {
        this.brokerageProducts.remove(brokerageProduct);
        brokerageProduct.getBrokerages().remove(this);
        return this;
    }

    public void setBrokerageProducts(Set<BrokerageProduct> brokerageProducts) {
        this.brokerageProducts = brokerageProducts;
    }

    public Set<BrokerageAssistance> getBrokerageAssistances() {
        return brokerageAssistances;
    }

    public Brokerage brokerageAssistances(Set<BrokerageAssistance> brokerageAssistances) {
        this.brokerageAssistances = brokerageAssistances;
        return this;
    }

    public Brokerage addBrokerageAssistance(BrokerageAssistance brokerageAssistance) {
        this.brokerageAssistances.add(brokerageAssistance);
        brokerageAssistance.getBrokerages().add(this);
        return this;
    }

    public Brokerage removeBrokerageAssistance(BrokerageAssistance brokerageAssistance) {
        this.brokerageAssistances.remove(brokerageAssistance);
        brokerageAssistance.getBrokerages().remove(this);
        return this;
    }

    public void setBrokerageAssistances(Set<BrokerageAssistance> brokerageAssistances) {
        this.brokerageAssistances = brokerageAssistances;
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
            ", loginEmail='" + isLoginEmail() + "'" +
            ", loginAccessCode='" + isLoginAccessCode() + "'" +
            ", loginCpf='" + isLoginCpf() + "'" +
            ", loginPassword='" + isLoginPassword() + "'" +
            ", loginToken='" + isLoginToken() + "'" +
            ", fee=" + getFee() +
            ", iss=" + getIss() +
            ", phone='" + getPhone() + "'" +
            ", website='" + getWebsite() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
