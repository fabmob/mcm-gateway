package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets applicableDays
 */
public enum StandardEnum {
    COVOITURAGE_STANDARD("covoiturage-standard"),
    TOMP_1_3_0("tomp-1.3.0"),
    GBFS("GBFS-??"),
    OTHER("other"),
    GATEWAY("");

    private String value;

    StandardEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static StandardEnum fromValue(String text) {
        for (StandardEnum b : StandardEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return GATEWAY;
    }
}
