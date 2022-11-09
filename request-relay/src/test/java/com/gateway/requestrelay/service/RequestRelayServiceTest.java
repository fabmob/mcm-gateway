package com.gateway.requestrelay.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.commonapi.dto.requestrelay.PartnerCallsFinalDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.requestrelay.service.impl.RequestRelayServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.lenient;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class RequestRelayServiceTest {

    public static final String IT_RESOURCES_PATH = "./src/test/resources/";
    public static final String RELAY_REQUEST_POST_OK_JSON = "request-relay/request/postRequestRelay_ok.json";
    public static final String RELAY_MOCK_POST_JSON = "request-relay/mock/postRequestRelayMock.json";
    public static final String DEMO_URL = "http://demo.io";


    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    RequestRelayServiceImpl requestRelayService;


    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ErrorMessages errorMessages;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testCalls() throws Exception {
        ResponseEntity<String> mspResponseMocked = createMockedMSPResponse();
        // mock makecall() operation with a stub object from createMocked function
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(DEMO_URL),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(mspResponseMocked);
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + RELAY_REQUEST_POST_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerCallsFinalDTO>() {
        });
        PartnerCallsFinalDTO call = objectReader.readValue(mockStringyfied);
        assertEquals(requestRelayService.processCalls(call, false).getBody().replace("\n", "").replace(" ", ""), mspResponseMocked.getBody().replace(" ", ""));

    }

    @Test
    void testCallsException() throws IOException {

        NotFoundException adapterException = new NotFoundException("exception");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(DEMO_URL),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + RELAY_REQUEST_POST_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerCallsFinalDTO>() {
        });
        PartnerCallsFinalDTO call = objectReader.readValue(mockStringyfied);
        assertThrows(UnavailableException.class, () -> {
            requestRelayService.processCalls(call, false);
        });

    }


    @Test
    void testCallsHttpClientErrorException() throws IOException {

        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(DEMO_URL),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + RELAY_REQUEST_POST_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerCallsFinalDTO>() {
        });
        PartnerCallsFinalDTO call = objectReader.readValue(mockStringyfied);
        assertThrows(NotFoundException.class, () -> {
            requestRelayService.processCalls(call, false);
        });

    }

    @Test
    void testCallsRestClientExceptionException() throws IOException {

        RestClientException adapterException = new RestClientException("error");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(DEMO_URL),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + RELAY_REQUEST_POST_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerCallsFinalDTO>() {
        });
        PartnerCallsFinalDTO call = objectReader.readValue(mockStringyfied);
        assertThrows(BadGatewayException.class, () -> {
            requestRelayService.processCalls(call, false);
        });

    }


    @Test
    void testCallsExceptionTOMP() throws IOException {

        NotFoundException adapterException = new NotFoundException("exception");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(DEMO_URL),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + RELAY_REQUEST_POST_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerCallsFinalDTO>() {
        });
        PartnerCallsFinalDTO call = objectReader.readValue(mockStringyfied);
        assertThrows(NotFoundException.class, () -> {
            requestRelayService.processCalls(call, true);
        });

    }

    @Test
    void testCallsExceptionBadProtocol() throws IOException {

        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerCallsFinalDTO>() {
        });
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + RELAY_REQUEST_POST_OK_JSON);
        PartnerCallsFinalDTO call = objectReader.readValue(mockStringyfied);

        call.setUrl("ftp://host.com");

        //Only http/https must be allowed
        assertThrows(BadRequestException.class, () -> {
            requestRelayService.processCalls(call, true);
        });
    }


    @Test
    void testCallsHttpClientErrorExceptionTOMP() throws IOException {

        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(DEMO_URL),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + RELAY_REQUEST_POST_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerCallsFinalDTO>() {
        });
        PartnerCallsFinalDTO call = objectReader.readValue(mockStringyfied);
        assertThrows(HttpClientErrorException.class, () -> {
            requestRelayService.processCalls(call, true);
        });

    }

    @Test
    void testCallsRestClientExceptionExceptionTOMP() throws IOException {

        RestClientException adapterException = new RestClientException("error");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(DEMO_URL),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + RELAY_REQUEST_POST_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerCallsFinalDTO>() {
        });
        PartnerCallsFinalDTO call = objectReader.readValue(mockStringyfied);
        assertThrows(RestClientException.class, () -> {
            requestRelayService.processCalls(call, true);
        });

    }


    /**
     * Create a mock of MSP response
     *
     * @return Fake object for mock
     * @throws IOException
     */

    private ResponseEntity<String> createMockedMSPResponse() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + RELAY_MOCK_POST_JSON);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "utf-16");
        ResponseEntity<String> response =
                new ResponseEntity<String>(mockStringyfied, headers, HttpStatus.OK);

        return response;
    }

}
