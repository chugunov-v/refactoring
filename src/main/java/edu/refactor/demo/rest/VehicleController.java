package edu.refactor.demo.rest;

import edu.refactor.demo.ResponseWrapper;
import edu.refactor.demo.entities.Vehicle;
import edu.refactor.demo.entities.enums.VehicleStatus;
import edu.refactor.demo.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/vehicle")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseWrapper<Iterable<Vehicle>> loadVehicleList() {
        return new ResponseWrapper<>(vehicleService.loadVehicleList());
    }

    @RequestMapping(value = "/status/update", method = RequestMethod.GET)
    public ResponseWrapper<Boolean> updateStatus(@RequestParam(name = "serialNumber") String serialNumber,
                                                 @RequestParam(name = "status") String nextStatus) {
        try {
            boolean result = vehicleService.updateStatus(serialNumber, nextStatus);
            return new ResponseWrapper<>(result);
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseWrapper<Vehicle> createVehicle(@RequestBody Map<String, String> vehicleMap) {
        try {
            Vehicle vehicle = vehicleService.createVehicle(vehicleMap);
            return new ResponseWrapper<>(vehicle);
        } catch (RuntimeException e) {
            return new ResponseWrapper<>(e.getMessage());
        }
    }
}
