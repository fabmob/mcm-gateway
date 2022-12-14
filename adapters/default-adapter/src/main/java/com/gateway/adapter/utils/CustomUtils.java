package com.gateway.adapter.utils;

import com.gateway.commonapi.exception.InternalException;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.IllegalFormatException;

import static com.gateway.adapter.utils.constant.AdapterMessageDict.*;

@Slf4j
public class CustomUtils {
    private CustomUtils() {
    }

    /**
     * Add minutes to the OffsetDateTime to enlarge search - Can be negative
     *
     * @param dateTime
     * @param precision
     * @return OffsetDateTime
     * @throws NumberFormatException
     */
    public static OffsetDateTime assignPrecision(OffsetDateTime dateTime, String precision) {

        try {
            Long precisionL = Long.valueOf(precision);
            return dateTime.plusMinutes(precisionL);
        } catch (Exception e) {
            throw new InternalException(ADDING_TIME_FAILED);
        }
    }

    /**
     * format the given dateTime to the expected timezone
     *
     * @param dateTime
     * @param timezone
     * @return OffsetDateTime
     * @throws IllegalFormatException
     */
    public static OffsetDateTime formatDateTime(OffsetDateTime dateTime, String timezone) {
        try {
            ZonedDateTime date = dateTime.toZonedDateTime();
            date = date.withZoneSameInstant(ZoneId.of(timezone));
            return date.toOffsetDateTime();
        } catch (Exception e) {
            throw new InternalException(BAD_TIMEZONE_FORMAT_OR_CAN_T_RETRIEVE_IT);
        }
    }

    /**
     * return value if it is not sensitive or **** if yes
     *
     * @param sensitive
     * @param value
     * @return censored value or not
     */
    public static String censoredParam(Integer sensitive, String value) {
        return ((sensitive != null) && (sensitive >= 1)) ? HIDDEN_TEXT : value;
    }
}
