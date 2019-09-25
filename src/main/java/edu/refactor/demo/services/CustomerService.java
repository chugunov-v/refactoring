package edu.refactor.demo.services;

import edu.refactor.demo.entities.BillingAccount;
import edu.refactor.demo.entities.Customer;
import edu.refactor.demo.entities.enums.CustomerStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CustomerService {

    void updateCustomerStatus(CustomerStatus status, String email, String login);

    List<Customer> loadCustomerList();

    Customer createNewCustomer(String login, String email);

    List<BillingAccount> getBillingAccounts(String login, String email);
}