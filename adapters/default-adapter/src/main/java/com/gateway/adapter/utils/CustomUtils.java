package com.gateway.adapter.utils;

import com.gateway.commonapi.exception.InternalException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.*;
import java.util.IllegalFormatException;

import static com.gateway.adapter.utils.constant.AdapterMessageDict.*;

@Slf4j
@NoArgsConstructor
public class CustomUtils {


    /**
     * Add minutes to the OffsetDateTime to enlarge search - Can be negative
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
     * @param sensitive
     * @param value
     * @return censored value or not
     */
    public static String censoredBodyParam(Integer sensitive, String value) {
        return sensitive >= 1 ? HIDDEN_TEXT : value;
    }
}
