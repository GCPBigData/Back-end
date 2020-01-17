package br.com.clearinvest.clivserver.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Brokerage entity.
 */
public class BrokerageDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @Size(min = 14, max = 18)
    private String cnpj;

    @Size(max = 100)
    private String address;

    @Size(max = 100)
    private String addressNeighborhood;

    @Size(max = 100)
    private String addressCity;

    @Size(max = 50)
    private String addressState;

    @NotNull
    private Boolean swingTrade;

    @NotNull
    private Boolean dayTrade;

    @NotNull
    private Boolean loginEmail;

    @NotNull
    private Boolean loginAccessCode;

    @NotNull
    private Boolean loginCpf;

    @NotNull
    private Boolean loginPassword;

    @NotNull
    private Boolean loginToken;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal fee;

    @DecimalMin(value = "0")
    @DecimalMax(value = "100.00")
    private BigDecimal iss;

    @Size(max = 50)
    private String phone;

    @Size(max = 100)
    private String website;

    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String logo;

    private Set<BrokerageClientDTO> brokerageClients = new HashSet<>();

    private Set<BrokerageProductDTO> brokerageProducts = new HashSet<>();

    private Set<BrokerageAssistanceDTO> brokerageAssistances = new HashSet<>();

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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressNeighborhood() {
        return addressNeighborhood;
    }

    public void setAddressNeighborhood(String addressNeighborhood) {
        this.addressNeighborhood = addressNeighborhood;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public Boolean isSwingTrade() {
        return swingTrade;
    }

    public void setSwingTrade(Boolean swingTrade) {
        this.swingTrade = swingTrade;
    }

    public Boolean isDayTrade() {
        return dayTrade;
    }

    public void setDayTrade(Boolean dayTrade) {
        this.dayTrade = dayTrade;
    }

    public Boolean isLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(Boolean loginEmail) {
        this.loginEmail = loginEmail;
    }

    public Boolean isLoginAccessCode() {
        return loginAccessCode;
    }

    public void setLoginAccessCode(Boolean loginAccessCode) {
        this.loginAccessCode = loginAccessCode;
    }

    public Boolean isLoginCpf() {
        return loginCpf;
    }

    public void setLoginCpf(Boolean loginCpf) {
        this.loginCpf = loginCpf;
    }

    public Boolean isLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(Boolean loginPassword) {
        this.loginPassword = loginPassword;
    }

    public Boolean isLoginToken() {
        return loginToken;
    }

    public void setLoginToken(Boolean loginToken) {
        this.loginToken = loginToken;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getIss() {
        return iss;
    }

    public void setIss(BigDecimal iss) {
        this.iss = iss;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Set<BrokerageClientDTO> getBrokerageClients() {
        return brokerageClients;
    }

    public void setBrokerageClients(Set<BrokerageClientDTO> brokerageClients) {
        this.brokerageClients = brokerageClients;
    }

    public Set<BrokerageProductDTO> getBrokerageProducts() {
        return brokerageProducts;
    }

    public void setBrokerageProducts(Set<BrokerageProductDTO> brokerageProducts) {
        this.brokerageProducts = brokerageProducts;
    }

    public Set<BrokerageAssistanceDTO> getBrokerageAssistances() {
        return brokerageAssistances;
    }

    public void setBrokerageAssistances(Set<BrokerageAssistanceDTO> brokerageAssistances) {
        this.brokerageAssistances = brokerageAssistances;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BrokerageDTO brokerageDTO = (BrokerageDTO) o;
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
        return "BrokerageDTO{" +
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
            ", logo='" + getLogo() + "'" +
            "}";
    }
}
