package edu.refactor.demo.core.customer;

import edu.refactor.demo.entities.BillingAccount;
import edu.refactor.demo.entities.Customer;
import edu.refactor.demo.entities.enums.CustomerStatus;
import edu.refactor.demo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author sofronov
 * Created: 25.09.2019
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;
    private final BillingAccountDAO billingAccountDAO;

    private static final Logger LOGGER = Logger.getLogger(CustomerServiceImpl.class.getName());

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO, BillingAccountDAO billingAccountDAO) {
        this.customerDAO = customerDAO;
        this.billingAccountDAO = billingAccountDAO;
    }

    @Override
    public void updateCustomerStatus(CustomerStatus status, String email, String login) throws RuntimeException {
        LOGGER.info("Updating customer status");

        Optional<Customer> customerOptional = customerDAO.loadCustomerByLoginAndEmail(login, email);
        if (!customerOptional.isPresent()) {
            LOGGER.severe("Customer not found");
            throw new RuntimeException("Customer does not exist");
        } else {

            Customer customer = customerOptional.get();
            customer.setStatus(status);
            customerDAO.save(customer);
            LOGGER.info("Customer status was updated");
        }
    }

    @Override
    public List<Customer> loadCustomerList() {
        LOGGER.info("Loading customer list");
        List<Customer> customers = customerDAO.loadNotDeletedCustomers();
        LOGGER.info("Customer list was loaded");
        return customers;
    }

    @Override
    public Customer createNewCustomer(String login, String email) throws RuntimeException {
        LOGGER.info("Creating new customer");

        boolean exists = customerDAO.existsByLoginAndEmail(login, email);
        if (exists) {
            LOGGER.severe("Duplicated customer");
            throw new RuntimeException("Customer already exists");
        }

        Customer newCustomer = Customer.create(email, login);
        newCustomer = customerDAO.save(newCustomer);
        LOGGER.info("New customer was created");

        BillingAccount newBillingAccount = BillingAccount.create(newCustomer);
        billingAccountDAO.save(newBillingAccount);
        LOGGER.info("New billing account was created");

        return newCustomer;
    }

    @Override
    public List<BillingAccount> getBillingAccounts(String login, String email) {
        LOGGER.info("Loading billing account list");
        List<BillingAccount> billingAccounts = billingAccountDAO.findBillingAccountsByLoginAndEmail(login, email);
        LOGGER.info("Billing account list was loaded");
        return billingAccounts;
    }
}
