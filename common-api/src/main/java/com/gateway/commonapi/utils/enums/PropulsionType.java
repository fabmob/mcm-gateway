package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 * The primary propulsion type of the vehicle.
 */
public enum PropulsionType {
    HUMAN ("human"),
    ELECTRIC_ASSIST("electric_assist"),
    ELECTRIC("electric"),
    COMBUSTION("combustion"),
    COMBUSTION_DIESEL("combustion_diesel"),
    HYBRID("hybrid"),
    PLUG_IN_HYBRID("plug_in_hybrid"),
    HYDROGEN_FUEL_CELL("hydrogen_fuel_cell"),
    UNKNOWN("");

    public final String value;
    PropulsionType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static PropulsionType forValue(String value) {
        return PropulsionType.valueOf(StringUtils.isNotBlank(value) ? value.toUpperCase() : StringUtils.EMPTY);
    }

    @JsonValue
    public String toValue() {
        return this.value;

    }
}
