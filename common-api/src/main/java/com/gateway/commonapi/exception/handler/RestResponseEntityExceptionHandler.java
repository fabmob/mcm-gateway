package com.gateway.commonapi.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.exceptions.BadGateway;
import com.gateway.commonapi.dto.exceptions.BadRequest;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gateway.commonapi.constants.ErrorCodeDict.INVALID_PARTNER_UUID_CODE;
import static com.gateway.commonapi.constants.GatewayApiPathDict.*;
import static com.gateway.commonapi.constants.GatewayErrorMessage.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    protected static final List<Integer> TOMP_CODES = new ArrayList<>(Arrays.asList(200, 400, 401, 403, 404, 500));
    protected static final List<Integer> CARPOOLING_CODES = new ArrayList<>(Arrays.asList(200, 201, 400, 401, 404, 409, 429, 500));

    // need to manage all those http status
    // 400  Bad request
    // 401 Unauthorized
    // 404 Not found
    // 409 Conflict
    // 500 Internal server error
    // 503 Service unavailable
    // 422
    // 502

    @Autowired
    ErrorMessages errorMessages;


    // ------- begin overrides from ResponseEntityExceptionHandler ------
    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GenericError errorBody = new GenericError();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalMediaTypeNotAcceptableCode()));
        errorBody.setLabel(this.errorMessages.getTechnicalMediaTypeNotAcceptableLabel());
        errorBody.setDescription(exception.getMessage() != null ? exception.getMessage() : this.errorMessages.getTechnicalMediaTypeNotAcceptableDescription());
        return getObjectResponseEntity(headers, status, request, errorBody);
    }

    @Override
    public ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GenericError errorBody = new GenericError();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalMediaTypeNotAcceptableCode()));
        errorBody.setDescription(isNotBlank(exception.getMessage()) ? exception.getMessage() : this.errorMessages.getTechnicalMediaTypeNotAcceptableDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalMediaTypeNotAcceptableLabel());
        return getObjectResponseEntity(headers, status, request, errorBody);
    }

    @Override
    public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GenericError errorBody = new GenericError();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalMissingPathVariableCode()));
        errorBody.setDescription(exception != null && isNotBlank(exception.getMessage()) ? exception.getMessage() : this.errorMessages.getTechnicalMissingPathVariableDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalMissingPathVariableLabel());

        return getObjectResponseEntity(headers, status, request, errorBody);
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BadRequest errorBody = new BadRequest();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalMissingServletRequestParamCode()));
        errorBody.setDescription(isNotBlank(exception.getMessage()) ? exception.getMessage() : this.errorMessages.getTechnicalMissingServletRequestParamDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalMissingServletRequestParamLabel());
        return getObjectResponseEntity(headers, status, request, errorBody);
    }

    @Override
    public ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BadRequest errorBody = new BadRequest();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalBindExceptionCode()));
        errorBody.setDescription(this.errorMessages.getTechnicalBindExceptionDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalBindExceptionLabel());
        return getObjectResponseEntity(headers, status, request, errorBody);
    }

    @Override
    public ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GenericError errorBody = new GenericError();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalConversionNotSupportedCode()));
        errorBody.setDescription(this.errorMessages.getTechnicalConversionNotSupportedDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalConversionNotSupportedLabel());
        return getObjectResponseEntity(headers, status, request, errorBody);
    }


    @Override
    public ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception,
                                                     HttpHeaders headers, HttpStatus status,
                                                     WebRequest request) {
        if (ExceptionUtils.getRootException(exception, BadRequestException.class) != null) {
            // Enum as request parameters cause mismatch errors, BadRequestException is thrown by enum converters
            return handleBadRequest((BadRequestException) ExceptionUtils.getRootException(exception, BadRequestException.class), request);
        }
        if (ExceptionUtils.getRootException(exception, IllegalArgumentException.class) != null && UUID.class == exception.getRequiredType()) {
            // for invalid uuid
            GenericError error = new GenericError();
            error.setErrorCode(INVALID_PARTNER_UUID_CODE);
            error.setLabel(INVALID_PARTNER_ID);
            error.setDescription(CommonUtils.placeholderFormat(INVALID_PARTNER_ID_MESSAGE, PLACEHOLDER, String.valueOf(exception.getValue())));

            return getExceptionResponseEntity(new BadRequestException(error), request, HttpStatus.BAD_REQUEST, Integer.valueOf(this.errorMessages.getTechnicalBadRequestCode()),
                    this.errorMessages.getTechnicalBadRequestLabel(), this.errorMessages.getTechnicalBadRequestDescription());

        } else {
            BadRequest errorBody = new BadRequest();
            errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalTypeMismatchExceptionCode()));
            if (exception.getCause() != null && StringUtils.isNotBlank(exception.getCause().getMessage())) {
                errorBody.setDescription(exception.getCause().getMessage());
            } else {
                errorBody.setDescription(this.errorMessages.getTechnicalTypeMismatchExceptionDescription());
            }
            errorBody.setLabel(this.errorMessages.getTechnicalTypeMismatchExceptionLabel());
            log.error(exception.getMessage(), exception);

            return getExceptionResponseEntity(new BadRequestException(errorBody), request, HttpStatus.BAD_REQUEST, Integer.valueOf(this.errorMessages.getTechnicalBadRequestCode()),
                    this.errorMessages.getTechnicalBadRequestLabel(), this.errorMessages.getTechnicalBadRequestDescription());

        }
    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException
                                                                       exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ExceptionUtils.getRootException(exception, BadRequestException.class) != null) {
            // Enum as body parameters cause notReadable errors, BadRequestException is thrown by enum converters
            return handleBadRequest((BadRequestException) ExceptionUtils.getRootException(exception, BadRequestException.class), request);
        }

        BadRequest errorBody = new BadRequest();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalMessageNotReadableCode()));
        if (exception.getCause() != null) {
            if (exception.getCause().getCause() != null && StringUtils.isNotBlank(exception.getCause().getCause().getMessage())) {
                errorBody.setDescription(exception.getCause().getCause().getMessage());
            } else if (StringUtils.isNotBlank(exception.getCause().getMessage())) {
                if (ExceptionUtils.getRootException(exception, InvalidFormatException.class) != null) {
                    errorBody.setDescription(INVALID_FORMAT_UUID);
                } else {
                    errorBody.setDescription(exception.getCause().getMessage());
                }
            }
        } else {
            errorBody.setDescription(this.errorMessages.getTechnicalMessageNotReadableDescription());
        }
        errorBody.setLabel(this.errorMessages.getTechnicalMessageNotReadableLabel());
        log.error(exception.getMessage(), exception);
        CallUtils.saveOutputStandardInCallThread(request);
        return getExceptionResponseEntity(new BadRequestException(errorBody), request, HttpStatus.BAD_REQUEST, Integer.valueOf(this.errorMessages.getTechnicalBadRequestCode()),
                this.errorMessages.getTechnicalBadRequestLabel(), this.errorMessages.getTechnicalBadRequestDescription());

    }

    @Override
    public ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException
                                                                       exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        GenericError errorBody = new GenericError();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalMessageNotWritableCode()));
        errorBody.setDescription(this.errorMessages.getTechnicalMessageNotWritableDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalMessageNotWritableLabel());
        return getObjectResponseEntity(headers, status, request, errorBody);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException
                                                                       exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BadRequest errorBody = new BadRequest();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalMethodNotAllowedCode()));
        errorBody.setDescription(this.errorMessages.getTechnicalMethodNotAllowedDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalMethodNotAllowedLabel());

        return getObjectResponseEntity(headers, status, request, errorBody);
    }

    @Override
    public ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException
                                                                          exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BadRequest errorBody = new BadRequest();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalMissingServletRequestParamCode()));
        errorBody.setDescription(this.errorMessages.getTechnicalMissingServletRequestParamDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalMissingServletRequestParamLabel());
        return getObjectResponseEntity(headers, status, request, errorBody);
    }

    @Override
    public ResponseEntity<Object> handleBindException(BindException exception, HttpHeaders headers, HttpStatus
            status, WebRequest request) {
        BadRequest errorBody = new BadRequest();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalBindExceptionCode()));
        errorBody.setDescription(this.errorMessages.getTechnicalBindExceptionDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalBindExceptionLabel());
        return getObjectResponseEntity(headers, status, request, errorBody);
    }

    @Override
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders
            headers, HttpStatus status, WebRequest request) {
        GenericError errorBody = new GenericError();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalNoHandlerFoundCode()));
        errorBody.setDescription(this.errorMessages.getTechnicalNoHandlerFoundDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalNoHandlerFoundLabel());
        return getObjectResponseEntity(headers, status, request, errorBody);
    }

    @Override
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException
                                                                              exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BadRequest errorBody = new BadRequest();
        errorBody.setErrorCode(Integer.valueOf(this.errorMessages.getTechnicalHttpRequestMethodNotSupportedCode()));
        errorBody.setDescription(this.errorMessages.getTechnicalHttpRequestMethodNotSupportedDescription());
        errorBody.setLabel(this.errorMessages.getTechnicalHttpRequestMethodNotSupportedLabel());
        return getObjectResponseEntity(headers, status, request, errorBody);
    }
    // ------- end of overrides -----

    private ResponseEntity<Object> getObjectResponseEntity(HttpHeaders headers, HttpStatus status, WebRequest
            request, @NotNull GenericError errorBody) {
        GenericError errorObject = new GenericError(errorBody.getStatus() != null ? HttpStatus.valueOf(errorBody.getStatus()) : HttpStatus.INTERNAL_SERVER_ERROR, errorBody.getErrorCode(), errorBody.getLabel(), errorBody.getDescription());
        if (org.apache.commons.lang3.StringUtils.isBlank(errorBody.getDescription())) {
            errorObject.setDescription(status.getReasonPhrase());
        }
        return handleExceptionInternal(new InternalException(errorObject), errorObject, headers, status, request);
    }

    @ExceptionHandler(value = {InternalException.class, Exception.class})
    public ResponseEntity<Object> handleInternalException(RuntimeException exception, WebRequest request) {
        log.debug("Internal error raised : {}", exception.getMessage());
        return getExceptionResponseEntity(exception, request, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception.getMessage());
    }

    @ExceptionHandler(value = {ConnectException.class})
    public ResponseEntity<Object> handleConnectException(RuntimeException exception, WebRequest request) {
        log.debug("Business error raised : {}", exception.getMessage());
        return getExceptionResponseEntity(exception, request, HttpStatus.BAD_GATEWAY, HttpStatus.BAD_GATEWAY.value(), HttpStatus.BAD_GATEWAY.getReasonPhrase(), exception.getMessage());
    }

    // begin custom exceptions handling
    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<Object> handleBusinessException(BusinessException exception, WebRequest request) {
        log.debug("Business error raised : {}", exception.getMessage());
        return getExceptionResponseEntity(exception, request, HttpStatus.UNPROCESSABLE_ENTITY, exception.getBusinessError().getErrorCode(), HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(), exception.getMessage());
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequest(BadRequestException exception, WebRequest request) {
        log.debug("Error handleBadRequest raised : {}", exception.getMessage());
        return getExceptionResponseEntity(exception, request, HttpStatus.BAD_REQUEST, Integer.valueOf(this.errorMessages.getTechnicalBadRequestCode()),
                this.errorMessages.getTechnicalBadRequestLabel(), this.errorMessages.getTechnicalBadRequestDescription());
    }

    @ExceptionHandler(value = {BadGatewayException.class})
    public ResponseEntity<Object> handleBadGateway(BadGatewayException exception, WebRequest request) {
        BadGateway badGatewayError = exception.getBadGatewayError();
        log.debug("Error handleBadGateway raised : {}", badGatewayError);
        injectCallIdFromCorrelationId(request, badGatewayError);

        return getExceptionResponseEntity(exception, request, HttpStatus.BAD_GATEWAY, Integer.valueOf(this.errorMessages.getTechnicalBadGatewayCode()),
                this.errorMessages.getTechnicalBadGatewayLabel(), this.errorMessages.getTechnicalBadGatewayDescription());
    }

    @ExceptionHandler(value = {UnauthorizedException.class, HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<Object> handleUnauthorized(RuntimeException exception, WebRequest request) {
        log.debug("Error handleUnauthorized raised : {}", exception.getMessage());
        return getExceptionResponseEntity(exception, request, HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), exception.getMessage());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFound(RuntimeException exception, WebRequest request) {
        log.info("Error handleNotFound raised : {}", exception.getMessage(), exception);
        return getExceptionResponseEntity(exception, request, HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), exception.getMessage());
    }

    @ExceptionHandler(value = {ConflictException.class})
    public ResponseEntity<Object> handleConflict(RuntimeException exception, WebRequest request) {
        log.debug("Error handleConflict raised : {}", exception.getMessage());
        return getExceptionResponseEntity(exception, request, HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), exception.getMessage());
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception exception, @Nullable Object body, HttpHeaders
            headers, HttpStatus status, WebRequest request) {
        if (exception instanceof InternalException) {
            GenericError genericError = ((InternalException) exception).getInternalError();
            return getExceptionResponseEntity(((InternalException) exception), request, status, genericError.getStatus(), genericError.getLabel(), genericError.getDescription());
        }
        return handleInternal((RuntimeException) exception, status, request);
    }

    @ExceptionHandler(value = {IllegalStateException.class, ClassCastException.class})
    public ResponseEntity<Object> handleInternal(RuntimeException exception, HttpStatus originalStatus, WebRequest
            request) {
        log.debug("Error handleInternal raised : {}", exception.getMessage());
        return getExceptionResponseEntity(exception, request, originalStatus, 500, originalStatus.getReasonPhrase(), exception.getMessage());
    }

    @ExceptionHandler(value = {UnavailableException.class})
    public ResponseEntity<Object> handleServiceUnavailable(RuntimeException exception, WebRequest request) {
        log.debug("Error handleServiceUnavailable raised : {}", exception.getMessage());
        return getExceptionResponseEntity(exception, request, HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), exception.getMessage());
    }

    // end of custom exceptions handling

    /**
     * Convert an exception to a responseEntity
     *
     * @param exception          raised exception
     * @param request            web request
     * @param httpStatus         http status
     * @param defaultErrorCode   default error code if not present in the exception message description
     * @param defaultLabel       default label if not present in the exception message description
     * @param defaultDescription default description if not present in the exception message description
     * @return The responseEntity with Error dto from caught exception
     */
    private ResponseEntity<Object> getExceptionResponseEntity(RuntimeException exception, WebRequest
            request, HttpStatus httpStatus, Integer defaultErrorCode, String defaultLabel, String defaultDescription) {

        // prepare the response error DTO with labels and message in exception raised
        GenericError bodyGenericError = new GenericError(httpStatus, defaultErrorCode, defaultLabel, defaultDescription);

        if ((!request.toString().contains(CARPOOLING_PATH) && !request.toString().contains(GET_PARTNERS_PATH)) || request.toString().contains(CACHE_PATH)) {
            //set GTW standard for data-api endpoints
            CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
        } else {
            CallUtils.saveOutputStandardInCallThread(request);
        }

        String outputStandard = request.getHeader(GlobalConstants.OUTPUT_STANDARD) != null ? request.getHeader(GlobalConstants.OUTPUT_STANDARD) : CallUtils.getOutputStandardFromCallThread();
        StandardEnum standardEnum = StandardEnum.fromValue(outputStandard);

        String validCodesString = request.getHeader(GlobalConstants.VALID_CODES) != null ? request.getHeader(GlobalConstants.VALID_CODES) : CallUtils.getValidCodesFromCallThread();
        List<Integer> validCodes = null;
        if (isNotBlank(validCodesString)) {
            String validCodesStr = validCodesString.replace("[", "").replace("]", "");
            validCodes = new ArrayList<>();
            for (String code : validCodesStr.split(", ")) {
                validCodes.add(Integer.parseInt(code));
            }
        } else if (standardEnum.equals(StandardEnum.COVOITURAGE_STANDARD)) {
            validCodes = CARPOOLING_CODES;
        } else if (standardEnum.equals(StandardEnum.TOMP_1_3_0)) {
            validCodes = TOMP_CODES;
        }


        mapJsonExceptionMessageWithErrorDto(exception, bodyGenericError);

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(httpStatus)) {
            request.setAttribute("javax.servlet.error.exception", exception, 0);
        }
        injectCallIdFromCorrelationId(request, bodyGenericError);

        ResponseEntity<Object> response;

        String rawResponseFromException = CommonUtils.getRawResponseFromException(exception);
        HttpStatus rawStatusCodeFromException = CommonUtils.getRawStatusResponseFromException(exception);

        switch (standardEnum) {
            case TOMP_1_3_0:
                response = TompErrorConverter.getResponseEntityForTompStandard(httpStatus, bodyGenericError, rawResponseFromException, rawStatusCodeFromException, exception, validCodes);
                break;
            case COVOITURAGE_STANDARD:
                response = CarpoolErrorConverter.getResponseEntityForCarpoolStandard(httpStatus, bodyGenericError, rawResponseFromException, rawStatusCodeFromException, exception, validCodes, request);
                break;
            case GBFS:
            case OTHER:
            default:
                response = GatewayErrorConverter.getResponseEntityForGTW(httpStatus, bodyGenericError, rawResponseFromException, rawStatusCodeFromException, defaultDescription, exception);
                break;
        }
        return response;
    }


    private void injectCallIdFromCorrelationId(WebRequest request, GenericError bodyGenericError) {
        String correlationIdFromContext;
        // if existing a CORRELATION_ID header use the value in it to extract the callId and put it in the error DTO response
        if (null != request.getHeader(GlobalConstants.CORRELATION_ID_HEADER) && CommonUtils.isUUID(request.getHeader(GlobalConstants.CORRELATION_ID_HEADER))) {
            correlationIdFromContext = request.getHeader(GlobalConstants.CORRELATION_ID_HEADER);
        } else {
            correlationIdFromContext = new ThreadLocalUserSession().get().getContextId();
        }
        bodyGenericError.setCallId(UUID.fromString(correlationIdFromContext));
    }

    /**
     * Fill the genericError object in param with the error data from exception message
     *
     * @param exception    Exception to convert into GenericError in json format
     * @param genericError Error DTO to enrich with data in exception
     */
    private void mapJsonExceptionMessageWithErrorDto(RuntimeException exception, GenericError genericError) {
        ObjectMapper objectMapper = new ObjectMapper();
        // override error with the json contained in the exception message
        GenericError errorFromMessage = null;
        try {
            // load the GenericError from stringyfied error in the exception message
            errorFromMessage = objectMapper.readValue(isNotBlank(exception.getMessage()) ? exception.getMessage() : "", GenericError.class);
        } catch (JsonProcessingException jsonException) {
            // in case this is not a json in the message but un simple string, we set message with it with new Generic error object
            errorFromMessage = new GenericError();
            errorFromMessage.setDescription(exception.getMessage());
        }
        if (errorFromMessage.getStatus() != null) {
            genericError.setStatus(errorFromMessage.getStatus());
        }

        // if attributes are not empty in the message coming from exception, we use it to erase the default from properties
        if (StringUtils.isNotBlank(errorFromMessage.getDescription())) {
            genericError.setDescription(errorFromMessage.getDescription());
        }
        if (errorFromMessage.getErrorCode() != null) {
            genericError.setErrorCode(errorFromMessage.getErrorCode());
        }
        if (!StringUtils.isEmpty(errorFromMessage.getLabel())) {
            genericError.setLabel(errorFromMessage.getLabel());
        }
        if (errorFromMessage.getCallId() != null) {
            genericError.setCallId(errorFromMessage.getCallId());
        }
        // if empty create date of error
        if (!StringUtils.isEmpty(errorFromMessage.getTimestamp())) {
            genericError.setTimestamp(new SimpleDateFormat(GlobalConstants.GATEWAY_DATE_FORMAT).format(new Date()));
        } else {
            genericError.setTimestamp(errorFromMessage.getTimestamp());
        }

    }

}