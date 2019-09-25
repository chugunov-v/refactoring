package edu.refactor.demo.entities.enums;

import org.springframework.lang.Nullable;

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
}
