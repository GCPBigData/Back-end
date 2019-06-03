package br.com.clearinvest.clivserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A BrokerageAccount.
 */
@Entity
@Table(name = "brokerage_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BrokerageAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 200)
    @Column(name = "login_email", length = 200)
    private String loginEmail;

    @Size(max = 300)
    @Column(name = "login_access_code", length = 300)
    private String loginAccessCode;

    @Size(max = 11)
    @Column(name = "login_cpf", length = 11)
    private String loginCpf;

    @Size(max = 100)
    @Column(name = "login_password", length = 100)
    private String loginPassword;

    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance;

    @DecimalMin(value = "0")
    @Column(name = "fee", precision = 10, scale = 2)
    private BigDecimal fee;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Brokerage brokerage;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public BrokerageAccount loginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
        return this;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }

    public String getLoginAccessCode() {
        return loginAccessCode;
    }

    public BrokerageAccount loginAccessCode(String loginAccessCode) {
        this.loginAccessCode = loginAccessCode;
        return this;
    }

    public void setLoginAccessCode(String loginAccessCode) {
        this.loginAccessCode = loginAccessCode;
    }

    public String getLoginCpf() {
        return loginCpf;
    }

    public BrokerageAccount loginCpf(String loginCpf) {
        this.loginCpf = loginCpf;
        return this;
    }

    public void setLoginCpf(String loginCpf) {
        this.loginCpf = loginCpf;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public BrokerageAccount loginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
        return this;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BrokerageAccount balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public BrokerageAccount fee(BigDecimal fee) {
        this.fee = fee;
        return this;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public User getUser() {
        return user;
    }

    public BrokerageAccount user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Brokerage getBrokerage() {
        return brokerage;
    }

    public BrokerageAccount brokerage(Brokerage brokerage) {
        this.brokerage = brokerage;
        return this;
    }

    public void setBrokerage(Brokerage brokerage) {
        this.brokerage = brokerage;
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
        BrokerageAccount brokerageAccount = (BrokerageAccount) o;
        if (brokerageAccount.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), brokerageAccount.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BrokerageAccount{" +
            "id=" + getId() +
            ", loginEmail='" + getLoginEmail() + "'" +
            ", loginAccessCode='" + getLoginAccessCode() + "'" +
            ", loginCpf='" + getLoginCpf() + "'" +
            ", loginPassword='" + getLoginPassword() + "'" +
            ", balance=" + getBalance() +
            ", fee=" + getFee() +
            "}";
    }
}
