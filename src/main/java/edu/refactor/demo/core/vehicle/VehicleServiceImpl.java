package edu.refactor.demo.core.vehicle;

import edu.refactor.demo.entities.Vehicle;
import edu.refactor.demo.entities.enums.VehicleStatus;
import edu.refactor.demo.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * @author sofronov
 * Created: 25.09.2019
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleDAO vehicleDAO;

    @Autowired
    public VehicleServiceImpl(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    @Override
    public Iterable<Vehicle> loadVehicleList() {
        return vehicleDAO.findAll();
    }

    @Override
    public Vehicle createVehicle(Map<String, String> vehicleMap) throws RuntimeException {
        String title = vehicleMap.get("title");
        Double price = Double.valueOf(vehicleMap.get("price"));
        String type = vehicleMap.get("type");
        String serialNumber = vehicleMap.get("serialNumber");
        if (isEmpty(title, price, type, serialNumber)) {
            throw new RuntimeException("Required param is empty");
        }

        Vehicle newVehicle = Vehicle.create(price, title, type, serialNumber);
        return vehicleDAO.save(newVehicle);
    }

    private boolean isEmpty(String title, Double price, String type, String serialNumber) {
        return StringUtils.isEmpty(title) || price == null || StringUtils.isEmpty(type) || StringUtils.isEmpty(serialNumber);
    }

    @Override
    public boolean updateStatus(String serialNumber, String nextStatus) throws RuntimeException {
        Optional<Vehicle> vehicleOptional = vehicleDAO.findVehicleBySerialNumber(serialNumber);
        if (!vehicleOptional.isPresent()) {
            throw new RuntimeException("Vehicle does not exist");
        } else {

            VehicleStatus newStatus = VehicleStatus.fromId(nextStatus);
            if (newStatus == null) {
                throw new RuntimeException("VehicleStatus does not exist");
            }
            Vehicle vehicle = vehicleOptional.get();
            String vehicleStatus = vehicle.getStatus();

            switch (newStatus) {
                case OPEN: {
                    if (vehicleStatus.equals(VehicleStatus.SERVICE.getId()) ||
                            vehicleStatus.equals(VehicleStatus.RETURNED.getId())) {
                        return setVehicleStatus(vehicle, VehicleStatus.OPEN);
                    }
                    break;
                }

                case LOST: {
                    if (vehicleStatus.equals(VehicleStatus.LEASED.getId())) {
                        return setVehicleStatus(vehicle, VehicleStatus.LOST);
                    }
                    break;
                }

                case DELETE: {
                    if (vehicleStatus.equals(VehicleStatus.OPEN.getId())) {
                        return setVehicleStatus(vehicle, VehicleStatus.DELETE);
                    }
                    break;
                }

                case RESERVED: {
                    if (vehicleStatus.equals(VehicleStatus.OPEN.getId())) {
                        return setVehicleStatus(vehicle, VehicleStatus.RESERVED);
                    }
                    break;
                }

                case RETURNED: {
                    if (vehicleStatus.equals(VehicleStatus.LEASED.getId())) {
                        return setVehicleStatus(vehicle, VehicleStatus.RETURNED);
                    }
                    break;
                }

                case SERVICE: {
                    if (vehicleStatus.equals(VehicleStatus.RETURNED.getId())) {
                        return setVehicleStatus(vehicle, VehicleStatus.SERVICE);
                    }
                    break;
                }

                case LEASED: {
                    if (vehicleStatus.equals(VehicleStatus.RESERVED.getId())) {
                        return setVehicleStatus(vehicle, VehicleStatus.LEASED);
                    }
                    break;
                }
            }
        }
        throw new RuntimeException("VehicleStatus can not be updated");
    }

    private boolean setVehicleStatus(Vehicle vehicle, VehicleStatus vehicleStatus) {
        vehicle.setStatus(vehicleStatus.getId());
        vehicleDAO.save(vehicle);
        return true;
    }
}
