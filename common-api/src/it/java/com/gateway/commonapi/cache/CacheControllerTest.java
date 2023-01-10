package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.commonapi.ApiITTestCase;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.api.geojson.Coordinates;
import com.gateway.commonapi.dto.api.geojson.Point;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.dto.data.GatewayParamsDTO;
import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import com.gateway.commonapi.dto.data.PriceListDTO;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.enums.ZoneStatus;
import com.gateway.commonapi.utils.enums.ZoneType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gateway.commonapi.cache.CacheUtil.ONE_DAY_IN_SECONDS;
import static com.gateway.commonapi.constants.GlobalConstants.CACHE_ACTIVATION;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@Slf4j
public class CacheControllerTest extends ApiITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/mock/";
    /*
     * Mock MVC
     */
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PartnerMetaCacheManager partnerCacheManager;

    @Autowired
    private ZoneCacheManager zoneCacheManager;

    @Autowired
    private VehicleTypesCacheManager vehicleTypesCacheManager;

    @Autowired
    private AssetTypeCacheManager assetTypeCacheManager;

    @Autowired
    private AssetCacheManager assetCacheManager;

    @Autowired
    private PriceListCacheManager priceListCacheManager;

    @Autowired
    private AroundMeCacheManager aroundMeCacheManager;

    @Autowired
    private StationCacheManager stationCacheManager;

    @Autowired
    private StationStatusCacheManager stationStatusCacheManager;

    @Autowired
    private ParkingCacheManager parkingCacheManager;

    @Autowired
    @InjectMocks
    private GatewayParamStatusManager gatewayParamStatusManager;

    @Autowired
    private CacheUtil cacheUtil;

    @Value("${gateway.service.dataapi.baseUrl}")
    private String uri;


    /*
     * Web Application Context
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build();
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, this.objectMapper);
    }


    /**
     * Load a list of Asset from json mock file
     * Method that should be removed when real data will be unmocked.
     *
     * @return
     */
    private List<Asset> createMockAssets() {

        try {
            String mockStringyfied = WsTestUtil.readJsonFromFilePath("./src/it/resources/mock/assets-mock.json");
            if (mockStringyfied.isBlank()) {
                log.error("Content file is empty or file not found");
            }
            ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Asset>>() {
            });
            return objectReader.readValue(mockStringyfied);
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }


    /**
     * Load a list of Asset from json mock file
     * Method that should be removed when real data will be unmocked.
     *
     * @return
     */
    private List<Station> createMockStations() {

        try {
            String mockStringyfied = WsTestUtil.readJsonFromFilePath("./src/it/resources/mock/stations-mock.json");
            if (mockStringyfied.isBlank()) {
                log.error("Content file is empty or file not found");
            }
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Station>>() {
            });
            return objectReader.readValue(mockStringyfied);
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }


    /**
     * Load a list of Asset from json mock file
     * Method that should be removed when real data will be unmocked.
     *
     * @return
     */
    private List<StationStatus> createMockStationStatus() {

        try {
            String mockStringyfied = WsTestUtil.readJsonFromFilePath("./src/it/resources/mock/stations-status-mock.json");
            if (mockStringyfied.isBlank()) {
                log.error("Content file is empty or file not found");
            }
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
            ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<StationStatus>>() {
            });
            return objectReader.readValue(mockStringyfied);
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }


    /**
     * Load a list of Asset from json mock file
     * Method that should be removed when real data will be unmocked.
     *
     * @return
     */
    private List<Parking> createMockParkings() {
        List<Parking> parkings = new ArrayList<>();
        Parking parking = new Parking();
        parking.setParkingId("id_parking_123");
        Point location = new Point(1.444209F, 43.604652F);
        parking.setLocation(location);
        parking.setPartnerId(UUID.fromString("00000000-0000-0000-0000-000000000004"));
        parkings.add(parking);
        return parkings;
    }

    private List<Asset> createMockAssetsWithoutGeoParams() {
        List<Asset> assets = new ArrayList<>();
        Asset asset = new Asset();
        asset.setAssetId("velo1");
        asset.setPartnerId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assets.add(asset);
        return assets;
    }

    @Test
    public void testAsset() throws Exception {

        UUID partnerId = UUID.fromString("00000000-0000-0000-0000-000000000001");

        // clear cache
        assetCacheManager.clearCache("*");

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setPartnerId(partnerId);
        cacheParamDTO.setHardTTL(700);

        assetCacheManager.populateCache(this.createMockAssets(), cacheParamDTO);
        assetCacheManager.getFromCache(partnerId);
        List<Asset> assetsCache = assetCacheManager.getAllAssetFromCache(partnerId);

        assertEquals("velo1", assetsCache.get(0).getAssetId());


    }


    @Test
    public void testStationStatus() throws Exception {

        UUID partnerId = UUID.fromString("00000000-0000-0000-0000-000000000002");
        String stationId = "ST400";

        // clear cache
        stationStatusCacheManager.clearCache("*");

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setPartnerId(partnerId);
        cacheParamDTO.setHardTTL(700);

        stationStatusCacheManager.populateStationStatusCache(this.createMockStationStatus(), cacheParamDTO);

        stationStatusCacheManager.getStationStatusFromCache(partnerId);
        List<StationStatus> stationStatusesCache = stationStatusCacheManager.getAllStationStatusFromCache(partnerId, stationId);

        assertEquals("https://station/asset/book", stationStatusesCache.get(0).getBookDeeplink());

    }

    /**
     * test GET on partnerMetas
     *
     * @throws Exception
     */
    @Test
    public void testGetAroundMe() throws Exception {
        PartnerMetaDTO partnerMetaDTO;

        UUID partnerId1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID partnerId2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
        UUID partnerId3 = UUID.fromString("00000000-0000-0000-0000-000000000003");
        UUID partnerId4 = UUID.fromString("00000000-0000-0000-0000-000000000004");

        partnerCacheManager.clearCache("*");
        aroundMeCacheManager.clearCache("*");
        stationCacheManager.clearCache("*");
        parkingCacheManager.clearCache("*");
        stationStatusCacheManager.clearCache("*");

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setPartnerId(partnerId1);
        cacheParamDTO.setHardTTL(700);

        assetCacheManager.populateAssetCacheGeoData(this.createMockAssets(), partnerId1, cacheParamDTO);
        stationCacheManager.populateStationsCache(this.createMockStations(), cacheParamDTO);
        stationStatusCacheManager.populateStationStatusCache(this.createMockStationStatus(), cacheParamDTO);
        parkingCacheManager.populateParkingCacheGeoData(this.createMockParkings(), partnerId4, cacheParamDTO);

        partnerMetaDTO = partnerCacheManager.getFromCache(partnerId1);
        log.info("ELEMENT FROM GET cache:{}", partnerMetaDTO);
        List<UUID> partnerIds = Stream.of(partnerId1, partnerId2, partnerId3, partnerId4).collect(Collectors.toList());

        stationCacheManager.getStationFromCache(partnerId1);
        parkingCacheManager.getFromCache(partnerId2);

        Point fakeUserLoc = new Point(1.444209, 43.604652);
        log.info("gonna search {} with radius {} meters", fakeUserLoc, 20000);

        GlobalView globalview = aroundMeCacheManager.searchElementsFromGeoCache(partnerIds,
                fakeUserLoc.getLatitude(), fakeUserLoc.getLongitude(), 20000F,
                RedisGeoCommands.DistanceUnit.METERS);

        assertEquals(5, globalview.getAssets().size());

        assertEquals(1, globalview.getStations().size());

        assertEquals(1, globalview.getStationsStatus().size());

        assertEquals(1, globalview.getParkings().size());

        log.info("gonna search {} with radius {} meters", fakeUserLoc, 200);
        globalview = aroundMeCacheManager.searchElementsFromGeoCache(partnerIds,
                fakeUserLoc.getLatitude(), fakeUserLoc.getLongitude(), 200F,
                RedisGeoCommands.DistanceUnit.METERS);

        assertEquals(2, globalview.getAssets().size());

        // Exception cases
        assertThrows(InternalException.class, () -> stationCacheManager.getAllStationFromCacheByGeoParams(partnerId1, null, null, null, RedisGeoCommands.DistanceUnit.METERS));
        assertThrows(InternalException.class, () -> parkingCacheManager.getAllParkingsFromCacheByGeoParams(partnerId1, null, null, null, RedisGeoCommands.DistanceUnit.METERS));
        assertThrows(InternalException.class, () -> assetCacheManager.getAllAssetsFromCacheByGeoParams(partnerId1, null, null, null, RedisGeoCommands.DistanceUnit.METERS));


    }


    @Test
    public void testGetPartnerMetas() throws Exception {
        PartnerMetaDTO partnerMetaDTO;

        UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");

        List<PartnerMetaDTO> partnerMetas = new ArrayList<>();
        partnerCacheManager.populateCache(this.createPartnerMetas());
        log.info("POPULATE CACHE");
        partnerMetaDTO = partnerCacheManager.getFromCache(id);
        long startList = System.currentTimeMillis();
        log.info("{} elements found", partnerCacheManager.getAllPartnersFromCache().size());
        long finishList = System.currentTimeMillis();
        long timeElapsedList = finishList - startList;
        log.info("timeElapsed getting from list cache = {} ms", timeElapsedList);


        List<PartnerMetaDTO> partners = partnerCacheManager.getAllPartnersFromCache();
        assertEquals(2, partners.size());

    }


    @Test
    public void testGetPartnerZone() throws Exception {
        PartnerZone partnerZone;

        UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        zoneCacheManager.clearCache("*");

        List<PartnerZone> zones = new ArrayList<>();
        PartnerZone zone = new PartnerZone();
        zone.setPartnerId(id);
        zone.setType("CAR");

        zone.setStatus(ZoneStatus.NO_ZONE);
        zones.add(zone);

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setPartnerId(id);
        cacheParamDTO.setHardTTL(700);

        zoneCacheManager.populateCache(zones, ZoneType.OPERATING, cacheParamDTO);
        log.info("POPULATE CACHE WITH PARTNER ZONES");

        partnerZone = zoneCacheManager.getPartnerZoneFromCache(id, ZoneType.OPERATING);

        assertEquals("CAR", partnerZone.getType());

    }


    @Test
    public void testGetVehicleType() throws Exception {


        UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        vehicleTypesCacheManager.clearCache("*");

        List<VehicleTypes> vehiclesTypes = new ArrayList<>();
        VehicleTypes vehicleType = new VehicleTypes();
        vehicleType.setVehicleTypeId("1234");
        vehicleType.setColor("green");
        vehiclesTypes.add(vehicleType);

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setPartnerId(id);
        cacheParamDTO.setHardTTL(700);

        vehicleTypesCacheManager.populateCache(vehiclesTypes, id, cacheParamDTO);
        log.info("POPULATE CACHE WITH VEHICLE TYPES");

        VehicleTypes vehicleTypes = vehicleTypesCacheManager.getFromCache(id);

        long startList = System.currentTimeMillis();
        log.info("{} elements found", vehicleTypesCacheManager.getAllVehicleTypesFromCache(id).size());
        long finishList = System.currentTimeMillis();
        long timeElapsedList = finishList - startList;
        log.info("timeElapsed getting from list cache = {} ms", timeElapsedList);

        List<VehicleTypes> vehicleTypesList = vehicleTypesCacheManager.getAllVehicleTypesFromCache(id);
        assertEquals("green", vehicleTypesList.get(0).getColor());

    }


    @Test
    public void testGetPriceList() throws Exception {


        UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        priceListCacheManager.clearCache("*");

        List<PriceListDTO> pricesList = new ArrayList<>();
        PriceListDTO priceList = new PriceListDTO();
        priceList.setPriceListId(UUID.fromString("b814c97e-df56-4651-ac50-115255379111"));
        priceList.setComment("price list comment");
        pricesList.add(priceList);

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setPartnerId(id);
        cacheParamDTO.setHardTTL(700);

        priceListCacheManager.populateCache(pricesList, id, "pricing-stat-1", cacheParamDTO);
        log.info("POPULATE CACHE WITH VEHICLE TYPES");

        PriceListDTO priceListDTO = priceListCacheManager.getPriceListFromCache(id);

        long startList = System.currentTimeMillis();
        log.info("{} elements found", priceListCacheManager.getAllPriceListFromCache(id, "pricing-stat-1").size());
        long finishList = System.currentTimeMillis();
        long timeElapsedList = finishList - startList;
        log.info("timeElapsed getting from list cache = {} ms", timeElapsedList);

        priceListCacheManager.getAllPriceListFromCache(id, null);

        List<PriceListDTO> pricesListCache = priceListCacheManager.getAllPriceListFromCache(id, "pricing-stat-1");
        assertEquals("price list comment", pricesListCache.get(0).getComment());

    }


    @Test
    public void testAvailableAsset() throws Exception {


        UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        assetTypeCacheManager.clearCache("*");

        List<AssetType> availableAssets = new ArrayList<>();

        AssetType assetType = new AssetType();
        assetType.setAssetSubClass("sub");
        assetType.setPartnerId(id);
        assetType.setStationId("stationId1");
        assetType.setAssetTypeId("assetTypeId111");
        // assets mock
        List<Asset> assets = new ArrayList<>();
        Asset asset = new Asset();
        asset.setAssetType("CAR");
        AssetProperties assetProperties = new AssetProperties();
        Place location = new Place();
        Coordinates coordinates = new Coordinates();
        coordinates.setLat(43.604652F);
        coordinates.setLng(1.444209F);
        location.setCoordinates(coordinates);
        assetProperties.setLocation(location);
        asset.setOverriddenProperties(assetProperties);
        asset.setAssetId("12222");
        asset.setPartnerId(id);
        assets.add(asset);

        assetType.setAssets(assets);
        availableAssets.add(assetType);

        CacheParamDTO cacheParamDTO = new CacheParamDTO();
        cacheParamDTO.setPartnerId(id);
        cacheParamDTO.setHardTTL(700);

        assetTypeCacheManager.populateAssetTypeCache(availableAssets, cacheParamDTO);
        log.info("POPULATE CACHE WITH VEHICLE TYPES");

        List<AssetType> assetTypeList = assetTypeCacheManager.getAllAssetTypeFromCacheByGeoParams(
                id, "stationId1", 43.604652D, 43.604652D, 1000000F, RedisGeoCommands.DistanceUnit.METERS);

        assetTypeCacheManager.getFromCache(id);

        List<AssetType> allAssetTypeList = assetTypeCacheManager.getAllAssetTypeFromCache(id, "stationId1");

        assertEquals("assetTypeId111", allAssetTypeList.get(0).getAssetTypeId());

        // exception case
        assertThrows(InternalException.class, () -> assetTypeCacheManager.getAllAssetTypeFromCacheByGeoParams(id, "stationId1", null, null, null, RedisGeoCommands.DistanceUnit.METERS));

    }

    @Test
    public void getCacheStatusTest() {
        GatewayParamsDTO gatewayParam = new GatewayParamsDTO(CACHE_ACTIVATION, "true");
        gatewayParamStatusManager.populateCacheActivation(gatewayParam, ONE_DAY_IN_SECONDS);
        log.info("POPULATE CACHE WITH CACHE_ACTIVATION");

        gatewayParamStatusManager.getCacheStatus();
        log.info("GET CACHE STATUS FROM CACHE");
        assertTrue(gatewayParamStatusManager.getCacheStatus());
    }

    @Test
    public void getCacheStatusWhenStatusIsNullAndPresentInDBTest() {
        gatewayParamStatusManager.clearCache("*");
        ResponseEntity<GatewayParamsDTO> gatewayParamDto = ResponseEntity.status(HttpStatus.OK).body(new GatewayParamsDTO(CACHE_ACTIVATION, "true"));
        lenient().when(restTemplate.exchange(
                ArgumentMatchers.contains("/gateway-params"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(GatewayParamsDTO.class))).thenReturn(gatewayParamDto);

        log.info("GET CACHE STATUS FROM CACHE");
        assertTrue(gatewayParamStatusManager.getCacheStatus());
    }

    @Test
    public void getCacheStatusWhenStatusIsNullAndNotPresentInDBTest() {
        gatewayParamStatusManager.clearCache("*");
        lenient().when(restTemplate.exchange(
                        ArgumentMatchers.contains("/gateway-params"),
                        ArgumentMatchers.eq(HttpMethod.GET),
                        any(),
                        ArgumentMatchers.eq(GatewayParamsDTO.class)))
                .thenThrow(HttpClientErrorException.NotFound.class);

        ResponseEntity<GatewayParamsDTO> gatewayParamDtoDefault = ResponseEntity.status(HttpStatus.OK).body(new GatewayParamsDTO(CACHE_ACTIVATION, "false"));

        lenient().when(restTemplate.postForEntity(
                        ArgumentMatchers.contains("/gateway-params"),
                        ArgumentMatchers.eq(new GatewayParamsDTO(CACHE_ACTIVATION, "false")),
                        ArgumentMatchers.eq(GatewayParamsDTO.class)))
                .thenReturn(gatewayParamDtoDefault);

        assertFalse(gatewayParamStatusManager.getCacheStatus());
    }


    public static List<PartnerMetaDTO> createPartnerMetas() {
        try {
            String mockStringyfied = WsTestUtil.readJsonFromFilePath("./src/it/resources/mock/partners-mock.json");
            if (mockStringyfied.isBlank()) {
                log.error("Content file is empty or file not found");
            }
            ObjectReader objectReader = new ObjectMapper().reader().forType(new TypeReference<List<PartnerMetaDTO>>() {
            });
            return objectReader.readValue(mockStringyfied);
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }


}
