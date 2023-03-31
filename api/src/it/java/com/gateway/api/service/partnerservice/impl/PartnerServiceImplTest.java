package com.gateway.api.service.partnerservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.api.GatewayApplication;
import com.gateway.api.model.PartnerMeta;
import com.gateway.api.util.ValidityUtils;
import com.gateway.commonapi.cache.PartnerMetaCacheManager;
import com.gateway.commonapi.cache.ZoneCacheManager;
import com.gateway.commonapi.dto.api.PartnerZone;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.cache.CacheService;
import com.gateway.commonapi.utils.enums.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = {"com.gateway.api"})
@TestPropertySource(properties = {"classpath:application.yml"})
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureDataJpa
@Ignore("Mother class of the tests")
@Slf4j
class PartnerServiceImplTest {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String SERVICE_EXPECTED_GET_PARTNERS_OK_JSON = "gateway-api/mockedservices-expected/service_getPartners_ok.json";
    public static final String SERVICE_EXPECTED_GET_PARTNER_OK_JSON = "gateway-api/mockedservices-expected/service_getPartner_by_id_ok.json";
    public static final String SERVICE_EXPECTED_GET_PARTNER_ZONE_OK_JSON = "gateway-api/mockedservices-expected/getPartnerZoneMock.json";
    public static final String MOCK_PARTNERS_JSON = "gateway-api/mockedservices-expected/getPartnersMock.json";

    @Value("${gateway.service.dataapi.baseUrl}")
    private String callUri;

    @Mock
    private ValidityUtils validityUtils;

    @InjectMocks
    private PartnerServiceImpl partnerService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ErrorMessages errorMessages;

    @Mock
    private ZoneCacheManager zoneCacheManager;

