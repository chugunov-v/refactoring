package edu.refactor.demo.services;

import edu.refactor.demo.entities.VehicleRental;

import java.util.Optional;

public interface VehicleRentalService {
	Iterable<VehicleRental> findAll();

	VehicleRental createBy(Long vehicleId, Long customerId);

	VehicleRental expireActiveVehicleRental(Long rentalId);

	Optional<VehicleRental> findById(Long rentalId);

	VehicleRental save(VehicleRental vehicleRental);
}
