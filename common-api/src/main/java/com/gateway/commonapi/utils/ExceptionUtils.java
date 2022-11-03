package com.gateway.commonapi.utils;

import com.gateway.commonapi.constants.ControllerMessageDict;
import com.gateway.commonapi.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.text.MessageFormat;

@Slf4j
public class ExceptionUtils {

    private ExceptionUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Convert HttpClientErrorException to a gateway runtime exception that will be caught by ControllerAdvice @{@link com.gateway.commonapi.exception.handler.RestResponseEntityExceptionHandler}
     *
     * @param httpClientErrorException Client exception thrown executing a request to a web service
     * @param message                  Description to override, can be empty or null to keep original message from exception
     * @return the http runtime exception
     */
    public static RuntimeException getMappedGatewayRuntimeException(HttpClientErrorException httpClientErrorException, String message) {

        String exceptionMessage = StringUtils.isEmpty(message) ? httpClientErrorException.getMessage() : message;
        switch (httpClientErrorException.getStatusCode()) {
            case BAD_REQUEST:
                return new BadRequestException(exceptionMessage);
            case UNAUTHORIZED:
                return new UnauthorizedException(exceptionMessage);
            case NOT_FOUND:
                return new NotFoundException(exceptionMessage);
            case CONFLICT:
                return new ConflictException(exceptionMessage);
            case FORBIDDEN:
            case METHOD_NOT_ALLOWED:
            case NOT_ACCEPTABLE:
            case GONE:
            case UNSUPPORTED_MEDIA_TYPE:
            case TOO_MANY_REQUESTS:
            case UNPROCESSABLE_ENTITY:
            default:
                return new InternalException(exceptionMessage);
        }
    }

    public static String getBadEnumValueMessage(Class<? extends Enum<?>> enumClass, String badValue) {
        return getBadEnumValueMessage(enumClass, ControllerMessageDict.BAD_ENUM_VALUE, badValue);
    }

    public static String getBadEnumValueMessage(Class<? extends Enum<?>> enumClass, String template, String badValue) {
        return MessageFormat.format(template, badValue, enumClass.getSimpleName(), StringUtils.join(enumClass.getEnumConstants(), ", "));
    }

    /**
     * If throwableClassSearched is provided, returns e first Throwable of exceptionClassSearched Class in stackTrace or null if not found.
     * If throwableClassSearched is not provided, return the last Throwable in stackTrace
     */
    public static Throwable getRootException(Throwable source, Class throwableClassSearched) {
        if (source != null) {
            if (throwableClassSearched != null && throwableClassSearched.isInstance(source)) {
                return source;
            } else if (source.getCause() != null) {
                return getRootException(source.getCause(), throwableClassSearched);
            } else if (throwableClassSearched != null) {
                return null;
            }
        }
        return source;
    }


}
