package edu.refactor.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.refactor.demo.entities.VehicleRental;

import java.util.Optional;

@Repository
public interface VehicleRentalDAO extends CrudRepository<VehicleRental, Long> {
    Optional<VehicleRental> findActiveById(Long id);
}
