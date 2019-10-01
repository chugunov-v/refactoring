package edu.refactor.demo.core.customer;

import edu.refactor.demo.entities.BillingAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingAccountDAO extends CrudRepository<BillingAccount, Long> {

    String LOGIN = "login";
    String EMAIL = "email";
    String VEHICLE_RENTAL_ID = "vehicleRentalId";
    String SELECT_BY_LOGIN_AND_EMAIL_QUERY = "select a from BillingAccount a join a.customer c where c.login=:" + LOGIN + " and c.email=:" + EMAIL;
    String SELECT_BY_RENTAL_ID_QUERY = "select a from BillingAccount a join a.customer c left join c.rentals r where r.id=:" + VEHICLE_RENTAL_ID;

    @Query(value = SELECT_BY_LOGIN_AND_EMAIL_QUERY)
    List<BillingAccount> findBillingAccountsByLoginAndEmail(@Param(LOGIN) String login, @Param(EMAIL) String email);

    @Query(value = SELECT_BY_RENTAL_ID_QUERY)
    List<BillingAccount> findByRental(@Param(VEHICLE_RENTAL_ID) Long vehicleRentalId);
}