package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
/**
 * Type of journey. A dynamic journey is happening in real time.
 */
public enum TypeEnumDriver {
    PLANNED("PLANNED"),

    DYNAMIC("DYNAMIC"),

    LINE("LINE");

    private String value;

    TypeEnumDriver(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnumDriver fromValue(String text) {
        for (TypeEnumDriver b : TypeEnumDriver.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
