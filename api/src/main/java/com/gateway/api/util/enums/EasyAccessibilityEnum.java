package com.gateway.api.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * describes if asset is or needs to be easily accessible
 */
public enum EasyAccessibilityEnum {
    LIFT("LIFT"),

    ESCALATOR("ESCALATOR"),

    GROUND_LEVEL("GROUND_LEVEL"),

    SIGHTIMPAIRMENT("SIGHTIMPAIRMENT"),

    HEARINGIMPAIRMENT("HEARINGIMPAIRMENT"),

    WHEELCHAIR("WHEELCHAIR");

    private String value;

    EasyAccessibilityEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static EasyAccessibilityEnum fromValue(String text) {
        for (EasyAccessibilityEnum b : EasyAccessibilityEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
