package edu.refactor.demo.core.vehicle;

import edu.refactor.demo.entities.Vehicle;
import edu.refactor.demo.entities.enums.VehicleStatus;
import edu.refactor.demo.services.VehicleService;
import org.springframework.stereotype.Service;

/**
 * @author sofronov
 * Created: 25.09.2019
 */
@Service
public class VehicleServiceImpl implements VehicleService {
    @Override
    public Iterable<Vehicle> findAll() {
        return null;
    }

    @Override
    public Vehicle create(String serialNumber, String title, String type, Double price) {
        return null;
    }

    @Override
    public boolean updateStatus(String serialNumber, VehicleStatus vehicleStatus) {
        return false;
    }
}
