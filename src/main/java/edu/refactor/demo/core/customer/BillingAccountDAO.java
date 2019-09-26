package edu.refactor.demo.core.customer;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.refactor.demo.entities.BillingAccount;

import java.util.List;

@Repository
public interface BillingAccountDAO extends CrudRepository<BillingAccount, Long> {

    List<BillingAccount> findBillingAccountsByLoginAndEmail(String login, String email);
    List<BillingAccount> findBy(Long vehicleRentalId);
}