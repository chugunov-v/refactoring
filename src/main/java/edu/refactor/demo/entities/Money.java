package edu.refactor.demo.entities;

import edu.refactor.demo.entities.enums.Currency;
import edu.refactor.demo.exceptions.ExceptionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "money")
public class Money implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column
    private Long id;



    @ManyToOne()
    @JoinColumn(name="billingAccountFK")
    private BillingAccount billingAccount;

    @Column
    private Currency currency;

    @Column
    private BigDecimal value;

    public BillingAccount getBillingAccount() {
        return billingAccount;
    }

    public void setBillingAccount(BillingAccount billingAccount) {
        this.billingAccount = billingAccount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * Process of exchange
     * All rates hardcoded with simulated fee on the exchange ( EURO to DOLLAR != DOLLAR to EURO )
     *
     * @param  from of payment
     * @param  to of payment
     * @return BigDecimal value of exchange
     */
    public static BigDecimal rate(Currency from, Currency to){
        if( from.getId().equals(to.getId()) ) return BigDecimal.ONE;
        switch( from ){
            case EUROS:
                switch( to ){
                    case RUBLES:
                        return new BigDecimal(70);
                    case DOLLARS:
                        return new BigDecimal(1.2);
                }
                break;
            case RUBLES:
                switch( to ){
                    case EUROS:
                        return new BigDecimal(0.12);
                    case DOLLARS:
                        return new BigDecimal(0.18);
                }
                break;
            case DOLLARS:
                switch( to ){
                    case RUBLES:
                        return new BigDecimal(60);
                    case EUROS:
                        return new BigDecimal(0.9);
                }
                break;
        }
        throw ExceptionUtils.NOT_FOUND_CURRENCY_EXCEPTION;
    }
}