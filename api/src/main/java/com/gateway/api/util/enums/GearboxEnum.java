package com.gateway.api.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * type of gearbox
 */
public enum GearboxEnum {
    MANUAL("MANUAL"),

    AUTOMATIC("AUTOMATIC"),

    SEMIAUTOMATIC("SEMIAUTOMATIC");

    private String value;

    GearboxEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static GearboxEnum fromValue(String text) {
        for (GearboxEnum b : GearboxEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}

