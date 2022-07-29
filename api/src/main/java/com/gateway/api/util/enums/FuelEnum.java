package com.gateway.api.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    private String value;

    FuelEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static FuelEnum fromValue(String text) {
        for (FuelEnum b : FuelEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
