package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

/**
 * Gets or Sets fuel
 */
public enum FuelEnum {
    NONE("NONE"),

    GASOLINE("GASOLINE"),

    DIESEL("DIESEL"),

    ELECTRIC("ELECTRIC"),

    HYBRID_GASOLINE("HYBRID_GASOLINE"),

    HYBRID_DIESEL("HYBRID_DIESEL"),

    HYBRID_GAS("HYBRID_GAS"),

    HYDROGEN("HYDROGEN"),

    GAS("GAS"),

    BIO_MASS("BIO_MASS"),

    KEROSINE("KEROSINE"),

    OTHER("OTHER");

    public final String value;

    public String getValue() {
        return value;
    }

    FuelEnum(String value) {
        this.value = value;
    }

    @JsonCreator
    public static FuelEnum forValue(String value) {
        return FuelEnum.valueOf(StringUtils.isNotBlank(value) ? value.toUpperCase() : StringUtils.EMPTY);
    }

    @JsonValue
    public String toValue() {
        return this.value;

    }
}
