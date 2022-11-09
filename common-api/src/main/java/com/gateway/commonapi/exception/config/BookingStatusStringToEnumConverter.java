package com.gateway.commonapi.exception.config;

import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.utils.enums.BookingStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

import static com.gateway.commonapi.constants.ErrorCodeDict.BOOKING_STATUS_CODE;
import static com.gateway.commonapi.constants.GatewayErrorMessage.INVALID_STATUS_COVOIT;

/**
 * custom converter for handling the exception before it was picked up by Mismatch exception
 */

@Component
public class BookingStatusStringToEnumConverter implements Converter<String, BookingStatus> {

    @Override
    public BookingStatus convert(String context) {
        try {
            return BookingStatus.valueOf(context);
        } catch (IllegalArgumentException e) {
            GenericError error = new GenericError();
            error.setErrorCode(BOOKING_STATUS_CODE);
            error.setDescription(MessageFormat.format(INVALID_STATUS_COVOIT, context));
            throw new BadRequestException(error);
        }
    }
}
