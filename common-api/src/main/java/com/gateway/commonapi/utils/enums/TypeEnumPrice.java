package com.gateway.commonapi.utils.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;

import java.text.MessageFormat;

import static com.gateway.commonapi.constants.ErrorCodeDict.PRICE_TYPE_CODE;
import static com.gateway.commonapi.constants.GatewayErrorMessage.INVALID_TYPE_OF_PRICE_COVOIT;


/**
 * Either « FREE », « PAYING » or « UNKNOWN ». « UNKNOWN » is given when it should be « PAYING » but we cannot set the price yet.
 */
public enum TypeEnumPrice {
    FREE("FREE"),

    PAYING("PAYING"),

    UNKNOWN("UNKNOWN");

    private String value;

    TypeEnumPrice(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnumPrice fromValue(String text) {
        for (TypeEnumPrice b : TypeEnumPrice.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }

        GenericError error = new GenericError();
        error.setErrorCode(PRICE_TYPE_CODE);
        error.setDescription(MessageFormat.format(INVALID_TYPE_OF_PRICE_COVOIT, text));
        throw new BadRequestException(error);
    }


}
