package com.gateway.commonapi.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.TompError;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.UnauthorizedException;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class TompErrorConverterTest {

    private List<Integer> tompValidCodes = List.of(200, 400, 401, 500);

    private final static UUID UUID_TEST = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
    }

    /**
     * Test that a preformatted TOMP Error is used as it
     */
    @Test
    public void testTompErrorFormat() throws JsonProcessingException {
        //Prepare
        TompError error = new TompError();
        error.setType("type_tomp");
        error.setDetail("detail_tomp");
        RuntimeException exception = new RuntimeException(CommonUtils.stringifyTompErrorDto(error));

        //Act
        ResponseEntity response = TompErrorConverter.getResponseEntityForTompStandard(HttpStatus.BAD_REQUEST, null, exception.getMessage(), HttpStatus.BAD_REQUEST, exception, tompValidCodes);

        //Verify
        assertNotNull(response.getBody());
        TompError responseError = objectMapper.readValue((String) response.getBody(), TompError.class);
        assertEquals("type_tomp", responseError.getType());
        assertEquals("detail_tomp", responseError.getDetail());
    }

    @Test
    public void testTompError_badRequest() throws JsonProcessingException {
        //Prepare
        GenericError error = new GenericError();
        error.setErrorCode(1234);
        error.setLabel("label");
        error.setDescription("description");
        error.setTimestamp("timestamp");
        error.setCallId(UUID_TEST);
        error.setStatus(400);
        BadRequestException exception = new BadRequestException(error);

        //Act
        ResponseEntity response = TompErrorConverter.getResponseEntityForTompStandard(HttpStatus.BAD_REQUEST, error, exception.getMessage(), HttpStatus.BAD_REQUEST, exception, tompValidCodes);

        //Verify
        assertNotNull(response.getBody());
        TompError responseError = (TompError) response.getBody();
        assertEquals(1234, responseError.getErrorcode());
        assertEquals("Invalid", responseError.getType());
        assertEquals("label", responseError.getTitle());
        assertEquals(400, responseError.getStatus());
        assertEquals("description", responseError.getDetail());
        assertEquals("Gateway callId " + UUID_TEST.toString(), responseError.getInstance());
        assertEquals("timestamp", responseError.getTimestamp());
    }

    @Test
    public void testTompError_unauthorized() throws JsonProcessingException {
        //Prepare
        GenericError error = new GenericError();
        error.setErrorCode(1234);
        error.setLabel("label");
        error.setDescription("description");
        error.setTimestamp("timestamp");
        error.setCallId(UUID_TEST);
        error.setStatus(401);
        UnauthorizedException exception = new UnauthorizedException(error);

        //Act
        ResponseEntity response = TompErrorConverter.getResponseEntityForTompStandard(HttpStatus.UNAUTHORIZED, error, exception.getMessage(), HttpStatus.UNAUTHORIZED, exception, tompValidCodes);

        //Verify
        assertNotNull(response.getBody());
        TompError responseError = (TompError) response.getBody();
        assertEquals(7401, responseError.getErrorcode());
        assertEquals("Technical issue", responseError.getType());
        assertEquals("Unauthorized", responseError.getTitle());
        assertEquals(401, responseError.getStatus());
        assertEquals("The request requires user authentication.", responseError.getDetail());
        assertEquals("Gateway callId " + UUID_TEST.toString(), responseError.getInstance());
        assertEquals("timestamp", responseError.getTimestamp());
    }

    @Test
    public void testTompError_serverError() throws JsonProcessingException {
        //Prepare
        GenericError error = new GenericError();
        error.setErrorCode(1234);
        error.setLabel("label");
        error.setDescription("description");
        error.setTimestamp("timestamp");
        error.setCallId(UUID_TEST);
        error.setStatus(500);
        InternalException exception = new InternalException(error);

        //Act
        ResponseEntity response = TompErrorConverter.getResponseEntityForTompStandard(HttpStatus.INTERNAL_SERVER_ERROR, error, exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, exception, tompValidCodes);

        //Verify
        assertNotNull(response.getBody());
        TompError responseError = (TompError) response.getBody();
        assertEquals(7500, responseError.getErrorcode());
        assertEquals("Technical issue", responseError.getType());
        assertEquals("Gateway error : label", responseError.getTitle());
        assertEquals(500, responseError.getStatus());
        assertEquals("Gateway error happen : description, contact your Gateway support.", responseError.getDetail());
        assertEquals("Gateway callId " + UUID_TEST.toString(), responseError.getInstance());
        assertEquals("timestamp", responseError.getTimestamp());
    }

    @Test
    public void testTompError_movedPermanently() throws JsonProcessingException {
        //Prepare
        RuntimeException exception = new RuntimeException("resource is now here");
        GenericError bodyGenericError = new GenericError(HttpStatus.MOVED_PERMANENTLY, HttpStatus.MOVED_PERMANENTLY.value(), HttpStatus.MOVED_PERMANENTLY.getReasonPhrase(), exception.getMessage());
        bodyGenericError.setCallId(UUID_TEST);

        //Act
        ResponseEntity response = TompErrorConverter.getResponseEntityForTompStandard(HttpStatus.MOVED_PERMANENTLY, bodyGenericError, exception.getMessage(), HttpStatus.MOVED_PERMANENTLY, exception, tompValidCodes);

        //Verify
        assertNotNull(response.getBody());
        TompError responseError = (TompError) response.getBody();
        assertEquals(7500, responseError.getErrorcode());
        assertEquals("Technical issue", responseError.getType());
        assertEquals("Gateway error : resource is now here", responseError.getTitle());
        assertEquals(500, responseError.getStatus());
        assertEquals("Gateway error happen : resource is now here, contact your Gateway support.", responseError.getDetail());
        assertEquals("Gateway callId " + UUID_TEST.toString(), responseError.getInstance());
        assertNotNull(responseError.getTimestamp());
    }

}
