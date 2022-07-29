package com.gateway.routingapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.data.AdaptersDTO;
import com.gateway.commonapi.dto.data.MspStandardDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import org.junit.Assert;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class RoutingServiceImplTest {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String MOCK = "mock/routeMock.json";

    @InjectMocks
    private RoutingServiceImpl routingService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ErrorMessages errorMessage;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(routingService, "defaultAdapterUri", "uri");
        ReflectionTestUtils.setField(routingService, "customAdapterUri", "uri");

    }

    @Test
    public void restTemplate() {
        assertNotNull(routingService.restTemplate());
    }

    private Object createMockResponse() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + MOCK);
        return (Object) expectedStringyfied;

    }

    private MspStandardDTO[] createMockMspStandard() {
        MspStandardDTO[] mspStandardDTO = new MspStandardDTO[2];
        mspStandardDTO[0] = new MspStandardDTO();
        mspStandardDTO[0].setMspStandardId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        mspStandardDTO[0].setStandardName("default-adapter");
        mspStandardDTO[0].setVersionDataMapping("v1");
        mspStandardDTO[0].setVersionStandard("v1");
        mspStandardDTO[0].setIsActive(true);
        mspStandardDTO[0].setMspId(UUID.fromString("f327579d-02f8-5599-b97b-ffb678e3f812"));
        mspStandardDTO[0].setMspActionsId(UUID.fromString("f107579d-02f8-5599-b97b-ffb678e3f899"));
        mspStandardDTO[0].setAdaptersId(UUID.fromString("f327579d-02f8-5599-b97b-ffb678e3f877"));

        mspStandardDTO[1] = new MspStandardDTO();
        mspStandardDTO[1].setMspStandardId(UUID.fromString("1457579d-02f8-4479-b97b-ffb678e3f801"));
        mspStandardDTO[1].setStandardName("non");
        return mspStandardDTO;
    }

    private AdaptersDTO createMockAdapter() {
        AdaptersDTO adaptersDTO = new AdaptersDTO();
        adaptersDTO.setAdapterId(UUID.fromString("f327579d-02f8-5599-b97b-ffb678e3f877"));
        adaptersDTO.setAdapterName("default-adapter");
        return adaptersDTO;
    }

    private AdaptersDTO createMockAdapterCustom() {
        AdaptersDTO adaptersDTO = new AdaptersDTO();
        adaptersDTO.setAdapterId(UUID.fromString("f327579d-02f8-5599-b97b-ffb678e3f877"));
        adaptersDTO.setAdapterName("custom-adapter");
        return adaptersDTO;
    }

    @Test
    void routeGetOperationTestDefault() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("clé", "valeur");
        params.put("clé2", "valeur2");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";


        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/msp-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapter());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);

        ResponseEntity<Object> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&mspId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(objectResponse);

        Object routeOperation = routingService.routeOperation(params, mspId, actionName, Optional.empty());
        assertEquals(createMockResponse(), routeOperation);
    }


    @Test
    void routeGetOperationTestCustom() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("clé", "valeur");
        params.put("clé2", "valeur2");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";


        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/msp-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapterCustom());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);

        ResponseEntity<Object> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&mspId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(objectResponse);

        Object routeOperation = routingService.routeOperation(params, mspId, actionName, Optional.empty());
        assertEquals(createMockResponse(), routeOperation);
    }

    @Test
    void activeVersionSearchRestException() {
        Map<String, String> params = new HashMap<>();
        params.put("clé", "valeur");
        params.put("clé2", "valeur2");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/msp-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenThrow(restException);

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        assertThrows(BadGatewayException.class, () -> {
            routingService.routeOperation(params, mspId, actionName, Optional.empty());

        });
    }
    @Test
    void activeVersionSearchException() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("clé", "valeur");
        params.put("clé2", "valeur2");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";


        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/msp-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            routingService.routeOperation(params, mspId, actionName, Optional.empty());
        });
        String actualMessage = exception.getMessage();
        Assert.assertEquals(null, actualMessage);

    }

    @Test
    void getAdaptersNameException() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("clé", "valeur");
        params.put("clé2", "valeur2");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/msp-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapterCustom());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            routingService.routeOperation(params, mspId, actionName, Optional.empty());
        });
        String actualMessage = exception.getMessage();
        Assert.assertEquals(null, actualMessage);

    }
    @Test
    void getAdaptersNameRestClientException() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("clé", "valeur");
        params.put("clé2", "valeur2");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/msp-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenThrow(restException);

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        assertThrows(BadGatewayException.class, () -> {
            routingService.routeOperation(params, mspId, actionName, Optional.empty());

        });
    }

    @Test
    void forwardRequestException() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("clé", "valeur");
        params.put("clé2", "valeur2");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/msp-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapterCustom());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);

        ResponseEntity<Object> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&mspId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            routingService.routeOperation(params, mspId, actionName, Optional.empty());
        });
        String actualMessage = exception.getMessage();
        Assert.assertEquals(null, actualMessage);

    }

    @Test
    void forwardRequestHttpClientErrorException() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("clé", "valeur");
        params.put("clé2", "valeur2");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/msp-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapterCustom());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);

        HttpClientErrorException exp = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&mspId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenThrow(exp);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            routingService.routeOperation(null, mspId, null, Optional.empty());
        });
    }

    @Test
    void forwardRequestRestClientException() throws IOException {
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/msp-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapterCustom());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);


        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&mspId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("connection failed");


        assertThrows(BadGatewayException.class, () -> {
            routingService.routeOperation(null, mspId, actionName, Optional.empty());
        });

    }


}
