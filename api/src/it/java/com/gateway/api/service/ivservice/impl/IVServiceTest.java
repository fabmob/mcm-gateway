package com.gateway.api.service.ivservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.api.ApiITTestCase;
import com.gateway.api.model.MSPMeta;
import com.gateway.api.model.PriceList;
import com.gateway.api.service.mspservice.impl.MSPServiceImpl;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class IVServiceImplTest extends ApiITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String SERVICE_EXPECTED_GET_GLOBAL_VIEW_OK_JSON = "gateway-api/mockedservices-expected/service_getGlobalView_ok.json";
    public static final String SERVICE_EXPECTED_GET_MSP_STATIONS_OK_JSON = "gateway-api/mockedservices-expected/service_getMspStations_ok.json";
    public static final String SERVICE_EXPECTED_GET_MSP_STATIONS_STATUS_OK_JSON = "gateway-api/mockedservices-expected/service_getMspStationsStatus_ok.json";
    public static final String SERVICE_EXPECTED_GET_MSP_ASSETS_OK_JSON = "gateway-api/mockedservices-expected/service_getMspAssets_ok.json";
    public static final String SERVICE_EXPECTED_GET_MSP_AVAILABLE_ASSETS_OK_JSON = "gateway-api/mockedservices-expected/service_getMspAvailableAssets_ok.json";
    public static final String SERVICE_EXPECTED_GET_VEHICLE_TYPES_OK_JSON = "gateway-api/mockedservices-expected/service_getVehicleTypes_ok.json";
    public static final String SERVICE_EXPECTED_GET_PRICE_LIST_OK_JSON = "gateway-api/mockedservices-expected/service_getPriceList_ok.json";
    public static final String SERVICE_EXPECTED_GET_DRIVRE_JOURNEYS_OK_JSON = "gateway-api/expected/driverJourneys.json";
    public static final String SERVICE_EXPECTED_GET_PASSENGER_JOURNEYS_OK_JSON = "gateway-api/expected/passengerJourneys.json";
    public static final String SERVICE_EXPECTED_GET_PASSENGER_REGULAR_TRIPS_OK_JSON = "gateway-api/expected/passengerRegularTrip.json";
    public static final String SERVICE_EXPECTED_GET_DRIVER_REGULAR_TRIPS_OK_JSON = "gateway-api/expected/driverRegularTrip.json";

    public static final String MOCK_MSP_ID = "14390fdf-34c1-41c9-885e-6ce66505b759";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IVServiceImpl ivService;

    @Mock
    private MSPServiceImpl mspService;

    @MockBean
    private ErrorMessages errorMessages;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    private List<Object> creatMockStations() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_MSP_STATIONS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;

    }

    private List<MSPMeta> createMockedMspList() {
        List<MSPMeta> msps = new ArrayList<>();
        MSPMeta msp1 = new MSPMeta();
        UUID mspId = UUID.fromString("a814c97e-df56-4651-ac50-11525537964f");
        msp1.setMspId(mspId);
        MSPMeta msp2 = new MSPMeta();
        UUID mspId2 = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        msp2.setMspId(mspId2);
        msps.add(msp1);
        msps.add(msp2);

        return msps;
    }

    private MSPAroundMeRequest createMockBody() {
        MSPAroundMeRequest request = new MSPAroundMeRequest();
        request.setLat((float) 6.169639);
        request.setLon((float) 7.169639);
        request.setMaxResult(2);
        request.setRadius(22F);
        List<UUID> mspsID = new ArrayList<>();
        UUID mspId1 = UUID.fromString("a814c97e-df56-4651-ac50-11525537964f");
        UUID mspId2 = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        mspsID.add(mspId1);
        mspsID.add(mspId2);
        request.setMspIds(mspsID);
        return request;
    }

    private List<Object> creatMockStationsStatus() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_MSP_STATIONS_STATUS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;

    }

    private List<Object> creatMockAsset() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_MSP_ASSETS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;
    }

    private List<Object> creatMockAvailableAssets() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_MSP_AVAILABLE_ASSETS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;
    }

    private List<Object> creatMockGlobalView() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_GLOBAL_VIEW_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;
    }

    private List<Object> creatMockgVehicleTypes() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_VEHICLE_TYPES_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;
    }

    private List<Object> creatMockPriceList() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PRICE_LIST_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;
    }

    private List<Object> creatMockDrivreJourneys() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_DRIVRE_JOURNEYS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;
    }

    private List<Object> creatMockPassengerJourneys() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PASSENGER_JOURNEYS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;
    }

    private List<Object> creatMockPassengerRegularTrips()throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_PASSENGER_REGULAR_TRIPS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;
    }

    private List<Object> creatMockDriverRegularTrips()throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + SERVICE_EXPECTED_GET_DRIVER_REGULAR_TRIPS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });
        List<Object> expectedResponse = objectReader.readValue(expectedStringyfied);
        return expectedResponse;
    }



    @Test
    void testGetStations() throws IOException {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockStations());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<Station> list = (List<Station>) ivService.getStations(mspId, 1F, 1F, 1F);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.get(0).getMspId().toString());
    }

    @Test
    void testGetStationsNull() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            ivService.getStations(mspId, null, null, null);
        });
    }

    @Test
    void testGetStationsExeption() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        Mockito.when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        assertThrows(UnavailableException.class, () -> {
            ivService.getStations(mspId, null, null, null);
        });
    }


    @Test
    void testGetStationsExceptionFromAdapter() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        HttpClientErrorException adapterException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenThrow(adapterException);

        assertThrows(NotFoundException.class, () -> {
            ivService.getStations(mspId, (float) 1.1, (float) 1.1, (float) 1.1);
        });


    }


    @Test
    void testGetStationsRestRequestException() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        RestClientException restException = new RestClientException("connection failed");
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenThrow(restException);

        lenient().when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("connection failed");


        assertThrows(BadGatewayException.class, () -> {
            ivService.getStations(mspId, (float) 1.1, (float) 1.1, (float) 1.1);
        });


    }


    @Test
    void testGetStationsStatus() throws IOException {
        UUID mspId = UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598");
        String stationId = "sta1";
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockStationsStatus());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<StationStatus> list = (List<StationStatus>) ivService.getStationStatus(mspId, stationId);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.get(0).getMspId().toString());

    }

    @Test
    void testGetStationsStatusExeption() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        Mockito.when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        UnavailableException exception = assertThrows(UnavailableException.class, () -> {
            ivService.getStationStatus(mspId, null);
        });
    }

    @Test
    void testGetStationsStatusNull() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            ivService.getStationStatus(mspId, null);
        });
    }

    @Test
    void testGetAssets() throws IOException {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");

        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockAsset());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<Asset> list = (List<Asset>) ivService.getAssets(mspId);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.get(0).getMspId().toString());

    }

    @Test
    void testGetAssetsExeption() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        Mockito.when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        UnavailableException exception = assertThrows(UnavailableException.class, () -> {
            ivService.getAssets(mspId);
        });
    }

    @Test
    void testGetAssetsNull() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            ivService.getAssets(mspId);
        });
    }

    @Test
    void testGetAvailableAssets() throws IOException {
        UUID mspId = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");
        String stationId = "stationId 1";
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockAvailableAssets());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        List<AssetType> list = (List<AssetType>) ivService.getAvailableAssets(mspId, stationId, 1F, 1F, 1F);
        assertEquals("b814c97e-df56-4651-ac50-11525537964f", list.get(0).getMspId().toString());
    }

    @Test
    void testGetAvailableAssetsNull() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        String stationId = "stationId 1";
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            ivService.getAvailableAssets(mspId, stationId, null, null, null);
        });
    }

    @Test
    void testGetAvailableAssetsExeption() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        String stationId = "stationId 1";
        Mockito.when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        UnavailableException exception = assertThrows(UnavailableException.class, () -> {
            ivService.getAvailableAssets(mspId, stationId, null, null, null);
        });
    }

    @Test
    void testGetGlobalView() throws IOException, InterruptedException {
        List<MSPMeta> mspListMocked = createMockedMspList();
        lenient().when(mspService.getMSPsMeta()).thenReturn(mspListMocked);
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockGlobalView());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        GlobalView list = (GlobalView) ivService.getGlobalView();
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.getStations().get(0).getMspId().toString());
    }

    @Test
    void testGetAroundMe() throws IOException {
        List<UUID> mspIds = List.of((UUID.fromString(MOCK_MSP_ID)), (UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b735")), (UUID.fromString("14390fdf-34c1-41c9-885e-6ce669264483")));
        MSPAroundMeRequest mspAroundMeRequest = new MSPAroundMeRequest((float) 6.169639, (float) 52.253279, 100F, 200, mspIds);
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockGlobalView());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        GlobalView list = (GlobalView) ivService.getAroundMe(mspAroundMeRequest);
        assertEquals("14390fdf-34c1-41c9-885e-6ce66505b759", list.getStations().get(0).getMspId().toString());
    }

    @Test
    void testGetAroundMeExp() {
        List<UUID> mspIds = List.of((UUID.fromString(MOCK_MSP_ID)), (UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b735")), (UUID.fromString("14390fdf-34c1-41c9-885e-6ce669264483")));
        MSPAroundMeRequest mspAroundMeRequest = new MSPAroundMeRequest((float) 6.169639, (float) 52.253279, 100F, 200, mspIds);
        Mockito.when(errorMessages.getTechnicalRestHttpClientError()).thenReturn("should throw exception");
        UnavailableException exception = assertThrows(UnavailableException.class, () -> {
            ivService.getAroundMe(mspAroundMeRequest);
        });
    }

    @Test
    void testGetVehicleTypes() throws IOException {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockgVehicleTypes());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        List<VehicleTypes> list = (List<VehicleTypes>) ivService.getVehicleTypes(mspId);
        assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", list.get(0).getVehicleTypeId());
    }

    @Test
    void testGetVehicleTypesNull() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            ivService.getVehicleTypes(mspId);
        });
    }

    @Test
    void testGetPricingPlan() throws IOException {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        String stationId = "station 1";
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockPriceList());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        List<PriceList> list = (List<PriceList>) ivService.getPricingPlan(mspId, stationId);
        assertEquals("comment", list.get(0).getComment());
    }

    @Test
    void testGetPricingPlanNull() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        String stationId = "station 1";
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            ivService.getPricingPlan(mspId, stationId);
        });
    }

    @Test
    void testGetDriverJourneys() throws IOException {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 5;
        float departureLng = 5;
        float arrivalLat = 5;
        float arrivalLng = 5;
        Integer departureDate = 115;
        Integer timeDelta = 115;
        float departureRadius = 1;
        float arrivalRadius = 2;
        Integer count = 3;
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockDrivreJourneys());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        List<DriverJourney> list = ivService.getDriverJourneys(mspId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count);
        assertEquals("carpool.mycity.com", list.get(0).getOperator());
    }

    @Test
    void testGetDriverJourneysNull() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
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
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            ivService.getDriverJourneys(mspId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count);
        });
    }

    @Test
    void testGetPassengerJourneys() throws IOException {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 5;
        float departureLng = 5;
        float arrivalLat = 5;
        float arrivalLng = 5;
        Integer departureDate = 115;
        Integer timeDelta = 115;
        float departureRadius = 1;
        float arrivalRadius = 2;
        Integer count = 3;
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockPassengerJourneys());
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        List<PassengerJourney> list = ivService.getPassengerJourneys(mspId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count);
        assertEquals("carpool.mycity.com", list.get(0).getOperator());
    }
    @Test
    void testGetPassengerJourneysNull() {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
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
        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);
        Mockito.when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("should throw exception");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            ivService.getPassengerJourneys(mspId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count);
        });
    }


        @Test
    void testGetPassengerRegularTrips() throws IOException {
            UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
            float departureLat = 3;
            float departureLng = 6;
            float arrivalLat = 6;
            float arrivalLng = 6;
            String departureTimeOfDay = "";
            List<String> departureWeekdays = new ArrayList<>();
            Integer timeDelta = 6;
            float departureRadius = 6;
            float arrivalRadius = 6;
            Integer minDepartureDate = 6;
            Integer maxDepartureDate = 6;
            Integer count = 6;
            ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockPassengerRegularTrips());

            lenient().when(restTemplate.postForEntity(
                    ArgumentMatchers.contains("mspId"),
                    ArgumentMatchers.eq(Optional.empty()),
                    ArgumentMatchers.eq(Object.class))).thenReturn(response);

            List<PassengerRegularTrip> passengerRegularTripsList = ivService.getPassengerRegularTrips(mspId,departureLat,
                    departureLng,arrivalLat,arrivalLng,
                    departureTimeOfDay,departureWeekdays,timeDelta,departureRadius,arrivalRadius,minDepartureDate,maxDepartureDate,count);

            assertEquals("carpool.mycity.com", passengerRegularTripsList.get(0).getOperator());
    }

    @Test
    void testGetPassengerRegularTripsResonseNull() throws IOException {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 3;
        float departureLng = 6;
        float arrivalLat = 6;
        float arrivalLng = 6;
        String departureTimeOfDay = "";
        List<String> departureWeekdays =  new ArrayList<>();
        Integer timeDelta = 6;
        float departureRadius = 6;
        float arrivalRadius = 6;
        Integer minDepartureDate = 6;
        Integer maxDepartureDate = 6;
        Integer count = 6;

        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ivService.getPassengerRegularTrips(mspId,departureLat,
                    departureLng,arrivalLat,arrivalLng,
                    departureTimeOfDay,departureWeekdays,timeDelta,departureRadius,arrivalRadius,minDepartureDate,maxDepartureDate,count);
        });
    }

    @Test
    void testGetDriverRegularTrips() throws IOException {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 3;
        float departureLng = 6;
        float arrivalLat = 6;
        float arrivalLng = 6;
        String departureTimeOfDay = "";
        List<String> departureWeekdays = new ArrayList<>();
        Integer timeDelta = 6;
        float departureRadius = 6;
        float arrivalRadius = 6;
        Integer minDepartureDate = 6;
        Integer maxDepartureDate = 6;
        Integer count = 6;
        ResponseEntity<Object> response = ResponseEntity.status(HttpStatus.OK).body(this.creatMockDriverRegularTrips());

        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(response);

        List<DriverRegularTrip> passengerRegularTripsList = ivService.getDriverRegularTrips(mspId,departureLat,
                departureLng,arrivalLat,arrivalLng,
                departureTimeOfDay,departureWeekdays,timeDelta,departureRadius,arrivalRadius,minDepartureDate,maxDepartureDate,count);

        assertEquals("carpool.mycity.com", passengerRegularTripsList.get(0).getOperator());
    }


    @Test
    void testGetDriverRegularTripsResponseNull() throws IOException {
        UUID mspId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        float departureLat = 3;
        float departureLng = 6;
        float arrivalLat = 6;
        float arrivalLng = 6;
        String departureTimeOfDay = "";
        List<String> departureWeekdays = new ArrayList<>();
        Integer timeDelta = 6;
        float departureRadius = 6;
        float arrivalRadius = 6;
        Integer minDepartureDate = 6;
        Integer maxDepartureDate = 6;
        Integer count = 6;

        lenient().when(restTemplate.postForEntity(
                ArgumentMatchers.contains("mspId"),
                ArgumentMatchers.eq(Optional.empty()),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            ivService.getDriverRegularTrips(mspId,departureLat,
                    departureLng,arrivalLat,arrivalLng,
                    departureTimeOfDay,departureWeekdays,timeDelta,departureRadius,arrivalRadius,minDepartureDate,maxDepartureDate,count);
        });
    }
}
