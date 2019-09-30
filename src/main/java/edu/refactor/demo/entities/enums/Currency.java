package edu.refactor.demo.entities.enums;

import org.springframework.lang.Nullable;

public enum Currency {
    DOLLARS("dollars"),
    RUBLES("rubles"),
    EUROS("euros");

    private String id;

    Currency(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static Currency fromId(String id) {
        for (Currency c : Currency.values()) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }
}