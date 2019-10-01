package edu.refactor.demo.services;

import edu.refactor.demo.entities.VehicleRental;

import java.util.List;

public interface VehicleRentalService {
    List<VehicleRental> findAll();

    VehicleRental create(Long vehicleId, Long customerId);

    VehicleRental markAsExpired(Long rentalId);

    void markAsExpiredIfNecessary();

    void complete(Long rentalId);

    void activate(Long rentalId);
}
