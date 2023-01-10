package com.gateway.cachemanager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.cachemanager.CacheManagerITTestCase;
import com.gateway.cachemanager.model.PositionsRequest;
import com.gateway.commonapi.cache.*;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.dto.data.GatewayParamsDTO;
import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.cache.CacheService;
import com.gateway.commonapi.utils.enums.ActionsEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static com.gateway.commonapi.constants.DataApiPathDict.GATEWAY_PARAMS_BASE_PATH;
import static com.gateway.commonapi.constants.GatewayApiPathDict.PARTNER_META_ENDPOINT;
import static com.gateway.commonapi.constants.GlobalConstants.CACHE_ACTIVATION;
import static com.gateway.commonapi.utils.enums.ActionsEnum.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;


class CacheManagerServiceTest extends CacheManagerITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/test/resources/";
    public static final String CACHE_POSITIONS_REQUEST_JSON = "request/requestPositions.json";



    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    CacheManagerServiceImpl cacheManagerService;

    @Mock
    private PartnerMetaCacheManager partnerMetaCache;

    @Mock
    AssetCacheManager assetCacheManager;

    @Mock
    AssetTypeCacheManager assetTypeCacheManager;

    @Mock
    VehicleTypesCacheManager vehicleTypesCacheManager;

    @Mock
    StationStatusCacheManager stationStatusCacheManager;

    @Mock
    StationCacheManager stationCacheManager;

    @Mock
    PriceListCacheManager priceListCacheManager;

    @Mock
    ParkingCacheManager parkingCacheManager;

    @Mock
    private ZoneCacheManager zoneCacheManager;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ErrorMessages errorMessages;

    @Mock
    private CacheService cacheService;

    @Mock
    private GatewayParamStatusManager cacheUtil;

    @Value("${spring.redis.keys.prefix:local}")
    private String keyPrefixEnv;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testClearCache() {
        Assertions.assertDoesNotThrow(() -> cacheManagerService.clearCache(null));
        List<UUID> ids = new ArrayList<>();
        ids.add(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"));
        Assertions.assertDoesNotThrow(() -> cacheManagerService.clearCache(ids));
    }

    @Test
    void testGetCacheStatus() {
        Mockito.when(cacheUtil.getCacheStatus()).thenReturn(true);
        Assertions.assertTrue(cacheManagerService.getCacheStatus());
    }

    @Test
    void testPutCacheStatus() {

        GatewayParamsDTO gatewayParamsDTO = new GatewayParamsDTO(CACHE_ACTIVATION, "true");
        ResponseEntity<GatewayParamsDTO> response = ResponseEntity.status(HttpStatus.OK).body(gatewayParamsDTO);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.contains(GATEWAY_PARAMS_BASE_PATH),
                ArgumentMatchers.eq(HttpMethod.PUT),
                any(),
                ArgumentMatchers.eq(GatewayParamsDTO.class))).thenReturn(response);

        Mockito.when(cacheUtil.getCacheStatus()).thenReturn(true);
        Assertions.assertTrue(cacheManagerService.putCacheStatus(true));


    }

    @Test
    void testPutCacheStatusUpdateRestException() {

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.endsWith(CACHE_ACTIVATION),
                ArgumentMatchers.eq(HttpMethod.PUT),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(RestClientException.class);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        Assertions.assertThrows(BadGatewayException.class, () -> cacheManagerService.putCacheStatus(false));

    }

    @Test
    void testPutCacheStatusUpdateException() {

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.endsWith(CACHE_ACTIVATION),
                ArgumentMatchers.eq(HttpMethod.PUT),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(InternalException.class);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        Assertions.assertThrows(UnavailableException.class, () -> cacheManagerService.putCacheStatus(false));

    }

    @Test
    void testPutCacheStatusCreateHttpException() {

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.endsWith(CACHE_ACTIVATION),
                ArgumentMatchers.eq(HttpMethod.PUT),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(HttpClientErrorException.NotFound.class);


        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.contains(GATEWAY_PARAMS_BASE_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(GatewayParamsDTO.class))).thenThrow(HttpClientErrorException.NotFound.class);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        Assertions.assertThrows(NullPointerException.class, () -> cacheManagerService.putCacheStatus(false));

    }

    @Test
    void testPutCacheStatusCreateRestException() {

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.endsWith(CACHE_ACTIVATION),
                ArgumentMatchers.eq(HttpMethod.PUT),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(HttpClientErrorException.NotFound.class);


        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.contains(GATEWAY_PARAMS_BASE_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(GatewayParamsDTO.class))).thenThrow(RestClientException.class);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        Assertions.assertThrows(BadGatewayException.class, () -> cacheManagerService.putCacheStatus(false));

    }

    @Test
    void testPutCacheStatusCreateException() {

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.endsWith(CACHE_ACTIVATION),
                ArgumentMatchers.eq(HttpMethod.PUT),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(HttpClientErrorException.NotFound.class);


        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.contains(GATEWAY_PARAMS_BASE_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(GatewayParamsDTO.class))).thenThrow(InternalException.class);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        Assertions.assertThrows(UnavailableException.class, () -> cacheManagerService.putCacheStatus(false));

    }

    @Test
    void testRefreshPartners() {

        PartnerMetaDTO[] partnerMetaDTOS = new PartnerMetaDTO[0];
        ResponseEntity<PartnerMetaDTO[]> response = ResponseEntity.status(HttpStatus.OK).body(partnerMetaDTOS);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains(PARTNER_META_ENDPOINT),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO[].class))).thenReturn(response);

        Assertions.assertDoesNotThrow(() -> cacheManagerService.refreshPartners());

        Mockito.verify(partnerMetaCache, times(1)).clearCache("getMeta:*");
        Mockito.verify(partnerMetaCache, times(1)).populateCache(any());


    }

    @Test
    void testRefreshPartnersHttpClientErrorException() {

        HttpClientErrorException exception = HttpClientErrorException.create("error", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), null, null, null);
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.endsWith(PARTNER_META_ENDPOINT),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO[].class))).thenThrow(exception);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        Assertions.assertThrows(NotFoundException.class, () -> cacheManagerService.refreshPartners());

    }

    @Test
    void testRefreshPartnersRestClientException() {

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.endsWith(PARTNER_META_ENDPOINT),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO[].class))).thenThrow(RestClientException.class);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        Assertions.assertThrows(BadGatewayException.class, () -> cacheManagerService.refreshPartners());

    }

    @Test
    void testRefreshPartnersException() {

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.endsWith(PARTNER_META_ENDPOINT),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO[].class))).thenThrow(InternalException.class);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        Assertions.assertThrows(UnavailableException.class, () -> cacheManagerService.refreshPartners());

    }

    @Test
    void testRefresh() {

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setHardTTL(3600);
        lenient().when(cacheService.getCacheParam(any(), any())).thenReturn(cacheParamDTO);

        List<Object> emptyList = Collections.emptyList();
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(emptyList);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("actionName"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);


        ArrayList<ActionsEnum> validActions = new ArrayList<>(Arrays.asList(ASSET_SEARCH, PARKING_SEARCH, STATION_SEARCH, PRICING_PLAN_SEARCH, VEHICLE_TYPES_SEARCH, STATION_STATUS_SEARCH, AVAILABLE_ASSET_SEARCH, MSP_ZONE_SEARCH));
        UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        for (ActionsEnum action : ActionsEnum.values()) {
            if (!validActions.contains(action)) {
                Assertions.assertThrows(BadRequestException.class, () -> cacheManagerService.refresh(id, action, null));
            } else {
                Assertions.assertDoesNotThrow(() -> cacheManagerService.refresh(id, action, null));
            }

        }


    }

    @Test
    void testRefreshWithPositions() throws IOException {

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setHardTTL(3600);
        lenient().when(cacheService.getCacheParam(any(), any())).thenReturn(cacheParamDTO);

        List<Object> emptyList = Collections.emptyList();
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(emptyList);
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("actionName"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + CACHE_POSITIONS_REQUEST_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PositionsRequest>() {
        });
        PositionsRequest positionsRequest = objectReader.readValue(mockStringyfied);

        Assertions.assertDoesNotThrow(() -> cacheManagerService.refresh(UUID.fromString("b814c97e-df56-4651-ac50-11525537964f"), ASSET_SEARCH, positionsRequest.getPositions()));

        Mockito.verify(restTemplate, times(2)).exchange(ArgumentMatchers.contains("actionName"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class));


    }

    @Test
    void testRefreshHttpClientErrorException() {

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setHardTTL(3600);
        lenient().when(cacheService.getCacheParam(any(), any())).thenReturn(cacheParamDTO);

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "error");
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.contains("actionName"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(exception);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        Assertions.assertThrows(NotFoundException.class, () -> cacheManagerService.refresh(id, ASSET_SEARCH, null));


    }

    @Test
    void testRefreshHttpServerErrorException() {

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setHardTTL(3600);
        lenient().when(cacheService.getCacheParam(any(), any())).thenReturn(cacheParamDTO);

        HttpServerErrorException exception = new HttpServerErrorException(HttpStatus.NOT_FOUND, "error");
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.contains("actionName"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(exception);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        Assertions.assertThrows(InternalException.class, () -> cacheManagerService.refresh(id, ASSET_SEARCH, null));


    }

    @Test
    void testRefreshRestClientException() {

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setHardTTL(3600);
        lenient().when(cacheService.getCacheParam(any(), any())).thenReturn(cacheParamDTO);

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.contains("actionName"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(RestClientException.class);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        Assertions.assertThrows(BadGatewayException.class, () -> cacheManagerService.refresh(id, ASSET_SEARCH, null));


    }

    @Test
    void testRefreshException() {

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setHardTTL(3600);
        lenient().when(cacheService.getCacheParam(any(), any())).thenReturn(cacheParamDTO);

        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.contains("actionName"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(InternalException.class);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("error");

        UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        Assertions.assertThrows(UnavailableException.class, () -> cacheManagerService.refresh(id, ASSET_SEARCH, null));


    }

}
