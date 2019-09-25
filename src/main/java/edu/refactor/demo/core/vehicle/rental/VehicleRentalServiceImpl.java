package edu.refactor.demo.core.vehicle.rental;

import edu.refactor.demo.core.customer.CustomerDAO;
import edu.refactor.demo.core.vehicle.VehicleDAO;
import edu.refactor.demo.entities.VehicleRental;
import edu.refactor.demo.services.VehicleRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class VehicleRentalServiceImpl implements VehicleRentalService {
    private final VehicleRentalDAO vehicleRentalDAO;
    private final CustomerDAO customerDAO;
    private final VehicleDAO vehicleDAO;

    @Autowired
    public VehicleRentalServiceImpl(VehicleRentalDAO vehicleRentalDAO, CustomerDAO customerDAO, VehicleDAO vehicleDAO) {
        this.vehicleRentalDAO = vehicleRentalDAO;
        this.customerDAO = customerDAO;
        this.vehicleDAO = vehicleDAO;
    }

    @Override
    public Iterable<VehicleRental> findAll() {
        return vehicleRentalDAO.findAll();
    }

    @Override
    public VehicleRental create(Long vehicleId, Long customerId) {
        return vehicleDAO.findById(vehicleId)
            .map(vehicle -> customerDAO
                .findById(customerId)
                .map(customer -> VehicleRental.create(customer, vehicle))
                .orElse(null))
            .orElse(null);
    }

    @Override
    public VehicleRental markAsExpired(Long rentalId) {
        return vehicleRentalDAO
            .findActiveById(rentalId)
            .map(this::saveExpired)
            .orElse(null);
    }

    private VehicleRental saveExpired(VehicleRental vehicleRental) {
        vehicleRental.markAsExpired(Instant.now());
        return vehicleRentalDAO.save(vehicleRental);
    }

    @Override
    public void markAsExpiredIfNecessary() {
        vehicleRentalDAO.updateCustomerStatus();
    }

    @Override
    public void complete(Long rentalId) {
//        Optional<VehicleRental> ro = vehicleRentalDao.findById(rentalId);
//        if (ro.isPresent()) {
//            VehicleRental vr = ro.get();
//            if (vr.status.equals("active")) {
//                vr.endRent = (Instant.now());
//                vr.status = ("completed");
//                List<BillingAccount> bs = vr.customer.billingAccounts;
//                double value = vr.vehicle.price;
//                for (BillingAccount ba : bs) {
//                    double v = ba.money - value;
//                    if (v >= 0) {
//                        value -= v;
//                        ba.money = v;
//                    } else {
//                        value -= ba.money;
//                        ba.money = 0;
//                    }
//                }
//                if (value < 0) {
//                    throw new IllegalStateException("value<0");
//                }
//                billingAccountDAO.saveAll(bs);
//                return vehicleRentalDao.save(vr);
//            }
//        }
//        return null;

    }

    @Override
    public void activate(Long rentalId) {
        vehicleRentalDAO.activate(rentalId);
    }
}
