package edu.refactor.demo.services;

import edu.refactor.demo.entities.Vehicle;
import edu.refactor.demo.entities.constants.VehicleStatus;

public interface VehicleService {
    Iterable<Vehicle> findAll();

    Vehicle create(String serialNumber, String title, String type, Double price);

    boolean updateStatus(String serialNumber, VehicleStatus vehicleStatus);
}