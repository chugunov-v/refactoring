package edu.refactor.demo.services;

import edu.refactor.demo.entities.VehicleRental;

public interface VehicleRentalService {
    Iterable<VehicleRental> findAll();

    VehicleRental create(Long vehicleId, Long customerId);

    VehicleRental markAsExpired(Long rentalId);

    void complete(Long rentalId);

    void activate(Long rentalId);
}
