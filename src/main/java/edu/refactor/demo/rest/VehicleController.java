package edu.refactor.demo.rest;

import edu.refactor.demo.entities.Vehicle;
import edu.refactor.demo.entities.constants.VehicleStatus;
import edu.refactor.demo.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @RequestMapping(value = "/vehicle", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<Vehicle> all() {
        return vehicleService.findAll();
    }

    @RequestMapping(value = "/vehicle/status/update", method = RequestMethod.POST)
    public @ResponseBody
    boolean statusUpdate(@RequestParam(name = "serialNumber") String serialNumber, @RequestParam(name = "status") String nextStatus) {
        VehicleStatus vehicleStatus = VehicleStatus.fromId(nextStatus);
        return vehicleService.updateStatus(serialNumber, vehicleStatus);
    }

    @RequestMapping(value = "/vehicle/create", method = RequestMethod.POST)
    public @ResponseBody
    Vehicle createVehicle(@RequestParam(name = "title") String title,
                          @RequestParam(name = "price") double price,
                          @RequestParam(name = "type") String type,
                          @RequestParam(name = "serialNumber") String serialNumber) {
        return vehicleService.create(serialNumber, title, type, price);
    }
}
