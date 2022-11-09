package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;
import io.swagger.v3.oas.annotations.media.Schema;

import java.text.MessageFormat;

import static com.gateway.commonapi.constants.ErrorCodeDict.BOOKING_STATUS_CODE;
import static com.gateway.commonapi.constants.GatewayErrorMessage.INVALID_STATUS_COVOIT;

@Schema(description = "Status of the booking.")
public enum BookingStatus {
    WAITING_CONFIRMATION("WAITING_CONFIRMATION"),
    CONFIRMED("CONFIRMED"),
    CANCELLED("CANCELLED"),
    COMPLETED_PENDING_VALIDATION("COMPLETED_PENDING_VALIDATION"),
    VALIDATED("VALIDATED");

    public final String value;

    public String getValue() {
        return value;
    }

    BookingStatus(String value) {
        this.value = value;
    }


    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }


    @JsonCreator
    public static BookingStatus fromValue(String text) {
        for (BookingStatus b : BookingStatus.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }

        GenericError error = new GenericError();
        error.setErrorCode(BOOKING_STATUS_CODE);
        error.setDescription(MessageFormat.format(INVALID_STATUS_COVOIT, text));
        throw new BadRequestException(error);
    }

}
