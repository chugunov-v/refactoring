package edu.refactor.demo.core.vehicle;

import edu.refactor.demo.entities.VehicleRental;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRentalDAO extends CrudRepository<VehicleRental, Long> {

    String ID = "id";
    String UPDATE_CUSTOMER_STATUS = "update customer c set c.status = 'expired' where c.id=" +
        "(select cust.id from vehicle_rental r join customer cust on cust.id=r.customer_id " +
        "where r.status='active' and ((datediff(msc, now(), r.start_rent) > 86400 and c.status='default') " +
        "or (datediff(msc, now(), r.start_rent) > 259200 and c.status='vip'))";
    String FIND_ACTIVE_BY_ID = "select r from VehicleRental r where r.id=:" + ID + " and r.status='active'";
    String UPDATE_STATUS = "update VehicleRental r set r.status = 'active', r.endRent=now() where r.id=:" + ID + " and (r.status='created' or r.status ='expired')";

    @Query(FIND_ACTIVE_BY_ID)
    Optional<VehicleRental> findActiveById(@Param(ID) Long id);

    @Query(value = UPDATE_CUSTOMER_STATUS, nativeQuery = true)
    @Modifying(clearAutomatically = true)
    void updateCustomerStatus();

    @Query(value = UPDATE_STATUS)
    @Modifying(clearAutomatically = true)
    void activate(@Param(ID) Long id);

    @Override
    List<VehicleRental> findAll();
}
