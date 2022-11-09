import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.gateway.commonapi.cache.CacheUtil;
import com.gateway.commonapi.dto.exceptions.BusinessError;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.exception.handler.RestResponseEntityExceptionHandler;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.UTTestCase;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import java.beans.PropertyChangeEvent;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:errors.properties")
public class ExceptionHandlerTest extends UTTestCase {

    public static final String DEFAULT_EXCEPTION_MESSAGE = "ExceptionMessage";

    @TestConfiguration
    public static class ErrorMessagesConfiguration {
        @Bean
        public ErrorMessages errorMessages() {
            return new ErrorMessages();
        }

        @Bean
        public CacheUtil<String, String> cacheUtil() {
            return cacheUtil;
        }

        @Bean
        public RestResponseEntityExceptionHandler responseEntityExceptionHandler() {
            new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);
            return new RestResponseEntityExceptionHandler();
        }
    }

    @Autowired
    ErrorMessages errorMessages3;

    @Autowired
    public static CacheUtil<String, String> cacheUtil;

    @Autowired
    RestResponseEntityExceptionHandler responseEntityExceptionHandler;

    //RestResponseEntityExceptionHandler responseEntityExceptionHandler = new RestResponseEntityExceptionHandler();
    ServletException ex = new ServletException();
    MockHttpServletRequest servletRequest = new MockHttpServletRequest();
    ServletWebRequest servletWebRequest = new ServletWebRequest(servletRequest);
    HttpHeaders headers = HttpHeaders.EMPTY;
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    ResponseEntity<Object> response;

    @Test
    public void testHandleHttpMediaTypeNotAcceptable() {
        response = responseEntityExceptionHandler.handleHttpMediaTypeNotAcceptable(new HttpMediaTypeNotAcceptableException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), DEFAULT_EXCEPTION_MESSAGE, "MediaType non acceptable", 415);
    }

    @Test
    public void testHandleHttpMediaTypeNotSupported() {
        response = responseEntityExceptionHandler.handleHttpMediaTypeNotSupported(new HttpMediaTypeNotSupportedException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), DEFAULT_EXCEPTION_MESSAGE, "MediaType non acceptable", 415);
    }

    @Test
    public void testHandleBadRequest() {
        response = responseEntityExceptionHandler.handleBadRequest(new BadRequestException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), DEFAULT_EXCEPTION_MESSAGE, "Bad Request", 400);
    }

    @Test
    public void testHandleBadGateway() {
        response = responseEntityExceptionHandler.handleBadGateway(new BadGatewayException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_GATEWAY.value(), DEFAULT_EXCEPTION_MESSAGE, "Communication error", 502);

    }

    @Test
    public void testhandleBindException() {
        response = responseEntityExceptionHandler.handleBindException
                (new BindException(new BeanPropertyBindingResult(this, "objectName")), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), "The format of the request is not the one expected", "", 400);
    }

    @Test
    public void testHandleBusinessException() {
        response = responseEntityExceptionHandler.handleBusinessException
                (new BusinessException(new BusinessError(42, "functional label", "functional descriptions")), servletWebRequest);
        checkErrorResponse(response, HttpStatus.UNPROCESSABLE_ENTITY.value(), "functional descriptions", "functional label", 42);
    }

    @Test
    public void testHandleConflict() {
        response = responseEntityExceptionHandler.handleConflict
                (new ConflictException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.CONFLICT.value(), DEFAULT_EXCEPTION_MESSAGE, "Conflict", 409);
    }

    @Test
    public void testHandleConnectException() {
        response = responseEntityExceptionHandler.handleConnectException
                (new RuntimeException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_GATEWAY.value(), DEFAULT_EXCEPTION_MESSAGE + ": " + DEFAULT_EXCEPTION_MESSAGE, "Bad Gateway", 502);
    }

    @Test
    public void testHandleNoHandlerFoundException() {
        response = responseEntityExceptionHandler.handleNoHandlerFoundException
                (new NoHandlerFoundException(DEFAULT_EXCEPTION_MESSAGE, "", headers), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected exception catcher", "Internal error", 500);
    }

    @Test
    public void testHandleNotFound() {
        response = responseEntityExceptionHandler.handleNotFound
                (new NotFoundException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.NOT_FOUND.value(), DEFAULT_EXCEPTION_MESSAGE, "Not Found", 404);
    }

    @Test
    public void testHandleConversionNotSupported() {
        response = responseEntityExceptionHandler.handleConversionNotSupported
                (new ConversionNotSupportedException(new PropertyChangeEvent("source", "propertyName",
                        "oldValue", "newValue"), null, null), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Conversion error", "Conversion not supported", 500);
    }

    @Test
    public void testHandleExceptionInternal() {
        response = responseEntityExceptionHandler.handleExceptionInternal
                (new NullPointerException(DEFAULT_EXCEPTION_MESSAGE), null, headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), DEFAULT_EXCEPTION_MESSAGE + ": " + DEFAULT_EXCEPTION_MESSAGE, "Internal Server Error", 500);
    }

    @Test
    public void testHandleHttpMessageNotReadable() {
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException(DEFAULT_EXCEPTION_MESSAGE);
        exception.initCause(new InvalidFormatException("The request format is not the expected one", "value", UUID.class));
        response = responseEntityExceptionHandler.handleHttpMessageNotReadable
                (exception, headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), "The request format is not the expected one", "Bad Request: Invalid Format", 400);
    }

    @Test
    public void testHandleHttpMessageNotWritable() {
        response = responseEntityExceptionHandler.handleHttpMessageNotWritable
                (new HttpMessageNotWritableException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "", 500);
    }

    @Test
    public void testHandleHttpRequestMethodNotSupported() {
        response = responseEntityExceptionHandler.handleHttpRequestMethodNotSupported
                (new HttpRequestMethodNotSupportedException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), "Unable to process the requested operation", "Method not supported", 400);
    }

    @Test
    public void testHandleInternal() {
        response = responseEntityExceptionHandler.handleInternal
                (new ClassCastException(DEFAULT_EXCEPTION_MESSAGE), status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), DEFAULT_EXCEPTION_MESSAGE + ": " + DEFAULT_EXCEPTION_MESSAGE, "Internal Server Error", 500);
    }

    @Test
    public void testHandleInternalException() {
        response = responseEntityExceptionHandler.handleInternalException
                (new InternalException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), DEFAULT_EXCEPTION_MESSAGE, "Internal Server Error", 500);
    }


    @Test
    public void testHandleMissingServletRequestParameter() {
        response = responseEntityExceptionHandler.handleMissingServletRequestParameter
                (new MissingServletRequestParameterException(DEFAULT_EXCEPTION_MESSAGE, "String"), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), "Required request parameter 'ExceptionMessage' for method parameter type String is not present", "", 400);
    }

    @Test
    public void testHandleMissingServletRequestPart() {
        response = responseEntityExceptionHandler.handleMissingServletRequestPart
                (new MissingServletRequestPartException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), "Internal Server Error", "", 400);
    }

    @Test
    public void testHandleServiceUnavailable() {
        response = responseEntityExceptionHandler.handleServiceUnavailable
                (new UnavailableException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.SERVICE_UNAVAILABLE.value(), DEFAULT_EXCEPTION_MESSAGE, "Service Unavailable", 503);
    }

    @Test
    public void testHandleServletRequestBindingException() {
        response = responseEntityExceptionHandler.handleServletRequestBindingException
                (new ServletRequestBindingException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), "The format of the request is not the one expected", "", 400);
    }

    @Test
    public void testHandleTypeMismatch() {
        TypeMismatchException exception = new TypeMismatchException("7962e1ef-4d4c-4300-9fe4", UUID.class, new IllegalArgumentException("Invalid UUID string: 7962e1ef-4d4c-4300-9fe4"));
        response = responseEntityExceptionHandler.handleTypeMismatch
                (exception, headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), "Invalid partner id 7962e1ef-4d4c-4300-9fe4. Unauthorized to reach this partner.", "Invalid partner id", 1543);
    }

    @Test
    public void testHandleUnauthorized() {
        response = responseEntityExceptionHandler.handleUnauthorized
                (new UnauthorizedException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), DEFAULT_EXCEPTION_MESSAGE, "Unauthorized", 401);
    }

    @SneakyThrows
    @Test
    public void testHandleMissingPathVariable() {
        response = responseEntityExceptionHandler.handleMissingPathVariable
                (new MissingPathVariableException("variableName", new MethodParameter(this.getClass().getMethod("testHandleMissingPathVariable"), -1)), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Required URI template variable 'variableName' for method parameter type void is not present", "MissingPathVariableException", 500);
    }

    @SneakyThrows
    @Test
    public void testHandleMethodArgumentNotValid() {
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        response = responseEntityExceptionHandler.handleMethodArgumentNotValid
                (new MethodArgumentNotValidException(new MethodParameter(this.getClass().getMethod("testHandleMissingPathVariable"), -1), bindingResult),
                        headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), "The invoked method is not allowed", "Unauthorized method", 405);
    }

    private void checkErrorResponse(ResponseEntity<Object> response, Integer expectedStatusCode, String expectedDescription, String expectedLabel, Integer expectedErrorCode) {
        Assert.assertEquals(expectedStatusCode, Optional.ofNullable(((GenericError) response.getBody()).getStatus()).get());
        Assert.assertEquals(expectedLabel, Optional.ofNullable(((GenericError) response.getBody()).getLabel()).get());
        Assert.assertEquals(expectedDescription, ((GenericError) response.getBody()).getDescription());
        Assert.assertEquals(expectedErrorCode, Optional.ofNullable(((GenericError) response.getBody()).getErrorCode()).get());
    }
}
