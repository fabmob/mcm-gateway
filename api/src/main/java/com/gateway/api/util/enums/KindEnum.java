package com.gateway.api.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * is this the default price or is this an additional part (discount, price surge). In case of a DISCOUNT, the amount must always be negative and in case of SURGE it must be positive. This also means, that when you're working with discounts or surges, you have to deliver 2 fareparts, one for the default price and one for the discount/surge. This can be used in combination with as well the fixed price parts as with the flex price parts.
 */
public enum KindEnum {
    DEFAULT("DEFAULT"),

    DISCOUNT("DISCOUNT"),

    SURGE("SURGE");

    private String value;

    KindEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static KindEnum fromValue(String text) {
        for (KindEnum b : KindEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
