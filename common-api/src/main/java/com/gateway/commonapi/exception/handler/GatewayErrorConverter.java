package com.gateway.commonapi.exception.handler;

import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.utils.CommonUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GatewayErrorConverter {
    private GatewayErrorConverter() {
    }

    public static ResponseEntity<Object> getResponseEntityForGTW(HttpStatus httpStatus, GenericError bodyGenericError, String rawResponseFromException, HttpStatus rawStatusCodeFromException, String defaultDescription, RuntimeException exception) {
        ResponseEntity<Object> response;
        if (CommonUtils.isGatewayException(exception)) {
            response = new ResponseEntity<>(bodyGenericError, new HttpHeaders(), httpStatus);
        } else {
            if (CommonUtils.isGatewayErrorFormat(rawResponseFromException)) {
                response = new ResponseEntity<>(rawResponseFromException, new HttpHeaders(), rawStatusCodeFromException);
            } else {
                bodyGenericError.setDescription(defaultDescription + ": " + rawResponseFromException);
                response = new ResponseEntity<>(bodyGenericError, new HttpHeaders(), httpStatus);
            }

        }
        return response;
    }
}
