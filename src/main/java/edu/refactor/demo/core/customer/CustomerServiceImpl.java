package edu.refactor.demo.core.customer;

import edu.refactor.demo.entities.BillingAccount;
import edu.refactor.demo.entities.Customer;
import edu.refactor.demo.entities.enums.CustomerStatus;
import edu.refactor.demo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author sofronov
 * Created: 25.09.2019
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerDAO customerDAO;
    private final BillingAccountDAO billingAccountDAO;

    @Autowired
    public CustomerServiceImpl(CustomerDAO customerDAO, BillingAccountDAO billingAccountDAO) {
        this.customerDAO = customerDAO;
        this.billingAccountDAO = billingAccountDAO;
    }

    @Override
    public void updateCustomerStatus(CustomerStatus status, String email, String login) throws RuntimeException {
        Optional<Customer> customerOptional = customerDAO.loadCustomerByLoginAndEmail(login, email);
        if (!customerOptional.isPresent()) {
            throw new RuntimeException("Customer does not exist");
        } else {
            Customer customer = customerOptional.get();
            customer.setStatus(status);
            customerDAO.save(customer);
        }
    }

    @Override
    public List<Customer> loadCustomerList() {
        return customerDAO.loadNotDeletedCustomers();
    }

    @Override
    public Customer createNewCustomer(String login, String email) throws RuntimeException {
        boolean exists = customerDAO.existsByLoginAndEmail(login, email);
        if (exists) {
            throw new RuntimeException("Customer already exists");
        }

        Customer newCustomer = Customer.create(email, login);
        newCustomer = customerDAO.save(newCustomer);

        BillingAccount newBillingAccount = BillingAccount.create(newCustomer);
        billingAccountDAO.save(newBillingAccount);

        return newCustomer;
    }

    @Override
    public List<BillingAccount> getBillingAccounts(String login, String email) {
        return billingAccountDAO.findBillingAccountsByLoginAndEmail(login, email);
    }
}
