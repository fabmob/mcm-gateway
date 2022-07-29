package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Either « FREE », « PAYING » or « UNKNOWN ». « UNKNOWN » is given when it should be « PAYING » but we cannot set the price yet.
 */
public enum TypeEnumPrice {
    FREE("FREE"),

    PAYING("PAYING"),

    UNKNOWN("UNKNOWN");

    private String value;

    TypeEnumPrice(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnumPrice fromValue(String text) {
        for (TypeEnumPrice b : TypeEnumPrice.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
