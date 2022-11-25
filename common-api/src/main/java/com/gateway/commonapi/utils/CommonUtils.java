package com.gateway.commonapi.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.exceptions.CarpoolError;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.TompError;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.monitoring.UserContext;
import com.gateway.commonapi.utils.enums.FormatFunctions;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Slf4j
public class CommonUtils {

    private static final Pattern uuidPattern = Pattern.compile(GlobalConstants.UUID_REGEXP);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private CommonUtils() {
    }

    public static final String FORMAT_REGEXP = "([A-Z_]*)(\\()(.*)(\\))";
    public static final String VALUE = "value";
    public static final String PARAMS_SEPARATOR = ",";
    public static final String KEY_VALUE_SEPARATOR = "=";


    public static boolean isUUID(String uuid) {
        Matcher matcher = uuidPattern.matcher(uuid);
        return matcher.matches();
    }

    /**
     * prepare context and headers for http call
     *
     * @return httpHeaders
     */
    public static HttpHeaders setHttpHeaders() {
        UserContext userContext = new ThreadLocalUserSession().get();
        String correlationId = userContext.getContextId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
        return httpHeaders;
    }

    /**
     * Concert GenericError to json as string
     *
     * @param error Error DTO
     * @return String with json inside
     */
    public static String stringifyGenericErrorDto(GenericError error) {
        String response = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(error);
        } catch (JsonProcessingException e) {
            log.error("Error parsing GenericError", e);
        }
        return response;
    }


    /**
     * Concert GenericError to json as string
     *
     * @param error Error DTO
     * @return String with json inside
     */
    public static String stringifyTompErrorDto(TompError error) {
        String response = "";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(error);
        } catch (JsonProcessingException e) {
            log.error("Error parsing TompError", e);
        }
        return response;
    }


    /**
     * Inject into a template using { } to delimiter variables the params passed. Params must be multiple of 2, first of couple is the name of the variable and the second one is the value to inject in the template.
     *
     * @param template String of the template with {vars} with  brackets delimiters
     * @param params   Parameters couple of variable to inject with associated value must be multiple of 2
     * @return the String resulting of the template formatting
     */
    public static String placeholderFormat(String template, String... params) {
        StringSubstitutor stringSubstitutor = new StringSubstitutor(toMap(params), "{", "}", '~');
        return stringSubstitutor.replace(template);
    }


    /**
     * Convert onto a map couple of params. First is the key and following the value. entries must be multiple of 2 otherwise raise IllegalArgumentException
     *
     * @param entries couple of paramKey and value to transform into map
     * @return the map of key values
     */
    public static Map<String, String> toMap(String... entries) {
        if (entries.length % 2 == 1)
            throw new IllegalArgumentException("Invalid entries");
        return IntStream.range(0, entries.length / 2).map(i -> i * 2).collect(HashMap::new,
                (m, i) -> m.put(entries[i], entries[i + 1]), Map::putAll);
    }

    public static HttpEntity<String> setHeaders() {
        String correlationId = new ThreadLocalUserSession().get().getContextId();
        org.springframework.http.HttpHeaders httpHeaders = new org.springframework.http.HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
        StandardEnum outpuStandard = new ThreadLocalUserSession().get().getOutPutStandard();
        if (outpuStandard != null) {
            httpHeaders.set(GlobalConstants.OUTPUT_STANDARD, outpuStandard.toString());
        }
        String validCodes = new ThreadLocalUserSession().get().getValidCodes();
        if (validCodes != null && !validCodes.isEmpty()) {
            httpHeaders.set(GlobalConstants.VALID_CODES, String.valueOf(validCodes));
        }
        return new HttpEntity<>(httpHeaders);
    }

    /**
     * Allows you to apply a function to a value
     *
     * @param value           string representing the value to process
     * @param processFunction string representing the function to apply to the value
     * @return the processed value
     */
    public static String processFunction(String value, String processFunction) {
        String finalValue = value;
        if (processFunction.equals("BASE64")) {
            finalValue = Base64.getEncoder().encodeToString(value.getBytes());
        }
        return finalValue;
    }


    /**
     * Construct url with given params
     *
     * @param urlCall
     * @param params
     * @return
     */
    public static String constructUrlTemplate(String urlCall, Map<String, String> params) {

        String urlTemplate = urlCall;

        if (params != null && isValid(urlTemplate)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                urlTemplate = UriComponentsBuilder.fromHttpUrl(urlTemplate).queryParam(key, value).encode().toUriString();

            }
        }

        return urlTemplate;
    }


    /**
     * Returns true if url is valid
     *
     * @param url
     * @return
     */
    private static boolean isValid(String url) {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }

        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if Format function of a datamapper is valid
     *
     * @param format
     * @return
     */
    public static boolean isValidFunction(String format) {

        ArrayList<String> operators = new ArrayList<>(Arrays.asList("*", "X", "x", "-", "+", "/"));
        boolean isValid = false;
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

            switch (FormatFunctions.valueOf(function)) {
                case NUMERIC_OPERATOR:
                    parametersList = parameters.split(PARAMS_SEPARATOR);
                    int last = parametersList[1].length() - 1;
                    Float factor = (parametersList[1].charAt(0) == '\"' && parametersList[1].charAt(last) == '\"') ? Float.valueOf(parametersList[1].substring(1, last)) : Float.valueOf(parametersList[1]);
                    if (operators.contains(parametersList[0]) && factor != null) {
                        isValid = true;
                    }
                    break;

                case CONVERT_LIST_TO_STRING:
                case FORMAT_DATE:
                    isValid = true;
                    break;


                case CONVERT_STRING_TO_BOOLEAN:
                    parametersList = parameters.split(PARAMS_SEPARATOR);
                    isValid = true;
                    for (String couple : parametersList) {
                        String association = couple.trim();
                        String[] associationElements = association.split(KEY_VALUE_SEPARATOR);
                        if (associationElements.length != 2) {
                            isValid = false;
                        } else {
                            for (String element : associationElements) {
                                int lastChar = element.length() - 1;
                                String finalElement = (element.charAt(0) == '\"' && element.charAt(lastChar) == '\"') ? element.substring(1, lastChar) : element;
                                if (finalElement.contains("\"")) {
                                    isValid = false;
                                }
                            }
                        }
                    }
                    break;

            }

            return isValid;

        } catch (Exception e) {
            return false;
        }
    }


    public static boolean shouldPreserveResponseStatus(String outPutStandard) {
        StandardEnum standardEnum = StandardEnum.fromValue(outPutStandard);
        return (standardEnum != null && standardEnum != StandardEnum.GATEWAY);
    }


    public static HttpStatus getRawStatusResponseFromException(Exception exception) {

        HttpStatus httpStatus;
        if (exception instanceof HttpServerErrorException) {
            httpStatus = HttpStatus.valueOf(((HttpServerErrorException) exception).getRawStatusCode());
        } else if (exception instanceof InternalException) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (exception instanceof HttpClientErrorException) {
            httpStatus = HttpStatus.valueOf(((HttpClientErrorException) exception).getRawStatusCode());
        } else if (exception instanceof NotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
        } else if (exception instanceof BadRequestException) {
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if (exception instanceof BadGatewayException) {
            httpStatus = HttpStatus.BAD_GATEWAY;
        } else if (exception instanceof ConflictException) {
            httpStatus = HttpStatus.CONFLICT;
        } else if (exception instanceof UnauthorizedException) {
            httpStatus = HttpStatus.UNAUTHORIZED;
        } else if (exception instanceof UnavailableException) {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        } else {
            log.error("Uncatched exception generic case {}", exception.getClass());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return httpStatus;
    }

    public static String getRawResponseFromException(Exception exception) {
        String rawResponse = "";
        if (exception instanceof HttpServerErrorException) {
            rawResponse = ((HttpServerErrorException) exception).getResponseBodyAsString();
        } else if (exception instanceof InternalException) {
            rawResponse = ((InternalException) exception).getInternalError().getDescription();
        } else if (exception instanceof HttpClientErrorException) {
            rawResponse = ((HttpClientErrorException) exception).getResponseBodyAsString();
        } else {
            log.error("Uncatched exception generic case {}", exception.getClass());
            rawResponse = exception.getMessage();
        }
        return rawResponse;
    }


    public static boolean isGatewayErrorFormat(String rawResponseFromException) {
        return isJsonModel(rawResponseFromException, GenericError.class);
    }


    public static boolean isGatewayException(RuntimeException exception) {
        return (exception instanceof BadGatewayException || exception instanceof BadRequestException || exception instanceof BusinessException || exception instanceof ConflictException || exception instanceof GlobalExceptionHandler || exception instanceof InternalException
                || exception instanceof NotFoundException || exception instanceof UnauthorizedException || exception instanceof UnavailableException);
    }


    public static boolean isTompErrorFormat(String rawResponseFromException) {
        return isJsonModel(rawResponseFromException, TompError.class);
    }

    public static String getValidStatusTompError(String rawResponseFromException, HttpStatus rawStatusCodeFromException, GenericError bodyGenericError) {
        return rawResponseFromException.replace(String.valueOf(rawStatusCodeFromException.value()), bodyGenericError.getStatus().toString());
    }

    public static boolean isCarpoolErrorFormat(String rawResponseFromException) {
        return isJsonModel(rawResponseFromException, CarpoolError.class);
    }

    public static boolean isJsonObject(String rawResponseFromException) {
        return rawResponseFromException.startsWith("{") && rawResponseFromException.endsWith("}");
    }

    private static boolean isJsonModel(String rawBody, Class<?> parseClass) {
        boolean isJsonModel = true;
        try {
            objectMapper.readValue(rawBody, parseClass);
        } catch (JsonProcessingException e) {
            // in this case le String is not a gateway format
            isJsonModel = false;
        }
        return isJsonModel;
    }

}
