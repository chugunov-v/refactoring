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
        try {
            return new ResponseWrapper<>(vehicleRentalService.findAll());
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
    }

    @RequestMapping(value = "/complete", method = RequestMethod.GET)
    public ResponseWrapper<Boolean> completeVehicleRental(@RequestParam(name = "rental") Long rentalId) {
        try {
            vehicleRentalService.complete(rentalId);
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
        return new ResponseWrapper<>(true);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseWrapper<Boolean> activeVehicleRental(@RequestParam(name = "rental") Long rentalId) {
        try {
            vehicleRentalService.activate(rentalId);
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
        return new ResponseWrapper<>(true);
    }

    @RequestMapping(value = "/expired", method = RequestMethod.GET)
    public ResponseWrapper<VehicleRental> expiredVehicleRental(@RequestParam(name = "rental") Long rentalId) {
        try {
            return new ResponseWrapper<>(vehicleRentalService.markAsExpired(rentalId));
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ResponseWrapper<VehicleRental> createVehicleRental(@RequestParam(name = "vehicle") Long vehicleId, @RequestParam(name = "customer") Long customerId) {
        try {
            return new ResponseWrapper<>(vehicleRentalService.create(vehicleId, customerId));
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
    }
}
