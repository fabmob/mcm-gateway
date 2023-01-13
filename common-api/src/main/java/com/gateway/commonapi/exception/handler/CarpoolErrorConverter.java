package com.gateway.commonapi.exception.handler;

import com.gateway.commonapi.dto.exceptions.CarpoolError;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.utils.CommonUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gateway.commonapi.constants.GatewayApiPathDict.CARPOOLING_PATH;
import static com.gateway.commonapi.constants.GatewayErrorMessage.*;

public class CarpoolErrorConverter {
    private CarpoolErrorConverter() {
    }

    public static ResponseEntity<Object> getResponseEntityForCarpoolStandard(HttpStatus httpStatus, GenericError bodyGenericError, String rawResponseFromException, HttpStatus rawStatusCodeFromException, RuntimeException exception, List<Integer> validCodes, WebRequest request) {
        CarpoolError carpoolError = new CarpoolError();
        ResponseEntity<Object> response;
        boolean preserveCode = validCodes.contains(rawStatusCodeFromException.value());
        HttpStatus responseStatus;
        String callId = bodyGenericError.getCallId().toString();


        //Case the error in exception is gateway format, we convert it into carpoolurage format
        if (CommonUtils.isGatewayErrorFormat(rawResponseFromException) || CommonUtils.isGatewayException(exception)) {
            if (preserveCode) {
                responseStatus = rawStatusCodeFromException;
            } else {
                responseStatus = HttpStatus.valueOf(500);
            }
            String carpoolErrorFromGenericError = bodyGenericError.getDescription() + " Technical gateway instance " + callId + ", errorcode " + convertErrorCodesToCarpoolErrorCodes(request, bodyGenericError.getErrorCode());
            carpoolError.setError(carpoolErrorFromGenericError);

            response = new ResponseEntity<>(carpoolError, new HttpHeaders(), responseStatus);

            //Errors from partner mapping
        } else {
            if (CommonUtils.isCarpoolErrorFormat(rawResponseFromException) && preserveCode) {
                if (httpStatus.is5xxServerError() && bodyGenericError.getLabel().equals(INTERNAL_SERVER_ERROR_LABEL)) {
                    carpoolError.setError(INTERNAL_MESSAGE_ERROR);
                    response = new ResponseEntity<>(carpoolError, new HttpHeaders(), rawStatusCodeFromException);
                } else {
                    response = new ResponseEntity<>(rawResponseFromException, new HttpHeaders(), rawStatusCodeFromException);
                }
            } else if (CommonUtils.isCarpoolErrorFormat(rawResponseFromException)) {
                response = new ResponseEntity<>(rawResponseFromException, new HttpHeaders(), httpStatus);
            } else {
                if (rawStatusCodeFromException.value() == 500) {
                    carpoolError.setError(INTERNAL_MESSAGE_ERROR_CARPOOLING);
                } else {
                    carpoolError.setError(rawResponseFromException);
                }
                if (preserveCode) {
                    responseStatus = rawStatusCodeFromException;
                } else {
                    responseStatus = httpStatus;
                }
                response = new ResponseEntity<>(carpoolError, new HttpHeaders(), responseStatus);
            }
        }
        return response;
    }

    /**
     * For specific URLs, change some errorCodes into specific carpool errorCodes. Otherwise, return the same errorCode
     *
     * @param request
     * @param errorCode
     * @return
     */
    protected static Integer convertErrorCodesToCarpoolErrorCodes(WebRequest request, Integer errorCode) {

        Integer convertedErrorCode = errorCode;

        if (request != null) {
            String url = request.toString();
            Map<String, List<Pair<Integer, Integer>>> codesByUrl = getErrorCodesToBeConverted();

            for (Map.Entry<String, List<Pair<Integer, Integer>>> entry : codesByUrl.entrySet()) {
                if (url.contains(entry.getKey())) {
                    //This URL has some codes that may be converted
                    for (Pair<Integer, Integer> possibleCodesToConvert : entry.getValue()) {
                        if (possibleCodesToConvert.getKey().equals(errorCode)) {
                            //This errorCode needs to be converted
                            return possibleCodesToConvert.getValue();
                        }
                    }
                }
            }
        }
        return convertedErrorCode;
    }

    /**
     * Some carpool endpoints use different error codes as other carpool endpoints, because they dont accept usual HTTP
     * status codes. It usually consists of replacing the first number in the error code, which act as a kind of
     * category for the errorCode
     */
    private static Map<String, List<Pair<Integer, Integer>>> getErrorCodesToBeConverted() {
        Map<String, List<Pair<Integer, Integer>>> codesByPath = new HashMap<>();

        // Carpooling status : does not handle 400 HTTP status, so 1xxx errorCodes need to be converted as 7xxx
        codesByPath.put(CARPOOLING_PATH + "/status", List.of(Pair.of(1535, 7535)));
        return codesByPath;
    }
}
