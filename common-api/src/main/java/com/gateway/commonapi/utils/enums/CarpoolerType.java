package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;

import java.text.MessageFormat;

import static com.gateway.commonapi.constants.ErrorCodeDict.CARPOOLER_TYPE_CODE;
import static com.gateway.commonapi.constants.GatewayErrorMessage.INVALID_TYPE_OF_RECIPIENT_COVOIT;

public enum CarpoolerType {

    DRIVER("DRIVER"),

    PASSENGER("PASSENGER");

    private String value;

    public String getValue() {
        return value;
    }

    CarpoolerType(String value) {
        this.value = value;
    }


    @JsonValue
    public String toValue() {
        return this.value;

    }

    @JsonCreator
    public static CarpoolerType fromValue(String text) {
        for (CarpoolerType b : CarpoolerType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }

        GenericError error = new GenericError();
        error.setErrorCode(CARPOOLER_TYPE_CODE);
        error.setDescription(MessageFormat.format(INVALID_TYPE_OF_RECIPIENT_COVOIT, text));
        throw new BadRequestException(error);
    }
}
