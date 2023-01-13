package com.gateway.commonapi.exception.handler;

import com.gateway.commonapi.constants.GatewayErrorMessage;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.TompError;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.text.MessageFormat;
import java.util.List;

import static com.gateway.commonapi.constants.GatewayErrorMessage.*;

@Slf4j
public class TompErrorConverter {
    private TompErrorConverter() {
    }

    public static ResponseEntity<Object> getResponseEntityForTompStandard(HttpStatus httpStatus, GenericError bodyGenericError, String rawResponseFromException, HttpStatus rawStatusCodeFromException, RuntimeException exception, List<Integer> validCodes) {
        TompError tompError = new TompError();
        ResponseEntity<Object> response;
        HttpStatus responseStatus;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        boolean preserveCode = validCodes.contains(rawStatusCodeFromException.value());

        if (CommonUtils.isTompErrorFormat(rawResponseFromException)) {
            //Typical case : a partner responded with a TOMP error, the gateway acts like a pass through
            log.debug("Response is already prepared as a TompError, using it");

            if (preserveCode) {
                response = new ResponseEntity<>(rawResponseFromException, headers, rawStatusCodeFromException);
            } else {
                response = new ResponseEntity<>(CommonUtils.getValidStatusTompError(rawResponseFromException, rawStatusCodeFromException, bodyGenericError), headers, httpStatus);
            }
        } else {
            log.debug("Building a TompError from exception");
            boolean exceptionInGatewayFormat = CommonUtils.isGatewayErrorFormat(rawResponseFromException) || CommonUtils.isGatewayException(exception);

            // Init TompError with default values and GenericError data
            tompError.setType(DEFAULT_TOMP_TYPE);
            tompError.setTitle((exceptionInGatewayFormat) ? bodyGenericError.getLabel() : rawResponseFromException);
            tompError.setDetail(bodyGenericError.getDescription());
            tompError.setTimestamp(bodyGenericError.getTimestamp());
            tompError.setInstance(DEFAULT_TOMP_INSTANCE + bodyGenericError.getCallId());

            // ErrorCode and status : init
            tompError.setErrorcode((exceptionInGatewayFormat && bodyGenericError.getErrorCode() != null) ? bodyGenericError.getErrorCode() : rawStatusCodeFromException.value());
            tompError.setStatus(rawStatusCodeFromException.value());
            responseStatus = rawStatusCodeFromException;

            // ErrorCode and status : if statusCode is not a valid Tomp one, overwrite it
            if (!preserveCode) {
                tompError.setErrorcode(500);
                tompError.setStatus(500);
                responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            // ErrorCode : specific values for status 401 et 500
            if (401 == tompError.getStatus()) {
                tompError.setErrorcode(7401);
            }

            if (403 == tompError.getStatus()) {
                tompError.setErrorcode(7403);

            }
            if (500 == tompError.getStatus()) {
                tompError.setErrorcode(7500);
                tompError.setTitle(GatewayErrorMessage.INTERNAL_ERROR_TITLE);
                tompError.setDetail(GatewayErrorMessage.INTERNAL_ERROR_DETAIL);
            }

            // Overwrite type
            if (400 == tompError.getStatus()) {
                tompError.setType(INVALID_TYPE);
            }

            // Overwrite title
            if (401 == tompError.getStatus()) {
                tompError.setTitle(UNAUTHORIZED_TITLE);
            }
            if (403 == tompError.getStatus()) {
                tompError.setTitle(FORBIDDEN_TITLE);
            }
            if (500 == tompError.getStatus()) {
                tompError.setTitle(MessageFormat.format(TITLE_FORMAT, tompError.getTitle()));
            }

            // Overwrite detail
            if (401 == tompError.getStatus()) {
                tompError.setDetail(DETAIL_MESSAGE_AUTHENTICATION);
            }
            if (403 == tompError.getStatus()) {
                tompError.setDetail(DETAIL_MESSAGE_FORBIDDEN);
            }
            if (500 == tompError.getStatus()) {
                tompError.setDetail(MessageFormat.format(DETAIL_FORMAT, tompError.getDetail()));
            }

            response = new ResponseEntity<>(tompError, headers, responseStatus);
        }
        return response;
    }


}
