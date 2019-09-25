package edu.refactor.demo.services;

import edu.refactor.demo.entities.Vehicle;
import edu.refactor.demo.entities.enums.VehicleStatus;

import java.util.Map;

public interface VehicleService {
    Iterable<Vehicle> loadVehicleList();

    Vehicle createVehicle(Map<String, String> vehicleMap);

    boolean updateStatus(String serialNumber, String nextStatus);
}