package edu.refactor.demo.rest;

import edu.refactor.demo.dao.CustomerDAO;
import edu.refactor.demo.entities.BillingAccount;
import edu.refactor.demo.entities.Customer;
import edu.refactor.demo.entities.constants.CustomerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class CustomerController {
  private final CustomerDAO customerDAO;

  private EntityManagerFactory entityManagerFactory;

  @Autowired
  public CustomerController(CustomerDAO customerDAO, EntityManagerFactory entityManagerFactory) {
    this.customerDAO = customerDAO;
    this.entityManagerFactory = entityManagerFactory;
  }

  @RequestMapping(value = "/customer", method = RequestMethod.GET)
  public @ResponseBody
  List<Customer> getAll() {
    return StreamSupport.stream(customerDAO.findAll().spliterator(), false).filter(Customer::isNotDeleted).collect(Collectors.toList());
  }

  @RequestMapping(value = "/customer/freeze", method = RequestMethod.POST)
  public @ResponseBody
  Customer freeze(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
    Iterable<Customer> cs = customerDAO.findAll();
    for (Customer c : cs) {
      if (c.getLogin().equals(login) && c.getEmail().equals(email)) {
        c.setStatus(CustomerStatus.FREEZE);
        return customerDAO.save(c);
      }
    }
    throw new RuntimeException("freeze error");
  }

  @RequestMapping(value = "/customer/delete", method = RequestMethod.POST)
  public @ResponseBody
  Customer delete(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
    Iterable<Customer> customers = customerDAO.findAll();
    for (Customer customer : customers) {
      if (customer.equalsBy(login, email)) {
        customer.setStatus(CustomerStatus.DELETE);
        return customerDAO.save(customer);
      }
    }
    throw new RuntimeException("freeze error");
  }

  @RequestMapping(value = "/customer/active", method = RequestMethod.POST)
  public @ResponseBody
  Customer active(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
    Iterable<Customer> cs = customerDAO.findAll();
    for (Customer c : cs) {
      if (c.getLogin().equals(login) && c.getEmail().equals(email)) {
        c.setStatus(CustomerStatus.ACTIVE);
        return customerDAO.save(c);
      }
    }
    throw new RuntimeException("active error");
  }

  @RequestMapping(value = "/customer/billingAccount", method = RequestMethod.GET)
  public @ResponseBody
  List<BillingAccount> billingAccount(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    return entityManager.createQuery("select e from Customer e JOIN FETCH e.billingAccounts where e.email = :email and e.login = :login  ", Customer.class)
        .setParameter("email", email)
        .setParameter("login", login)
        .getSingleResult()
        .getBillingAccounts();
  }

  @RequestMapping(value = "/customer/create", method = RequestMethod.POST)
  public @ResponseBody
  Customer create(@RequestParam(name = "login") String login, @RequestParam(name = "email") String email) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    Iterable<Customer> customers = customerDAO.findAll();
    for (Customer customer : customers) {
      if (customer.getLogin().equals(login) || customer.getEmail().equals(email)) {
        throw new RuntimeException("Create customer error");
      }
    }

    Customer customer = entityManager.merge(Customer.create(email, login));
    BillingAccount billingAccount= BillingAccount.create(customer);
     entityManager.merge(billingAccount);
    transaction.commit();
    return entityManager.find(Customer.class, customer.getId());
  }
}
