package edu.refactor.demo.core.vehicle;

import edu.refactor.demo.entities.Vehicle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleDAO extends CrudRepository<Vehicle, Long> {

    String SERIAL_NUMBER = "serialNumber";
    String SELECT_QUERY = "select v from Vehicle v where serialNumber=:" + SERIAL_NUMBER;

    @Query(value = SELECT_QUERY)
    Optional<Vehicle> findVehicleBySerialNumber(@Param(SERIAL_NUMBER) String serialNumber);
}