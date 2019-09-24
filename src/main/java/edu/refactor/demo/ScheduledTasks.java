package edu.refactor.demo;

import edu.refactor.demo.dao.VehicleRentalDAO;
import edu.refactor.demo.entities.VehicleRental;
import edu.refactor.demo.entities.constants.CustomerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class ScheduledTasks {
    private final VehicleRentalDAO vehicleRentalDao;

    @Autowired
    public ScheduledTasks(VehicleRentalDAO vehicleRentalDao) {
        this.vehicleRentalDao = vehicleRentalDao;
    }

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        Iterable<VehicleRental> vrs = vehicleRentalDao.findAll();
        for (VehicleRental vr : vrs) {
            if (vr.status.equals("active")) {
                Instant i = vr.getStartRent();
                long j = Duration.between(i, Instant.now()).getSeconds();
                if (CustomerStatus.DEFAULT == vr.customer.getStatus()) {
                    if (j > 86400) {
                        vr.status = ("expired");
                    }
                } else if (CustomerStatus.VIP == vr.customer.getStatus()) {
                    if (j > 259200) {
                        vr.status = ("expired");
                    }
                }
            }
            vehicleRentalDao.saveAll(vrs);
        }
    }
}