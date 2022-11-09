package com.gateway.commonapi.exception.config;


import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.utils.enums.BookingStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class EnumConverterTest {

    private BookingStatusStringToEnumConverter bookingStatusStringToEnumConverter;

    @Before
    public void setup() {
        bookingStatusStringToEnumConverter = new BookingStatusStringToEnumConverter();
    }

    @Test
    public void testBookingStatus() {
        assertEquals(BookingStatus.COMPLETED_PENDING_VALIDATION, bookingStatusStringToEnumConverter.convert("COMPLETED_PENDING_VALIDATION"));
    }

    @Test
    public void testBookingStatus_error() {
        BadRequestException exception = null;
        try {
            bookingStatusStringToEnumConverter.convert("wrong value");
        } catch (BadRequestException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("\"errorCode\":3530"));
    }

}
