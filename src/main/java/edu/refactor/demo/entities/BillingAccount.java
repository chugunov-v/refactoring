package edu.refactor.demo.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
public class BillingAccount implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column
  public double money = 0;

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

  public double getMoney() {
    return money;
  }

  public void setMoney(double money) {
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
    BillingAccount billingAccount = BillingAccount.create(customer);
    billingAccount.setCustomer(customer);
    billingAccount.setCreatedDate(Instant.now());
    return billingAccount;
  }
}
