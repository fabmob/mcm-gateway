import com.gateway.commonapi.cache.CacheUtil;
import com.gateway.commonapi.dto.exceptions.BusinessError;
import com.gateway.commonapi.dto.exceptions.TompError;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.exception.handler.RestResponseEntityExceptionHandler;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.BeforeClass;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.gateway.commonapi.constants.GatewayErrorMessage.INTERNAL_ERROR_TITLE;

@Slf4j
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:errors.properties")
public class ExceptionHandlerTompStandardTest {
    public static final String DEFAULT_EXCEPTION_MESSAGE = "ExceptionMessage";
    public static final String GATEWAY_ERROR = "Gateway error : ";
    public static final String GATEWAY_ERROR_HAPPEN = "Gateway error happen : ";
    public static final String CONTACT_GATEWAY_SUPPORT = ", contact your Gateway support.";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";

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
            new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
            ArrayList<Integer> TOMP_CODES = new ArrayList<>(Arrays.asList(200, 400, 401, 403, 404, 500));
            CallUtils.saveValidCodesInCallThread(TOMP_CODES);
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
    static MockHttpServletRequest servletRequest = new MockHttpServletRequest();
    ServletWebRequest servletWebRequest = new ServletWebRequest(servletRequest);
    HttpHeaders headers = HttpHeaders.EMPTY;
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
    ResponseEntity<Object> response;

    @BeforeClass
    public static void setUp() {
        servletRequest.setRequestURI("v1/partners/");
    }

    @Test
    public void testHandleHttpMediaTypeNotAcceptable() {
        response = responseEntityExceptionHandler.handleHttpMediaTypeNotAcceptable(new HttpMediaTypeNotAcceptableException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 7500);
    }

