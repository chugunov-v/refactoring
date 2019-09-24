package edu.refactor.demo.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.refactor.demo.entities.Vehicle;

@Repository
public interface VehicleDAO extends CrudRepository<Vehicle, Long> {
}