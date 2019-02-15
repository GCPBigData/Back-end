package br.com.clearinvest.clivserver.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the Brokerage entity.
 */
public class BrokerageDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(min = 14, max = 14)
    private String cnpj;

    @NotNull
    @Size(max = 100)
    private String address;

    @Size(max = 100)
    private String addressNeighborhood;

    @NotNull
    @Size(max = 100)
    private String addressCity;

    @NotNull
    @Size(min = 2, max = 2)
    private String addressState;

    @NotNull
    private Boolean swingTrade;

    @NotNull
    private Boolean dayTrade;

    @NotNull
    private BigDecimal feeBrokerage;

    @NotNull
    private BigDecimal feeIss;

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

    public BigDecimal getFeeBrokerage() {
        return feeBrokerage;
    }

    public void setFeeBrokerage(BigDecimal feeBrokerage) {
        this.feeBrokerage = feeBrokerage;
    }

    public BigDecimal getFeeIss() {
        return feeIss;
    }

    public void setFeeIss(BigDecimal feeIss) {
        this.feeIss = feeIss;
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
