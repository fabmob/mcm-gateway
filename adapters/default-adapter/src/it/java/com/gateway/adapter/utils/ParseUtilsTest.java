package com.gateway.adapter.utils;

import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.data.PartnerActionDTO;
import com.gateway.commonapi.dto.data.PriceListDTO;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.utils.enums.ActionsEnum;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.gateway.commonapi.utils.enums.ActionsEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class ParseUtilsTest {

    @InjectMocks
    private ParseUtils parseUtils;


    @Test
    void testParseToGatewayDTOByAction() throws JSONException {
        List<Object> list = new ArrayList<>();
        JSONObject jsonObjectResponse = new JSONObject();

        // STATION_STATUS_SEARCH
        PartnerActionDTO mspAction = new PartnerActionDTO();
        mspAction.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        mspAction.setAction(STATION_STATUS_SEARCH);
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspAction, mspId);

        Assertions.assertEquals(list.get(0).getClass(), StationStatus.class);

        List<Object> finalList = new ArrayList<>();
        JSONObject response = new JSONObject("{'partnerId':'test'}");
        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(finalList, response, mspAction, mspId));


        // PARKING_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionParking = new PartnerActionDTO();
        mspActionParking.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f890"));
        mspActionParking.setAction(PARKING_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionParking, mspId);

        assertEquals(list.get(0).getClass(), Parking.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(finalList, response, mspActionParking, mspId));

        // STATION_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionStation = new PartnerActionDTO();
        mspActionStation.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f891"));
        mspActionStation.setAction(STATION_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionStation, mspId);

        assertEquals(list.get(0).getClass(), Station.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(finalList, response, mspActionStation, mspId));

        //AVAILABLE_ASSET_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionAvailableAsset = new PartnerActionDTO();
        mspActionAvailableAsset.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f811"));
        mspActionAvailableAsset.setAction(AVAILABLE_ASSET_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionAvailableAsset, mspId);

        assertEquals(list.get(0).getClass(), AssetType.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(finalList, response, mspActionAvailableAsset, mspId));

        //ASSET_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionAsset = new PartnerActionDTO();
        mspActionAsset.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f810"));
        mspActionAsset.setAction(ASSET_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionAsset, mspId);

        assertEquals(list.get(0).getClass(), Asset.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(finalList, response, mspActionAsset, mspId));


        //AROUND_ME_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionAroundMe = new PartnerActionDTO();
        mspActionAroundMe.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionAroundMe.setAction(AROUND_ME_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionAroundMe, mspId);
        assertEquals(list.get(0).getClass(), GlobalView.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(null, response, mspActionAroundMe, mspId));

        //MSP_ZONE_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionMspZone = new PartnerActionDTO();
        mspActionMspZone.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionMspZone.setAction(MSP_ZONE_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionMspZone, mspId);

        assertEquals(list.get(0).getClass(), PartnerZone.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(finalList, response, mspActionMspZone, mspId));

        //VehicleTypes
        list = new ArrayList<>();
        PartnerActionDTO mspActionVehicleTypes = new PartnerActionDTO();
        mspActionVehicleTypes.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionVehicleTypes.setAction(VEHICLE_TYPES_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionVehicleTypes, mspId);

        assertEquals(list.get(0).getClass(), VehicleTypes.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(null, response, mspActionVehicleTypes, mspId));

        //PRICING_PLAN_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionPriceList = new PartnerActionDTO();
        mspActionPriceList.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionPriceList.setAction(PRICING_PLAN_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionPriceList, mspId);

        assertEquals(list.get(0).getClass(), PriceListDTO.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(null, response, mspActionPriceList, mspId));

        //DRIVER_JOURNEYS_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionDriverJourneys = new PartnerActionDTO();
        mspActionDriverJourneys.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionDriverJourneys.setAction(DRIVER_JOURNEYS_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionDriverJourneys, mspId);
        assertEquals(list.get(0).getClass(), DriverJourney.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(null, response, mspActionDriverJourneys, mspId));

        //PASSENGER_JOURNEYS_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionPassengerJourneys = new PartnerActionDTO();
        mspActionPassengerJourneys.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionPassengerJourneys.setAction(PASSENGER_JOURNEYS_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionPassengerJourneys, mspId);
        assertEquals(list.get(0).getClass(), PassengerJourney.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(null, response, mspActionPassengerJourneys, mspId));

        //PASSENGER_REGULAR_TRIPS_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionPassengerRegularTrip = new PartnerActionDTO();
        mspActionPassengerRegularTrip.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionPassengerRegularTrip.setAction(PASSENGER_REGULAR_TRIPS_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionPassengerRegularTrip, mspId);
        assertEquals(list.get(0).getClass(), PassengerRegularTrip.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(null, response, mspActionPassengerRegularTrip, mspId));

        //DRIVER_REGULAR_TRIPS_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionDriverRegularTrip = new PartnerActionDTO();
        mspActionDriverRegularTrip.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f800"));
        mspActionDriverRegularTrip.setAction(DRIVER_REGULAR_TRIPS_SEARCH);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionDriverRegularTrip, mspId);
        assertEquals(list.get(0).getClass(), DriverRegularTrip.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(null, response, mspActionDriverRegularTrip, mspId));


        //CARPOOLING_BOOK
        //CARPOOLING_BOOKING_SEARCH
        list = new ArrayList<>();
        PartnerActionDTO mspActionBookSearch = new PartnerActionDTO();
        mspActionBookSearch.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f813"));
        mspActionBookSearch.setAction(CARPOOLING_BOOK);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspActionBookSearch, mspId);

        assertEquals(list.get(0).getClass(), Booking.class);

        JSONObject response2 = new JSONObject("{'id':'test'}");
        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(finalList, response2, mspActionBookSearch, mspId));


        //Message
        list = new ArrayList<>();
        PartnerActionDTO partnerActionMessage = new PartnerActionDTO();
        partnerActionMessage.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f899"));
        partnerActionMessage.setAction(SEND_MESSAGE);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, partnerActionMessage, mspId);

        assertEquals(list.get(0).getClass(), Message.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(null, response, partnerActionMessage, mspId));


        //BOOKING_EVENT
        list = new ArrayList<>();
        PartnerActionDTO partnerActionBookingEvent = new PartnerActionDTO();
        partnerActionBookingEvent.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f899"));
        partnerActionBookingEvent.setAction(BOOKING_EVENT);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, partnerActionBookingEvent, mspId);

        assertEquals(list.get(0).getClass(), CarpoolBookingEvent.class);

        assertThrows(NotFoundException.class, () -> parseUtils.parseToGatewayDTOByAction(null, response, partnerActionBookingEvent, mspId));

        //STATUS
        list = new ArrayList<>();
        PartnerActionDTO partnerActionStatus = new PartnerActionDTO();
        partnerActionStatus.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f899"));
        partnerActionStatus.setAction(STATUS);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, partnerActionStatus, mspId);

        //PING
        list = new ArrayList<>();
        PartnerActionDTO partnerActionPing = new PartnerActionDTO();
        partnerActionPing.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f899"));
        partnerActionPing.setAction(PING);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, partnerActionPing, mspId);
    }

    @Test
    void testParseToGatewayDTOByActionExceptions() {
        assertThrows(BadRequestException.class, () -> ActionsEnum.fromValue("FAKE_ACTION"));
    }
}