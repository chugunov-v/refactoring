package edu.refactor.demo.entities.enums;

import org.springframework.lang.Nullable;

public enum VehicleRentalStatus {
	CREATED("created"),
	EXPIRED("expired"),
	COMPLETED("completed");

	private String id;

	VehicleRentalStatus(String value) {
		this.id = value;
	}

	public String getId() {
		return id;
	}

	@Nullable
	public static VehicleRentalStatus fromId(String id) {
		for (VehicleRentalStatus at : VehicleRentalStatus.values()) {
			if (at.getId().equals(id)) {
				return at;
			}
		}
		return null;
	}
}
