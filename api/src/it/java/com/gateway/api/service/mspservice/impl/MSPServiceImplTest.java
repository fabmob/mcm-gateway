package com.gateway.api.service.mspservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.api.model.MSPMeta;
import com.gateway.commonapi.dto.api.MSPZone;
import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.enums.ZoneStatus;
import com.gateway.commonapi.utils.enums.ZoneType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class MSPServiceImplTest {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String SERVICE_EXPECTED_GET_MSPs_OK_JSON = "gateway-api/mockedservices-expected/service_getMsps_ok.json";
    public static final String SERVICE_EXPECTED_GET_MSP_OK_JSON = "gateway-api/mockedservices-expected/service_getMsp_by_id_ok.json";
    public static final String SERVICE_EXPECTED_GET_MSP_ZONE_OK_JSON = "gateway-api/mockedservices-expected/getMspZoneMock.json";

    public static final String MOCK_MSPS_JSON = "gateway-api/mockedservices-expected/getMspsMock.json";

    @Value("${gateway.service.dataapi.baseUrl}")
    private String callUri;

    @InjectMocks
    private MSPServiceImpl mspService;

    @Autowired
    private MSPServiceImpl service;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ErrorMessages errorMessages;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private List<Object> createMockMspZone() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_MSP_ZONE_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;
    }

    /**
     * Test list Msps operation
     *
     * @throws Exception
     */
    @Test
    void testGetMspMetas() throws Exception {
        MspMetaDTO[] mspListMocked = createMockedMspList();
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<MspMetaDTO[]> mspMetasDto = new ResponseEntity(mspListMocked, httpHeaders, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(mspMetasDto);

        //Expected response
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_MSPs_OK_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<MSPMeta>>() {
        });
        List<MSPMeta> expectedMSPs = objectReader.readValue(mockStringyfied);
        List<MSPMeta> expectedMSPsLinked = new ArrayList<>();
        for (MSPMeta msp : expectedMSPs) {
            try {
                msp.setHasVehicle(true);
                msp.setHasStation(true);
                msp.setHasStationStatus(true);
                msp.setHasHold(true);
                msp.setHasParking(true);
                msp.setHasOperatingZone(true);
                msp.setHasNoParkingZone(true);
                msp.setHasPrefParkingZone(true);
                msp.setHasSpeedLimitZone(true);
                mspService.addLinks(msp);
                expectedMSPsLinked.add(msp);
            } catch (NotFoundException e) {
                log.error("No metadata for MSP identifier {}", msp.getMspId(), e);
            }
        }


        assertEquals(expectedMSPsLinked, mspService.getMSPsMeta());

    }

    @Test
    void testGetMspMetasExceptions() {
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            mspService.getMSPsMeta();
        });
        String expectedMessage = null;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void testGetMspMetasExceptionFromDataApi()  {

        HttpClientErrorException.NotFound adapterException = (HttpClientErrorException.NotFound) HttpClientErrorException.create(HttpStatus.NOT_FOUND,"exception",null,null,null);
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("exception");

        assertThrows(NotFoundException.class, () -> {
            mspService.getMSPsMeta();
        });


    }


    @Test
    void testGetMspMetasRestRequestException()  {
        RestClientException restException = new RestClientException("connection failed");
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        assertThrows(BadGatewayException.class, () -> {
            mspService.getMSPsMeta();
        });


    }


    /**
     * Test list Msp by id operation
     *
     * @throws Exception
     */
    @Test
    void testGetMspMetasById() throws Exception {
        MspMetaDTO[] mspListMocked = createMockedMspList();
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<MspMetaDTO> mspMetaDto = new ResponseEntity(mspListMocked[0], httpHeaders, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(mspMetaDto);

        //Expected response
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_MSP_OK_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<MSPMeta>() {
        });
        MSPMeta expectedMSP = objectReader.readValue(mockStringyfied);
        expectedMSP.setHasVehicle(true);
        expectedMSP.setHasStation(true);
        expectedMSP.setHasStationStatus(true);
        expectedMSP.setHasHold(true);
        expectedMSP.setHasParking(true);
        expectedMSP.setHasOperatingZone(true);
        expectedMSP.setHasNoParkingZone(true);
        expectedMSP.setHasPrefParkingZone(true);
        expectedMSP.setHasSpeedLimitZone(true);
        mspService.addLinks(expectedMSP);

        assertEquals(expectedMSP, mspService.getMSPMeta(UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83")));

    }

    @Test
    void testGetMspMetaByIdExceptions() {
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(null);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            mspService.getMSPMeta(UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83"));
        });
        String expectedMessage = null;
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void testGetMspMetaByIdExceptionFromDataApi()  {

        HttpClientErrorException.NotFound adapterException = (HttpClientErrorException.NotFound) HttpClientErrorException.create(HttpStatus.NOT_FOUND,"exception",null,null,null);
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("exception");

        assertThrows(NotFoundException.class, () -> {
            mspService.getMSPMeta(UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83"));
        });


    }


    @Test
    void testGetMspMetaByIdRestRequestException()  {
        RestClientException restException = new RestClientException("connection failed");
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        assertThrows(BadGatewayException.class, () -> {
            mspService.getMSPMeta(UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83"));
        });


    }


    @Test
    void testGetMSPZone() throws IOException {
        ZoneType areaType = ZoneType.OPERATING;
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(createMockMspZone());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        MSPZone objectResponse = mspService.getMSPZone(mspId, areaType);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", objectResponse.getMspId().toString());
    }

    @Test
    void testGetMSPZoneException()  {
        ZoneType areaType = ZoneType.OPERATING;
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        MSPZone expectedResponse = new MSPZone(UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759"), "msp1", ZoneStatus.AVAILABLE);
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(expectedResponse);
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            mspService.getMSPZone(mspId, areaType);
        });
        String actualMessage = exception.getMessage();
        Assert.assertEquals(null, actualMessage);

    }



    @Test
    void testGetMSPZoneExceptionFromAdapter()  {
        ZoneType areaType = ZoneType.OPERATING;
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        assertThrows(NotFoundException.class, () -> {
            mspService.getMSPZone(mspId, areaType);
        });


    }


    @Test
    void testGetMSPZoneRestRequestException()  {
        ZoneType areaType = ZoneType.OPERATING;
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");


        assertThrows(BadGatewayException.class, () -> {
            mspService.getMSPZone(mspId, areaType);
        });


    }

    /**
     * Create a mock of Data_api response
     *
     * @return Fake object for mock
     * @throws IOException
     */

    private MspMetaDTO[] createMockedMspList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + MOCK_MSPS_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<MspMetaDTO[]>() {
        });
        return objectReader.readValue(mockStringyfied);
    }


}

