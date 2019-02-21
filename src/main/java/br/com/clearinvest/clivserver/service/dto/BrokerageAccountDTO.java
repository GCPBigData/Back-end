package br.com.clearinvest.clivserver.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the BrokerageAccount entity.
 */
public class BrokerageAccountDTO implements Serializable {

    private Long id;

    @Size(max = 200)
    private String loginEmail;

    @Size(max = 300)
    private String loginAccessCode;

    @Size(max = 11)
    private String loginCpf;

    @Size(max = 100)
    private String loginPassword;

    private Long userId;

    private String userLogin;

    private Long brokerageId;

    private String brokerageName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getLoginAccessCode() {
        return loginAccessCode;
    }

    public void setLoginAccessCode(String loginAccessCode) {
        this.loginAccessCode = loginAccessCode;
    }

    public String getLoginCpf() {
        return loginCpf;
    }

    public void setLoginCpf(String loginCpf) {
        this.loginCpf = loginCpf;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
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

    public Long getBrokerageId() {
        return brokerageId;
    }

    public void setBrokerageId(Long brokerageId) {
        this.brokerageId = brokerageId;
    }

    public String getBrokerageName() {
        return brokerageName;
    }

    public void setBrokerageName(String brokerageName) {
        this.brokerageName = brokerageName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BrokerageAccountDTO brokerageAccountDTO = (BrokerageAccountDTO) o;
        if (brokerageAccountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), brokerageAccountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BrokerageAccountDTO{" +
            "id=" + getId() +
            ", loginEmail='" + getLoginEmail() + "'" +
            ", loginAccessCode='" + getLoginAccessCode() + "'" +
            ", loginCpf='" + getLoginCpf() + "'" +
            ", loginPassword='" + getLoginPassword() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", brokerage=" + getBrokerageId() +
            ", brokerage='" + getBrokerageName() + "'" +
            "}";
    }
}
