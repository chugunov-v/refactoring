package edu.refactor.demo;

import edu.refactor.demo.services.VehicleRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VehicleRentalStatusUpdaterTask {
    private final VehicleRentalService vehicleRentalService;

    @Autowired
    public VehicleRentalStatusUpdaterTask(VehicleRentalService vehicleRentalService) {
        this.vehicleRentalService = vehicleRentalService;
    }

    @Scheduled(initialDelay = 60000, fixedDelay = 1000)
    public void reportCurrentTime() {
        vehicleRentalService.markAsExpiredIfNecessary();
    }
}