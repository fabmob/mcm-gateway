package com.gateway.api.service.ivservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.api.ApiITTestCase;
import com.gateway.api.model.PartnerMeta;
import com.gateway.api.model.PriceList;
import com.gateway.api.service.partnerservice.impl.PartnerServiceImpl;
import com.gateway.api.util.ValidityUtils;
import com.gateway.commonapi.cache.*;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.data.*;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.cache.CacheService;
import com.gateway.commonapi.utils.enums.MSPType;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static com.gateway.commonapi.utils.enums.ActionsEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@Slf4j
class IVServiceImplTest extends ApiITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String SERVICE_EXPECTED_GET_GLOBAL_VIEW_OK_JSON = "gateway-api/mockedservices-expected/service_getGlobalView_ok.json";
    public static final String SERVICE_EXPECTED_GET_PARTNER_STATIONS_OK_JSON = "gateway-api/mockedservices-expected/service_getPartnerStations_ok.json";
    public static final String SERVICE_EXPECTED_GET_PARTNER_STATIONS_STATUS_OK_JSON = "gateway-api/mockedservices-expected/service_getPartnerStationsStatus_ok.json";
    public static final String SERVICE_EXPECTED_GET_PARTNER_ASSETS_OK_JSON = "gateway-api/mockedservices-expected/service_getPartnerAssets_ok.json";
    public static final String SERVICE_EXPECTED_GET_PARTNER_AVAILABLE_ASSETS_OK_JSON = "gateway-api/mockedservices-expected/service_getPartnerAvailableAssets_ok.json";
    public static final String SERVICE_EXPECTED_GET_VEHICLE_TYPES_OK_JSON = "gateway-api/mockedservices-expected/service_getVehicleTypes_ok.json";
    public static final String SERVICE_EXPECTED_GET_PRICE_LIST_OK_JSON = "gateway-api/mockedservices-expected/service_getPriceList_ok.json";
    public static final String SERVICE_EXPECTED_GET_DRIVER_JOURNEYS_OK_JSON = "gateway-api/expected/driverJourneys.json";
    public static final String SERVICE_EXPECTED_GET_PASSENGER_JOURNEYS_OK_JSON = "gateway-api/expected/passengerJourneys.json";
    public static final String SERVICE_EXPECTED_GET_PASSENGER_REGULAR_TRIPS_OK_JSON = "gateway-api/expected/passengerRegularTrip.json";
    public static final String SERVICE_EXPECTED_GET_DRIVER_REGULAR_TRIPS_OK_JSON = "gateway-api/expected/driverRegularTrip.json";
    public static final String GATEWAY_API_POST_MESSAGE_JSON = "gateway-api/request/postMessage.json";

    public static final String MOCK_PARTNER_ID = "14390fdf-34c1-41c9-885e-6ce66505b759";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AssetCacheManager assetCacheManager;

    @Mock
    private StationStatusCacheManager stationStatusCacheManager;

    @Mock
    private StationCacheManager stationCacheManager;

    @Mock
    private VehicleTypesCacheManager vehicleTypesCacheManager;

    @Mock
    private PriceListCacheManager priceListCacheManager;

    @Mock
    private AssetTypeCacheManager assetTypeCacheManager;

    @Mock
    private CacheService cacheService;

    @InjectMocks
    private IVServiceImpl ivService;

    @Mock
    private ValidityUtils validityUtils;

    @Mock
    private PartnerServiceImpl partnerService;

    @Mock
    private ErrorMessages errorMessages;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(cacheService.getCacheParam(any(), any())).thenReturn(new CacheParamDTO());
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);
    }

    @AfterEach
    void tearDown() {
    }


    private List<Object> createMockStations() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PARTNER_STATIONS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);

    }

    private List<PartnerMeta> createMockedPartnerList() {
        List<PartnerMeta> partners = new ArrayList<>();
        PartnerMeta partner1 = new PartnerMeta();
        UUID partnerId = UUID.fromString("a814c97e-df56-4651-ac50-11525537964f");
        partner1.setPartnerId(partnerId);
        PartnerMeta partner2 = new PartnerMeta();
        UUID partnerId2 = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        partner2.setPartnerId(partnerId2);
        partners.add(partner1);
        partners.add(partner2);

        return partners;
    }

    private PartnerMeta createMockedPartnerMeta() {
        PartnerMeta partnerMeta = new PartnerMeta();
        partnerMeta.setPartnerId(UUID.fromString("a814c97e-df56-4651-ac50-11525537964f"));
        partnerMeta.setPartnerType(PartnerTypeEnum.MSP);
        return partnerMeta;
    }

    private PartnerAroundMeRequest createMockBody() {
        PartnerAroundMeRequest request = new PartnerAroundMeRequest();
        request.setLat((float) 6.169639);
        request.setLon((float) 7.169639);
        request.setMaxResult(2);
        request.setRadius(22F);
        List<UUID> partnersID = new ArrayList<>();
        UUID partnerId1 = UUID.fromString("a814c97e-df56-4651-ac50-11525537964f");
        UUID partnerId2 = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        partnersID.add(partnerId1);
        partnersID.add(partnerId2);
        request.setPartnersIds(partnersID);
        return request;
    }

    private List<Object> createMockStationsStatus() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PARTNER_STATIONS_STATUS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);

    }

    private List<Object> createMockAsset() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PARTNER_ASSETS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private List<Object> createMockAvailableAssets() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PARTNER_AVAILABLE_ASSETS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private List<Object> createMockGlobalView() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_GLOBAL_VIEW_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private List<Object> createMockVehicleTypes() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_VEHICLE_TYPES_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private List<Object> createMockPriceList() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PRICE_LIST_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private List<Object> createMockDriverJourneys() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_DRIVER_JOURNEYS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private List<Object> createMockPassengerJourneys() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PASSENGER_JOURNEYS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private List<Object> createMockPassengerRegularTrips() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PASSENGER_REGULAR_TRIPS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private List<Object> createMockDriverRegularTrips() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_DRIVER_REGULAR_TRIPS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        return objectReader.readValue(expectedStringyfied);
    }

    private Message createMockMessageObject() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_POST_MESSAGE_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<Message>() {
        });
        return objectReader.readValue(expectedStringyfied);

    }

    @Test
    void testGetStations() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockStations());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<Station> list = ivService.getStations(partnerId, 1F, 1F, 1F);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.get(0).getPartnerId().toString());
    }

    @Test
    void testGetStationsUsingCache() {
        lenient().when(cacheService.useCache()).thenReturn(true);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        List<Station> list = new ArrayList<>();
        Station station = new Station();
        station.setContactPhone("0751388500");
        list.add(station);
        lenient().when(stationCacheManager.getAllStationFromCacheByGeoParams(partnerId, 1D, 1D, 1F, RedisGeoCommands.DistanceUnit.METERS)).thenReturn(list);

        assertEquals("0751388500", ivService.getStations(partnerId, 1F, 1F, 1F).get(0).getContactPhone());
    }

    private PartnerCallsDTO[] createMockPartnerCallsDTO() {
        List<PartnerCallsDTO> partnerCallsDTOList = new ArrayList<>();
        PartnerCallsDTO callDTO = new PartnerCallsDTO();
        callDTO.setPartnerCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        callDTO.setMethod("POST");
        callDTO.setNbCalls(1);

        ParamsDTO paramsDTO = new ParamsDTO();
        paramsDTO.setValue("null");
        paramsDTO.setKeyMapper("lat");
        Set<ParamsDTO> paramsDTOSet = new HashSet<>();
        paramsDTOSet.add(paramsDTO);

        callDTO.setParams(paramsDTOSet);
        callDTO.setUrl("https://api.abc.com/vls/v1/stations");

        partnerCallsDTOList.add(callDTO);
        return partnerCallsDTOList.toArray(PartnerCallsDTO[]::new);
    }

    private PartnerCallsDTO[] createMockPartnerCallsDTOException() {
        List<PartnerCallsDTO> partnerCallsDTOList = new ArrayList<>();
        PartnerCallsDTO callDTO = new PartnerCallsDTO();
        callDTO.setPartnerCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        callDTO.setMethod("POST");
        callDTO.setNbCalls(1);

        Set<ParamsDTO> paramsDTOSet = new HashSet<>();

        ParamsDTO paramsDTO2 = new ParamsDTO();
        paramsDTO2.setValue(null);
        paramsDTO2.setKeyMapper("lon");
        paramsDTOSet.add(paramsDTO2);

        callDTO.setParams(paramsDTOSet);
        callDTO.setUrl("https://api.abc.com/vls/v1/stations");

        partnerCallsDTOList.add(callDTO);
        return partnerCallsDTOList.toArray(PartnerCallsDTO[]::new);
    }

    private PartnerStandardDTO[] createMockPartnerStandard() {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        PartnerStandardDTO partnerStandardDTO = new PartnerStandardDTO();
        partnerStandardDTO.setPartnerActionsId(partnerActionId);
        partnerStandardDTO.setPartnerId(partnerId);
        return new PartnerStandardDTO[]{partnerStandardDTO};
    }

    @Test
    void testGetStationsUsingCacheParamsGeoNull() {
        lenient().when(cacheService.useCache()).thenReturn(true);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        String actionName = STATION_SEARCH.value;

        List<Station> list = new ArrayList<>();
        Station station = new Station();
        station.setContactPhone("0751388500");
        list.add(station);
        lenient().when(stationCacheManager.getAllStationFromCache(partnerId)).thenReturn(list);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("&partnerActionsName=" + actionName),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        assertEquals("0751388500", ivService.getStations(partnerId, null, null, null).get(0).getContactPhone());


        // coverage exceptions getCall from database
        when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenThrow(HttpClientErrorException.NotFound.class);
        assertThrows(NullPointerException.class, () -> ivService.getStations(partnerId, null, null, null));

        when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenThrow(RestClientException.class);
        assertThrows(NullPointerException.class, () -> ivService.getStations(partnerId, null, null, null));

        when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> ivService.getStations(partnerId, null, null, null));

        //coverage exceptions getStandard from database

        when(restTemplate.exchange(ArgumentMatchers.contains("&partnerActionsName=" + actionName),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenThrow(HttpClientErrorException.NotFound.class);
        assertThrows(NullPointerException.class, () -> ivService.getStations(partnerId, null, null, null));

        when(restTemplate.exchange(ArgumentMatchers.contains("&partnerActionsName=" + actionName),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenThrow(RestClientException.class);
        assertThrows(NullPointerException.class, () -> ivService.getStations(partnerId, null, null, null));

        when(restTemplate.exchange(ArgumentMatchers.contains("&partnerActionsName=" + actionName),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> ivService.getStations(partnerId, null, null, null));
    }


    @Test
    void testGetStationsUsingCacheParamsGeoNullAndRequiredForPartner() {
        lenient().when(cacheService.useCache()).thenReturn(true);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        String actionName = STATION_SEARCH.value;

        List<Station> list = new ArrayList<>();
        Station station = new Station();
        station.setContactPhone("0751388500");
        list.add(station);
        lenient().when(stationCacheManager.getAllStationFromCache(partnerId)).thenReturn(list);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("&partnerActionsName=" + actionName),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponseError = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOException());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponseError);
        assertThrows(NotFoundException.class, () -> ivService.getStations(partnerId, null, null, null));

    }

    @Test
    void testGetStationsNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        assertThrows(NotFoundException.class, () -> ivService.getStations(partnerId, null, null, null));
    }


    @Test
    void testGetStationsException() {
        lenient().when(cacheService.useCache()).thenReturn(true);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        assertThrows(UnavailableException.class, () -> ivService.getStations(partnerId, null, null, null));
    }


    @Test
    void testGetStationsExceptionFromAdapter() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(NotFoundException.class, () -> ivService.getStations(partnerId, (float) 1.1, (float) 1.1, (float) 1.1));


    }

    @Test
    void testGetStationsExceptionFromAdapter2() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpServerErrorException adapterException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(InternalException.class, () -> ivService.getStations(partnerId, (float) 1.1, (float) 1.1, (float) 1.1));


    }


    @Test
    void testGetStationsRestRequestException() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");

        assertThrows(BadGatewayException.class, () -> ivService.getStations(partnerId, (float) 1.1, (float) 1.1, (float) 1.1));


    }


    @Test
    void testGetStationsExceptionTOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        assertThrows(NullPointerException.class, () -> ivService.getStations(partnerId, null, null, null));
    }


    @Test
    void testGetStationsExceptionFromAdapterTOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(HttpClientErrorException.class, () -> ivService.getStations(partnerId, (float) 1.1, (float) 1.1, (float) 1.1));


    }

    @Test
    void testGetStationsExceptionFromAdapter2TOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpServerErrorException adapterException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("Exception");

        assertThrows(HttpServerErrorException.class, () -> ivService.getStations(partnerId, (float) 1.1, (float) 1.1, (float) 1.1));


    }


    @Test
    void testGetStationsRestRequestExceptionTOMP() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");


        assertThrows(RestClientException.class, () -> ivService.getStations(partnerId, (float) 1.1, (float) 1.1, (float) 1.1));


    }


    @Test
    void testGetStationsStatus() throws IOException {
        UUID partnerId = UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598");
        String stationId = "sta1";

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockStationsStatus());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<StationStatus> list = ivService.getStationStatus(partnerId, stationId);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.get(0).getPartnerId().toString());

    }

    @Test
    void testGetStationsStatusUsingCache() {

        lenient().when(cacheService.useCache()).thenReturn(true);
        UUID partnerId = UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598");
        String stationId = "sta1";

        List<StationStatus> list = new ArrayList<>();
        StationStatus stationStatus = new StationStatus();
        stationStatus.setIsInstalled(true);
        list.add(stationStatus);
        lenient().when(stationStatusCacheManager.getAllStationStatusFromCache(partnerId, stationId)).thenReturn(list);

        assertEquals(true, ivService.getStationStatus(partnerId, stationId).get(0).getIsInstalled());
    }

    @Test
    void testGetStationsCacheException() {
        lenient().when(cacheService.useCache()).thenReturn(true);
        when(stationStatusCacheManager.getAllStationStatusFromCache(null, null)).thenThrow(InternalException.class);
        assertThrows(NullPointerException.class, () -> ivService.getStationStatus(null, null));
    }

    @Test
    void testGetStationsStatusException() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        assertThrows(UnavailableException.class, () -> ivService.getStationStatus(partnerId, null));
    }

    @Test
    void testGetStationsStatusNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        assertThrows(NotFoundException.class, () -> ivService.getStationStatus(partnerId, null));
    }


    @Test
    void testGetAssets() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockAsset());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<Asset> list = ivService.getAssets(partnerId);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.get(0).getPartnerId().toString());

    }


    @Test
    void testGetAssetsUsingCache() {
        lenient().when(cacheService.useCache()).thenReturn(true);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        List<Asset> assets = new ArrayList<>();
        Asset asset = new Asset();
        asset.setAssetType("VELO");
        assets.add(asset);

        lenient().when(assetCacheManager.getAllAssetFromCache(partnerId)).thenReturn(assets);
        assertEquals("VELO", ivService.getAssets(partnerId).get(0).getAssetType());

        //Exception case
        lenient().when(assetCacheManager.getAllAssetFromCache(partnerId)).thenThrow(new InternalException("problem"));
        assertThrows(InternalException.class, () -> ivService.getAssets(partnerId));
    }


    @Test
    void testGetAssetsException() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        assertThrows(UnavailableException.class, () -> ivService.getAssets(partnerId));
    }

    @Test
    void testGetAssetsNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        assertThrows(NotFoundException.class, () -> ivService.getAssets(partnerId));
    }

    @Test
    void testGetAvailableAssets() throws IOException {
        UUID partnerId = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        String stationId = "stationId 1";

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockAvailableAssets());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<AssetType> list = ivService.getAvailableAssets(partnerId, stationId, 1F, 1F, 1F);
        assertEquals("b814c97e-df56-4651-ac50-11525537964f", list.get(0).getPartnerId().toString());
    }

    @Test
    void testGetAvailableAssetsUsingCache() {
        lenient().when(cacheService.useCache()).thenReturn(true);
        UUID partnerId = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        String stationId = "stationId 1";

        List<AssetType> assetsType = new ArrayList<>();
        AssetType assetType = new AssetType();
        assetType.setType(MSPType.AUTOPARTAGE);
        assetType.setAssetTypeId("assetTypeId123");
        assetsType.add(assetType);
        lenient().when(assetTypeCacheManager.getAllAssetTypeFromCacheByGeoParams(partnerId, stationId, 1D, 1D, 1F, RedisGeoCommands.DistanceUnit.METERS)).thenReturn(assetsType);

        assertEquals("assetTypeId123", ivService.getAvailableAssets(partnerId, stationId, 1F, 1F, 1F).get(0).getAssetTypeId());
    }


    @Test
    void testGetAvailableAssetsUsingCacheException() {
        lenient().when(cacheService.useCache()).thenReturn(true);
        UUID partnerId = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        String stationId = "stationId 1";
        String actionName = AVAILABLE_ASSET_SEARCH.value;

        List<AssetType> list = new ArrayList<>();
        AssetType assetType = new AssetType();
        assetType.setType(MSPType.AUTOPARTAGE);
        list.add(assetType);


        lenient().when(assetTypeCacheManager.getAllAssetTypeFromCache(partnerId, stationId)).thenReturn(list);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("&partnerActionsName=" + actionName),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        assertEquals(MSPType.AUTOPARTAGE, ivService.getAvailableAssets(partnerId, stationId, null, null, null).get(0).getType());
    }


    @Test
    void testGetAvailableAssetsNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        String stationId = "stationId 1";

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        assertThrows(NotFoundException.class, () -> ivService.getAvailableAssets(partnerId, stationId, null, null, null));
    }

    @Test
    void testGetAvailableAssetsException() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        String stationId = "stationId 1";
        when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        assertThrows(UnavailableException.class, () -> ivService.getAvailableAssets(partnerId, stationId, null, null, null));
    }

    @Test
    void testGetGlobalView() throws IOException {
        List<PartnerMeta> mspListMocked = createMockedPartnerList();
        lenient().when(partnerService.getPartnersMetaByPartnerType(PartnerTypeEnum.MSP, null)).thenReturn(mspListMocked);

        ResponseEntity<Object> stations = ResponseEntity.status(HttpStatus.OK).body(this.createMockStations());
        ResponseEntity<Object> stationStatus = ResponseEntity.status(HttpStatus.OK).body(this.createMockStationsStatus());
        ResponseEntity<Object> assets = ResponseEntity.status(HttpStatus.OK).body(this.createMockAsset());
        ResponseEntity<Object> aroundMe = ResponseEntity.status(HttpStatus.OK).body(this.createMockGlobalView());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(STATION_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(stations);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(STATION_STATUS_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(stationStatus);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(ASSET_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(assets);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(AROUND_ME_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(aroundMe);

        GlobalView list = ivService.getGlobalView();
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.getStations().get(0).getPartnerId().toString());
    }

    @Test
    void testGetAroundMe() throws IOException {
        List<UUID> partnerIds = List.of((UUID.fromString(MOCK_PARTNER_ID)), (UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b735")), (UUID.fromString("14390fdf-34c1-41c9-885e-6ce669264483")));
        PartnerAroundMeRequest partnerAroundMeRequest = new PartnerAroundMeRequest((float) 6.169639, (float) 52.253279, 100F, 200, partnerIds);

        lenient().when(partnerService.getPartnerMeta(any())).thenReturn(this.createMockedPartnerMeta());

        ResponseEntity<Object> stations = ResponseEntity.status(HttpStatus.OK).body(this.createMockStations());
        ResponseEntity<Object> stationStatus = ResponseEntity.status(HttpStatus.OK).body(this.createMockStationsStatus());
        ResponseEntity<Object> assets = ResponseEntity.status(HttpStatus.OK).body(this.createMockAsset());
        ResponseEntity<Object> aroundMe = ResponseEntity.status(HttpStatus.OK).body(this.createMockGlobalView());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(STATION_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(stations);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(STATION_STATUS_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(stationStatus);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(ASSET_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(assets);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(AROUND_ME_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(aroundMe);


        GlobalView list = ivService.getAroundMe(partnerAroundMeRequest);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.getStations().get(0).getPartnerId().toString());
    }

    @Test
    void testGetAroundMeExceptionParams() throws IOException {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        List<UUID> partnerIds = List.of((UUID.fromString(MOCK_PARTNER_ID)), (UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b735")), (UUID.fromString("14390fdf-34c1-41c9-885e-6ce669264483")));
        PartnerAroundMeRequest partnerAroundMeRequest = new PartnerAroundMeRequest((float) 669639, (float) 5253279, -100F, -200, partnerIds);

        assertThrows(BadRequestException.class, () -> ivService.getAroundMe(partnerAroundMeRequest));

    }

    @Test
    void testGetAroundMeEmptyPartnerIdsList() throws IOException {
        List<UUID> partnerIds = new ArrayList<>();
        PartnerAroundMeRequest partnerAroundMeRequest = new PartnerAroundMeRequest((float) 6.169639, (float) 52.253279, 100F, 200, partnerIds);

        lenient().when(partnerService.getPartnersMetaByPartnerType(PartnerTypeEnum.MSP, null)).thenReturn(List.of(this.createMockedPartnerMeta()));
        lenient().when(partnerService.getPartnerMeta(any())).thenReturn(this.createMockedPartnerMeta());

        ResponseEntity<Object> stations = ResponseEntity.status(HttpStatus.OK).body(this.createMockStations());
        ResponseEntity<Object> stationStatus = ResponseEntity.status(HttpStatus.OK).body(this.createMockStationsStatus());
        ResponseEntity<Object> assets = ResponseEntity.status(HttpStatus.OK).body(this.createMockAsset());
        ResponseEntity<Object> aroundMe = ResponseEntity.status(HttpStatus.OK).body(this.createMockGlobalView());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(STATION_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(stations);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(STATION_STATUS_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(stationStatus);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(ASSET_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(assets);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(AROUND_ME_SEARCH.value),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(aroundMe);


        GlobalView list = ivService.getAroundMe(partnerAroundMeRequest);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.getStations().get(0).getPartnerId().toString());
    }

    @Test
    void testGetAroundMeExp() {
        List<UUID> partnerIds = List.of((UUID.fromString(MOCK_PARTNER_ID)), (UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b735")), (UUID.fromString("14390fdf-34c1-41c9-885e-6ce669264483")));
        PartnerAroundMeRequest partnerAroundMeRequest = new PartnerAroundMeRequest((float) 6.169639, (float) 52.253279, 100F, 200, partnerIds);
        lenient().when(partnerService.getPartnerMeta(any())).thenReturn(this.createMockedPartnerMeta());
        when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        assertThrows(InternalException.class, () -> ivService.getAroundMe(partnerAroundMeRequest));
    }

    @Test
    void testGetVehicleTypes() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockVehicleTypes());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<VehicleTypes> list = ivService.getVehicleTypes(partnerId);
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", list.get(0).getVehicleTypeId());
    }

    @Test
    void testGetVehicleTypesWhenIdIsWrong() throws IOException {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.TOMP_1_3_0);
        UUID partnerId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        when(partnerService.getPartnerMeta(partnerId)).thenThrow(BadRequestException.class);
        assertThrows(NullPointerException.class, () -> ivService.getVehicleTypes(partnerId));
    }

    @Test
    void testGetVehicleTypesUsingCache() throws IOException {
        lenient().when(cacheService.useCache()).thenReturn(true);

        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        List<VehicleTypes> list = new ArrayList<>();
        VehicleTypes vehicleTypes = new VehicleTypes();
        vehicleTypes.setColor("red");
        list.add(vehicleTypes);
        lenient().when(vehicleTypesCacheManager.getAllVehicleTypesFromCache(partnerId)).thenReturn(list);

        assertEquals("red", ivService.getVehicleTypes(partnerId).get(0).getColor());
    }

    @Test
    void testGetVehicleTypesCacheException() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        lenient().when(cacheService.useCache()).thenReturn(true);
        lenient().when(vehicleTypesCacheManager.getAllVehicleTypesFromCache(partnerId)).thenThrow(InternalException.class);
        assertThrows(NullPointerException.class, () -> ivService.getVehicleTypes(partnerId));
    }


    @Test
    void testGetVehicleTypesNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).build();
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        assertThrows(NotFoundException.class, () -> ivService.getVehicleTypes(partnerId));
    }

    @Test
    void testGetPricingPlan() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        String stationId = "station 1";

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockPriceList());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<PriceList> list = ivService.getPricingPlan(partnerId, stationId);
        assertEquals("comment", list.get(0).getComment());
    }

    @Test
    void testGetPricingPlanUsingCache() {
        lenient().when(cacheService.useCache()).thenReturn(true);
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        String stationId = "station 1";

        List<PriceListDTO> list = new ArrayList<>();
        PriceListDTO priceList = new PriceListDTO();
        priceList.setComment("price");
        list.add(priceList);
        lenient().when(priceListCacheManager.getAllPriceListFromCache(partnerId, stationId)).thenReturn(list);

        assertEquals("price", ivService.getPricingPlan(partnerId, stationId).get(0).getComment());
    }


    @Test
    void testGetPricingPlanNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        String stationId = "station 1";
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        assertThrows(NotFoundException.class, () -> ivService.getPricingPlan(partnerId, stationId));
    }

    @Test
    void testDriverJourneysWhenIdIsWrong() throws IOException {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.COVOITURAGE_STANDARD);
        float departureLat = 5;
        float departureLng = 5;
        float arrivalLat = 5;
        float arrivalLng = 5;
        Integer departureDate = 115;
        Integer timeDelta = 115;
        float departureRadius = 1;
        float arrivalRadius = 2;
        Integer count = 3;
        UUID partnerId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        when(partnerService.getPartnerMeta(partnerId)).thenThrow(BadRequestException.class);
        assertThrows(NullPointerException.class, () ->
                ivService.getDriverJourneys(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count));
    }

    @Test
    void testDriverJourneysWhenParamsAreWrong() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.COVOITURAGE_STANDARD);
        float departureLat = 5;
        float departureLng = 5;
        float arrivalLat = 5;
        float arrivalLng = 5;
        Integer departureDate = -115;
        Integer timeDelta = 115;
        float departureRadius = 1;
        float arrivalRadius = -2;
        Integer count = 3;
        UUID partnerId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        when(partnerService.getPartnerMeta(partnerId)).thenReturn(new PartnerMeta());
        assertThrows(BadRequestException.class, () ->
                ivService.getDriverJourneys(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count));
    }

    @Test
    void testGetDriverJourneys() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 5;
        float departureLng = 5;
        float arrivalLat = 5;
        float arrivalLng = 5;
        Integer departureDate = 115;
        Integer timeDelta = 115;
        float departureRadius = 1;
        float arrivalRadius = 2;
        Integer count = 3;
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockDriverJourneys());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        List<DriverJourney> list = ivService.getDriverJourneys(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count);
        assertEquals("carpool.mycity.com", list.get(0).getOperator());
    }

    @Test
    void testGetDriverJourneysNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 5;
        float departureLng = 5;
        float arrivalLat = 5;
        float arrivalLng = 5;
        Integer departureDate = 115;
        Integer timeDelta = 115;
        float departureRadius = 1;
        float arrivalRadius = 2;
        Integer count = 3;
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        assertThrows(NotFoundException.class, () -> ivService.getDriverJourneys(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count));
    }

    @Test
    void testGetPassengerJourneys() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 5;
        float departureLng = 5;
        float arrivalLat = 5;
        float arrivalLng = 5;
        Integer departureDate = 115;
        Integer timeDelta = 115;
        float departureRadius = 1;
        float arrivalRadius = 2;
        Integer count = 3;
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockPassengerJourneys());
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        List<PassengerJourney> list = ivService.getPassengerJourneys(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count);
        assertEquals("carpool.mycity.com", list.get(0).getOperator());
    }

    @Test
    void testGetPassengerJourneysNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 5;
        float departureLng = 5;
        float arrivalLat = 5;
        float arrivalLng = 5;
        Integer departureDate = 115;
        Integer timeDelta = 115;
        float departureRadius = 1;
        float arrivalRadius = 2;
        Integer count = 3;
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        assertThrows(NotFoundException.class, () -> ivService.getPassengerJourneys(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count));
    }


    @Test
    void testGetPassengerRegularTrips() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 3;
        float departureLng = 6;
        float arrivalLat = 6;
        float arrivalLng = 6;
        String departureTimeOfDay = "11:12:34";
        List<String> departureWeekdays = new ArrayList<>();
        Integer timeDelta = 6;
        float departureRadius = 6;
        float arrivalRadius = 6;
        Integer minDepartureDate = 6;
        Integer maxDepartureDate = 6;
        Integer count = 6;
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockPassengerRegularTrips());

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<PassengerRegularTrip> passengerRegularTripsList = ivService.getPassengerRegularTrips(partnerId, departureLat,
                departureLng, arrivalLat, arrivalLng,
                departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count);

        assertEquals("carpool.mycity.com", passengerRegularTripsList.get(0).getOperator());
    }

    @Test
    void testGetPassengerRegularTripsResponseNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 3;
        float departureLng = 6;
        float arrivalLat = 6;
        float arrivalLng = 6;
        String departureTimeOfDay = "11:32:18";
        List<String> departureWeekdays = new ArrayList<>();
        Integer timeDelta = 6;
        float departureRadius = 6;
        float arrivalRadius = 6;
        Integer minDepartureDate = 6;
        Integer maxDepartureDate = 6;
        Integer count = 6;

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        assertThrows(NullPointerException.class, () -> ivService.getPassengerRegularTrips(partnerId, departureLat,
                departureLng, arrivalLat, arrivalLng,
                departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count));
    }

    @Test
    void testGetDriverRegularTrips() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 3;
        float departureLng = 6;
        float arrivalLat = 6;
        float arrivalLng = 6;
        String departureTimeOfDay = "11:01:01";
        List<String> departureWeekdays = new ArrayList<>();
        Integer timeDelta = 6;
        float departureRadius = 6;
        float arrivalRadius = 6;
        Integer minDepartureDate = 6;
        Integer maxDepartureDate = 6;
        Integer count = 6;
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.createMockDriverRegularTrips());

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<DriverRegularTrip> passengerRegularTripsList = ivService.getDriverRegularTrips(partnerId, departureLat,
                departureLng, arrivalLat, arrivalLng,
                departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count);

        assertEquals("carpool.mycity.com", passengerRegularTripsList.get(0).getOperator());
    }


    @Test
    void testGetDriverRegularTripsResponseNull() {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 3;
        float departureLng = 6;
        float arrivalLat = 6;
        float arrivalLng = 6;
        String departureTimeOfDay = "11:09:11";
        List<String> departureWeekdays = new ArrayList<>();
        Integer timeDelta = 6;
        float departureRadius = 6;
        float arrivalRadius = 6;
        Integer minDepartureDate = 6;
        Integer maxDepartureDate = 6;
        Integer count = 6;

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        assertThrows(NullPointerException.class, () -> ivService.getDriverRegularTrips(partnerId, departureLat,
                departureLng, arrivalLat, arrivalLng,
                departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count));
    }


    @Test
    void testPostMessage() throws IOException {
        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.created(null).build();

        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        ivService.postMessage(partnerId, this.createMockMessageObject());

        verify(restTemplate, times(1)).exchange(
                ArgumentMatchers.contains("partnerId"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class));

    }
}
