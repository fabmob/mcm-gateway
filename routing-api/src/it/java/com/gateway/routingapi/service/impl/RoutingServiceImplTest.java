package com.gateway.routingapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.data.AdaptersDTO;
import com.gateway.commonapi.dto.data.PartnerStandardDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.enums.StandardEnum;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);

    }


    private Object createMockResponse() throws IOException {
        return WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + MOCK);

    }

    private PartnerStandardDTO[] createMockPartnerStandard() {
        PartnerStandardDTO[] partnerStandardDTO = new PartnerStandardDTO[2];
        partnerStandardDTO[0] = new PartnerStandardDTO();
        partnerStandardDTO[0].setPartnerStandardId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        partnerStandardDTO[0].setStandardName("default-adapter");
        partnerStandardDTO[0].setVersionDataMapping("v1");
        partnerStandardDTO[0].setVersionStandard("v1");
        partnerStandardDTO[0].setIsActive(true);
        partnerStandardDTO[0].setPartnerId(UUID.fromString("f327579d-02f8-5599-b97b-ffb678e3f812"));
        partnerStandardDTO[0].setPartnerActionsId(UUID.fromString("f107579d-02f8-5599-b97b-ffb678e3f899"));
        partnerStandardDTO[0].setAdaptersId(UUID.fromString("f327579d-02f8-5599-b97b-ffb678e3f877"));

        partnerStandardDTO[1] = new PartnerStandardDTO();
        partnerStandardDTO[1].setPartnerStandardId(UUID.fromString("1457579d-02f8-4479-b97b-ffb678e3f801"));
        partnerStandardDTO[1].setStandardName("non");
        return partnerStandardDTO;
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
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";


        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapter());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);

        ResponseEntity<Object> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&partnerId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(objectResponse);
        Object routeOperation = routingService.routeOperation(params, partnerId, actionName, Optional.empty());
        assertEquals(createMockResponse(), routeOperation);
    }


    @Test
    void routeGetOperationTestCustom() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";


        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapterCustom());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);

        ResponseEntity<Object> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&partnerId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(objectResponse);

        Object routeOperation = routingService.routeOperation(params, partnerId, actionName, Optional.empty());
        assertEquals(createMockResponse(), routeOperation);
    }

    @Test
    void activeVersionSearchRestException() {
        Map<String, String> params = new HashMap<>();
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenThrow(restException);

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        Optional optional = Optional.empty();
        assertThrows(BadGatewayException.class, () -> routingService.routeOperation(params, partnerId, actionName, optional));
    }

    @Test
    void activeVersionSearchHttpClientException() {
        Map<String, String> params = new HashMap<>();
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        Mockito.when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenThrow(HttpClientErrorException.NotFound.class);

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        Optional optional = Optional.empty();
        assertThrows(NotFoundException.class, () -> routingService.routeOperation(params, partnerId, actionName, optional));
    }

    @Test
    void activeVersionSearchException() {
        Map<String, String> params = new HashMap<>();
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";


        ResponseEntity.status(HttpStatus.OK).build();
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> routingService.routeOperation(params, partnerId, actionName, Optional.empty()));
        String actualMessage = exception.getMessage();
        Assert.assertEquals(null, actualMessage);

    }

    @Test
    void getAdaptersNameException() {
        Map<String, String> params = new HashMap<>();
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity.status(HttpStatus.OK).build();
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> routingService.routeOperation(params, partnerId, actionName, Optional.empty()));
        String actualMessage = exception.getMessage();
        Assert.assertEquals(null, actualMessage);

    }

    @Test
    void getAdaptersNameRestClientException() {
        Map<String, String> params = new HashMap<>();
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenThrow(restException);

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        Optional optional = Optional.empty();
        assertThrows(BadGatewayException.class, () -> routingService.routeOperation(params, partnerId, actionName, optional));
    }

    @Test
    void getAdaptersNameHttpClientException() {
        Map<String, String> params = new HashMap<>();
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenThrow(HttpClientErrorException.NotFound.class);

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        Optional optional = Optional.empty();
        assertThrows(NullPointerException.class, () -> routingService.routeOperation(params, partnerId, actionName, optional));
    }

    @Test
    void forwardRequestException() {
        Map<String, String> params = new HashMap<>();
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapterCustom());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&partnerId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> routingService.routeOperation(params, partnerId, actionName, Optional.empty()));
        String actualMessage = exception.getMessage();
        Assert.assertEquals(null, actualMessage);

    }

    @Test
    void forwardRequestHttpClientErrorException() {
        Map<String, String> params = new HashMap<>();
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapterCustom());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);

        HttpClientErrorException exp = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&partnerId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(exp);
        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        Optional optional = Optional.empty();
        assertThrows(NotFoundException.class, () -> routingService.routeOperation(null, partnerId, null, optional));
    }

    @Test
    void forwardRequestHttpServerErrorException() {
        Map<String, String> params = new HashMap<>();
        params.put("key", "value");
        params.put("key2", "value2");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapterCustom());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);

        HttpServerErrorException exp = new HttpServerErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&partnerId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(exp);
        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        Optional optional = Optional.empty();
        assertThrows(InternalException.class, () -> routingService.routeOperation(null, partnerId, null, optional));
    }


    @Test
    void forwardRequestRestClientException() {
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f815");
        String actionName = "STATION_SEARCH";

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-standards"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<AdaptersDTO> adaptersDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAdapterCustom());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/adapters/f327579d-02f8-5599-b97b-ffb678e3f877"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(AdaptersDTO.class))).thenReturn(adaptersDTOResponse);


        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.endsWith("adapt?actionId=f107579d-02f8-5599-b97b-ffb678e3f899&partnerId=f457579d-02f8-4479-b97b-ffb678e3f815"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);
        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("connection failed");


        Optional optional = Optional.empty();
        assertThrows(BadGatewayException.class, () -> routingService.routeOperation(null, partnerId, actionName, optional));

    }


}