    @Test
    public void testHandleHttpMediaTypeNotSupported() {
        response = responseEntityExceptionHandler.handleHttpMediaTypeNotSupported(new HttpMediaTypeNotSupportedException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleBadRequest() {
        response = responseEntityExceptionHandler.handleBadRequest(new BadRequestException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 400);
    }

    @Test
    public void testHandleBadGateway() {
        response = responseEntityExceptionHandler.handleBadGateway(new BadGatewayException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 7500);

    }

    @Test
    public void testhandleBindException() {
        response = responseEntityExceptionHandler.handleBindException
                (new BindException(new BeanPropertyBindingResult(this, "objectName")), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleBusinessException() {
        response = responseEntityExceptionHandler.handleBusinessException
                (new BusinessException(new BusinessError(42, "functional label", "functional descriptions")), servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleConflict() {
        response = responseEntityExceptionHandler.handleConflict
                (new ConflictException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 7500);
    }

    @Test
    public void testHandleConnectException() {
        response = responseEntityExceptionHandler.handleConnectException
                (new RuntimeException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleNoHandlerFoundException() {
        response = responseEntityExceptionHandler.handleNoHandlerFoundException
                (new NoHandlerFoundException(DEFAULT_EXCEPTION_MESSAGE, "", headers), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleNotFound() {
        response = responseEntityExceptionHandler.handleNotFound
                (new NotFoundException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.NOT_FOUND.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 404);
    }

    @Test
    public void testHandleConversionNotSupported() {
        response = responseEntityExceptionHandler.handleConversionNotSupported
                (new ConversionNotSupportedException(new PropertyChangeEvent("source", "propertyName",
                        "oldValue", "newValue"), null, null), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleExceptionInternal() {
        response = responseEntityExceptionHandler.handleExceptionInternal
                (new NullPointerException(DEFAULT_EXCEPTION_MESSAGE), null, headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleHttpMessageNotReadable() {
        MockHttpServletRequest servlet = new MockHttpServletRequest();
        servlet.setRequestURI("v1/partners");
        ServletWebRequest request = new ServletWebRequest(servlet);

        response = responseEntityExceptionHandler.handleHttpMessageNotReadable
                (new HttpMessageNotReadableException(DEFAULT_EXCEPTION_MESSAGE), headers, status, request);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 400);
    }

    @Test
    public void testHandleHttpMessageNotWritable() {
        response = responseEntityExceptionHandler.handleHttpMessageNotWritable
                (new HttpMessageNotWritableException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleHttpRequestMethodNotSupported() {
        response = responseEntityExceptionHandler.handleHttpRequestMethodNotSupported
                (new HttpRequestMethodNotSupportedException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleInternal() {
        response = responseEntityExceptionHandler.handleInternal
                (new ClassCastException(DEFAULT_EXCEPTION_MESSAGE), status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleInternalException() {
        response = responseEntityExceptionHandler.handleInternalException
                (new InternalException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }


    @Test
    public void testHandleMissingServletRequestParameter() {
        response = responseEntityExceptionHandler.handleMissingServletRequestParameter
                (new MissingServletRequestParameterException(DEFAULT_EXCEPTION_MESSAGE, "String"), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 400);
    }

    @Test
    public void testHandleMissingServletRequestPart() {
        response = responseEntityExceptionHandler.handleMissingServletRequestPart
                (new MissingServletRequestPartException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleServiceUnavailable() {
        response = responseEntityExceptionHandler.handleServiceUnavailable
                (new UnavailableException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 7500);
    }

    @Test
    public void testHandleServletRequestBindingException() {
        response = responseEntityExceptionHandler.handleServletRequestBindingException
                (new ServletRequestBindingException(DEFAULT_EXCEPTION_MESSAGE), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @Test
    public void testHandleTypeMismatch() {
        MockHttpServletRequest servlet = new MockHttpServletRequest();
        servlet.setRequestURI("v1/partners");
        ServletWebRequest request = new ServletWebRequest(servlet);

        TypeMismatchException exception = new TypeMismatchException(DEFAULT_EXCEPTION_MESSAGE, String.class);

        response = responseEntityExceptionHandler.handleTypeMismatch
                (exception, headers, status, request);
        checkErrorResponse(response, HttpStatus.BAD_REQUEST.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 400);
    }

    @Test
    public void testHandleTypeMismatchWhenTompIsThrows() {
        MockHttpServletRequest servlet = new MockHttpServletRequest();
        servlet.setRequestURI("v1/partners");
        ServletWebRequest request = new ServletWebRequest(servlet);

        TompError tompError = new TompError();
        tompError.setType("Invalid");

        Throwable throwable = new BadRequestException(tompError);
        Throwable throwable2 = new Throwable("message", throwable);

        TypeMismatchException exception2 = new TypeMismatchException(DEFAULT_EXCEPTION_MESSAGE, String.class, throwable2);
        response = responseEntityExceptionHandler.handleTypeMismatch(exception2, headers, status, request);
        Assert.assertEquals(400, response.getStatusCode().value());

        // coverage JsonProcessingException e
        Throwable throwableBadRequest = new BadRequestException("hey");
        Throwable throwableBadRequest2 = new Throwable("message", throwableBadRequest);
        TypeMismatchException exception = new TypeMismatchException(DEFAULT_EXCEPTION_MESSAGE, String.class, throwableBadRequest2);
        response = responseEntityExceptionHandler.handleTypeMismatch(exception, headers, status, request);
        Assert.assertEquals(400, response.getStatusCode().value());
    }

    @Test
    public void testHandleUnauthorized() {
        response = responseEntityExceptionHandler.handleUnauthorized
                (new UnauthorizedException(DEFAULT_EXCEPTION_MESSAGE), servletWebRequest);
        checkErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "The request requires user authentication.", "Unauthorized", 401);
    }

    @SneakyThrows
    @Test
    public void testHandleMissingPathVariable() {
        response = responseEntityExceptionHandler.handleMissingPathVariable(new MissingPathVariableException("variableName", new MethodParameter(this.getClass().getMethod("testHandleMissingPathVariable"), -1)), headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    @SneakyThrows
    @Test
    public void testHandleMethodArgumentNotValid() {
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "objectName");
        response = responseEntityExceptionHandler.handleMethodArgumentNotValid
                (new MethodArgumentNotValidException(new MethodParameter(this.getClass().getMethod("testHandleMissingPathVariable"), -1), bindingResult),
                        headers, status, servletWebRequest);
        checkErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR, INTERNAL_ERROR_TITLE, 500);
    }

    private void checkErrorResponse(ResponseEntity<Object> response, Integer expectedStatusCode, String expectedDescription, String expectedLabel, Integer expectedErrorCode) {
        if (500 == expectedStatusCode) {
            expectedLabel = GATEWAY_ERROR + expectedLabel;
            expectedDescription = GATEWAY_ERROR_HAPPEN + expectedDescription + CONTACT_GATEWAY_SUPPORT;
        }

        Assert.assertEquals(expectedStatusCode, Optional.ofNullable(((TompError) response.getBody()).getStatus()).get());
        Assert.assertEquals(expectedLabel, Optional.ofNullable(((TompError) response.getBody()).getTitle()).get());
        Assert.assertEquals(expectedDescription, ((TompError) response.getBody()).getDetail());
    }
}
