package edu.refactor.demo.entities.enums;

import org.junit.Test;

import static edu.refactor.demo.entities.enums.VehicleStatus.*;
import static org.junit.Assert.assertEquals;

public class VehicleStatusTest {

    @Test
    public void testVehicleStatusModel() {
        assertEquals(DELETE, VehicleStatus.getNexStatus(OPEN, DELETE).get());
        assertEquals(RESERVED, VehicleStatus.getNexStatus(OPEN, RESERVED).get());
        assertEquals(RETURNED, VehicleStatus.getNexStatus(LEASED, RETURNED).get());
        assertEquals(SERVICE, VehicleStatus.getNexStatus(RETURNED, SERVICE).get());
        assertEquals(LEASED, VehicleStatus.getNexStatus(RESERVED, LEASED).get());
        assertEquals(OPEN, VehicleStatus.getNexStatus(DELETE, OPEN).get());
    }
}