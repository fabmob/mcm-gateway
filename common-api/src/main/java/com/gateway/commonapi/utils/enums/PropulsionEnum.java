package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * way in which the asset is powered
 */
public enum PropulsionEnum {
    MUSCLE("MUSCLE"),

    ELECTRIC("ELECTRIC"),

    GASOLINE("GASOLINE"),

    DIESEL("DIESEL"),

    HYBRID("HYBRID"),

    LPG("LPG"),

    HYDROGEN("HYDROGEN");

    private String value;

    PropulsionEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static PropulsionEnum fromValue(String text) {
        for (PropulsionEnum b : PropulsionEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}

