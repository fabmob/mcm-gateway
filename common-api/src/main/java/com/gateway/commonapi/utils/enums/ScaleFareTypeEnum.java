package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets scaleType
 */
public enum ScaleFareTypeEnum {
    KM("KM"),

    MILE("MILE"),

    HOUR("HOUR"),

    MINUTE("MINUTE");

    private String value;

    ScaleFareTypeEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static ScaleFareTypeEnum fromValue(String text) {
        for (ScaleFareTypeEnum b : ScaleFareTypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
