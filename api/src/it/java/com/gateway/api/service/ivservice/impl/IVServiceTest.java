package com.gateway.api.service.ivservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.api.ApiITTestCase;
import com.gateway.api.model.*;
import com.gateway.api.service.ivservice.IVService;
import com.gateway.commonapi.tests.WsTestUtil;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


class IVServiceImplTest  extends ApiITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String SERVICE_EXPECTED_GET_GLOBAL_VIEW_OK_JSON = "gateway-api/mockedservices-expected/service_getGlobalView_ok.json";
    public static final String SERVICE_EXPECTED_GET_MSP_STATIONS_OK_JSON = "gateway-api/mockedservices-expected/service_getMspStations_ok.json";
    public static final String SERVICE_EXPECTED_GET_MSP_STATIONS_STATUS_OK_JSON = "gateway-api/mockedservices-expected/service_getMspStationsStatus_ok.json";
    public static final String SERVICE_EXPECTED_GET_MSP_ASSETS_OK_JSON = "gateway-api/mockedservices-expected/service_getMspAssets_ok.json";
    public static final String SERVICE_EXPECTED_GET_MSP_AVAILABLE_ASSETS_OK_JSON = "gateway-api/mockedservices-expected/service_getMspAvailableAssets_ok.json";

    public static final String MOCK_MSP_ID = "14390fdf-34c1-41c9-885e-6ce66505b759";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IVService ivService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testGetStations() throws IOException {

        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+ SERVICE_EXPECTED_GET_MSP_STATIONS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Station>>(){});
        List<Station> expectedResponse = objectReader.readValue(expectedStringyfied);
        assertEquals(expectedResponse,ivService.getStations(UUID.fromString(MOCK_MSP_ID)));
    }

    @Test
    void testGetStationsStatus() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+ SERVICE_EXPECTED_GET_MSP_STATIONS_STATUS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<StationStatus>>(){});
        List<StationStatus> expectedResponse = objectReader.readValue(expectedStringyfied);
        assertEquals(expectedResponse,ivService.getStationStatus(UUID.fromString(MOCK_MSP_ID)));

    }

    @Test
    void testGetAssets() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+ SERVICE_EXPECTED_GET_MSP_ASSETS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Asset>>(){});
        List<Asset> expectedResponse = objectReader.readValue(expectedStringyfied);
        assertEquals(expectedResponse,ivService.getAssets(UUID.fromString(MOCK_MSP_ID)));


    }

    @Test
    void testGetAvailableAssets() throws IOException {

        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+ SERVICE_EXPECTED_GET_MSP_AVAILABLE_ASSETS_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<AssetType>>(){});
        List<AssetType> expectedResponse = objectReader.readValue(expectedStringyfied);
        assertEquals(expectedResponse,ivService.getAvailableAssets(UUID.fromString(MOCK_MSP_ID),null));

    }

    @Test
   void testGetGlobalView() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+ SERVICE_EXPECTED_GET_GLOBAL_VIEW_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<GlobalView>(){});
        GlobalView expectedResponse = objectReader.readValue(expectedStringyfied);
        assertEquals(expectedResponse,ivService.getGlobalView());

    }

    @Test
    void testGetAroundMe() throws IOException {
        String expectedStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+ SERVICE_EXPECTED_GET_GLOBAL_VIEW_OK_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<GlobalView>(){});
        GlobalView expectedResponse = objectReader.readValue(expectedStringyfied);
        List<UUID> mspIds = List.of((UUID.fromString(MOCK_MSP_ID)),(UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b735")),(UUID.fromString("14390fdf-34c1-41c9-885e-6ce669264483")));
        MSPAroundMeRequest mspAroundMeRequest = new MSPAroundMeRequest((float) 6.169639, (float) 52.253279, 100F,200, mspIds);
        assertEquals(expectedResponse,ivService.getAroundMe(mspAroundMeRequest));

    }
}