    @Mock
    private PartnerMetaCacheManager partnerMetaCacheManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(cacheService.getCacheParam(any(), any())).thenReturn(new CacheParamDTO());
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);
    }

    private List<Object> createMockPartnerZone() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PARTNER_ZONE_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private List<Object> createMockPartnerZoneException() {
        return new ArrayList<>();
    }


    @Test
    void testGetPartnersMetaByType() throws Exception {
        PartnerMetaDTO[] partnerListMocked = createMockedPartnerList();
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<PartnerMetaDTO[]> PartnerMetasDto = new ResponseEntity<>(partnerListMocked, httpHeaders, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(PartnerMetasDto);
        assertEquals(UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83"), partnerService.getPartnersMetaByPartnerType(PartnerTypeEnum.MSP, null).get(0).getPartnerId());
    }

    @Test
    void testGetPartnersMetaByTypeUsingCache() throws Exception {
        lenient().when(cacheService.useCache()).thenReturn(true);
        PartnerMetaDTO[] partnerListCache = createMockedPartnerList();

        Mockito.when(partnerMetaCacheManager.getAllPartnersFromCache()).thenReturn(List.of(partnerListCache));
        assertEquals("Voi Trotti", partnerService.getPartnersMetaByPartnerType(PartnerTypeEnum.MSP, null).get(0).getOperator());
    }


    @Test
    void testGetPartnersMetaByTypeException() throws Exception {


        PartnerMetaDTO[] partnerListMocked = createMockedPartnerList();
        HttpHeaders httpHeaders = new HttpHeaders();
        new ResponseEntity<>(partnerListMocked, httpHeaders, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(null);

        assertThrows(NullPointerException.class, () -> partnerService.getPartnersMetaByPartnerType(PartnerTypeEnum.MSP, null));
    }

    @Test
    void testGetPartnerMetas() throws Exception {
        PartnerMetaDTO[] partnerListMocked = createMockedPartnerList();
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<PartnerMetaDTO[]> PartnerMetasDto = new ResponseEntity<>(partnerListMocked, httpHeaders, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(PartnerMetasDto);

        //Expected response
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PARTNERS_OK_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<PartnerMeta>>() {
        });
        List<PartnerMeta> expectedPartners = objectReader.readValue(mockStringyfied);
        List<PartnerMeta> expectedPartnersLinked = new ArrayList<>();
        for (PartnerMeta partner : expectedPartners) {
            try {
                partner.setHasVehicle(true);
                partner.setHasStation(true);
                partner.setType(TypeEnum.CARPOOLING);
                partner.setPartnerType(PartnerTypeEnum.MSP);
                partner.setHasStationStatus(true);
                partner.setHasHold(true);
                partner.setHasParking(true);
                partner.setHasOperatingZone(true);
                partner.setHasNoParkingZone(true);
                partner.setHasPrefParkingZone(true);
                partner.setHasSpeedLimitZone(true);
                partner.setHasCarpoolingBookingGet(true);
                partner.setHasCarpoolingBookingPatch(true);
                partner.setHasCarpoolingBookingPost(true);
                partner.setHasCarpoolingDriverJourney(true);
                partner.setHasCarpoolingDriverTrip(true);
                partner.setHasCarpoolingPassengerTrip(true);
                partner.setHasCarpoolingPassengerJourney(true);
                partner.setHasAroundMe(true);
                partner.setHasCarpoolingMessages(true);
                partner.setHasPing(true);
                partner.setHasPricingPlan(true);
                partner.setHasVehicleTypes(true);
                partner.setHasCarpoolingStatus(true);

                partnerService.addLinks(partner);
                expectedPartnersLinked.add(partner);
            } catch (NotFoundException e) {
                log.error("No metadata for Partner identifier {}", partner.getPartnerId(), e);
            }
        }


        assertEquals(expectedPartnersLinked, partnerService.getPartnersMeta());

    }


    @Test
    void testGetPartnerMetasUsingCache() throws Exception {
        lenient().when(cacheService.useCache()).thenReturn(true);
        PartnerMetaDTO[] partnersMetaCache = createMockedPartnerList();
        Mockito.when(partnerMetaCacheManager.getAllPartnersFromCache()).thenReturn(List.of(partnersMetaCache));
        assertEquals("PNG", partnerService.getPartnersMeta().get(0).getLogoFormat());
    }

    @Test
    void testGetPartnerMetasWithExample() throws IOException {
        PartnerMetaDTO[] partnerListMocked = createMockedPartnerList();
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<PartnerMetaDTO[]> PartnerMetasDto = new ResponseEntity<>(partnerListMocked, httpHeaders, HttpStatus.OK);

        PartnerMeta partnerMetaExample = new PartnerMeta();
        partnerMetaExample.setPartnerType(PartnerTypeEnum.MSP);
        partnerMetaExample.setType(TypeEnum.CARPOOLING);
        partnerMetaExample.setName("Voi");
        partnerMetaExample.setOperator("mock");

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(PartnerMetasDto);
        assertDoesNotThrow(() -> partnerService.getPartnersMetaByExample(partnerMetaExample, PartnerTypeRequestHeader.MSP));
    }

    @Test
    void testGetPartnerMetasWithExampleWithCache() throws IOException {
        lenient().when(cacheService.useCache()).thenReturn(true);

        PartnerMetaDTO[] partnerListMocked = createMockedPartnerList();
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<PartnerMetaDTO[]> PartnerMetasDto = new ResponseEntity<>(partnerListMocked, httpHeaders, HttpStatus.OK);

        PartnerMeta partnerMetaExample = new PartnerMeta();
        partnerMetaExample.setPartnerType(PartnerTypeEnum.MSP);
        partnerMetaExample.setType(TypeEnum.CARPOOLING);
        partnerMetaExample.setName("Voi");
        partnerMetaExample.setOperator("mock");

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(PartnerMetasDto);
        assertDoesNotThrow(() -> partnerService.getPartnersMetaByExample(partnerMetaExample, PartnerTypeRequestHeader.MSP));
    }

    @Test
    void testGetPartnerMetasExceptions() {
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(null);
        assertThrows(NullPointerException.class, () -> partnerService.getPartnersMeta());
    }


    @Test
    void testGetPartnerMetasExceptionFromDataApi() {

        HttpClientErrorException.NotFound adapterException = (HttpClientErrorException.NotFound) HttpClientErrorException.create(HttpStatus.NOT_FOUND, "exception", null, null, null);
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("exception");

        assertThrows(NotFoundException.class, () -> partnerService.getPartnersMeta());


    }


    @Test
    void testGetPartnerMetasRestRequestException() {
        RestClientException restException = new RestClientException("connection failed");
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        assertThrows(BadGatewayException.class, () -> partnerService.getPartnersMeta());


    }


    @Test
    void testGetPartnerMetasById() throws Exception {
        PartnerMetaDTO[] partnerListMocked = createMockedPartnerList();
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<PartnerMetaDTO> PartnerMetaDto = new ResponseEntity<>(partnerListMocked[0], httpHeaders, HttpStatus.OK);

        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(PartnerMetaDto);

        //Expected response
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PARTNER_OK_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerMeta>() {
        });
        PartnerMeta expectedPartner = objectReader.readValue(mockStringyfied);
        expectedPartner.setHasVehicle(true);
        expectedPartner.setHasStation(true);
        expectedPartner.setType(TypeEnum.CARPOOLING);
        expectedPartner.setPartnerType(PartnerTypeEnum.MSP);
        expectedPartner.setHasStationStatus(true);
        expectedPartner.setHasHold(true);
        expectedPartner.setHasParking(true);
        expectedPartner.setHasOperatingZone(true);
        expectedPartner.setHasNoParkingZone(true);
        expectedPartner.setHasPrefParkingZone(true);
        expectedPartner.setHasSpeedLimitZone(true);
        expectedPartner.setHasCarpoolingBookingGet(true);
        expectedPartner.setHasCarpoolingBookingPatch(true);
        expectedPartner.setHasCarpoolingBookingPost(true);
        expectedPartner.setHasCarpoolingDriverJourney(true);
        expectedPartner.setHasAroundMe(true);
        expectedPartner.setHasCarpoolingDriverTrip(true);
        expectedPartner.setHasCarpoolingPassengerTrip(true);
        expectedPartner.setHasCarpoolingPassengerJourney(true);
        expectedPartner.setHasCarpoolingMessages(true);
        expectedPartner.setHasPing(true);
        expectedPartner.setHasPricingPlan(true);
        expectedPartner.setHasVehicleTypes(true);
        expectedPartner.setHasCarpoolingStatus(true);
        partnerService.addLinks(expectedPartner);

        assertEquals(expectedPartner, partnerService.getPartnerMeta(UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83")));

    }


    @Test
    void testGetPartnerMetasByIdUsingCache() throws Exception {
        lenient().when(cacheService.useCache()).thenReturn(true);
        PartnerMetaDTO partnerMetaCache = createMockedPartnerList()[0];
        Mockito.when(partnerMetaCacheManager.getFromCache(UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83"))).thenReturn(partnerMetaCache);
        assertEquals("Trotti", partnerService.getPartnerMeta(UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83")).getName());

    }

    @Test
    void testGetPartnerMetaByIdExceptions() {
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenReturn(null);
        UUID partnerId = UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83");
        assertThrows(NullPointerException.class, () -> partnerService.getPartnerMeta(partnerId));

    }


    @Test
    void testGetPartnerMetaByIdExceptionFromDataApi() {

        HttpClientErrorException.NotFound adapterException = (HttpClientErrorException.NotFound) HttpClientErrorException.create(HttpStatus.NOT_FOUND, "exception", null, null, null);
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("exception");

        UUID partnerId = UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83");
        assertThrows(BadRequestException.class, () -> partnerService.getPartnerMeta(partnerId));


    }


    @Test
    void testGetPartnerMetaByIdRestRequestException() {
        RestClientException restException = new RestClientException("connection failed");
        Mockito.when(restTemplate.exchange(anyString(), any(), any(), any(Class.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        UUID partnerId = UUID.fromString("ada5039d-81a5-4676-9885-516384ccdc83");
        assertThrows(BadGatewayException.class, () -> partnerService.getPartnerMeta(partnerId));


    }


    @Test
    void testGetPartnerZone() throws IOException {

        ZoneType areaType = ZoneType.OPERATING;
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        PartnerMetaDTO partnerMeta = new PartnerMetaDTO();
        partnerMeta.setPartnerId(partnerId);
        partnerMeta.setHasVehicle(true);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(partnerMeta);
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-metas"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(createMockPartnerZone());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        ResponseEntity<CacheParamDTO[]> cacheParams = ResponseEntity.status(HttpStatus.OK).body(this.createMockCacheParamsZoneSearch());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/cache-params"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(CacheParamDTO[].class))).thenReturn(cacheParams);

        PartnerZone objectResponse = partnerService.getPartnerZone(partnerId, areaType);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", objectResponse.getPartnerId().toString());
    }


    @Test
    void testGetPartnerZoneUsingCache() {
        lenient().when(cacheService.useCache()).thenReturn(true);

        ZoneType areaType = ZoneType.OPERATING;
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<CacheParamDTO[]> cacheParams = ResponseEntity.status(HttpStatus.OK).body(this.createMockCacheParamsZoneSearch());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/cache-params"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(CacheParamDTO[].class))).thenReturn(cacheParams);

        PartnerZone partnerZone = new PartnerZone(UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759"));
        partnerZone.setPartner("VELIB");
        lenient().when(zoneCacheManager.getPartnerZoneFromCache(partnerId, ZoneType.OPERATING)).thenReturn(partnerZone);

        PartnerMetaDTO partnerMeta = new PartnerMetaDTO();
        partnerMeta.setPartnerId(UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759"));
        partnerMeta.setHasVehicle(true);
        lenient().when(partnerMetaCacheManager.getFromCache(partnerId)).thenReturn(partnerMeta);

        assertEquals("VELIB", partnerService.getPartnerZone(partnerId, areaType).getPartner());

        lenient().when(cacheService.getCacheParam(any(), anyString())).thenThrow(new RestClientException("problem"));
        assertThrows(RestClientException.class, () -> partnerService.getPartnerZone(partnerId, areaType));
    }


    private CacheParamDTO[] createMockCacheParamsZoneSearch() {
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        UUID cacheId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f899");
        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setPartnerId(partnerId);
        cacheParamDTO.setCacheParamId(cacheId);
        cacheParamDTO.setActionType("MSP_ZONE_SEARCH");
        return new CacheParamDTO[]{cacheParamDTO};
    }


    @Test
    void testGetPartnerZoneException() {
        ZoneType areaType = ZoneType.OPERATING;
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(createMockPartnerZoneException());

        PartnerMetaDTO partnerMeta = new PartnerMetaDTO();
        partnerMeta.setPartnerId(partnerId);
        partnerMeta.setHasVehicle(true);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(partnerMeta);
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-metas"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        Exception exception = assertThrows(Exception.class, () -> partnerService.getPartnerZone(partnerId, areaType));
        String actualMessage = exception.getMessage();
        assertEquals("Index 0 out of bounds for length 0", actualMessage);

    }


    @Test
    void testGetPartnerZoneExceptionFromAdapter() {
        ZoneType areaType = ZoneType.OPERATING;
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);

        PartnerMetaDTO partnerMeta = new PartnerMetaDTO();
        partnerMeta.setPartnerId(partnerId);
        partnerMeta.setHasVehicle(true);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(partnerMeta);
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-metas"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        ResponseEntity<CacheParamDTO[]> cacheParams = ResponseEntity.status(HttpStatus.OK).body(this.createMockCacheParamsZoneSearch());

        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/cache-params"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(CacheParamDTO[].class))).thenReturn(cacheParams);

        assertThrows(NotFoundException.class, () -> partnerService.getPartnerZone(partnerId, areaType));


    }

    @Test
    void testGetPartnerZoneExceptionFromAdapter2() {
        ZoneType areaType = ZoneType.OPERATING;
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpServerErrorException adapterException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

        PartnerMetaDTO partnerMeta = new PartnerMetaDTO();
        partnerMeta.setPartnerId(partnerId);
        partnerMeta.setHasVehicle(true);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(partnerMeta);
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-metas"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(InternalException.class, () -> partnerService.getPartnerZone(partnerId, areaType));


    }


    @Test
    void testGetPartnerZoneRestRequestException() {
        ZoneType areaType = ZoneType.OPERATING;
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        RestClientException restException = new RestClientException("connection failed");

        PartnerMetaDTO partnerMeta = new PartnerMetaDTO();
        partnerMeta.setPartnerId(partnerId);
        partnerMeta.setHasVehicle(true);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(partnerMeta);
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-metas"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        ResponseEntity<CacheParamDTO[]> cacheParams = ResponseEntity.status(HttpStatus.OK).body(this.createMockCacheParamsZoneSearch());

        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/cache-params"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(CacheParamDTO[].class))).thenReturn(cacheParams);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");


        assertThrows(BadGatewayException.class, () -> partnerService.getPartnerZone(partnerId, areaType));
        assertThrows(BadGatewayException.class, () -> partnerService.getPartnerZone(partnerId, areaType));


    }


    @Test
    void testGetPartnerZoneExceptionTOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        ZoneType areaType = ZoneType.OPERATING;
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        PartnerMetaDTO partnerMeta = new PartnerMetaDTO();
        partnerMeta.setPartnerId(partnerId);
        partnerMeta.setHasVehicle(true);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(partnerMeta);
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-metas"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(createMockPartnerZoneException());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        Exception exception = assertThrows(Exception.class, () -> partnerService.getPartnerZone(partnerId, areaType));
        String actualMessage = exception.getMessage();
        assertEquals("Index 0 out of bounds for length 0", actualMessage);

    }


    @Test
    void testGetPartnerZoneExceptionFromAdapterTOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        ZoneType areaType = ZoneType.OPERATING;
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);

        PartnerMetaDTO partnerMeta = new PartnerMetaDTO();
        partnerMeta.setPartnerId(partnerId);
        partnerMeta.setHasVehicle(true);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(partnerMeta);
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-metas"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(HttpClientErrorException.class, () -> partnerService.getPartnerZone(partnerId, areaType));


    }

    @Test
    void testGetPartnerZoneExceptionFromAdapter2TOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        ZoneType areaType = ZoneType.OPERATING;
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpServerErrorException adapterException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);

        PartnerMetaDTO partnerMeta = new PartnerMetaDTO();
        partnerMeta.setPartnerId(partnerId);
        partnerMeta.setHasVehicle(true);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(partnerMeta);
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-metas"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(HttpServerErrorException.class, () -> partnerService.getPartnerZone(partnerId, areaType));


    }


    @Test
    void testGetPartnerZoneRestRequestExceptionTOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        ZoneType areaType = ZoneType.OPERATING;
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        PartnerMetaDTO partnerMeta = new PartnerMetaDTO();
        partnerMeta.setPartnerId(partnerId);
        partnerMeta.setHasVehicle(true);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(partnerMeta);
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("/partner-metas"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");


        assertThrows(RestClientException.class, () -> partnerService.getPartnerZone(partnerId, areaType));


    }


    private PartnerMetaDTO[] createMockedPartnerList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + MOCK_PARTNERS_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerMetaDTO[]>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    @Test
    void testGetStatus() {
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        UUID partnerId = UUID.randomUUID();
        doNothing().when(validityUtils).checkPartnerId(partnerId);

        partnerService.getStatus(partnerId);

        verify(restTemplate, times(1)).exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class));
    }

    @Test
    void testPing() {
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        UUID partnerId = UUID.randomUUID();
        doNothing().when(validityUtils).checkPartnerId(partnerId);
        partnerService.ping(partnerId);

        verify(restTemplate, times(1)).exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class));
    }


}

