package edu.refactor.demo.services.impl;

import edu.refactor.demo.core.customer.BillingAccountDAO;
import edu.refactor.demo.core.customer.CustomerDAO;
import edu.refactor.demo.core.vehicle.VehicleDAO;
import edu.refactor.demo.core.vehicle.VehicleRentalDAO;
import edu.refactor.demo.entities.BillingAccount;
import edu.refactor.demo.entities.Customer;
import edu.refactor.demo.entities.Vehicle;
import edu.refactor.demo.entities.VehicleRental;
import edu.refactor.demo.services.VehicleRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;

import static edu.refactor.demo.exceptions.ExceptionUtils.*;

@Service
public class VehicleRentalServiceImpl implements VehicleRentalService {
    private final VehicleRentalDAO vehicleRentalDAO;
    private final CustomerDAO customerDAO;
    private final VehicleDAO vehicleDAO;
    private final BillingAccountDAO billingAccountDAO;

    private static final Logger LOGGER = Logger.getLogger(VehicleRentalServiceImpl.class.getName());

    @Autowired
    public VehicleRentalServiceImpl(VehicleRentalDAO vehicleRentalDAO, CustomerDAO customerDAO, VehicleDAO vehicleDAO, BillingAccountDAO billingAccountDAO) {
        this.vehicleRentalDAO = vehicleRentalDAO;
        this.customerDAO = customerDAO;
        this.vehicleDAO = vehicleDAO;
        this.billingAccountDAO = billingAccountDAO;
    }

    @Override
    public List<VehicleRental> findAll() {
        LOGGER.info("Loading vehicle rental list");
        List<VehicleRental> vehicleRentals = vehicleRentalDAO.findAll();
        LOGGER.info("Vehicle rental list was loaded");
        return vehicleRentals;
    }

    @Override
    @Transactional
    public VehicleRental create(Long vehicleId, Long customerId) {
        LOGGER.info("Creating vehicle rental");
        VehicleRental vehicleRental = create(getVehicle(vehicleId), getCustomer(customerId));
        LOGGER.info("Vehicle rental was created");
        return vehicleRental;
    }

    private Customer getCustomer(Long vehicleId) {
        return customerDAO.findById(vehicleId).orElseThrow(() -> NOT_FOUND_CUSTOMER_EXCEPTION);
    }

    private Vehicle getVehicle(Long vehicleId) {
        return vehicleDAO.findById(vehicleId).orElseThrow(() -> NOT_FOUND_VEHICLE_EXCEPTION);
    }

    private VehicleRental create(Vehicle vehicle, Customer customer) {
        return vehicleRentalDAO.save(VehicleRental.create(customer, vehicle));
    }

    @Override
    public VehicleRental markAsExpired(Long rentalId) {
        return saveExpired(getActiveVehicleRental(rentalId));
    }

    private VehicleRental saveExpired(VehicleRental vehicleRental) {
        LOGGER.info("Marking vehicle rental as expired");
        vehicleRental.markAsExpired(Instant.now());
        return vehicleRentalDAO.save(vehicleRental);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsExpiredIfNecessary() {
        LOGGER.info("Scheduled task was started");
        vehicleRentalDAO.updateCustomerStatus();
    }

    @Override
    @Transactional
    public void activate(Long rentalId) {
        LOGGER.info("Activating vehicle rental");
        vehicleRentalDAO.activate(rentalId);
        LOGGER.info("Vehicle rental was activated");
    }

    @Override
    @Transactional
    public void complete(Long rentalId) {
        complete(getActiveVehicleRental(rentalId));
    }

    private VehicleRental getActiveVehicleRental(Long rentalId) {
        return vehicleRentalDAO
            .findActiveById(rentalId)
            .orElseThrow(() -> NOT_FOUND_VEHICLE_RENTAL_EXCEPTION);
    }

    private void complete(VehicleRental vehicleRental) {
        LOGGER.info("Complete the vehicle rental");
        withdrawMoneyFromBillingAccount(vehicleRental);
        vehicleRental.markAsCompleted(Instant.now());
        vehicleRentalDAO.save(vehicleRental);
    }

    private void withdrawMoneyFromBillingAccount(VehicleRental vehicleRental) {
        LOGGER.info("Trying to withdraw money from billing account");
        BigDecimal vehiclePrice = vehicleRental.getVehiclePrice();
        List<BillingAccount> billingAccounts = billingAccountDAO.findByRental(vehicleRental.getId());
        withdrawMoney(billingAccounts, vehiclePrice);
    }

    private void withdrawMoney(List<BillingAccount> billingAccounts, BigDecimal vehiclePrice) {
        billingAccounts
            .stream()
            .filter(account -> BillingAccount.withdrawMoney(account, vehiclePrice))
            .findFirst()
            .map(billingAccountDAO::save)
            .orElseThrow(() -> NOT_FOUND_MONEY_EXCEPTION);
        LOGGER.info("the billing account was successfully updated");
    }
}
