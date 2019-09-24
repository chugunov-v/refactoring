package edu.refactor.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.refactor.demo.entities.constants.CustomerStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static edu.refactor.demo.entities.constants.CustomerStatus.ACTIVE;
import static edu.refactor.demo.entities.constants.CustomerStatus.DELETE;
import static java.time.Instant.now;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_CATEGORY = "default";

    @Column
    private String login;

    @Column
    private String email;

    @Column
    private Instant registration;

    @Column
    private String status = ACTIVE.getId();

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @JsonIgnore
    @OneToMany(fetch = LAZY, mappedBy = "customer", cascade = ALL, orphanRemoval = true)
    private List<VehicleRental> rentals = new ArrayList<>();

    @Column
    private String category = DEFAULT_CATEGORY;

    @JsonIgnore
    @OneToMany(fetch = LAZY, mappedBy = "customer", cascade = ALL, orphanRemoval = true)
    private List<BillingAccount> billingAccounts;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getRegistration() {
        return registration;
    }

    public void setRegistration(Instant registration) {
        this.registration = registration;
    }

    public CustomerStatus getStatus() {
        return CustomerStatus.valueOf(status);
    }

    public void setStatus(CustomerStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<VehicleRental> getRentals() {
        return rentals;
    }

    public void setRentals(List<VehicleRental> rentals) {
        this.rentals = rentals;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<BillingAccount> getBillingAccounts() {
        return billingAccounts;
    }

    public void setBillingAccounts(List<BillingAccount> billingAccounts) {
        this.billingAccounts = billingAccounts;
    }

    public static Customer create(String email, String login) {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setLogin(login);
        customer.setRegistration(now());
        return customer;
    }

    public boolean equalsBy(String login, String email) {
        return Objects.equals(getEmail(), email) && Objects.equals(getLogin(), login);
    }

    public boolean isNotDeleted() {
        return getStatus() != DELETE;
    }
}
