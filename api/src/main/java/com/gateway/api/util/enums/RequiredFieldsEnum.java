package com.gateway.api.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets requiredFields
 */
public enum RequiredFieldsEnum {
    FROM_ADDRESS("FROM_ADDRESS"),

    TO_ADDRESS("TO_ADDRESS"),

    BIRTHDATE("BIRTHDATE"),

    EMAIL("EMAIL"),

    PERSONAL_ADDRESS("PERSONAL_ADDRESS"),

    PHONE_NUMBERS("PHONE_NUMBERS"),

    LICENSES("LICENSES"),

    BANK_CARDS("BANK_CARDS"),

    DISCOUNT_CARDS("DISCOUNT_CARDS"),

    TRAVEL_CARDS("TRAVEL_CARDS"),

    ID_CARDS("ID_CARDS"),

    CREDIT_CARDS("CREDIT_CARDS"),

    NAME("NAME"),

    AGE("AGE"),

    BLOCKCHAIN_CLAIMS("BLOCKCHAIN_CLAIMS");

    private String value;

    RequiredFieldsEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static RequiredFieldsEnum fromValue(String text) {
        for (RequiredFieldsEnum b : RequiredFieldsEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }
}
