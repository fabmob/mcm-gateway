package com.gateway.api.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import com.gateway.api.ApiITTestCase;
import com.gateway.api.model.*;
import com.gateway.api.service.ivservice.impl.IVServiceImpl;
import com.gateway.api.service.mspservice.impl.MSPServiceImpl;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.tests.enums.JsonResponseTypeEnum;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.gateway.api.util.constant.GatewayApiPathDict.*;
import static com.gateway.api.util.constant.GatewayMessageDict.AREA_TYPE;
import static com.gateway.commonapi.utils.enums.ZoneType.OPERATING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * Test class for #ApiControllerTests
 */
@Slf4j
class ApiControllerTest extends ApiITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String GATEWAY_API_EXPECTED_GET_MSPS_OK_JSON = "gateway-api/expected/getMsps_ok.json";
    public static final String GATEWAY_API_EXPECTED_GET_MSP_BY_ID_OK_JSON = "gateway-api/expected/getMsp_by_id_ok.json";
    public static final String GATEWAY_API_MOCK_GET_MSPS_MOCK_JSON = "gateway-api/mock/getMspsMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_GLOBAL_VIEW_OK_JSON = "gateway-api/expected/getGlobalView_ok.json";
    public static final String GATEWAY_API_MOCK_GET_GLOBAL_VIEW_MOCK_JSON = "gateway-api/mock/getGlobalViewMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_MSP_ZONE_OK_JSON = "gateway-api/expected/getMspZone_ok.json";
    public static final String GATEWAY_API_MOCK_GET_MSP_ZONE_MOCK_JSON = "gateway-api/mock/getMspZoneMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_MSP_STATIONS_OK_JSON = "gateway-api/expected/getMspStations_ok.json";
    public static final String GATEWAY_API_MOCK_GET_MSP_STATIONS_MOCK_JSON = "gateway-api/mock/getMspStationsMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_MSP_STATIONS_STATUS_OK_JSON = "gateway-api/expected/getMspStationsStatus_ok.json";
    public static final String GATEWAY_API_MOCK_GET_MSP_STATIONS_STATUS_MOCK_JSON = "gateway-api/mock/getMspStationsStatusMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_MSP_ASSETS_OK_JSON = "gateway-api/expected/getMspAssets_ok.json";
    public static final String GATEWAY_API_MOCK_GET_MSP_ASSETS_MOCK_JSON = "gateway-api/mock/getMspAssetsMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_MSP_AVAILABLE_ASSETS_OK_JSON = "gateway-api/expected/getMspAvailableAssets_ok.json";
    public static final String GATEWAY_API_MOCK_GET_MSP_AVAILABLE_ASSETS_MOCK_JSON = "gateway-api/mock/getMspAvailableAssetsMock.json";
    public static final String GATEWAY_API_REQUEST_AROUND_ME_JSON = "gateway-api/request/aroundMeRequest.json";
    public static final String GATEWAY_API_EXPECTED_GET_VEHICULE_TYPES_OK_JSON = "gateway-api/expected/getVehiculeTypes.json";
    public static final String GATEWAY_API_EXPECTED_GET_PRICE_LIST_OK_JSON = "gateway-api/expected/priceList.json";
    public static final String GATEWAY_API_EXPECTED_GET_DRIVER_JOURNEYS_OK_JSON = "gateway-api/expected/driverJourneys.json";
    public static final String GATEWAY_API_EXPECTED_GET_PASSENGER_JOURNEYS_OK_JSON = "gateway-api/expected/passengerJourneys.json";
    public static final String GATEWAY_API_MOCK_GET_DRIVER_JOURNEYS_OK_JSON = "gateway-api/mock/getDriverJourneys.json";
    public static final String GATEWAY_API_MOCK_GET_PASSENGER_JOURNEYS_OK_JSON = "gateway-api/mock/getPassengerJourneys.json";
    public static final String GATEWAY_API_EXPECTED_GET_DRIVER_REGULAR_TRIP_OK_JSON = "gateway-api/expected/driverRegularTrip.json";
    public static final String GATEWAY_API_EXPECTED_GET_PASSENGER_REGULAR_TRIP_OK_JSON = "gateway-api/expected/passengerRegularTrip.json";
    public static final String GATEWAY_API_MOCK_GET_DRIVER_REGULAR_TRIP_OK_JSON = "gateway-api/mock/getDriverRegularTrip.json";
    public static final String GATEWAY_API_MOCK_GET_PASSENGER_REGULAR_TRIP_OK_JSON = "gateway-api/mock/getPassengerRegularTrip.json";


    /*
     * Mock MVC
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    MSPServiceImpl mockMspServiceImpl;

    @MockBean
    IVServiceImpl mockIVServiceImpl;

    /*
     * Web Application Context
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, this.objectMapper);
    }

    /**
     * Test list Msps operation
     *
     * @throws Exception
     */
    @Test
    void testGetMspMetas() throws Exception {
        testHttpRequestWithExpectedResult(GET_MSPS_PATH, HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_MSPS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get msps",null);
    }

    /**
     * Test list Msp by id operation
     *
     * @throws Exception
     */
    @Test
    void testGetMspMetasById() throws Exception {
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(GET_MSPS_PATH + GET_MSP_BY_ID_PATH, MSP_ID, "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_MSP_BY_ID_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get msps by id",null);
    }


    /**
     * Test get a global view operation
     *
     * @throws Exception
     */
    @Test
    void testGetGlobalView() throws Exception {
        testHttpRequestWithExpectedResult(GET_MSPS_PATH + GET_MSPS_GLOBAL_VIEW_PATH, HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_GLOBAL_VIEW_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get global-view",null);
    }

    /**
     * Test post a around-me operation
     *
     * @throws Exception
     */
    @Test
    void testGetAroundMe() throws Exception {
        testHttpRequestWithExpectedResult(GET_MSPS_PATH + GET_MSPS_AROUND_ME_PATH, HttpMethod.POST, HttpStatus.OK,
                GATEWAY_API_REQUEST_AROUND_ME_JSON, GATEWAY_API_EXPECTED_GET_GLOBAL_VIEW_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test post around-me",null);
    }


    /**
     * Test get a MSPZone by mspId and areaType operation
     *
     * @throws Exception
     */
    @Test
    void testGetMSPZone() throws Exception {
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(GET_MSPS_PATH + GET_MSP_AREAS_TYPE_PATH, MSP_ID, "87930fdf-34c1-41c9-885e-6ce66505b598", AREA_TYPE, OPERATING.value), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_MSP_ZONE_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get msp zone",null);
    }


    /**
     * Test list Stations by MSP id operation
     *
     * @throws Exception
     */
    @Test
    void testGetMSPStations() throws Exception {
        testHttpRequestWithExpectedResult(GET_MSPS_PATH + GET_MSP_STATIONS_PATH.replace("{mspId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_MSP_STATIONS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get stations by mspId",null);
    }

    /**
     * Test list Stations-Status by MSP id operation
     *
     * @throws Exception
     */
    @Test
    void testGetMSPStationsStatus() throws Exception {
        testHttpRequestWithExpectedResult(GET_MSPS_PATH + GET_MSP_STATIONS_STATUS_PATH.replace("{mspId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_MSP_STATIONS_STATUS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get stations status by mspId",null);
    }


    /**
     * Test list Assets by MSP id operation
     *
     * @throws Exception
     */
    @Test
    void testGetMSPAssets() throws Exception {
        testHttpRequestWithExpectedResult(GET_MSPS_PATH + GET_MSP_ASSETS_PATH.replace("{mspId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_MSP_ASSETS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get stations status by mspId",null);
    }

    /**
     * Test list available Assets by MSP id operation
     *
     * @throws Exception
     */
    @Test
    void testGetMSPAvailableAssets() throws Exception {
        testHttpRequestWithExpectedResult(GET_MSPS_PATH + GET_MSP_AVAILABLE_ASSETS_PATH.replace("{mspId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_MSP_AVAILABLE_ASSETS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get stations status by mspId",null);
    }

    /**
     * Test list VehiculeTypes
     *
     * @throws Exception
     */
    @Test
    void testGetVehiculeTypes() throws Exception {
        testHttpRequestWithExpectedResult(GET_MSPS_PATH + GET_MSP_VEHICLE_TYPES_PATH.replace("{mspId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_VEHICULE_TYPES_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get vehicule types by mspId",null);
    }

    /**
     * Test list PricingPlan
     *
     * @throws Exception
     */
    @Test
    void testGetMSPPricingPlan() throws Exception {
        testHttpRequestWithExpectedResult(GET_MSPS_PATH + GET_MSP_PRICING_PLAN_PATH.replace("{mspId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PRICE_LIST_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get pricing plan  by mspId",null);
    }

    /**
     * Test list DriverJourneys
     *
     * @throws Exception
     */
    @Test
    void testGetDriverJourneys() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12));
        parameters.add("departureLng", String.valueOf(12));
        parameters.add("arrivalLat", String.valueOf(12));
        parameters.add("arrivalLng", String.valueOf(12));
        parameters.add("departureDate", String.valueOf(1655452466));
        testHttpRequestWithExpectedResult(GET_MSPS_PATH + GET_MSP_DRIVER_JOURNEY_PATH.replace("{mspId}", "87930fdf-34c1-41c9-885e-6ce66505b598") ,
                HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_DRIVER_JOURNEYS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get driver journeys  by mspId", parameters);
    }

    /**
     * Test list PassengerJourneys
     *
     * @throws Exception
     */
    @Test
    void testGetPassengerJourneys() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12));
        parameters.add("departureLng", String.valueOf(12));
        parameters.add("arrivalLat", String.valueOf(12));
        parameters.add("arrivalLng", String.valueOf(12));
        parameters.add("departureDate", String.valueOf(1655452466));
        testHttpRequestWithExpectedResult(GET_MSPS_PATH +GET_MSP_PASSENGER_JOURNEY_PATH.replace("{mspId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PASSENGER_JOURNEYS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get passenger journeys  by mspId",parameters);
    }

    /**
     * Test list DriverRegularTrip
     *
     * @throws Exception
     */
    @Test
    void testGetDriverRegularTrip() throws Exception {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12.1f));
        parameters.add("departureLng", String.valueOf(12.1f));
        parameters.add("arrivalLat", String.valueOf(12f));
        parameters.add("arrivalLng", String.valueOf(1.9f));
        parameters.add("departureTimeOfDay", String.valueOf(7));

        testHttpRequestWithExpectedResult(GET_MSPS_PATH +GET_MSP_DRIVER_REGULAR_TRIPS_PATH .replace("{mspId}",
                        "87930fdf-34c1-41c9-885e-6ce66505b598") , HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_DRIVER_REGULAR_TRIP_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get driver regular trip  by mspId",parameters);
    }


    /**
     * Test list PassengerRegularTrip
     *
     * @throws Exception
     */
    @Test
    void testGetPassengerRegularTrip() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12.1f));
        parameters.add("departureLng", String.valueOf(12.1f));
        parameters.add("arrivalLat", String.valueOf(12f));
        parameters.add("arrivalLng", String.valueOf(1.9f));
        parameters.add("departureTimeOfDay", "7:30:00 AM");
        testHttpRequestWithExpectedResult(GET_MSPS_PATH + GET_MSP_PASSENGER_REGULAR_TRIPS_PATH.replace("{mspId}", "87930fdf-34c1-41c9-885e-6ce66505b598") , HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PASSENGER_REGULAR_TRIP_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get Passenger Regular Trip by mspId",parameters);
    }


    /**
     * Central management of the ApiControllerTest
     *
     * @param uri                      the uri of the operation
     * @param httpMethod               HTTP VERB (GET, PUT, POST, PATCH, DELETE)
     * @param httpStatusExpectedResult Https code status expected
     * @param requestPayloadPath       payload of the request (optionnal for some verbs)
     * @param expectedResultPath       path of the json expected answer file
     * @param resulType                JsonArray or JsonObject expected as result
     * @param message                  Message of the test running
     * @throws Exception
     */
    private void testHttpRequestWithExpectedResult(String uri, HttpMethod httpMethod,
                                                   HttpStatus httpStatusExpectedResult, String requestPayloadPath,
                                                   String expectedResultPath, JsonResponseTypeEnum resulType,
                                                   final String message, MultiValueMap<String, String> parameters) throws Exception {

        List<MSPMeta> mspListMoked = createMockedMspList();
        GlobalView globalViewMoked = createMockedGlobalView();
        MSPZone mspZoneMoked = createMockedMspZone();
        List<Station> stationListMoked = createMockedStationList();
        List<StationStatus> stationStatusListMoked = createMockedStationStatusList();
        List<Asset> assetsListMoked = createMockedAssetList();
        List<AssetType> availableAssetsListMoked = createMockedAvailableAssetList();
        List<VehicleTypes> vehicleTypes = createMockedVehicleTypesList();
        List<PriceList> priceListsMocked = createMockedPriceList();

        // Mock carpooling IV operations
        List<DriverJourney> mockedDriverJourneys = createMockedDriverJourneys();
        List<PassengerJourney> mockedPassengerJourneys = createMockedPassengerJourneys();
        List<PassengerRegularTrip> mockedPassengerRegularTrip = createMockedPassengerRegularTrip();
        List<DriverRegularTrip> mockedDriverRegularTrip = createMockedDriverRegularTrip();

        // mock all operations with a stub object from createMocked functions
        doReturn(mspListMoked).when(mockMspServiceImpl).getMSPsMeta();
        doReturn(mspListMoked.get(0)).when(mockMspServiceImpl).getMSPMeta(any());
        doReturn(globalViewMoked).when(mockIVServiceImpl).getGlobalView();
        doReturn(globalViewMoked).when(mockIVServiceImpl).getAroundMe(any());
        doReturn(mspZoneMoked).when(mockMspServiceImpl).getMSPZone(any(), any());
        doReturn(stationListMoked).when(mockIVServiceImpl).getStations(any(), any(), any(), any());
        doReturn(stationStatusListMoked).when(mockIVServiceImpl).getStationStatus(any(), any());
        doReturn(assetsListMoked).when(mockIVServiceImpl).getAssets(any());
        doReturn(availableAssetsListMoked).when(mockIVServiceImpl).getAvailableAssets(any(), any(), any(), any(), any());
        doReturn(vehicleTypes).when(mockIVServiceImpl).getVehicleTypes(any());
        doReturn(priceListsMocked).when(mockIVServiceImpl).getPricingPlan(any(), any());

        doReturn(mockedDriverJourneys).when(mockIVServiceImpl).getDriverJourneys(any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
        doReturn(mockedPassengerJourneys).when(mockIVServiceImpl).getPassengerJourneys(any(), any(), any(), any(), any(), any(), any(), any(), any(), any());


        doReturn(mockedDriverRegularTrip).when(mockIVServiceImpl).getDriverRegularTrips(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598"),
                12.1f, 12.1f, 12f, 1.9f, "7",
               null, null,null, null, null, null, null);

        doReturn(mockedPassengerRegularTrip).when(mockIVServiceImpl).getPassengerRegularTrips(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598"),
                 12.1f, 12.1f, 12f, 1.9f, "7:30:00 AM",
                null, null,null, null, null, null, null);


        // preparing the service call and expected elements
        ResultMatcher mockResultMatcher = WsTestUtil.getResultMatcher(httpStatusExpectedResult);
        String requestPayloadContent = StringUtils.isNotBlank(requestPayloadPath) ? WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + requestPayloadPath) : "";
        MockHttpServletRequestBuilder requestBuilder = WsTestUtil.getMockHttpServletRequestBuilder("", uri, httpMethod, requestPayloadContent);
        MediaType responseContentType = WsTestUtil.getMediaType(httpMethod);

        // trigger the call and check expected encoding and http status code
        String content;
        if (parameters == null || parameters.isEmpty()) {
            if (responseContentType != null) {
                content = this.mockMvc.
                        perform(requestBuilder)
                        .andExpect(mockResultMatcher)
                        .andExpect(MockMvcResultMatchers.content().contentType(responseContentType))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            } else {
                content = this.mockMvc.perform(requestBuilder).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
            }
        } else {
            content = this.mockMvc.perform(get("" + uri).params(parameters)).andExpect(mockResultMatcher).andExpect(MockMvcResultMatchers.content().contentType(responseContentType)).andReturn().getResponse().getContentAsString();

        }

        String pathFile = IT_RESOURCES_PATH + expectedResultPath;
        // get the expected json response as string
        final String jsonRef = StringUtils.isBlank(expectedResultPath) ? "" : WsTestUtil.readJsonFromFilePath(pathFile);

        // compare the expected json with the result
        if (resulType != null) {
            switch (resulType) {
                case JSON_ARRAY:
                    JSONArray expectedJsonArray = new JSONArray(jsonRef);
                    JSONArray resultJsonArray = new JSONArray(content);
                    Assertions.assertEquals(expectedJsonArray.toString(4), resultJsonArray.toString(4), message);
                    break;
                case JSON_OBJECT:
                    JSONObject expectedJsonObject = new JSONObject(jsonRef);
                    JSONObject resultJsonObject = new JSONObject(content);
                    Assertions.assertEquals(expectedJsonObject.toString(4), resultJsonObject.toString(4), message);
                    break;
                default:
                    break;
            }
            log.info("Test executed : {}", message);
        }
    }

    /**
     * Create a mock of 'List<MSPMeta>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<MSPMeta> createMockedMspList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_MSPS_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<MSPMeta>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }


    /**
     * Create a mock of 'GlobalView'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private GlobalView createMockedGlobalView() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_GLOBAL_VIEW_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<GlobalView>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create a mock of 'MSPZone'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private MSPZone createMockedMspZone() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_MSP_ZONE_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<MSPZone>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create a mock of 'List<Station>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<Station> createMockedStationList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_MSP_STATIONS_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Station>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create a mock of 'List<StationStatus>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<StationStatus> createMockedStationStatusList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_MSP_STATIONS_STATUS_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<StationStatus>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create a mock of 'List<Asset>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<Asset> createMockedAssetList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_MSP_ASSETS_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Asset>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create a mock of 'List<Asset>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<AssetType> createMockedAvailableAssetList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_MSP_AVAILABLE_ASSETS_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<AssetType>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create a mock of 'List<VehicleTypes>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<VehicleTypes> createMockedVehicleTypesList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_EXPECTED_GET_VEHICULE_TYPES_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<VehicleTypes>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create a mock of 'List<PriceList>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<PriceList> createMockedPriceList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_EXPECTED_GET_PRICE_LIST_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<PriceList>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create a mock of DriverJourney
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<DriverJourney> createMockedDriverJourneys() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_DRIVER_JOURNEYS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<DriverJourney>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create a mock of PassengerJourneys
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<PassengerJourney> createMockedPassengerJourneys() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_PASSENGER_JOURNEYS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<PassengerJourney>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }


    /**
     * Create a mock of PassengerRegularTrip
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<PassengerRegularTrip> createMockedPassengerRegularTrip() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_PASSENGER_REGULAR_TRIP_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<PassengerRegularTrip>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }


    /**
     * Create a mock of DriverRegularTrip
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<DriverRegularTrip> createMockedDriverRegularTrip() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_DRIVER_REGULAR_TRIP_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<DriverRegularTrip>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }



}
