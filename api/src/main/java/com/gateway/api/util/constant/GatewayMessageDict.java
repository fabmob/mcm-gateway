package com.gateway.api.util.constant;

public class GatewayMessageDict {
    private GatewayMessageDict() {
    }

    public static final String IGNORED = "Ignored exception: ";
    public static final String NO_ACTIVE_ACTION_FOUND = "No active version of action";

    //keys name in the list of links in the response body
    public static final String NO_PARKING_ZONE = "noParkingZone";
    public static final String OPERATING_ZONE = "operatingZone";
    public static final String PREFERENTIAL_PARKING_ZONE = "preferentialParkingZone";
    public static final String SPEED_LIMIT_ZONE = "speedLimitZone";
    public static final String STATIONS = "stations";
    public static final String STATIONS_STATUS = "stationsStatus";
    public static final String ASSETS = "assets";
    public static final String VEHICLE_TYPES = "vehicleTypes";
    public static final String PRICING_PLAN = "pricingPlan";
    public static final String PING_BIS = "ping";
    public static final String CARPOOLING_BOOKING_POST = "carpoolingBookingPost";
    public static final String CARPOOLING_BOOKING_PATCH = "carpoolingBookingPatch";
    public static final String CARPOOLING_BOOKING_GET = "carpoolingBookingGet";
    public static final String CARPOOLING_DRIVER_JOURNEY = "carpoolingDriverJourney";
    public static final String CARPOOLING_PASSENGER_JOURNEY = "carpoolingPassengerJourney";
    public static final String CARPOOLING_DRIVER_TRIP = "carpoolingDriverTrip";
    public static final String CARPOOLING_PASSENGER_TRIP = "carpoolingPassengerTrip";
    public static final String CARPOOLING_MESSAGES = "carpoolingMessages";
    public static final String CARPOOLING_STATUS = "carpoolingStatus";
    public static final String AROUND_ME_KEY_NAME = "aroundMe";
    public static final String GLOBAL_VIEW_KEY_NAME = "globalView";
    public static final String BOOKING_EVENT_KEY_NAME = "carpoolingBookingEventsPost";


    //log errors when link cannot be generated when msp does not handle a feature
    public static final String NO_STATIONS_STATUS_FOR_PARTNER_IDENTIFIER = "No stations status for MSP identifier {}";
    public static final String NO_STATIONS_FOR_PARTNER_IDENTIFIER = "No stations for MSP identifier {}";
    public static final String NO_VEHICLES_FOR_PARTNER_IDENTIFIER = "No Vehicles for MSP identifier {}";
    public static final String NO_SPEED_LIMIT_AREA_FOR_PARTNER_IDENTIFIER = "No Speed limit area for MSP identifier {}";
    public static final String NO_PREFERENTIAL_PARKING_AREA_FOR_PARTNER_IDENTIFIER = "No preferential parking area for MSP identifier {}";
    public static final String NO_PROHIBITED_PARKING_AREA_FOR_PARTNER_IDENTIFIER = "No prohibited parking area for MSP identifier {}";
    public static final String NO_OPERATING_AREA_FOR_PARTNER_IDENTIFIER = "No operating area for MSP identifier {}";
    public static final String NO_METADATA_FOR_PARTNER_IDENTIFIER = "No metadata for MSP identifier {}";
    public static final String NO_VEHICLE_TYPES_FOR_PARTNER_IDENTIFIER = "No Vehicle Types for MSP identifier {}";
    public static final String NO_PRICING_PLAN_FOR_PARTNER_IDENTIFIER = "No pricing plan for MSP identifier {}";
    public static final String NO_PING_FOR_PARTNER_IDENTIFIER = "No ping for MSP identifier {}";
    public static final String NO_CARPOOL_BOOKING_POST_FOR_PARTNER_IDENTIFIER = "No carpool booking POST for MSP identifier {}";
    public static final String NO_CARPOOL_BOOKING_PATCH_FOR_PARTNER_IDENTIFIER = "No carpool booking PATCH for MSP identifier {}";
    public static final String NO_CARPOOL_BOOKING_GET_FOR_PARTNER_IDENTIFIER = "No carpool booking GET for MSP identifier {}";
    public static final String NO_CARPOOL_DRIVER_JOURNEY_GET_FOR_PARTNER_IDENTIFIER = "No carpool driver journey for MSP identifier {}";
    public static final String NO_CARPOOL_PASSENGER_JOURNEY_FOR_PARTNER_IDENTIFIER = "No carpool passenger journey for MSP identifier {}";
    public static final String NO_CARPOOL_DRIVER_TRIP_FOR_PARTNER_IDENTIFIER = "No carpool driver trip for MSP identifier {}";
    public static final String NO_CARPOOL_PASSENGER_TRIP_FOR_PARTNER_IDENTIFIER = "No carpool passenger trip for MSP identifier {}";
    public static final String NO_CARPOOL_MESSAGES_GET_FOR_PARTNER_IDENTIFIER = "No carpool messages for MSP identifier {}";
    public static final String NO_CARPOOL_STATUS_GET_FOR_PARTNER_IDENTIFIER = "No carpool status for MSP identifier {}";
    public static final String NO_AROUND_ME_SEARCH_AVAILABLE = "No aroundMe for MSP identifier {}";
    public static final String NO_GLOBAL_VIEW_AVAILABLE = "No globalView for MSP identifier {}";
    public static final String NO_CARPOOL_BOOKING_EVENT_FOR_PARTNER_IDENTIFIER = "No carpool booking event for MSP identifier {}";


    public static final String ZONE_TYPE = "ZoneType";
    public static final String AREA_TYPE = "areaType";
    public static final String FOR_PARTNER_WITH_ID_PARTNER_ID = " for MSP with id : {mspId} ";
    public static final String CALL_ID_MESSAGE_PATTERN = "CallId: {0}, {1}";
    public static final String MISSING_FIELD = "Field(s) {0} need(s) to be provided";
    public static final String INVALID_BODY = "Passed body is not a valid {0} object";
    public static final String MESSAGE_OBJECT = "Message";
    public static final String BOOKING_OBJECT = "Booking";
    public static final String BOOKING_EVENT_OBJECT = "CarpoolBookingEvent";
    public static final String FIRST_PLACEHOLDER = "0";
    public static final String SECOND_PLACEHOLDER = "1";
    public static final String THIRD_PLACEHOLDER = "2";

    public static final String IMPOSSIBLE_GEOLOCALISATION = "If you want geolocated data, please insert all parameters, or none to have the exhaustive list";

    public static final String GET_FROM_CACHE_ERREUR = "Cache on {0} for partner with id: {partnerId}";
    public static final String GET_ALL_FROM_CACHE_ERREUR = "Cache for all {0}";


    public static final String ERROR_FOR_URL_CALL = "Error calling url {}. {}";
    public static final String LOG_URL_CALL = "Calling {}";

    public static final String PARTNER_META_DOES_NOT_CORRESPOND_TO_PARTNER = "This partner meta with id : {0} does not correspond to msp meta  ";

}
