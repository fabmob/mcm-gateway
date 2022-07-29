package com.gateway.api.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * class of this fare part. Could be FARE or ANCILLARY
 */
public enum PropertyClassEnum {
    FARE("FARE"),

    ANCILLARY("ANCILLARY");

    private String value;

    PropertyClassEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static PropertyClassEnum fromValue(String text) {
        for (PropertyClassEnum b : PropertyClassEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}