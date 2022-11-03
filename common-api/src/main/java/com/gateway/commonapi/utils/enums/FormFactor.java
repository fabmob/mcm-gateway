package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 *The vehicle's general form factor.
 */
public enum FormFactor {
    BICYCLE("bicycle"),
    CARGO_BICYCLE("cargo_bicycle"),
    CAR("car"),
    SCOOTER("scooter"),
    SCOOTER_STANDING("scooter_standing"),
    SCOOTED_SEATED("scooted_seated"),
    MOPED("moped"),
    UNKNOWN("");


    public final String value;

    public String getValue() {
        return value;
    }

    FormFactor(String value) {
        this.value = value;
    }

    @JsonCreator
    public static FormFactor forValue(String value) {
        return FormFactor.valueOf(StringUtils.isNotBlank(value) ? value.toUpperCase() : StringUtils.EMPTY);
    }

    @JsonValue
    public String toValue() {
        return this.value;

    }
}
