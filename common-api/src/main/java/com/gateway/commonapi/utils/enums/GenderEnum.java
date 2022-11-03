package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;

import java.text.MessageFormat;

import static com.gateway.commonapi.constants.ErrorCodeDict.GENDER_CODE;
import static com.gateway.commonapi.constants.GatewayErrorMessage.INVALID_GENDER_COVOIT;

/**
 * User's gender. 'O' stands for 'Other'.
 */
public enum GenderEnum {
    F("F"),

    M("M"),

    O("O");


    public final String value;

    public String getValue() {
        return value;
    }

    GenderEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static GenderEnum fromValue(String text) {
        for (GenderEnum b : GenderEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }

        GenericError error = new GenericError();
        error.setErrorCode(GENDER_CODE);
        error.setDescription(MessageFormat.format(INVALID_GENDER_COVOIT, text));
        throw new BadRequestException(error);
    }
}
