package edu.refactor.demo.rest;

import edu.refactor.demo.dao.BillingAccountDAO;
import edu.refactor.demo.entities.BillingAccount;
import edu.refactor.demo.entities.VehicleRental;
import edu.refactor.demo.services.VehicleRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
public class VehicleRentalController {
    private final VehicleRentalService vehicleRentalService;
    private final BillingAccountDAO billingAccountDAO;

    @Autowired
    public VehicleRentalController(VehicleRentalService vehicleRentalService, BillingAccountDAO billingAccountDAO) {
        this.vehicleRentalService = vehicleRentalService;
        this.billingAccountDAO = billingAccountDAO;
    }

    @RequestMapping(value = "/rental", method = RequestMethod.GET)
    public @ResponseBody
    Iterable<VehicleRental> all() {
        return vehicleRentalService.findAll();
    }

    @RequestMapping(value = "/rental/complete", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental completeVehicle(@RequestParam(name = "rental") Long rentalId) {
        Optional<VehicleRental> ro = vehicleRentalService.findById(rentalId);
        if (ro.isPresent()) {
            VehicleRental vr = ro.get();
            if (vr.status.equals("active")) {
                vr.endRent = (Instant.now());
                vr.status = ("completed");
                List<BillingAccount> bs = vr.customer.getBillingAccounts();
                double value = vr.getVehiclePrice();
                for (BillingAccount ba : bs) {
                    double v = ba.money - value;
                    if (v >= 0) {
                        value -= v;
                        ba.money = v;
                    } else {
                        value -= ba.money;
                        ba.money = 0;
                    }
                }
                if (value < 0) {
                    throw new IllegalStateException("value<0");
                }
                billingAccountDAO.saveAll(bs);
                return vehicleRentalService.save(vr);
            }
        }
        return null;
    }

    @RequestMapping(value = "/rental/active", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental activeVehicle(@RequestParam(name = "rental") Long rentalId) {
        Optional<VehicleRental> rental = vehicleRentalService.findById(rentalId);
        if (rental.isPresent()) {
            VehicleRental vehicleRental = rental.get();
            if (vehicleRental.getStatus().equals("created") || vehicleRental.getStatus().equals("expired")) {
                vehicleRental.endRent = (Instant.now());
                vehicleRental.setStatus("active");
                return vehicleRentalService.save(vehicleRental);
            }
        }
        return null;
    }

    @RequestMapping(value = "/rental/expired", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental expiredVehicle(@RequestParam(name = "rental") Long rentalId) {
        return vehicleRentalService.expireActiveVehicleRental(rentalId);
    }

    @RequestMapping(value = "/rental/create", method = RequestMethod.POST)
    public @ResponseBody
    VehicleRental createVehicle(@RequestParam(name = "vehicle") Long vehicleId, @RequestParam(name = "customer") Long customerId) {
        return vehicleRentalService.createBy(vehicleId, customerId);
    }
}
