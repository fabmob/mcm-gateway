import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.exceptions.BadGateway;
import com.gateway.commonapi.dto.exceptions.BusinessError;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.exception.handler.RestResponseEntityExceptionHandler;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.tests.UTTestCase;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ExceptionsTest extends UTTestCase {

    @Test
    public void testMediaTypeCharset() throws IOException, JSONException {
        String testLabel = "Test of business exception DTO";
        GenericError error = new BusinessError(12, "myLabel", "myDescription");
        BusinessException businessException = new BusinessException(CommonUtils.stringifyGenericErrorDto(error));

        String pathFile = "src/test/resources/exceptions/expected/businessException.json";
        final String jsonExpected = WsTestUtil.readJsonFromFilePath(pathFile);

        // we use object mapper to reformat unecessary spaces in the json reading and writing it
        ObjectMapper objectMapper = new ObjectMapper();
        String cleanedExpected = WsTestUtil.ignoreTimeStamp(objectMapper.writeValueAsString(objectMapper.readValue(jsonExpected, Object.class)));

        String cleanedResult = WsTestUtil.ignoreTimeStamp(businessException.getMessage());
        JSONObject expectedJsonObject = new JSONObject(cleanedExpected);
        JSONObject resultJsonObject = new JSONObject(cleanedResult);

        log.info("Testing {}", testLabel);
        log.info("expected: " + cleanedExpected);
        log.info("result  : " + cleanedResult);

        // reformat json and compare to have a string comparison result
        Assertions.assertEquals(expectedJsonObject.toString(4), resultJsonObject.toString(4), testLabel);
    }

    @Test
    public void testMapJsonExceptionMessageWithoutErrorDto() throws NullPointerException {
        RestResponseEntityExceptionHandler responseEntityExceptionHandler = new RestResponseEntityExceptionHandler();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpServletRequest servletRequest = new MockHttpServletRequest();
        WebRequest request = new ServletWebRequest(servletRequest);
        String message = "functionnal error";
        BusinessException exception = new BusinessException(message);
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);
        ResponseEntity<Object> response = responseEntityExceptionHandler.handleBusinessException(exception, request);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        Assertions.assertEquals(message, ((GenericError) response.getBody()).getDescription());
    }

    @Test
    public void testMapJsonExceptionMessageWithErrorDto() throws NullPointerException {
        RestResponseEntityExceptionHandler responseEntityExceptionHandler = new RestResponseEntityExceptionHandler();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpServletRequest servletRequest = new MockHttpServletRequest();
        WebRequest request = new ServletWebRequest(servletRequest);
        String message = "functionnal error";
        GenericError errorDto = generateErrorDto();
        errorDto.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        BusinessException businessException = new BusinessException(CommonUtils.stringifyGenericErrorDto(errorDto));
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);
        ResponseEntity<Object> response = responseEntityExceptionHandler.handleBusinessException(businessException, request);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        Assertions.assertEquals(42, ((GenericError) response.getBody()).getErrorCode());
        Assertions.assertEquals("test label", ((GenericError) response.getBody()).getLabel());
        Assertions.assertEquals("test description", ((GenericError) response.getBody()).getDescription());
    }


    @Test
    public void testAllConstructorWithGeneric() {
        GenericError stubError = generateErrorDto();
        BadGatewayException badGatewayException = new BadGatewayException(stubError);
        Assertions.assertEquals(HttpStatus.BAD_GATEWAY.value(), badGatewayException.getBadGatewayError().getStatus());
        BadRequestException badRequestException = new BadRequestException(stubError);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), badRequestException.getBadRequest().getStatus());
        BusinessException businessErrorException = new BusinessException(stubError);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), businessErrorException.getBusinessError().getStatus());
        ConflictException conflictException = new ConflictException(stubError);
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), conflictException.getConflict().getStatus());
        NotFoundException notFoundException = new NotFoundException(stubError);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), notFoundException.getNotFound().getStatus());
        UnauthorizedException unauthorizedException = new UnauthorizedException(stubError);
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), unauthorizedException.getUnauthorized().getStatus());
        UnavailableException unavailableException = new UnavailableException(stubError);
        Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), unavailableException.getServiceUnavailable().getStatus());

        stubError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        InternalException internalException = new InternalException(stubError);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), internalException.getInternalError().getStatus());
    }

    @Test
    public void testBadGatewayException() {
        GenericError stubError = generateErrorDto();
        String stringyfiedStubError = CommonUtils.stringifyGenericErrorDto(stubError);
        // Bad gateway
        BadGatewayException badGatewayException = new BadGatewayException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, badGatewayException.getMessage());
        Assertions.assertEquals("test description", badGatewayException.getBadGatewayError().getDescription());
        Assertions.assertEquals(42, badGatewayException.getBadGatewayError().getErrorCode());
        Assertions.assertEquals(HttpStatus.BAD_GATEWAY.value(), badGatewayException.getBadGatewayError().getStatus());

        Assertions.assertEquals("label", new BadGateway("label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway("label", "description").getDescription());
        Assertions.assertEquals(15, new BadGateway(15, "label", "description").getErrorCode());
        Assertions.assertEquals("label", new BadGateway(15, "label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway(15, "label", "description").getDescription());
        Assertions.assertEquals("description", new BadGateway("description").getDescription());
    }

    @Test
    public void testBadRequestException() {
        GenericError stubError = generateErrorDto();
        String stringyfiedStubError = CommonUtils.stringifyGenericErrorDto(stubError);
        // Bad gateway
        BadRequestException badRequestException = new BadRequestException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, badRequestException.getMessage());
        Assertions.assertEquals("test description", badRequestException.getBadRequest().getDescription());
        Assertions.assertEquals(42, badRequestException.getBadRequest().getErrorCode());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), badRequestException.getBadRequest().getStatus());

        Assertions.assertEquals("label", new BadGateway("label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway("label", "description").getDescription());
        Assertions.assertEquals(15, new BadGateway(15, "label", "description").getErrorCode());
        Assertions.assertEquals("label", new BadGateway(15, "label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway(15, "label", "description").getDescription());
        Assertions.assertEquals("description", new BadGateway("description").getDescription());
    }

    @Test
    public void testBusinessErrorException() {
        GenericError stubError = generateErrorDto();
        String stringyfiedStubError = CommonUtils.stringifyGenericErrorDto(stubError);
        // Bad gateway
        BusinessException businessErrorException = new BusinessException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, businessErrorException.getMessage());
        Assertions.assertEquals("test description", businessErrorException.getBusinessError().getDescription());
        Assertions.assertEquals(42, businessErrorException.getBusinessError().getErrorCode());
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), businessErrorException.getBusinessError().getStatus());

        Assertions.assertEquals("label", new BadGateway("label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway("label", "description").getDescription());
        Assertions.assertEquals(15, new BadGateway(15, "label", "description").getErrorCode());
        Assertions.assertEquals("label", new BadGateway(15, "label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway(15, "label", "description").getDescription());
        Assertions.assertEquals("description", new BadGateway("description").getDescription());
    }

    @Test
    public void testConflictException() {
        GenericError stubError = generateErrorDto();
        String stringyfiedStubError = CommonUtils.stringifyGenericErrorDto(stubError);
        // Bad gateway
        ConflictException conflictException = new ConflictException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, conflictException.getMessage());
        Assertions.assertEquals("test description", conflictException.getConflict().getDescription());
        Assertions.assertEquals(42, conflictException.getConflict().getErrorCode());
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), conflictException.getConflict().getStatus());

        Assertions.assertEquals("label", new BadGateway("label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway("label", "description").getDescription());
        Assertions.assertEquals(15, new BadGateway(15, "label", "description").getErrorCode());
        Assertions.assertEquals("label", new BadGateway(15, "label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway(15, "label", "description").getDescription());
        Assertions.assertEquals("description", new BadGateway("description").getDescription());
    }

    @Test
    public void testInternalException() {
        GenericError stubError = generateErrorDto();
        stubError.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String stringyfiedStubError = CommonUtils.stringifyGenericErrorDto(stubError);
        // InternalException
        InternalException internalException = new InternalException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, internalException.getMessage());
        Assertions.assertEquals("test description", internalException.getInternalError().getDescription());
        Assertions.assertEquals(42, internalException.getInternalError().getErrorCode());
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), internalException.getInternalError().getStatus());

        Assertions.assertEquals("label", new BadGateway("label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway("label", "description").getDescription());
        Assertions.assertEquals(15, new BadGateway(15, "label", "description").getErrorCode());
        Assertions.assertEquals("label", new BadGateway(15, "label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway(15, "label", "description").getDescription());
        Assertions.assertEquals("description", new BadGateway("description").getDescription());
    }

    @Test
    public void testNotFoundException() {
        GenericError stubError = generateErrorDto();
        String stringyfiedStubError = CommonUtils.stringifyGenericErrorDto(stubError);
        // not found
        NotFoundException notFoundException = new NotFoundException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, notFoundException.getMessage());
        Assertions.assertEquals("test description", notFoundException.getNotFound().getDescription());
        Assertions.assertEquals(42, notFoundException.getNotFound().getErrorCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), notFoundException.getNotFound().getStatus());

        Assertions.assertEquals("label", new BadGateway("label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway("label", "description").getDescription());
        Assertions.assertEquals(15, new BadGateway(15, "label", "description").getErrorCode());
        Assertions.assertEquals("label", new BadGateway(15, "label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway(15, "label", "description").getDescription());
        Assertions.assertEquals("description", new BadGateway("description").getDescription());
    }

    @Test
    public void testUnauthorizedException() {
        GenericError stubError = generateErrorDto();
        String stringyfiedStubError = CommonUtils.stringifyGenericErrorDto(stubError);
        // Bad gateway
        UnauthorizedException unauthorizedException = new UnauthorizedException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, unauthorizedException.getMessage());
        Assertions.assertEquals("test description", unauthorizedException.getUnauthorized().getDescription());
        Assertions.assertEquals(42, unauthorizedException.getUnauthorized().getErrorCode());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), unauthorizedException.getUnauthorized().getStatus());

        Assertions.assertEquals("label", new BadGateway("label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway("label", "description").getDescription());
        Assertions.assertEquals(15, new BadGateway(15, "label", "description").getErrorCode());
        Assertions.assertEquals("label", new BadGateway(15, "label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway(15, "label", "description").getDescription());
        Assertions.assertEquals("description", new BadGateway("description").getDescription());
    }

    @Test
    public void testUnavailableException() {
        GenericError stubError = generateErrorDto();
        String stringyfiedStubError = CommonUtils.stringifyGenericErrorDto(stubError);
        // unavailable exeception
        UnavailableException unavailableException = new UnavailableException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, unavailableException.getMessage());
        Assertions.assertEquals("test description", unavailableException.getServiceUnavailable().getDescription());
        Assertions.assertEquals(42, unavailableException.getServiceUnavailable().getErrorCode());
        Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), unavailableException.getServiceUnavailable().getStatus());

        Assertions.assertEquals("label", new BadGateway("label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway("label", "description").getDescription());
        Assertions.assertEquals(15, new BadGateway(15, "label", "description").getErrorCode());
        Assertions.assertEquals("label", new BadGateway(15, "label", "description").getLabel());
        Assertions.assertEquals("description", new BadGateway(15, "label", "description").getDescription());
        Assertions.assertEquals("description", new BadGateway("description").getDescription());
    }


    @Test
    public void testExceptions() {
        GenericError stubError = generateErrorDto();
        // Bad request
        BadRequestException exception = new BadRequestException(stubError);
        Assertions.assertEquals(WsTestUtil.ignoreTimeStamp("{\"status\":403,\"errorCode\":42,\"label\":\"test label\",\"description\":\"test description\",\"timestamp\":\"2022-04-06T20:11:12.529+02:00\"}"), WsTestUtil.ignoreTimeStamp(exception.getMessage()));
        Assertions.assertEquals(stubError.getDescription(), WsTestUtil.ignoreTimeStamp(exception.getBadRequest().getDescription()));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getBadRequest().getStatus());

        String stringyfiedStubError = CommonUtils.stringifyGenericErrorDto(stubError);

        // business exception
        BusinessException businessException = new BusinessException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, businessException.getMessage());
        Assertions.assertEquals("test description", businessException.getBusinessError().getDescription());
        Assertions.assertEquals(42, businessException.getBusinessError().getErrorCode());
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), businessException.getBusinessError().getStatus());

        // conflictException
        ConflictException conflictException = new ConflictException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, conflictException.getMessage());
        Assertions.assertEquals("test description", conflictException.getConflict().getDescription());
        Assertions.assertEquals(42, conflictException.getConflict().getErrorCode());
        Assertions.assertEquals(HttpStatus.CONFLICT.value(), conflictException.getConflict().getStatus());

        // ExceptionNotFound
        NotFoundException exceptionNotFound = new NotFoundException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, exceptionNotFound.getMessage());
        Assertions.assertEquals("test description", exceptionNotFound.getNotFound().getDescription());
        Assertions.assertEquals(42, exceptionNotFound.getNotFound().getErrorCode());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), exceptionNotFound.getNotFound().getStatus());

        // UnauthorizedException
        UnauthorizedException unauthorizedException = new UnauthorizedException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, unauthorizedException.getMessage());
        Assertions.assertEquals("test description", unauthorizedException.getUnauthorized().getDescription());
        Assertions.assertEquals(42, unauthorizedException.getUnauthorized().getErrorCode());
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), unauthorizedException.getUnauthorized().getStatus());

        // unavailableException
        UnavailableException unavailableException = new UnavailableException(stringyfiedStubError);
        Assertions.assertEquals(stringyfiedStubError, unavailableException.getMessage());
        Assertions.assertEquals("test description", unavailableException.getServiceUnavailable().getDescription());
        Assertions.assertEquals(42, unavailableException.getServiceUnavailable().getErrorCode());
        Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), unavailableException.getServiceUnavailable().getStatus());
    }

    public GenericError generateErrorDto() {
        GenericError genericError = new GenericError();
        genericError.setErrorCode(42);
        genericError.setLabel("test label");
        genericError.setDescription("test description");
        genericError.setStatus(HttpStatus.FORBIDDEN.value());
        return genericError;
    }

    @Test
    public void testExceptionMapper() {
        Assertions.assertEquals(BadRequestException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
        Assertions.assertEquals(UnauthorizedException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
        Assertions.assertEquals(NotFoundException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
        Assertions.assertEquals(ConflictException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.CONFLICT, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
        Assertions.assertEquals(InternalException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.FORBIDDEN, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
        Assertions.assertEquals(InternalException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.METHOD_NOT_ALLOWED, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
        Assertions.assertEquals(InternalException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.NOT_ACCEPTABLE, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
        Assertions.assertEquals(InternalException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.GONE, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
        Assertions.assertEquals(InternalException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
        Assertions.assertEquals(InternalException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.TOO_MANY_REQUESTS, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
        Assertions.assertEquals(InternalException.class, ExceptionUtils.getMappedGatewayRuntimeException(HttpClientErrorException.create(HttpStatus.UNPROCESSABLE_ENTITY, "", HttpHeaders.EMPTY, "".getBytes(), null), "").getClass());
    }
}
