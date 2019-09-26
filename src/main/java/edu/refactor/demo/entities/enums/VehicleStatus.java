package edu.refactor.demo.entities.enums;

import org.springframework.lang.Nullable;

import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;

public enum VehicleStatus {
    OPEN("open"),
    LOST("lost"),
    DELETE("delete"),
    RESERVED("reserved"),
    RETURNED("returned"),
    SERVICE("service"),
    LEASED("leased");

    private String id;

    VehicleStatus(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static VehicleStatus fromId(String id) {
        for (VehicleStatus at : VehicleStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }

    public static Optional<VehicleStatus> getNexStatus(VehicleStatus current, VehicleStatus proposedStatus) {
        VehicleStatus vehicleStatus;
        switch (current) {
            case SERVICE:
                vehicleStatus = isProposedStatusAllowed(proposedStatus, OPEN);
                break;
            case RESERVED:
                vehicleStatus = isProposedStatusAllowed(proposedStatus, OPEN, LEASED);
                break;
            case LEASED:
                vehicleStatus = isProposedStatusAllowed(proposedStatus, RETURNED);
                break;
            case OPEN:
                vehicleStatus = isProposedStatusAllowed(proposedStatus, DELETE, RESERVED);
                break;
            case RETURNED:
                vehicleStatus = isProposedStatusAllowed(proposedStatus, SERVICE);
                break;
            case DELETE:
                vehicleStatus = isProposedStatusAllowed(proposedStatus, OPEN);
                break;
            default:
                vehicleStatus = null;
        }
        return ofNullable(vehicleStatus);
    }

    private static VehicleStatus isProposedStatusAllowed(VehicleStatus proposedStatus, VehicleStatus... allowedStatuses) {
        return stream(allowedStatuses)
            .anyMatch(status -> status == proposedStatus) ? proposedStatus : null;
    }
}
