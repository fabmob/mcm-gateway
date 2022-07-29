package com.gateway.api.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Energy efficiency
 */
public enum EnergyLabelEnum {
    A("A"),

    B("B"),

    C("C"),

    D("D"),

    E("E");

    private String value;

    EnergyLabelEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static EnergyLabelEnum fromValue(String text) {
        for (EnergyLabelEnum b : EnergyLabelEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
