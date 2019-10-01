package edu.refactor.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.refactor.demo.entities.enums.Currency;
import edu.refactor.demo.exceptions.ExceptionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "billing_account")
public class BillingAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleRental> rentals = new ArrayList<>();

    @JsonIgnore
    @OneToMany
    @JoinTable(name="money", joinColumns={ @JoinColumn(name="billingAccountFK", referencedColumnName="id") })
    private List<Money> money;

    @Column(name = "is_primary")
    private boolean isPrimary = true;

    @ManyToOne
    @JoinColumn(name = "billing_account_id")
    private Customer customer;

    @Column(name = "created_date")
    private Instant createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Money> getMoney() {
        return money;
    }

    public void setMoney(List<Money> money) {
        this.money = money;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public static BillingAccount create(Customer customer) {
        BillingAccount billingAccount = new BillingAccount();
        billingAccount.setCustomer(customer);
        billingAccount.setCreatedDate(Instant.now());
        return billingAccount;
    }

    /**
     * Process of payment with check
     *
     * @param  currency of payment
     * @param  value of payment
     * @return True if payment is possible
     */
    public void checkBalanceAndPay(Currency currency, BigDecimal value){
        if(check(currency, value))
            pay( currency, value);
        else
            throw ExceptionUtils.NOT_FOUND_MONEY_EXCEPTION;
    }

    /**
     * Validation of possibility to pay
     *
     * @param  currency of payment
     * @param  value of payment
     * @return True if payment is possible
     */
    public boolean check(Currency currency, BigDecimal value){
        BigDecimal balance = BigDecimal.ZERO;
        for( Money m : this.money ){
            balance = balance.add(Money.rate(m.getCurrency(), currency).multiply(m.getValue()));
        }
        if( balance.compareTo(value) > 0 ){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Process of payment
     * Will be used at first bill with the same currency
     * if there will not be enough - then will be used other bills
     *
     * @param  currency of payment
     * @param  value of payment
     * @return True if payment is possible
     */
    public void pay(Currency currency, BigDecimal value){
        BigDecimal balance = value;
        // try to pay in same currency without exchange fees
        for( Money m : this.money ){
            if( m.getCurrency().getId().equals(currency.getId()) ){
                if( m.getValue().compareTo(value) > 0 ) {
                    m.setValue(m.getValue().subtract(value));
                    return;
                } else{
                    balance = balance.subtract( m.getValue());
                    m.setValue(BigDecimal.ZERO);
                }
            }
        }

        // if on main bill not enough money check other bills
        for( Money m : this.money ){
            if( !m.getCurrency().getId().equals(currency.getId()) ){
                BigDecimal current = Money.rate(m.getCurrency(), currency).multiply(m.getValue());
                if( current.compareTo(value) > 0 ) {
                    m.setValue(current.subtract(value).multiply( Money.rate(currency, m.getCurrency()).multiply(m.getValue())));
                    return;
                } else{
                    balance = balance.subtract( current );
                    m.setValue(BigDecimal.ZERO);
                }
            }
        }
    }

    public static Optional<BillingAccount> withdrawMoney(BillingAccount billingAccount, BigDecimal vehiclePrice) {
        billingAccount.checkBalanceAndPay( Currency.DOLLARS, vehiclePrice );
        return Optional.of(billingAccount);
    }
}
