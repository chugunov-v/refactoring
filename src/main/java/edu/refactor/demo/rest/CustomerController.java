package edu.refactor.demo.rest;

import edu.refactor.demo.ResponseWrapper;
import edu.refactor.demo.entities.BillingAccount;
import edu.refactor.demo.entities.Customer;
import edu.refactor.demo.entities.enums.CustomerStatus;
import edu.refactor.demo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseWrapper<List<Customer>> getCustomerList() {
        return new ResponseWrapper<>(customerService.loadCustomerList());
    }

    @RequestMapping(value = "/freeze", method = RequestMethod.GET)
    public ResponseWrapper<Boolean> updateCustomerStatusFreeze(@RequestParam(name = "login") String login,
                                                               @RequestParam(name = "email") String email) {
        try {
            customerService.updateCustomerStatus(CustomerStatus.FREEZE, email, login);
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
        return new ResponseWrapper<>(true);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseWrapper<Boolean> updateCustomerStatusDelete(@RequestParam(name = "login") String login,
                                                               @RequestParam(name = "email") String email) {
        try {
            customerService.updateCustomerStatus(CustomerStatus.DELETE, email, login);
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
        return new ResponseWrapper<>(true);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseWrapper<Boolean> updateCustomerStatusActive(@RequestParam(name = "login") String login,
                                                               @RequestParam(name = "email") String email) {
        try {
            customerService.updateCustomerStatus(CustomerStatus.ACTIVE, email, login);
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
        return new ResponseWrapper<>(true);
    }

    @RequestMapping(value = "/billingAccount/list", method = RequestMethod.GET)
    public ResponseWrapper<List<BillingAccount>> getBillingAccountList(@RequestParam(name = "login") String login,
                                                                       @RequestParam(name = "email") String email) {
        return new ResponseWrapper<>(customerService.getBillingAccounts(login, email));
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ResponseWrapper<Customer> createNewCustomer(@RequestParam(name = "login") String login,
                                                       @RequestParam(name = "email") String email) {
        try {
            Customer customer = customerService.createNewCustomer(login, email);
            return new ResponseWrapper<>(customer);
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
    }
}
