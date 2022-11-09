package com.gateway.api.util;

import com.gateway.api.service.partnerservice.impl.PartnerServiceImpl;
import com.gateway.commonapi.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.gateway.commonapi.constants.ErrorCodeDict.*;
import static com.gateway.commonapi.constants.GatewayErrorMessage.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidityUtilsTest {

    List<String> messages;
    List<String> titles;
    List<Integer> errorCodes;

    @InjectMocks
    ValidityUtils validityUtils;

    @Mock
    private PartnerServiceImpl partnerService;

    @BeforeEach
    void setUp() {
        clearLists();
    }

    private void clearLists() {
        messages = new ArrayList<>();
        titles = new ArrayList<>();
        errorCodes = new ArrayList<>();
    }

    private void assertNoError() {
        assertTrue(messages.isEmpty());
        assertTrue(titles.isEmpty());
        assertTrue(errorCodes.isEmpty());
    }

    private void assertError(Integer expectedErrorCode, boolean mustHaveTitle) {
        assertFalse(messages.isEmpty());
        if (mustHaveTitle) {
            assertFalse(titles.isEmpty());
        }
        assertFalse(errorCodes.isEmpty());
        assertTrue(errorCodes.stream().allMatch(expectedErrorCode::equals));
    }

    @Test
    void checkPartnerId() {
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        when(partnerService.getPartnerMeta(partnerId)).thenThrow(BadRequestException.class);
        assertThrows(BadRequestException.class, () -> validityUtils.checkPartnerId(partnerId));
    }

    @Test
    void checkLatitude() {
        Double latitude = 10D;
        validityUtils.checkLatitude(messages, titles, errorCodes, latitude);
        assertNoError();

        latitude = 200D;
        validityUtils.checkLatitude(messages, titles, errorCodes, latitude);
        assertTrue(errorCodes.contains(LATITUDE_CODE));
        assertTrue(titles.contains(INVALID_LAT_TITLE));
    }

    @Test
    void checkLongitude() {
        Double longitude = 20D;
        validityUtils.checkLongitude(messages, titles, errorCodes, longitude);
        assertNoError();

        longitude = 200D;
        validityUtils.checkLongitude(messages, titles, errorCodes, longitude);
        assertTrue(errorCodes.contains(LONGITUDE_CODE));
        assertTrue(titles.contains(INVALID_LON_TITLE));
    }

    @Test
    void checkIfParameterIsPositive() {
        Float duration = 2F;
        validityUtils.checkIfParameterIsPositive(messages, titles, errorCodes, duration, INVALID_DURATION_COVOIT, "invalid duration", DURATION_CODE);
        assertNoError();

        duration = -2F;
        validityUtils.checkIfParameterIsPositive(messages, titles, errorCodes, duration, INVALID_DURATION_COVOIT, "invalid duration", DURATION_CODE);
        assertTrue(errorCodes.contains(DURATION_CODE));
        assertTrue(titles.contains("invalid duration"));
    }

    @Test
    void checkTimeOfDay() {
        //Correct hours
        ValidityUtils.checkTimeOfDay(messages, titles, errorCodes, "00:00:00");
        ValidityUtils.checkTimeOfDay(messages, titles, errorCodes, "12:25:55");
        assertNoError();

        //Bad hours
        ValidityUtils.checkTimeOfDay(messages, titles, errorCodes, "wrong format");
        assertError(1544, false);
        clearLists();

        ValidityUtils.checkTimeOfDay(messages, titles, errorCodes, "24:33:33");
        assertError(1544, false);
        clearLists();

        ValidityUtils.checkTimeOfDay(messages, titles, errorCodes, "12:99:50");
        assertError(1544, false);
        clearLists();

        ValidityUtils.checkTimeOfDay(messages, titles, errorCodes, "12:33:99");
        assertError(1544, false);
        clearLists();

        ValidityUtils.checkTimeOfDay(messages, titles, errorCodes, "12:33:44+01:00");
        assertError(1544, false);
        clearLists();

        ValidityUtils.checkTimeOfDay(messages, titles, errorCodes, "12:33:99.987654321");
        assertError(1544, false);
        clearLists();
    }

    @Test
    void checkDayOfWeek() {
        ValidityUtils.checkDayOfWeek(messages, titles, errorCodes, List.of("MON", "FRI"));
        assertNoError();

        ValidityUtils.checkDayOfWeek(messages, titles, errorCodes, List.of("MON", "WRONG_DAY", "FRI"));
        assertError(1546, false);
    }

    @Test
    void checkMinAndMaxDepartureDate() {
        ValidityUtils.checkMinAndMaxDepartureDate(messages, titles, errorCodes, 1000, 1500);
        assertNoError();

        ValidityUtils.checkMinAndMaxDepartureDate(messages, titles, errorCodes, 1000, 500);
        assertError(1547, false);
    }

    @Test
    void checkGrade() {
        ValidityUtils.checkGrade(messages, titles, errorCodes, 1);
        assertNoError();


        ValidityUtils.checkGrade(messages, titles, errorCodes, 5);
        assertNoError();

        ValidityUtils.checkGrade(messages, titles, errorCodes, 6);
        assertError(3533, false);
    }
}