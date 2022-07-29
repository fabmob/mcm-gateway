package com.gateway.api.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets applicableDays
 */
public enum ApplicableDaysEnum {
    MO("MO"),

    TU("TU"),

    WE("WE"),

    TH("TH"),

    FR("FR"),

    SA("SA"),

    SU("SU");

    private String value;

    ApplicableDaysEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static ApplicableDaysEnum fromValue(String text) {
        for (ApplicableDaysEnum b : ApplicableDaysEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
