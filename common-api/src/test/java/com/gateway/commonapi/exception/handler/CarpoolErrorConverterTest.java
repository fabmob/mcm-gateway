package com.gateway.commonapi.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.exceptions.CarpoolError;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;


@Slf4j
public class CarpoolErrorConverterTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Integer> carpoolTestValidCodes = List.of(200, 429, 500);
    private UUID UUID_TEST = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    WebRequest request;

    @Before
    public void setup() {
        request = Mockito.mock(WebRequest.class);
    }

    @Test
    public void testErrorCodeNotChanged_limits() throws JsonProcessingException {
        assertEquals(12, (int) CarpoolErrorConverter.convertErrorCodesToCarpoolErrorCodes(null, 12));
        assertNull(CarpoolErrorConverter.convertErrorCodesToCarpoolErrorCodes(request, null));
        assertNull(CarpoolErrorConverter.convertErrorCodesToCarpoolErrorCodes(null, null));
    }

    @Test
    public void testErrorCodeNotChanged_urlNotStatus() throws JsonProcessingException {
        //Prepare
        GenericError error = new GenericError();
        error.setErrorCode(1535);
        error.setCallId(UUID_TEST);
        error.setDescription("text");
        BadRequestException exception = new BadRequestException(error);

        Mockito.when(request.toString()).thenReturn("http://host/carpooling/driver_journeys");

        //Act
        ResponseEntity response = CarpoolErrorConverter.getResponseEntityForCarpoolStandard(HttpStatus.BAD_REQUEST, error, exception.getMessage(), HttpStatus.BAD_REQUEST, exception, carpoolTestValidCodes, request);

        //Verify
        assertNotNull(response.getBody());
        CarpoolError responseError = (CarpoolError) response.getBody();
        assertEquals("text Technical gateway instance 123e4567-e89b-12d3-a456-426614174000, errorcode 1535", responseError.getError());
    }

    @Test
    public void testErrorCodeNotChanged_urlStatus_errorCodeNotChanged() throws JsonProcessingException {
        //Prepare
        GenericError error = new GenericError();
        error.setErrorCode(9999);
        error.setCallId(UUID_TEST);
        error.setDescription("text");
        BadRequestException exception = new BadRequestException(error);

        Mockito.when(request.toString()).thenReturn("http://host/carpooling/status");

        //Act
        ResponseEntity response = CarpoolErrorConverter.getResponseEntityForCarpoolStandard(HttpStatus.BAD_REQUEST, error, exception.getMessage(), HttpStatus.BAD_REQUEST, exception, carpoolTestValidCodes, request);

        //Verify
        assertNotNull(response.getBody());
        CarpoolError responseError = (CarpoolError) response.getBody();
        assertEquals("text Technical gateway instance 123e4567-e89b-12d3-a456-426614174000, errorcode 9999", responseError.getError());
    }

    @Test
    public void testErrorCodeOverwritten() throws JsonProcessingException {
        //Prepare
        GenericError error = new GenericError();
        error.setErrorCode(1535);
        error.setCallId(UUID_TEST);
        error.setDescription("text");
        InternalException exception = new InternalException(error);

        Mockito.when(request.toString()).thenReturn("http://host/carpooling/status");

        //Act
        ResponseEntity response = CarpoolErrorConverter.getResponseEntityForCarpoolStandard(HttpStatus.INTERNAL_SERVER_ERROR, error, exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, exception, carpoolTestValidCodes, request);

        //Verify
        assertNotNull(response.getBody());
        CarpoolError responseError = (CarpoolError) response.getBody();
        assertEquals("text Technical gateway instance 123e4567-e89b-12d3-a456-426614174000, errorcode 7535", responseError.getError());
    }
}
