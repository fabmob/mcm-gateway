package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * type of fare part. If there is only one farepart and this field is missing, it should be assumed it is 'FIXED'. In all other situations this field is mandatory.
 */
public enum FareTypeEnum {
    FIXED("FIXED"),

    FLEX("FLEX"),

    MAX("MAX");

    private String value;

    FareTypeEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static FareTypeEnum fromValue(String text) {
        for (FareTypeEnum b : FareTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
