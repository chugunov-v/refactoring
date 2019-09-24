package edu.refactor.demo.entities.constants;

import org.springframework.lang.Nullable;

public enum CustomerStatus {
    ACTIVE("active"),
    DELETE("delete"),
    DEFAULT("default"),
    VIP("vip"),
    FREEZE("freeze");

    private String id;

    CustomerStatus(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static CustomerStatus fromId(String id) {
        for (CustomerStatus at : CustomerStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}
