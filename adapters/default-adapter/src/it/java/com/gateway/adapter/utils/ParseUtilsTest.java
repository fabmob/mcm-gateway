package com.gateway.adapter.utils;

import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.data.MspActionDTO;
import com.gateway.commonapi.dto.data.PriceListDTO;
import com.gateway.commonapi.dto.data.TokenDTO;
import com.gateway.commonapi.exception.NotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class ParseUtilsTest {

    @InjectMocks
    private ParseUtils parseUtils;

    @Mock
    private ParseUtils parseUtil;

    @Test
    void testParseToGatewayDTOByAction() throws IOException, JSONException {
        List<Object> list = new ArrayList<>();
        JSONObject jsonObjectResponse = new JSONObject();

        MspActionDTO mspAction = new MspActionDTO();
        mspAction.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        mspAction.setAction("STATION_STATUS_SEARCH");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspAction, mspId);

        assertEquals(list.get(0).getClass(), StationStatus.class);

        List<Object> finalList = new ArrayList<>();
        JSONObject response = new JSONObject("{'mspId':'test'}");
        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(finalList, response, mspAction, mspId);
        });


        // PARKING_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionParking = new MspActionDTO();
        mspActionParking.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f890"));
        mspActionParking.setAction("PARKING_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionParking, mspId);

        assertEquals(list.get(0).getClass(), Parking.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(finalList, response, mspActionParking, mspId);
        });

        // STATION_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionStation = new MspActionDTO();
        mspActionStation.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f891"));
        mspActionStation.setAction("STATION_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionStation, mspId);

        assertEquals(list.get(0).getClass(), Station.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(finalList, response, mspActionStation, mspId);
        });

        //AVAILABLE_ASSET_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionAvailableAsset = new MspActionDTO();
        mspActionAvailableAsset.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f811"));
        mspActionAvailableAsset.setAction("AVAILABLE_ASSET_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionAvailableAsset, mspId);

        assertEquals(list.get(0).getClass(), AssetType.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(finalList, response, mspActionAvailableAsset, mspId);
        });

        //ASSET_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionAsset = new MspActionDTO();
        mspActionAsset.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f810"));
        mspActionAsset.setAction("ASSET_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionAsset, mspId);

        assertEquals(list.get(0).getClass(), Asset.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(finalList, response, mspActionAsset, mspId);
        });


        //GLOBAL_VIEW_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionGlobalView = new MspActionDTO();
        mspActionGlobalView.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionGlobalView.setAction("GLOBAL_VIEW_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionGlobalView, mspId);
        assertEquals(list.get(0).getClass(), GlobalView.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(null, response, mspActionGlobalView, mspId);
        });

        //MSP_ZONE_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionMspZone = new MspActionDTO();
        mspActionMspZone.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionMspZone.setAction("MSP_ZONE_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionMspZone, mspId);

        assertEquals(list.get(0).getClass(), MSPZone.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(finalList, response, mspActionMspZone, mspId);
        });

        //VehicleTypes
        list = new ArrayList<>();
        MspActionDTO mspActionVehicleTypes = new MspActionDTO();
        mspActionVehicleTypes.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionVehicleTypes.setAction("VEHICLE_TYPES_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionVehicleTypes, mspId);

        assertEquals(list.get(0).getClass(), VehicleTypes.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(null, response, mspActionVehicleTypes, mspId);
        });
        //PRICING_PLAN_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionPriceList = new MspActionDTO();
        mspActionPriceList.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionPriceList.setAction("PRICING_PLAN_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionPriceList, mspId);

        assertEquals(list.get(0).getClass(), PriceListDTO.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(null, response, mspActionPriceList, mspId);
        });
        //DRIVER_JOURNEYS_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionDriverJourneys = new MspActionDTO();
        mspActionDriverJourneys.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionDriverJourneys.setAction("DRIVER_JOURNEYS_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionDriverJourneys, mspId);
        assertEquals(list.get(0).getClass(), DriverJourney.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(null, response, mspActionDriverJourneys, mspId);
        });
        //PASSENGER_JOURNEYS_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionPassengerJourneys = new MspActionDTO();
        mspActionPassengerJourneys.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionPassengerJourneys.setAction("PASSENGER_JOURNEYS_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionPassengerJourneys, mspId);
        assertEquals(list.get(0).getClass(), PassengerJourney.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(null, response, mspActionPassengerJourneys, mspId);
        });
        //PASSENGER_REGULAR_TRIPS_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionPassengerRegularTrip = new MspActionDTO();
        mspActionPassengerRegularTrip.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionPassengerRegularTrip.setAction("PASSENGER_REGULAR_TRIPS_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionPassengerRegularTrip, mspId);
        assertEquals(list.get(0).getClass(), PassengerRegularTrip.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(null, response, mspActionPassengerRegularTrip, mspId);
        });
        //DRIVER_REGULAR_TRIPS_SEARCH
        list = new ArrayList<>();
        MspActionDTO mspActionDriverRegularTrip = new MspActionDTO();
        mspActionDriverRegularTrip.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionDriverRegularTrip.setAction("DRIVER_REGULAR_TRIPS_SEARCH");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionDriverRegularTrip, mspId);
        assertEquals(list.get(0).getClass(), DriverRegularTrip.class);

        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(null, response, mspActionDriverRegularTrip, mspId);
        });
        //isAuthentication
        list = new ArrayList<>();
        MspActionDTO mspActionAuthentication = new MspActionDTO();
        mspActionAuthentication.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f812"));
        mspActionAuthentication.setAuthentication(true);
        jsonObjectResponse.put("expires_in", "3600");

        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionAuthentication, mspId);

        assertEquals(list.get(0).getClass(), TokenDTO.class);

        JSONObject response2 = new JSONObject("{'mspId':'test'}");
        assertThrows(NotFoundException.class, () -> {
            parseUtils.parseToGatewayDTOByAction(finalList, response2, mspActionAuthentication, mspId);
        });
    }

}