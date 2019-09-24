package edu.refactor.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.refactor.demo.entities.BillingAccount;

@Repository
public interface BillingAccountDAO extends CrudRepository<BillingAccount, Long> {
}