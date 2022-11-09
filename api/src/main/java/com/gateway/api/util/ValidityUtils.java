package com.gateway.api.util;

import com.gateway.api.service.partnerservice.PartnerService;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.Day;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.gateway.api.util.constant.GatewayMessageDict.FIRST_PLACEHOLDER;
import static com.gateway.api.util.constant.GatewayMessageDict.SECOND_PLACEHOLDER;
import static com.gateway.commonapi.constants.ErrorCodeDict.*;
import static com.gateway.commonapi.constants.GatewayErrorMessage.*;

/**
 * Class for ckecking the validity of an input params
 */
@Slf4j
@Component
public class ValidityUtils {
    public static final Integer MAX_GRADE = 5;
    public static final Integer MIN_GRADE = 1;
    private static final DateTimeFormatter FORMAT_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private PartnerService partnerService;

    private ValidityUtils() {
    }

    /**
     * Check the validity of partner id and throw exception in standard format if it's not valid
     *
     * @param partnerId
     */
    public void checkPartnerId(UUID partnerId) {
        try {
            partnerService.getPartnerMeta(partnerId);
        } catch (BadRequestException e) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            String msg = CommonUtils.placeholderFormat(UNKNOWN_PARTNER_ID_MESSAGE, FIRST_PLACEHOLDER, partnerId.toString());
            GenericError genericError = new GenericError();
            genericError.setErrorCode(PARTNER_ID_CODE);
            genericError.setTimestamp(simpleDateFormat.format(new Date()));
            genericError.setDescription(msg);
            genericError.setLabel(UNKNOWN_PARTNER_ID);
            throw new BadRequestException(genericError);
        }
    }

    /**
     * check lat according to the standard
     *
     * @param messages
     * @param titles
     * @param errorCodes
     * @param lat
     */
    public static void checkLatitude(List<String> messages, List<String> titles, List<Integer> errorCodes, Double lat) {
        if (lat != null && (lat < -90 || lat > 90)) {
            String msg = CommonUtils.placeholderFormat(INVALID_LAT_MESSAGE, FIRST_PLACEHOLDER, lat.toString());

            titles.add(INVALID_LAT_TITLE);
            messages.add(msg);
            errorCodes.add(LATITUDE_CODE);

        }
    }

    /**
     * check lon according to the standard
     *
     * @param messages
     * @param titles
     * @param errorCodes
     * @param lon
     */
    public static void checkLongitude(List<String> messages, List<String> titles, List<Integer> errorCodes, Double lon) {

        if (lon != null && (lon < -180 || lon > 180)) {
            String msg = CommonUtils.placeholderFormat(INVALID_LON_MESSAGE, FIRST_PLACEHOLDER, lon.toString());

            titles.add(INVALID_LON_TITLE);
            messages.add(msg);
            errorCodes.add(LONGITUDE_CODE);

        }
    }

    /**
     * check the positivity of param according to the standard
     *
     * @param messages
     * @param titles
     * @param errorCodes
     * @param param
     * @param message
     * @param title
     * @param errorCode
     */
    public static void checkIfParameterIsPositive(List<String> messages, List<String> titles, List<Integer> errorCodes,
                                                  Float param, String message, String title, Integer errorCode) {
        if (param != null && (param < 0)) {
            String msg = CommonUtils.placeholderFormat(message, FIRST_PLACEHOLDER, param.toString());

            titles.add(title);
            messages.add(msg);
            errorCodes.add(errorCode);

        }
    }


    /**
     * check Time of day for carpool standard
     *
     * @param messages
     * @param departureTimeOfDay
     */
    public static void checkTimeOfDay(List<String> messages, List<String> titles, List<Integer> errorCodes, String departureTimeOfDay) {

        if (departureTimeOfDay != null) {
            try {
                FORMAT_TIME.parse(departureTimeOfDay);
            } catch (Exception e) {
                String msg = CommonUtils.placeholderFormat(INVALID_TIME_OF_DAY_COVOIT, FIRST_PLACEHOLDER, departureTimeOfDay);
                messages.add(msg);
                errorCodes.add(TIME_OF_DAY_CODE);
            }
        }
    }

    /**
     * check Day of week for carpool standard
     *
     * @param messages
     * @param departureWeekdays
     */
    public static void checkDayOfWeek(List<String> messages, List<String> titles, List<Integer> errorCodes, List<String> departureWeekdays) {
        List<String> wrongDays = new ArrayList<>();
        if (departureWeekdays != null && !departureWeekdays.isEmpty()) {
            for (String day : departureWeekdays) {
                if (!EnumUtils.isValidEnum(Day.class, day)) {
                    wrongDays.add(day);
                }
            }
            if (!wrongDays.isEmpty()) {
                String msg = CommonUtils.placeholderFormat(INVALID_DAY_OF_WEEK_COVOIT, FIRST_PLACEHOLDER, wrongDays.toString());
                messages.add(msg);
                errorCodes.add(DAY_OF_WEEK_CODE);
            }
        }
    }

    /**
     * check min and max date for carpool standard
     *
     * @param messages
     * @param minDepartureDate
     * @param maxDepartureDate
     */
    public static void checkMinAndMaxDepartureDate(List<String> messages, List<String> titles, List<Integer> errorCodes, Integer minDepartureDate, Integer maxDepartureDate) {
        if (minDepartureDate != null && (maxDepartureDate != null) && minDepartureDate > maxDepartureDate) {
            String msg = CommonUtils.placeholderFormat(INVALID_MIN_OR_MAX_DEPARTURE_DATE_COVOIT, FIRST_PLACEHOLDER, minDepartureDate.toString(), SECOND_PLACEHOLDER, maxDepartureDate.toString());
            messages.add(msg);
            errorCodes.add(DEPARTURE_DATE_CODE);
        }
    }

    /**
     * check grade for carpool standard
     *
     * @param messages
     * @param grade
     */
    public static void checkGrade(List<String> messages, List<String> titles, List<Integer> errorCodes, Integer grade) {
        if (grade != null && (grade > MAX_GRADE || grade < MIN_GRADE)) {
            String msg = CommonUtils.placeholderFormat(INVALID_GRADE_COVOIT, FIRST_PLACEHOLDER, grade.toString());
            messages.add(msg);
            errorCodes.add(GRADE_CODE);
        }
    }
}
