package edu.refactor.demo.core.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.refactor.demo.entities.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerDAO extends CrudRepository<Customer, Long> {

    List<Customer> loadNotDeletedCustomers();

    boolean existsByLoginAndEmail(String login, String email);

    Optional<Customer> loadCustomerByLoginAndEmail(String login, String email);
}