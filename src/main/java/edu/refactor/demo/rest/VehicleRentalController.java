package edu.refactor.demo.rest;

import edu.refactor.demo.ResponseWrapper;
import edu.refactor.demo.entities.VehicleRental;
import edu.refactor.demo.services.VehicleRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rental")
public class VehicleRentalController {
    private final VehicleRentalService vehicleRentalService;

    @Autowired
    public VehicleRentalController(VehicleRentalService vehicleRentalService) {
        this.vehicleRentalService = vehicleRentalService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseWrapper<Iterable<VehicleRental>> loadVehicleRentalList() {
        return new ResponseWrapper<>(vehicleRentalService.findAll());
    }

    @RequestMapping(value = "/complete", method = RequestMethod.GET)
    public ResponseWrapper<Boolean> completeVehicle(@RequestParam(name = "rental") Long rentalId) {
        try {
            vehicleRentalService.complete(rentalId);
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
        return new ResponseWrapper<>(true);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseWrapper<Boolean> activeVehicle(@RequestParam(name = "rental") Long rentalId) {
        vehicleRentalService.activate(rentalId);
        return new ResponseWrapper<>(true);
    }

    @RequestMapping(value = "/expired", method = RequestMethod.GET)
    public ResponseWrapper<VehicleRental> expiredVehicle(@RequestParam(name = "rental") Long rentalId) {
        return new ResponseWrapper<>(vehicleRentalService.markAsExpired(rentalId));
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ResponseWrapper<VehicleRental> createVehicle(@RequestParam(name = "vehicle") Long vehicleId, @RequestParam(name = "customer") Long customerId) {
        return new ResponseWrapper<>(vehicleRentalService.create(vehicleId, customerId));
    }
}
