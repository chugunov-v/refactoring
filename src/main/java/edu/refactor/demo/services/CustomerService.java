package edu.refactor.demo.services;

import edu.refactor.demo.entities.BillingAccount;
import edu.refactor.demo.entities.Customer;
import edu.refactor.demo.entities.constants.CustomerStatus;

import java.util.List;

public interface CustomerService {
    void updateStatus(CustomerStatus status, String email, String login);

    List<Customer> findAll();

    Customer create(String login, String email);

    List<BillingAccount> getBillingAccounts(String login, String email);
}