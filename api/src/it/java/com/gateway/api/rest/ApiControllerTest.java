package com.gateway.api.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.api.ApiITTestCase;
import com.gateway.api.model.PartnerMeta;
import com.gateway.api.model.PriceList;
import com.gateway.api.service.bookingservice.impl.BookingServiceImpl;
import com.gateway.api.service.ivservice.impl.IVServiceImpl;
import com.gateway.api.service.partnerservice.impl.PartnerServiceImpl;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.tests.enums.JsonResponseTypeEnum;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.BookingStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.gateway.api.util.constant.GatewayMessageDict.AREA_TYPE;
import static com.gateway.commonapi.constants.GatewayApiPathDict.*;
import static com.gateway.commonapi.utils.enums.ZoneType.OPERATING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Test class for #ApiControllerTests
 */

@Slf4j
class ApiControllerTest extends ApiITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String GATEWAY_API_EXPECTED_GET_PARTNERS_OK_JSON = "gateway-api/expected/getPartners_ok.json";
    public static final String GATEWAY_API_EXPECTED_GET_PARTNER_BY_ID_OK_JSON = "gateway-api/expected/getPartner_by_id_ok.json";
    public static final String GATEWAY_API_MOCK_GET_PARTNERS_MOCK_JSON = "gateway-api/mock/getPartnersMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_GLOBAL_VIEW_OK_JSON = "gateway-api/expected/getGlobalView_ok.json";
    public static final String GATEWAY_API_MOCK_GET_GLOBAL_VIEW_MOCK_JSON = "gateway-api/mock/getGlobalViewMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_PARTNER_ZONE_OK_JSON = "gateway-api/expected/getPartnerZone_ok.json";
    public static final String GATEWAY_API_MOCK_GET_PARTNER_ZONE_MOCK_JSON = "gateway-api/mock/getPartnerZoneMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_PARTNER_STATIONS_OK_JSON = "gateway-api/expected/getPartnerStations_ok.json";
    public static final String GATEWAY_API_MOCK_GET_PARTNER_STATIONS_MOCK_JSON = "gateway-api/mock/getPartnerStationsMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_PARTNER_STATIONS_STATUS_OK_JSON = "gateway-api/expected/getPartnerStationsStatus_ok.json";
    public static final String GATEWAY_API_MOCK_GET_PARTNER_STATIONS_STATUS_MOCK_JSON = "gateway-api/mock/getPartnerStationsStatusMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_PARTNER_ASSETS_OK_JSON = "gateway-api/expected/getPartnerAssets_ok.json";
    public static final String GATEWAY_API_MOCK_GET_PARTNER_ASSETS_MOCK_JSON = "gateway-api/mock/getPartnerAssetsMock.json";
    public static final String GATEWAY_API_EXPECTED_GET_PARTNER_AVAILABLE_ASSETS_OK_JSON = "gateway-api/expected/getPartnerAvailableAssets_ok.json";
    public static final String GATEWAY_API_MOCK_GET_PARTNER_AVAILABLE_ASSETS_MOCK_JSON = "gateway-api/mock/getPartnerAvailableAssetsMock.json";
    public static final String GATEWAY_API_REQUEST_AROUND_ME_JSON = "gateway-api/request/aroundMeRequest.json";
    public static final String GATEWAY_API_EXPECTED_GET_VEHICLE_TYPES_OK_JSON = "gateway-api/expected/getVehicleTypes.json";
    public static final String GATEWAY_API_EXPECTED_GET_PRICE_LIST_OK_JSON = "gateway-api/expected/priceList.json";
    public static final String GATEWAY_API_EXPECTED_GET_DRIVER_JOURNEYS_OK_JSON = "gateway-api/expected/driverJourneys.json";
    public static final String GATEWAY_API_EXPECTED_GET_PASSENGER_JOURNEYS_OK_JSON = "gateway-api/expected/passengerJourneys.json";
    public static final String GATEWAY_API_MOCK_GET_DRIVER_JOURNEYS_OK_JSON = "gateway-api/mock/getDriverJourneys.json";
    public static final String GATEWAY_API_MOCK_GET_PASSENGER_JOURNEYS_OK_JSON = "gateway-api/mock/getPassengerJourneys.json";
    public static final String GATEWAY_API_EXPECTED_GET_DRIVER_REGULAR_TRIP_OK_JSON = "gateway-api/expected/driverRegularTrip.json";
    public static final String GATEWAY_API_EXPECTED_GET_PASSENGER_REGULAR_TRIP_OK_JSON = "gateway-api/expected/passengerRegularTrip.json";
    public static final String GATEWAY_API_MOCK_GET_DRIVER_REGULAR_TRIP_OK_JSON = "gateway-api/mock/getDriverRegularTrip.json";
    public static final String GATEWAY_API_MOCK_GET_PASSENGER_REGULAR_TRIP_OK_JSON = "gateway-api/mock/getPassengerRegularTrip.json";
    public static final String GATEWAY_API_BOOKING_JSON = "gateway-api/request/postBookingBody.json";
    public static final String GATEWAY_API_POST_MESSAGE_JSON = "gateway-api/request/postMessage.json";
    public static final String GATEWAY_API_POST_BOOKING_EVENT_JSON = "gateway-api/request/postBookingEvent.json";
    public static final String GATEWAY_API_ERROR_JSON = "gateway-api/expected/expectedError.json";
    public static final String GATEWAY_API_CARPOOL_ERROR_JSON = "gateway-api/expected/expectedCarpoolError.json";


    /*
     * Mock MVC
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PartnerServiceImpl mockPartnerServiceImpl;

    @MockBean
    IVServiceImpl mockIVServiceImpl;

    @MockBean
    BookingServiceImpl mockBookingServiceImpl;

    /*
     * Web Application Context
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build();
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, this.objectMapper);
    }

    /**
     * Test list Partners operation
     *
     * @throws Exception
     */
    @Test
    void testGetPartnerMetas() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH, HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PARTNERS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get Partners", null);
    }

    /**
     * Test list Partner by id operation
     *
     * @throws Exception
     */
    @Test
    void testGetPartnerMetasById() throws Exception {
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(GET_PARTNERS_PATH + GET_PARTNER_BY_ID_PATH, PARTNER_ID, "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PARTNER_BY_ID_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get Partners by id", null);
    }

    /**
     * Test list Partner by id operation
     *
     * @throws Exception
     */
    @Test
    void testGetPartnerMetasByType() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + "?partnerType=MSP", HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PARTNERS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get Partners", null);
    }


    /**
     * Test get a global view operation
     *
     * @throws Exception
     */
    @Test
    void testGetGlobalView() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNERS_GLOBAL_VIEW_PATH, HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_GLOBAL_VIEW_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get global-view", null);
    }

    /**
     * Test post around-me operation
     *
     * @throws Exception
     */
    @Test
    void testGetAroundMe() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNERS_AROUND_ME_PATH, HttpMethod.POST, HttpStatus.OK,
                GATEWAY_API_REQUEST_AROUND_ME_JSON, GATEWAY_API_EXPECTED_GET_GLOBAL_VIEW_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test post around-me", null);
    }


    /**
     * Test get a PartnerZone by partnerId and areaType operation
     *
     * @throws Exception
     */
    @Test
    void testGetPartnerZone() throws Exception {
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(GET_PARTNERS_PATH + GET_PARTNER_AREAS_TYPE_PATH, PARTNER_ID, "87930fdf-34c1-41c9-885e-6ce66505b598", AREA_TYPE, OPERATING.value), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PARTNER_ZONE_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get Partner zone", null);
    }


    @Test
    void testGetPartnerZoneWhenTypeZoneIsWrong() throws Exception {
        PartnerZone partnerZoneMoked = createMockedPartnerZone();
        doReturn(partnerZoneMoked).when(mockPartnerServiceImpl).getPartnerZone(any(), any());

        ResultMatcher mockResultMatcher = WsTestUtil.getResultMatcher(HttpStatus.BAD_REQUEST);
        String requestPayloadContent = StringUtils.isNotBlank(null) ? WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + null) : "";
        String uri = CommonUtils.placeholderFormat(GET_PARTNERS_PATH +
                        GET_PARTNER_AREAS_TYPE_PATH, PARTNER_ID, "87930fdf-34c1-41c9-885e-6ce66505b598",
                AREA_TYPE, "blablabla");
        MockHttpServletRequestBuilder requestBuilder = WsTestUtil.getMockHttpServletRequestBuilder("", uri, HttpMethod.GET, requestPayloadContent);
        MediaType responseContentType = WsTestUtil.getMediaType(HttpMethod.GET);
        String content;

        content = this.mockMvc.
                perform(requestBuilder)
                .andExpect(mockResultMatcher)
                .andExpect(MockMvcResultMatchers.content().contentType(responseContentType))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 1531 Error code defines in the converter StringToEnumConverter
        Assertions.assertTrue(content.contains("1531"));
    }

    /**
     * Test list Stations by Partner id operation
     *
     * @throws Exception
     */
    @Test
    void testGetPartnerStations() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_STATIONS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PARTNER_STATIONS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get stations by partnerId", null);
    }

    /**
     * Test list Stations-Status by Partner id operation
     *
     * @throws Exception
     */
    @Test
    void testGetPartnerStationsStatus() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_STATIONS_STATUS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PARTNER_STATIONS_STATUS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get stations status by partnerId", null);
    }


    /**
     * Test list Assets by Partner id operation
     *
     * @throws Exception
     */
    @Test
    void testGetPartnerAssets() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_ASSETS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PARTNER_ASSETS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get assets by partnerId", null);
    }

    @Test
    void testGetPartnerAssetsError() throws Exception {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        Mockito.when(mockIVServiceImpl.getAssets(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"))).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_ASSETS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.NOT_FOUND,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test get assets by partnerId", null);
    }

    @Test
    void testGetPartnerAssetsErrorWithBody() throws Exception {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        Mockito.when(mockIVServiceImpl.getAssets(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"))).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_ASSETS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.BAD_REQUEST,
                null, GATEWAY_API_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get assets by partnerId", null);
    }

    /**
     * Test list available Assets by Partner id operation
     *
     * @throws Exception
     */
    @Test
    void testGetPartnerAvailableAssets() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_AVAILABLE_ASSETS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PARTNER_AVAILABLE_ASSETS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get available assets by partnerId", null);
    }


    @Test
    void testGetPartnerAvailableAssetsError() throws Exception {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND);
        Mockito.when(mockIVServiceImpl.getAvailableAssets(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"), null, null, null, null)).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_AVAILABLE_ASSETS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.NOT_FOUND,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test get available assets by partnerId", null);
    }

    @Test
    void testGetPartnerAvailableAssetsErrorWithBody() throws Exception {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        Mockito.when(mockIVServiceImpl.getAvailableAssets(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"), null, null, null, null)).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_AVAILABLE_ASSETS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.BAD_REQUEST,
                null, GATEWAY_API_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get available assets by partnerId", null);
    }

    /**
     * Test list VehicleTypes
     *
     * @throws Exception
     */
    @Test
    void testGetVehicleTypes() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_VEHICLE_TYPES_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_VEHICLE_TYPES_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get vehicle types by partnerId", null);
    }

    /**
     * Test list PricingPlan
     *
     * @throws Exception
     */
    @Test
    void testGetPartnerPricingPlan() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_PRICING_PLAN_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PRICE_LIST_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get pricing plan  by partnerId", null);
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
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_DRIVER_JOURNEY_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598"),
                HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_DRIVER_JOURNEYS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get driver journeys  by PartnerId", parameters);
    }

    @Test
    void testGetDriverJourneysError() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12));
        parameters.add("departureLng", String.valueOf(12));
        parameters.add("arrivalLat", String.valueOf(12));
        parameters.add("arrivalLng", String.valueOf(12));
        parameters.add("departureDate", String.valueOf(1655452466));

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        Mockito.when(mockIVServiceImpl.getDriverJourneys(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"), 12f, 12f, 12f, 12f, 1655452466, 900, 1f, 1f, null)).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_DRIVER_JOURNEY_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.UNAUTHORIZED,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test get driver journeys by partnerId", parameters);
    }

    @Test
    void testGetDriverJourneysErrorWithBody() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12));
        parameters.add("departureLng", String.valueOf(12));
        parameters.add("arrivalLat", String.valueOf(12));
        parameters.add("arrivalLng", String.valueOf(12));
        parameters.add("departureDate", String.valueOf(1655452466));

        String error = "bad request";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error", error.getBytes(), null);

        Mockito.when(mockIVServiceImpl.getDriverJourneys(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"), 12f, 12f, 12f, 12f, 1655452466, 900, 1f, 1f, null)).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_DRIVER_JOURNEY_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.BAD_REQUEST,
                null, GATEWAY_API_CARPOOL_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get driver journeys by partnerId", parameters);
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
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_PASSENGER_JOURNEY_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PASSENGER_JOURNEYS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get passenger journeys  by PartnerId", parameters);
    }


    @Test
    void testGetPassengerJourneysError() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12));
        parameters.add("departureLng", String.valueOf(12));
        parameters.add("arrivalLat", String.valueOf(12));
        parameters.add("arrivalLng", String.valueOf(12));
        parameters.add("departureDate", String.valueOf(1655452466));

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        Mockito.when(mockIVServiceImpl.getPassengerJourneys(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"), 12f, 12f, 12f, 12f, 1655452466, 900, 1f, 1f, null)).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_PASSENGER_JOURNEY_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.UNAUTHORIZED,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test get passenger journeys by partnerId", parameters);
    }

    @Test
    void testGetPassengerJourneysErrorWithBody() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12));
        parameters.add("departureLng", String.valueOf(12));
        parameters.add("arrivalLat", String.valueOf(12));
        parameters.add("arrivalLng", String.valueOf(12));
        parameters.add("departureDate", String.valueOf(1655452466));

        String error = "bad request";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error", error.getBytes(), null);

        Mockito.when(mockIVServiceImpl.getPassengerJourneys(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"), 12f, 12f, 12f, 12f, 1655452466, 900, 1f, 1f, null)).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_PASSENGER_JOURNEY_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.BAD_REQUEST,
                null, GATEWAY_API_CARPOOL_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get passenger journeys by partnerId", parameters);
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
        parameters.add("departureWeekdays", "MON");
        parameters.add("departureRadius", String.valueOf(12.1f));
        parameters.add("arrivalRadius", String.valueOf(12.1f));
        parameters.add("timeDelta", String.valueOf(900));

        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_DRIVER_REGULAR_TRIPS_PATH.replace("{partnerId}",
                        "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_DRIVER_REGULAR_TRIP_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get driver regular trip  by PartnerId", parameters);
    }

    @Test
    void testGetDriverTripError() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12.1f));
        parameters.add("departureLng", String.valueOf(12.1f));
        parameters.add("arrivalLat", String.valueOf(12f));
        parameters.add("arrivalLng", String.valueOf(1.9f));
        parameters.add("departureTimeOfDay", String.valueOf(7));
        parameters.add("departureWeekdays", "MON");
        parameters.add("departureRadius", String.valueOf(12.1f));
        parameters.add("arrivalRadius", String.valueOf(12.1f));
        parameters.add("timeDelta", String.valueOf(900));

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        List<String> departureWeekdays = new ArrayList<>();
        departureWeekdays.add("MON");
        Mockito.when(mockIVServiceImpl.getDriverRegularTrips(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"),
                12.1f, 12.1f, 12f, 1.9f, "7",
                departureWeekdays, 900, 12.1f, 12.1f, null, null, null)).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_DRIVER_REGULAR_TRIPS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.UNAUTHORIZED,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test get driver regular tri by partnerId", parameters);
    }

    @Test
    void testGetDriverTripErrorWithBody() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12.1f));
        parameters.add("departureLng", String.valueOf(12.1f));
        parameters.add("arrivalLat", String.valueOf(12f));
        parameters.add("arrivalLng", String.valueOf(1.9f));
        parameters.add("departureTimeOfDay", String.valueOf(7));
        parameters.add("departureWeekdays", "MON");
        parameters.add("departureRadius", String.valueOf(12.1f));
        parameters.add("arrivalRadius", String.valueOf(12.1f));
        parameters.add("timeDelta", String.valueOf(900));

        String error = "bad request";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error", error.getBytes(), null);

        List<String> departureWeekdays = new ArrayList<>();
        departureWeekdays.add("MON");
        Mockito.when(mockIVServiceImpl.getDriverRegularTrips(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"),
                12.1f, 12.1f, 12f, 1.9f, "7",
                departureWeekdays, 900, 12.1f, 12.1f, null, null, null)).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_DRIVER_REGULAR_TRIPS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.BAD_REQUEST,
                null, GATEWAY_API_CARPOOL_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get driver regular tri by partnerId", parameters);
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
        parameters.add("departureWeekdays", "MON");
        parameters.add("departureRadius", String.valueOf(12.1f));
        parameters.add("arrivalRadius", String.valueOf(12.1f));
        parameters.add("timeDelta", String.valueOf(900));

        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_PASSENGER_REGULAR_TRIPS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_EXPECTED_GET_PASSENGER_REGULAR_TRIP_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test get Passenger Regular Trip by PartnerId", parameters);
    }

    @Test
    void testGetPassengerRegularTripError() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12.1f));
        parameters.add("departureLng", String.valueOf(12.1f));
        parameters.add("arrivalLat", String.valueOf(12f));
        parameters.add("arrivalLng", String.valueOf(1.9f));
        parameters.add("departureTimeOfDay", "7:30:00 AM");
        parameters.add("departureWeekdays", "MON");
        parameters.add("departureRadius", String.valueOf(12.1f));
        parameters.add("arrivalRadius", String.valueOf(12.1f));
        parameters.add("timeDelta", String.valueOf(900));

        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        List<String> departureWeekdays = new ArrayList<>();
        departureWeekdays.add("MON");
        Mockito.when(mockIVServiceImpl.getPassengerRegularTrips(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"),
                12.1f, 12.1f, 12f, 1.9f, "7:30:00 AM",
                departureWeekdays, 900, 12.1f, 12.1f, null, null, null)).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_PASSENGER_REGULAR_TRIPS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.UNAUTHORIZED,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test get passenger regular tri by partnerId", parameters);
    }

    @Test
    void testGetPassengerRegularTripErrorWithBody() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("departureLat", String.valueOf(12.1f));
        parameters.add("departureLng", String.valueOf(12.1f));
        parameters.add("arrivalLat", String.valueOf(12f));
        parameters.add("arrivalLng", String.valueOf(1.9f));
        parameters.add("departureTimeOfDay", "7:30:00 AM");
        parameters.add("departureWeekdays", "MON");
        parameters.add("departureRadius", String.valueOf(12.1f));
        parameters.add("arrivalRadius", String.valueOf(12.1f));
        parameters.add("timeDelta", String.valueOf(900));

        String error = "bad request";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error", error.getBytes(), null);

        List<String> departureWeekdays = new ArrayList<>();
        departureWeekdays.add("MON");
        Mockito.when(mockIVServiceImpl.getPassengerRegularTrips(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b594"),
                12.1f, 12.1f, 12f, 1.9f, "7:30:00 AM",
                departureWeekdays, 900, 12.1f, 12.1f, null, null, null)).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PARTNER_PASSENGER_REGULAR_TRIPS_PATH.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.GET, HttpStatus.BAD_REQUEST,
                null, GATEWAY_API_CARPOOL_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get driver regular tri by partnerId", parameters);
    }

    /**
     * Test post Carpool Booking
     *
     * @throws Exception
     */
    @Test
    void testPostBookingCarpool() throws Exception {
        //Mock carpooling Booking operations
        Booking mockedBooking = createMockedBooking();
        doReturn(mockedBooking).when(mockBookingServiceImpl).postBooking(any(), any());
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + POST_CARPOOLING_BOOKING.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598"), HttpMethod.POST, HttpStatus.CREATED,
                GATEWAY_API_BOOKING_JSON, GATEWAY_API_BOOKING_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test post booking", null);
    }

    @Test
    void testPostBookingError() throws Exception {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        Mockito.when(mockBookingServiceImpl.postBooking(any(), any())).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + POST_CARPOOLING_BOOKING.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.POST, HttpStatus.UNAUTHORIZED,
                GATEWAY_API_BOOKING_JSON, null,
                JsonResponseTypeEnum.EMPTY, "Test post booking", null);
    }

    @Test
    void testPostBookingErrorWithBody() throws Exception {
        String error = "bad request";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error", error.getBytes(), null);
        Mockito.when(mockBookingServiceImpl.postBooking(any(), any())).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + POST_CARPOOLING_BOOKING.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.POST, HttpStatus.BAD_REQUEST,
                GATEWAY_API_BOOKING_JSON, GATEWAY_API_CARPOOL_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test post booking", null);
    }

    /**
     * Test get carpool Booking
     *
     * @throws Exception
     */
    @Test
    void testGetCarpoolBooking() throws Exception {
        //Mock carpooling Booking operations
        Booking mockedBooking = createMockedBooking();
        doReturn(mockedBooking).when(mockBookingServiceImpl).getBooking(any(), any());
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_CARPOOLING_BOOKING.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598").replace("{bookingId}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"), HttpMethod.GET, HttpStatus.OK,
                null, GATEWAY_API_BOOKING_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get booking", null);
    }

    @Test
    void testGetBookingError() throws Exception {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        Mockito.when(mockBookingServiceImpl.getBooking(any(), any())).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_CARPOOLING_BOOKING.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594").replace("{bookingId}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"), HttpMethod.GET, HttpStatus.UNAUTHORIZED,
                GATEWAY_API_BOOKING_JSON, null,
                JsonResponseTypeEnum.EMPTY, "Test get booking", null);
    }

    @Test
    void testGetBookingErrorWithBody() throws Exception {
        String error = "bad request";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error", error.getBytes(), null);

        Mockito.when(mockBookingServiceImpl.getBooking(any(), any())).thenThrow(exception);
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_CARPOOLING_BOOKING.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594").replace("{bookingId}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"), HttpMethod.GET, HttpStatus.BAD_REQUEST,
                GATEWAY_API_BOOKING_JSON, GATEWAY_API_CARPOOL_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test get booking", null);
    }

    /**
     * Test patch carpool Booking
     *
     * @throws Exception
     */
    @Test
    void testPatchCarpoolBooking() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("status", String.valueOf(BookingStatus.CONFIRMED));
        parameters.add("message", "test");
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + PATCH_CARPOOLING_BOOKING.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b598").replace("{bookingId}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"), HttpMethod.PATCH, HttpStatus.OK,
                null, GATEWAY_API_BOOKING_JSON,
                JsonResponseTypeEnum.EMPTY, "Test patch booking", parameters);
    }

    @Test
    void testPatchBookingError() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("status", String.valueOf(BookingStatus.CONFIRMED));
        parameters.add("message", "test");
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        doThrow(exception).when(mockBookingServiceImpl).patchBooking(any(), any(), any(), any());
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + PATCH_CARPOOLING_BOOKING.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594").replace("{bookingId}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"), HttpMethod.PATCH, HttpStatus.UNAUTHORIZED,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test patch booking", parameters);
    }

    @Test
    void testPatchBookingErrorWithBody() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("status", String.valueOf(BookingStatus.CONFIRMED));
        parameters.add("message", "test");

        String error = "bad request";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error", error.getBytes(), null);

        doThrow(exception).when(mockBookingServiceImpl).patchBooking(any(), any(), any(), any());
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + PATCH_CARPOOLING_BOOKING.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594").replace("{bookingId}", "3fa85f64-5717-4562-b3fc-2c963f66afa6"), HttpMethod.PATCH, HttpStatus.BAD_REQUEST,
                null, GATEWAY_API_CARPOOL_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test patch booking", parameters);
    }


    @Test
    void testPostMessage() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + POST_MESSAGE_MAAS_PARTNER.replace("{partnerId}", "a32eacc8-3302-406d-a4c5-68f6318c486f"), HttpMethod.POST, HttpStatus.CREATED,
                GATEWAY_API_POST_MESSAGE_JSON, null,
                JsonResponseTypeEnum.EMPTY, "Test post message by partnerId", null);
    }

    @Test
    void testPostMessageError() throws Exception {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        doThrow(exception).when(mockIVServiceImpl).postMessage(any(), any());
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + POST_MESSAGE_MAAS_PARTNER.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.POST, HttpStatus.UNAUTHORIZED,
                GATEWAY_API_POST_MESSAGE_JSON, null,
                JsonResponseTypeEnum.EMPTY, "Test post Message", null);
    }

    @Test
    void testPostMessageErrorWithBody() throws Exception {
        String error = "bad request";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error", error.getBytes(), null);

        doThrow(exception).when(mockIVServiceImpl).postMessage(any(), any());
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + POST_MESSAGE_MAAS_PARTNER.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.POST, HttpStatus.BAD_REQUEST,
                GATEWAY_API_POST_MESSAGE_JSON, GATEWAY_API_CARPOOL_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test post Message", null);
    }

    @Test
    void testPostBookingEvent() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + POST_BOOKING_EVENTS.replace("{partnerId}", "a32eacc8-3302-406d-a4c5-68f6318c486f"), HttpMethod.POST, HttpStatus.OK,
                GATEWAY_API_POST_BOOKING_EVENT_JSON, null,
                JsonResponseTypeEnum.EMPTY, "Test post booking event by partnerId", null);
    }

    @Test
    void testPostBookingEventError() throws Exception {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
        doThrow(exception).when(mockBookingServiceImpl).postBookingEvents(any(), any());
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + POST_BOOKING_EVENTS.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.POST, HttpStatus.UNAUTHORIZED,
                GATEWAY_API_POST_BOOKING_EVENT_JSON, null,
                JsonResponseTypeEnum.EMPTY, "Test post booking event", null);
    }

    @Test
    void testPostBookingEventErrorWithBody() throws Exception {
        String error = "bad request";
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "error", error.getBytes(), null);

        doThrow(exception).when(mockBookingServiceImpl).postBookingEvents(any(), any());
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + POST_BOOKING_EVENTS.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b594"), HttpMethod.POST, HttpStatus.BAD_REQUEST,
                GATEWAY_API_POST_BOOKING_EVENT_JSON, GATEWAY_API_CARPOOL_ERROR_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test post booking event", null);
    }

    /**
     * Test status for carpool
     *
     * @throws Exception
     */
    @Test
    void testGetStatus() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_STATUS.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b599"), HttpMethod.GET, HttpStatus.OK,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test get status by partnerId", null);
    }

    /**
     * Test ping for TOMP
     *
     * @throws Exception
     */
    @Test
    void testPing() throws Exception {
        testHttpRequestWithExpectedResult(GET_PARTNERS_PATH + GET_PING.replace("{partnerId}", "87930fdf-34c1-41c9-885e-6ce66505b599"), HttpMethod.GET, HttpStatus.OK,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test get status by partnerId", null);
    }


    /**
     * Central management of the ApiControllerTest
     *
     * @param uri                      the uri of the operation
     * @param httpMethod               HTTP VERB (GET, PUT, POST, PATCH, DELETE)
     * @param httpStatusExpectedResult Https code status expected
     * @param requestPayloadPath       payload of the request (optional for some verbs)
     * @param expectedResultPath       path of the json expected answer file
     * @param resulType                JsonArray or JsonObject expected as result
     * @param message                  Message of the test running
     * @throws Exception
     */
    private void testHttpRequestWithExpectedResult(String uri, HttpMethod httpMethod,
                                                   HttpStatus httpStatusExpectedResult, String requestPayloadPath,
                                                   String expectedResultPath, JsonResponseTypeEnum resulType,
                                                   final String message, MultiValueMap<String, String> parameters) throws Exception {

        List<PartnerMeta> partnerListMocked = createMockedPartnerList();
        GlobalView globalViewMocked = createMockedGlobalView();
        PartnerZone partnerZoneMocked = createMockedPartnerZone();
        List<Station> stationListMocked = createMockedStationList();
        List<StationStatus> stationStatusListMocked = createMockedStationStatusList();
        List<Asset> assetsListMocked = createMockedAssetList();
        List<AssetType> availableAssetsListMocked = createMockedAvailableAssetList();
        List<VehicleTypes> vehicleTypes = createMockedVehicleTypesList();
        List<PriceList> priceListsMocked = createMockedPriceList();

        // Mock carpooling IV operations
        List<DriverJourney> mockedDriverJourneys = createMockedDriverJourneys();
        List<PassengerJourney> mockedPassengerJourneys = createMockedPassengerJourneys();
        List<PassengerRegularTrip> mockedPassengerRegularTrip = createMockedPassengerRegularTrip();
        List<DriverRegularTrip> mockedDriverRegularTrip = createMockedDriverRegularTrip();


        // mock all operations with a stub object from createMocked functions
        doReturn(partnerListMocked).when(mockPartnerServiceImpl).getPartnersMeta();
        doReturn(partnerListMocked).when(mockPartnerServiceImpl).getPartnersMetaByType(any());
        doReturn(partnerListMocked.get(0)).when(mockPartnerServiceImpl).getPartnerMeta(any());
        doReturn(globalViewMocked).when(mockIVServiceImpl).getGlobalView();
        doReturn(globalViewMocked).when(mockIVServiceImpl).getAroundMe(any());
        doReturn(partnerZoneMocked).when(mockPartnerServiceImpl).getPartnerZone(any(), any());
        doReturn(stationListMocked).when(mockIVServiceImpl).getStations(any(), any(), any(), any());
        doReturn(stationStatusListMocked).when(mockIVServiceImpl).getStationStatus(any(), any());
        doReturn(assetsListMocked).when(mockIVServiceImpl).getAssets(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598"));
        doReturn(availableAssetsListMocked).when(mockIVServiceImpl).getAvailableAssets(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598"), null, null, null, null);
        doReturn(vehicleTypes).when(mockIVServiceImpl).getVehicleTypes(any());
        doReturn(priceListsMocked).when(mockIVServiceImpl).getPricingPlan(any(), any());

        doReturn(mockedDriverJourneys).when(mockIVServiceImpl).getDriverJourneys(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598"), 12f, 12f, 12f, 12f, 1655452466, 900, 1f, 1f, null);
        doReturn(mockedPassengerJourneys).when(mockIVServiceImpl).getPassengerJourneys(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598"), 12f, 12f, 12f, 12f, 1655452466, 900, 1f, 1f, null);

        List<String> departureWeekdays = new ArrayList<>();
        departureWeekdays.add("MON");
        doReturn(mockedDriverRegularTrip).when(mockIVServiceImpl).getDriverRegularTrips(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598"),
                12.1f, 12.1f, 12f, 1.9f, "7",
                departureWeekdays, 900, 12.1f, 12.1f, null, null, null);

        doReturn(mockedPassengerRegularTrip).when(mockIVServiceImpl).getPassengerRegularTrips(UUID.fromString("87930fdf-34c1-41c9-885e-6ce66505b598"),
                12.1f, 12.1f, 12f, 1.9f, "7:30:00 AM",
                departureWeekdays, 900, 12.1f, 12.1f, null, null, null);


        // preparing the service call and expected elements
        ResultMatcher mockResultMatcher = WsTestUtil.getResultMatcher(httpStatusExpectedResult);
        String requestPayloadContent = StringUtils.isNotBlank(requestPayloadPath) ? WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + requestPayloadPath) : "";
        MockHttpServletRequestBuilder requestBuilder = WsTestUtil.getMockHttpServletRequestBuilder("", uri, httpMethod, requestPayloadContent);
        MediaType responseContentType = WsTestUtil.getMediaType(httpMethod);

        // trigger the call and check expected encoding and http status code
        String content;
        if (parameters == null || parameters.isEmpty()) {
            if (responseContentType != null && expectedResultPath != null) {
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
            if (expectedResultPath != null) {
                switch (httpMethod) {
                    case GET:
                        content = this.mockMvc.perform(get("" + uri).params(parameters)).andExpect(mockResultMatcher).andExpect(MockMvcResultMatchers.content().contentType(responseContentType)).andReturn().getResponse().getContentAsString();
                        break;
                    case PATCH:
                        content = this.mockMvc.perform(patch("" + uri).params(parameters)).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
                        break;
                    case POST:
                        content = this.mockMvc.perform(post("" + uri).params(parameters)).andExpect(mockResultMatcher).andExpect(MockMvcResultMatchers.content().contentType(responseContentType)).andReturn().getResponse().getContentAsString();
                        break;

                    default:
                        content = null;
                }
            } else {
                switch (httpMethod) {
                    case GET:
                        content = this.mockMvc.perform(get("" + uri).params(parameters)).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
                        break;
                    case PATCH:
                        content = this.mockMvc.perform(patch("" + uri).params(parameters)).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
                        break;
                    case POST:
                        content = this.mockMvc.perform(post("" + uri).params(parameters)).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
                        break;

                    default:
                        content = null;
                }
            }


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
                    if (expectedResultPath.equals(GATEWAY_API_ERROR_JSON)) {
                        Assertions.assertTrue(CommonUtils.isTompErrorFormat(resultJsonObject.toString()));
                    } else {
                        Assertions.assertEquals(expectedJsonObject.toString(4), resultJsonObject.toString(4), message);
                    }
                    break;
                default:
                    break;
            }
            log.info("Test executed : {}", message);
        }
    }

    /**
     * Create mock of 'List<PartnerMeta>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<PartnerMeta> createMockedPartnerList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_PARTNERS_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<PartnerMeta>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }


    /**
     * Create mock of 'GlobalView'
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
     * Create mock of 'PartnerZone'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private PartnerZone createMockedPartnerZone() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_PARTNER_ZONE_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<PartnerZone>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create mock of 'List<Station>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<Station> createMockedStationList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_PARTNER_STATIONS_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Station>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create mock of 'List<StationStatus>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<StationStatus> createMockedStationStatusList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_PARTNER_STATIONS_STATUS_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<StationStatus>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create mock of 'List<Asset>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<Asset> createMockedAssetList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_PARTNER_ASSETS_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Asset>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create mock of 'List<Asset>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<AssetType> createMockedAvailableAssetList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_MOCK_GET_PARTNER_AVAILABLE_ASSETS_MOCK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<AssetType>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create mock of 'List<VehicleTypes>'
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private List<VehicleTypes> createMockedVehicleTypesList() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_EXPECTED_GET_VEHICLE_TYPES_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<VehicleTypes>>() {
        });
        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create mock of 'List<PriceList>'
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
     * Create mock of DriverJourney
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
     * Create mock of PassengerJourneys
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
     * Create mock of PassengerRegularTrip
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
     * Create mock of DriverRegularTrip
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

    /**
     * Create mock of Booking
     *
     * @return Fake objects for mock
     * @throws IOException
     */
    private Booking createMockedBooking() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + GATEWAY_API_BOOKING_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<Booking>() {
        });
        return objectReader.readValue(mockStringyfied);
    }


}
