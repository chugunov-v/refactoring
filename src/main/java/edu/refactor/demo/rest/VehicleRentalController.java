package edu.refactor.demo.rest;

import edu.refactor.demo.entities.VehicleRental;
import edu.refactor.demo.services.VehicleRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleRentalController {
    private final VehicleRentalService vehicleRentalService;

    @Autowired
    public VehicleRentalController(VehicleRentalService vehicleRentalService) {
        this.vehicleRentalService = vehicleRentalService;
    }

    @RequestMapping(value = "/rental", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<VehicleRental> all() {
        return vehicleRentalService.findAll();
    }

    @RequestMapping(value = "/rental/complete", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental completeVehicle(@RequestParam(name = "rental") Long rentalId) {
        vehicleRentalService.complete(rentalId);
        return null;
    }

    @RequestMapping(value = "/rental/active", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental activeVehicle(@RequestParam(name = "rental") Long rentalId) {
        vehicleRentalService.activate(rentalId);
        return null;
    }

    @RequestMapping(value = "/rental/expired", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental expiredVehicle(@RequestParam(name = "rental") Long rentalId) {
        return vehicleRentalService.markAsExpired(rentalId);
    }

    @RequestMapping(value = "/rental/create", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental createVehicle(@RequestParam(name = "vehicle") Long vehicleId, @RequestParam(name = "customer") Long customerId) {
        return vehicleRentalService.create(vehicleId, customerId);
    }
}
