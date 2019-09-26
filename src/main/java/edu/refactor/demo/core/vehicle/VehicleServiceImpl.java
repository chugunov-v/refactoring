package edu.refactor.demo.core.vehicle;

import edu.refactor.demo.entities.Vehicle;
import edu.refactor.demo.entities.enums.VehicleStatus;
import edu.refactor.demo.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author sofronov
 * Created: 25.09.2019
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleDAO vehicleDAO;

    private static final Logger LOGGER = Logger.getLogger(VehicleServiceImpl.class.getName());

    @Autowired
    public VehicleServiceImpl(VehicleDAO vehicleDAO) {
        this.vehicleDAO = vehicleDAO;
    }

    @Override
    public Iterable<Vehicle> loadVehicleList() {
        LOGGER.info("Loading vehicle list");
        Iterable<Vehicle> vehicles = vehicleDAO.findAll();
        LOGGER.info("Vehicle list was loaded");
        return vehicles;
    }

    @Override
    public Vehicle createVehicle(Map<String, String> vehicleMap) throws RuntimeException {
        LOGGER.info("Creating vehicle");

        String title = vehicleMap.get("title");
        Double price = Double.valueOf(vehicleMap.get("price"));
        String type = vehicleMap.get("type");
        String serialNumber = vehicleMap.get("serialNumber");

        if (isEmpty(title, price, type, serialNumber)) {
            LOGGER.severe("Empty param");
            throw new RuntimeException("Required param is empty");
        }

        Vehicle newVehicle = Vehicle.create(price, title, type, serialNumber);
        newVehicle = vehicleDAO.save(newVehicle);

        LOGGER.info("Vehicle was created");
        return newVehicle;
    }

    private boolean isEmpty(String title, Double price, String type, String serialNumber) {
        return StringUtils.isEmpty(title) || price == null || StringUtils.isEmpty(type) || StringUtils.isEmpty(serialNumber);
    }

    @Override
    public boolean updateStatus(String serialNumber, String nextStatus) throws RuntimeException {
        LOGGER.info("Updating vehicle status: " + nextStatus);

        Optional<Vehicle> vehicleOptional = vehicleDAO.findVehicleBySerialNumber(serialNumber);
        if (!vehicleOptional.isPresent()) {
            LOGGER.severe("Vehicle not found");
            throw new RuntimeException("Vehicle does not exist");
        } else {

            VehicleStatus newStatus = VehicleStatus.fromId(nextStatus);
            if (newStatus == null) {
                LOGGER.severe("VehicleStatus not found");
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

        LOGGER.severe("VehicleStatus was not updated");
        throw new RuntimeException("VehicleStatus can not be updated");
    }

    private boolean setVehicleStatus(Vehicle vehicle, VehicleStatus vehicleStatus) {
        vehicle.setStatus(vehicleStatus.getId());
        vehicleDAO.save(vehicle);

        LOGGER.info(vehicleStatus.getId() + " VehicleStatus was updated");
        return true;
    }
}
