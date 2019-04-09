package br.com.clearinvest.clivserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A StockWatch.
 */
@Entity
@Table(name = "stock_order")
@IdClass(StockOrder.class)
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class StockOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    // https://vladmihalcea.com/how-to-map-a-composite-identifier-using-an-automatically-generatedvalue-with-jpa-and-hibernate/
    @Id
    private Date date;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="stock_order_seq")
    @SequenceGenerator(name="stock_order_seq", sequenceName="stock_order_seq")
    private Integer orderId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private Stock stock;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("")
    private User user;

    public static  class StockOrderId {

        private Date date;
        private Integer orderId;

        private StockOrderId() { }

        public StockOrderId(Date date, Integer orderId) {
            this.date = date;
            this.orderId = orderId;
        }

        @Override
        public boolean equals(Object o) {
            if ( this == o ) {
                return true;
            }
            if ( o == null || getClass() != o.getClass() ) {
                return false;
            }
            StockOrderId pk = (StockOrderId) o;
            return Objects.equals(date, pk.date) &&
                Objects.equals(orderId, pk.orderId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(date, orderId);
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Stock getStock() {
        return stock;
    }

    public StockOrder stock(Stock stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public User getUser() {
        return user;
    }

    public StockOrder user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockOrder that = (StockOrder) o;
        return Objects.equals(date, that.date) &&
            Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, orderId);
    }
}
