package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 * The conditions for returning the vehicle at the end of the rental.
 */
public enum ReturnConstraint {
    FREE_FLOATING("free_floating"),
    ROUNDTRIP_STATION("roundtrip_station"),
    ANY_STATION("any_station"),
    HYBRID("hybrid"),
    UNKNOWN("");

    public final String value;

    ReturnConstraint(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ReturnConstraint forValue(String value) {
        return ReturnConstraint.valueOf(StringUtils.isNotBlank(value) ? value.toUpperCase() : StringUtils.EMPTY);
    }

    @JsonValue
    public String toValue() {
        return this.value;

    }
}
