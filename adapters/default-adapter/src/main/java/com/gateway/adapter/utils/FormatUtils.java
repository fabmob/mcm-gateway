package com.gateway.adapter.utils;


import com.gateway.adapter.utils.enums.FormatFunctions;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.DataMapperDTO;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import java.text.MessageFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static com.gateway.adapter.utils.constant.AdapterMessageDict.*;


@Slf4j
public class FormatUtils {

    /**
     * Default constructor.
     *
     * @throws IllegalStateException Utility class, constructor should not be used.
     */
    private FormatUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static final String FORMAT_REGEXP = "([A-Z_]*)(\\()(.*)(\\))";
    public static final String VALUE = "value";
    public static final String CHAMPINTERNE = "champInterne";
    public static final String TIMESTAMP = "timestamp";
    public static final String PARAMS_SEPARATOR = ",";
    public static final String KEY_VALUE_SEPARATOR = "=";
    private static final String correlationId = String.valueOf(CommonUtils.setHeader().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));


    /**
     * Parse format field and redirects to the correct formatting function
     *
     * @param value
     * @param format
     * @param champInterne
     * @return Map containing champInterne to create and formatted value assigned to it
     */
    public static Map<String, Object> formatValue(Object value, String format, String champInterne, String timezone, DataMapperDTO mapperDTO) {
        log.info(FORMATTING_FUNCTION + mapperDTO.getDataMapperId().toString() + " :");
        try {
            String function = "";
            String parameters = "";
            Pattern pattern = Pattern.compile(FORMAT_REGEXP);
            Matcher matcher = pattern.matcher(format);
            while (matcher.find()) {
                function = matcher.group(1);
                parameters = matcher.group(3);
            }
            String[] parametersList;

            Map<String, Object> formattedData = null;
            switch (FormatFunctions.valueOf(function)) {
                case NUMERIC_OPERATOR:
                    parametersList = parameters.split(PARAMS_SEPARATOR);
                    formattedData = numericOperator(parametersList[0], Float.valueOf(parametersList[1]), value, champInterne);
                    break;

                case CONVERT_LIST_TO_STRING:
                    formattedData = convertListToString(parameters.replace("\"", ""), value, champInterne);
                    break;

                case FORMAT_DATE:
                    formattedData = formatDate(parameters.replace("\"", ""), timezone, value, champInterne);
                    break;

                case CONVERT_STRING_TO_BOOLEAN:
                    // Map containing all couples : [String value] / [Associated Boolean champInterne]
                    Map<String, String> associationsMap = new HashMap<>();
                    parametersList = parameters.split(PARAMS_SEPARATOR);
                    for (String couple : parametersList) {
                        String association = couple.trim().replace("\"", "");
                        String[] associationElements = association.split(KEY_VALUE_SEPARATOR);
                        if (associationElements.length == 2) {
                            associationsMap.put(associationElements[0], associationElements[1]);
                        } else {
                            throw new NotFoundException(CommonUtils.placeholderFormat(UNRECOGNISED_FUNCTION, DATA_MAPPER_ID, mapperDTO.getDataMapperId().toString()));
                        }
                    }
                    formattedData = convertStringToBoolean(associationsMap, value);
                    break;

            }

            return formattedData;

        } catch (IllegalArgumentException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new InternalException(CommonUtils.placeholderFormat(UNRECOGNISED_FUNCTION, DATA_MAPPER_ID, mapperDTO.getDataMapperId().toString()));

        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new InternalException(e.getMessage());
        }
    }


    /**
     * Apply a numeric operation with given factor on value
     *
     * @param operator
     * @param factor
     * @param value
     * @param champInterne
     * @return map containing new value and associated champInterne
     */
    public static Map<String, Object> numericOperator(String operator, Float factor, Object value, String champInterne) {
        log.info(NUMERIC_OPERATOR + operator + PARAMS_SEPARATOR + factor);
        Map<String, Object> formattedData = new HashMap<>();
        Float result;
        try {
            switch (operator) {
                case "*":
                case "X":
                case "x":
                    result = Float.parseFloat(String.valueOf(value)) * factor;
                    break;
                case "-":
                    result = Float.parseFloat(String.valueOf(value)) - factor;
                    break;
                case "+":
                    result = Float.parseFloat(String.valueOf(value)) + factor;
                    break;
                case "/":
                    result = Float.parseFloat(String.valueOf(value)) / factor;
                    break;
                default:
                    throw new InternalException(UNRECOGNISED_OPERATOR);
            }
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new InternalException(e.getMessage());
        }

        formattedData.put(VALUE, result);
        formattedData.put(CHAMPINTERNE, champInterne);
        return formattedData;
    }

    /**
     * Concat, with given separator, a list of string into a string
     *
     * @param separator
     * @param value
     * @param champInterne
     * @return map containing new value and associated champInterne
     */
    public static Map<String, Object> convertListToString(String separator, Object value, String champInterne) {
        log.info(CONVERT_LIST_TO_STRING + separator + "\"");
        Map<String, Object> formattedData = new HashMap<>();

        try {
            if (value instanceof JSONArray) {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < ((JSONArray) value).length(); i++) {
                    list.add(((JSONArray) value).getString(i));
                }
                String result = String.join(separator, list);
                formattedData.put(VALUE, result);
            } else {
                throw new InternalException(CommonUtils.placeholderFormat(NOT_LIST, VALUE, value.toString()));
            }
        } catch (Exception e) {
            throw new InternalException(CommonUtils.placeholderFormat(NOT_LIST, VALUE, value.toString()));
        }


        formattedData.put(CHAMPINTERNE, champInterne);
        return formattedData;
    }

    /**
     * Format date from its original format and timezone into OffsetDateTime Z
     *
     * @param format
     * @param timezone
     * @param value
     * @param champInterne
     * @return map containing new value and associated champInterne
     */
    public static Map<String, Object> formatDate(String format, String timezone, Object value, String champInterne) {
        log.info(FORMAT_DATE + format + "\"");
        Map<String, Object> formattedData = new HashMap<>();
        try {
            Object newValue;
            if (format.equalsIgnoreCase(TIMESTAMP)) {
                Long timestamp = Long.valueOf((Integer) value) * 1000L;
                Date currentDate = new Date(timestamp);
                ZonedDateTime date = ZonedDateTime.ofInstant(currentDate.toInstant(),
                        ZoneId.of(timezone));
                newValue = ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
            } else {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
                OffsetDateTime date = LocalDateTime.parse(value.toString(), dateTimeFormatter)
                        .atZone(ZoneId.of(timezone))
                        .toOffsetDateTime();
                newValue = OffsetDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
            }

            formattedData.put(VALUE, newValue);
            formattedData.put(CHAMPINTERNE, champInterne);
            return formattedData;

        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new InternalException(e.getMessage());
        }
    }

    /**
     * Assign true to Boolean associated to the initial string value
     *
     * @param associationsMap
     * @param value
     * @return map containing new value and associated champInterne
     */
    public static Map<String, Object> convertStringToBoolean(Map<String, String> associationsMap, Object value) {
        log.info(CONVERT_STRING_TO_BOOLEAN + associationsMap.entrySet());
        Map<String, Object> formattedData = new HashMap<>();
        Object newValue = null;
        String newChampInterne = associationsMap.get(value.toString());
        if (StringUtils.isNotBlank(newChampInterne)) {
            newValue = true;
        }

        formattedData.put(VALUE, newValue);
        formattedData.put(CHAMPINTERNE, newChampInterne);
        return formattedData;
    }
}

