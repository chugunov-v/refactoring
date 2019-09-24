package edu.refactor.demo.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

import static edu.refactor.demo.entities.constants.VehicleRentalStatus.CREATED;

@Entity
public class VehicleRental implements Serializable {
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    public Customer customer;

    @Column(name = "start_rent")
    private Instant startRent;

    @Column(name = "end_rent")
    public Instant endRent;

    @Column
    public String status = CREATED.getId();

    public static VehicleRental create(Customer customer, Vehicle vehicle) {
        VehicleRental vehicleRental = new VehicleRental();
        vehicleRental.customer = customer;
        vehicleRental.vehicle = vehicle;
        vehicleRental.startRent = Instant.now();
        return vehicleRental;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Double getVehiclePrice() {
        return vehicle == null ? null : vehicle.price;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Instant getStartRent() {
        return startRent;
    }

    public void setStartRent(Instant startRent) {
        this.startRent = startRent;
    }

    public Instant getEndRent() {
        return endRent;
    }

    public void setEndRent(Instant endRent) {
        this.endRent = endRent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
