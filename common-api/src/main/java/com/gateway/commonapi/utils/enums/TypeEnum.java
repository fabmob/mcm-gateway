package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * type of external reference (GTFS, CHB).
 */
public enum TypeEnum {
    GTFS_STOP_ID("GTFS_STOP_ID"),

    GTFS_STOP_CODE("GTFS_STOP_CODE"),

    GTFS_AREA_ID("GTFS_AREA_ID"),

    CHB_STOP_PLACE_CODE("CHB_STOP_PLACE_CODE"),

    CHB_QUAY_CODE("CHB_QUAY_CODE"),

    NS_CODE("NS_CODE");

    private String value;

    TypeEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
        for (TypeEnum b : TypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
