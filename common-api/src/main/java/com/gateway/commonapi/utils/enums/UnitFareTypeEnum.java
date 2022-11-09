package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * in case of 'FLEX' mandatory, otherwise not allowed. E.g. 0.5 EUR per HOUR
 */
public enum UnitFareTypeEnum {
    KM("KM"),

    SECOND("SECOND"),

    MINUTE("MINUTE"),

    HOUR("HOUR"),

    MILE("MILE"),

    PERCENTAGE("PERCENTAGE");

    private String value;

    UnitFareTypeEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static UnitFareTypeEnum fromValue(String text) {
        for (UnitFareTypeEnum b : UnitFareTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
