package com.gateway.adapter.utils;

import com.gateway.adapter.utils.constant.AdapterMessageDict;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.ParamsMultiCallsDTO;
import com.gateway.commonapi.dto.data.TokenDTO;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static com.gateway.adapter.utils.constant.AdapterMessageDict.BASE_ERROR_MESSAGE;

/**
 * class to process decoding  params operations
 */
@Slf4j
public class CustomParamUtils {
    private CustomParamUtils() {
    }

    /**
     * decode initValue from a paramMultiCalls and retrieve start date that can be NOW (current date) or Shifted with an Offset (current date + Offset)
     *
     * @param recursiveParams a paramMultiCalls
     * @return date in string format
     */
    public static String decodeInitValue(ParamsMultiCallsDTO recursiveParams) {
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AdapterMessageDict.DATE_TIME_FORMAT);
        ZonedDateTime startDate = ZonedDateTime.now(ZoneOffset.UTC);
        if (recursiveParams.getTimezone() != null && !recursiveParams.getTimezone().isEmpty()) {
            try {
                startDate = ZonedDateTime.now(ZoneId.of(recursiveParams.getTimezone()));
            } catch (Exception exception) {
                log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, exception.getMessage()), exception);
                startDate = ZonedDateTime.now(ZoneOffset.UTC);
            }
        }
        if (startDate.truncatedTo(ChronoUnit.HOURS).until(startDate, ChronoUnit.MINUTES) > 29) {
            startDate = truncateStartDate(startDate, 60);
        } else {
            startDate = truncateStartDate(startDate, 30);
        }
        if (AdapterMessageDict.NOW.equals(recursiveParams.getInitValue())) {
            return startDate.format(formatter);
        } else if (AdapterMessageDict.OFFSET.equals(recursiveParams.getInitValue())) {
            return startDate.plus(Long.parseLong(recursiveParams.getValueOffset()), ChronoUnit.MINUTES).format(formatter);
        }
        return startDate.format(formatter);
    }

    /**
     * truncate Start Date
     *
     * @param date
     * @param amountToAdd
     * @return
     */
    public static ZonedDateTime truncateStartDate(ZonedDateTime date, Integer amountToAdd) {
        return date.truncatedTo(ChronoUnit.HOURS).plus(amountToAdd, ChronoUnit.MINUTES);
    }

    /**
     * initialize date params
     *
     * @param paramsOption map that contains list of params multi calls
     * @param params       map that contains a list of dates
     */
    public static void reinitParams(Map<String, ParamsMultiCallsDTO> paramsOption, Map<String, List<String>> params) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AdapterMessageDict.DATE_TIME_FORMAT);
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            List<String> list = entry.getValue();
            if (list != null && list.size() == 1) {
                ZonedDateTime zonedDateTime = LocalDateTime.parse(list.get(0), formatter).atZone(ZoneOffset.UTC);
                if (paramsOption.get(entry.getKey()).getTimezone() != null && !paramsOption.get(entry.getKey()).getTimezone().isEmpty()) {
                    zonedDateTime = LocalDateTime.parse(list.get(0), formatter).atZone(ZoneId.of(paramsOption.get(entry.getKey()).getTimezone()));
                }
                zonedDateTime = zonedDateTime.plus(Long.parseLong(paramsOption.get(entry.getKey()).getValueOffset()), ChronoUnit.MINUTES);
                list.clear();
                list.add(zonedDateTime.format(formatter));
            }
        }
    }

    /**
     * Check the validity of the token
     *
     * @param token the authentication token
     * @return true if the token is valid
     */
    public static boolean isValidToken(TokenDTO token, Long timeInMinutes) {
        boolean result = false;
        // if Token is expired or it expires in a minute
        if (token != null && token.getExpireAt() != null) {
            result = !((token.getExpireAt() != null) && (token.getExpireAt().isBefore(LocalDateTime.now().plusMinutes(timeInMinutes))));
        }
        return result;
    }

    /**
     * retrieve token expiration time from partner response
     *
     * @param jsonObjectResponse partner response
     * @return token expiration date
     * @throws JSONException
     */
    public static LocalDateTime tokenExpireAt(JSONObject jsonObjectResponse) throws JSONException {
        String expiresIn = jsonObjectResponse.get("expires_in").toString();
        String expireAt = String.valueOf(LocalDateTime.now().plusSeconds(Long.parseLong(expiresIn)));
        return LocalDateTime.parse(expireAt);
    }

}
