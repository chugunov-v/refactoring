package edu.refactor.demo.rest;

import edu.refactor.demo.entities.BillingAccount;
import edu.refactor.demo.entities.Customer;
import edu.refactor.demo.entities.constants.CustomerStatus;
import edu.refactor.demo.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public @ResponseBody
    List<Customer> getAll() {
        return customerService.findAll();
    }

    @RequestMapping(value = "/customer/freeze", method = RequestMethod.POST)
    public @ResponseBody
    Customer freeze(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        customerService.updateStatus(CustomerStatus.FREEZE, email, login);
        throw new RuntimeException("freeze error");
    }

    @RequestMapping(value = "/customer/delete", method = RequestMethod.POST)
    public @ResponseBody
    Customer delete(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        customerService.updateStatus(CustomerStatus.DELETE, email, login);
        throw new RuntimeException("freeze error");
    }

    @RequestMapping(value = "/customer/active", method = RequestMethod.POST)
    public @ResponseBody
    Customer active(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        customerService.updateStatus(CustomerStatus.ACTIVE, email, login);
        throw new RuntimeException("active error");
    }

    @RequestMapping(value = "/customer/billingAccount", method = RequestMethod.POST)
    public @ResponseBody
    List<BillingAccount> billingAccount(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        return customerService.getBillingAccounts(login, email);
    }

    @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
    public @ResponseBody
    Customer create(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
        return customerService.create(login, email);
    }
}
