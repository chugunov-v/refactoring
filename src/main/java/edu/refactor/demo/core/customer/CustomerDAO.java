package edu.refactor.demo.core.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.refactor.demo.entities.Customer;

@Repository
public interface CustomerDAO extends CrudRepository<Customer, Long> {
}