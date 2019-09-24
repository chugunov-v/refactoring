package edu.refactor.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.refactor.demo.entities.VehicleRental;

@Repository
public interface VehicleRentalDAO extends CrudRepository<VehicleRental, Long> {
}
