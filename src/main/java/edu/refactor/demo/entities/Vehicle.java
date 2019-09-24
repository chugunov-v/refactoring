package edu.refactor.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static edu.refactor.demo.entities.constants.VehicleStatus.OPEN;

@Entity
public class Vehicle implements Serializable {
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    @Column
    private Long id;
    @Column
    private String title;
    @Column
    public double price;
    @Column
    private String status = OPEN.getId();
    @Column
    private String type;
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VehicleRental> rentals = new ArrayList<>();
    @Column
    public String serialNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<VehicleRental> getRentals() {
        return rentals;
    }

    public void setRentals(List<VehicleRental> rentals) {
        this.rentals = rentals;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public static Vehicle create(double price, String title, String type, String serialNumber) {
        Vehicle vehicle = new Vehicle();
        vehicle.setPrice(price);
        vehicle.setTitle(title);
        vehicle.setType(type);
        vehicle.setSerialNumber(serialNumber);
        return vehicle;
    }
}
