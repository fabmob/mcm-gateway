package com.gateway.commonapi.constants;

public class GatewayApiPathDict {

    public static final String GET_CALLS_PATH = "/partner-calls";

    public static final String GET_BY_ACTIONS_ID_PATH = "?partnerActionId={actionId}";

    public static final String CACHE_PATH = "cache";

    public static final String GET_PARTNER_BY_ID_PATH = "/{partnerId}";

    public static final String GET_AREA_TYPE_PATH = "/{areaType}";

    public static final String GET_PARTNER_ID_PATH = "?partnerId={partnerId}";

    public static final String GET_ACTION_NAME_PATH = "&actionName={actionName}";

    public static final String GET_STATION_ID_PATH = "&stationId={stationId}";

    public static final String GET_PARTNER_STATIONS_STATUS_PATH = GET_PARTNER_BY_ID_PATH + "/stations-status";

    public static final String GET_PARTNER_STATIONS_PATH = GET_PARTNER_BY_ID_PATH + "/stations";


    public static final String GET_PARTNER_AVAILABLE_ASSETS_PATH = GET_PARTNER_BY_ID_PATH + "/available-assets";

    public static final String GET_PARTNER_VEHICLE_TYPES_PATH = GET_PARTNER_BY_ID_PATH + "/vehicle-types";

    public static final String GET_PARTNER_PRICING_PLAN_PATH = GET_PARTNER_BY_ID_PATH + "/system-pricing-plan";


    public static final String GET_PARTNER_ASSETS_PATH = GET_PARTNER_BY_ID_PATH + "/assets";

    public static final String GET_PARTNER_AREAS_TYPE_PATH = GET_PARTNER_BY_ID_PATH + "/areas" + GET_AREA_TYPE_PATH;

    public static final String GET_PARTNERS_GLOBAL_VIEW_PATH = "/global-view";

    public static final String GET_PARTNERS_AROUND_ME_PATH = "/around-me";

    public static final String PARTNER_META_ENDPOINT = "/partner-metas/";

    public static final String PARTNER_TYPE_FILTER = "?partnerType=";

    public static final String PARTNER_METAS_PATH_PARAM = "/partner-metas?";

    public static final String CACHE_PARAM_ENDPOINT = "/cache-params";


    public static final String GET_ASSETS_BY_PARTNER_ID = "/partners/{partnerId}/assets";

    /********Carpooling**********/

    public static final String CARPOOLING_PATH = "/carpooling";


    public static final String GET_CARPOOLING_BY_PARTNER_ID = GET_PARTNER_BY_ID_PATH + CARPOOLING_PATH;
    public static final String GET_PARTNER_DRIVER_JOURNEY_PATH = GET_CARPOOLING_BY_PARTNER_ID + "/driver_journeys";
    public static final String GET_PARTNER_PASSENGER_JOURNEY_PATH = GET_CARPOOLING_BY_PARTNER_ID + "/passenger_journeys";
    public static final String GET_PARTNER_DRIVER_REGULAR_TRIPS_PATH = GET_CARPOOLING_BY_PARTNER_ID + "/driver_regular_trips";
    public static final String GET_PARTNER_PASSENGER_REGULAR_TRIPS_PATH = GET_CARPOOLING_BY_PARTNER_ID + "/passenger_regular_trips";
    public static final String GET_STATUS = GET_PARTNER_BY_ID_PATH + CARPOOLING_PATH + "/status";
    public static final String GET_PING = GET_PARTNER_BY_ID_PATH + "/ping";

    public static final String POST_MESSAGE_MAAS_PARTNER = GET_CARPOOLING_BY_PARTNER_ID + "/messages";
    public static final String POST_BOOKING_EVENTS = GET_CARPOOLING_BY_PARTNER_ID + "/booking_events";
    public static final String POST_CARPOOLING_BOOKING = GET_CARPOOLING_BY_PARTNER_ID + "/bookings";
    public static final String GET_CARPOOLING_BOOKING = GET_CARPOOLING_BY_PARTNER_ID + "/bookings/{bookingId}";
    public static final String PATCH_CARPOOLING_BOOKING = GET_CARPOOLING_BY_PARTNER_ID + "/bookings/{bookingId}";

    public static final String GET_PARTNERS_PATH = "/partners";
    public static final String PARTNER_ID = "partnerId";
    public static final String BOOKING_ID = "bookingId";
    public static final String ACTION_NAME = "actionName";
    public static final String STATION_ID = "stationId";


    public static final String BASE_ERROR_MESSAGE = "CallId: {0}, {1}";
    public static final String GET_PARTNER_ZONE_BY_PARTNER_ID_AND_AREA_TYPE = "/v1/partners/{partnerId}/areas/{areaType}";

    // Parameters descriptions :

    public static final String DEPARTURE_LAT = "Latitude of searched departure point.";
    public static final String DEPARTURE_LNG = "Longitude of searched departure point.";
    public static final String ARRIVAL_LAT = "Latitude of searched arrival point.";
    public static final String ARRIVAL_LNG = "Longitude of searched arrival point.";
    public static final String DEPARTURE_DATE = "Departure datetime using a UNIX UTC timestamp in seconds.";
    public static final String TIME_DELTA = "Time margin in seconds. The retrieved journeys must match the given time parameters within a +timeDelta / -timeDelta interval .";
    public static final String DEPARTURE_RADIUS = "Search radius in kilometers around the departure point.";
    public static final String ARRIVAL_RADIUS = "Search radius in kilometers around the arrival point.";
    public static final String COUNT = "Maximum number of returned results. If missing, all matching results are returned.";
    public static final String DEPARTURE_TIME_OF_DAY = "Departure time of day represented as RFC3339 partial-time.";
    public static final String DEPARTURE_WEEK_DAYS = "Departure days of week. The retrieved trips should have at least one schedule applicable on one of the departureWeekdays.";
    public static final String MIN_DEPARTURE_DAY = "Minimum date of departure for the returned journeys. Datetime using a UNIX UTC timestamp in seconds.";
    public static final String MAX_DEPARTURE_DAY = "Maximum date of departure for the returned journeys. Datetime using a UNIX UTC timestamp in seconds.";

    public static final String STATUS_DESCRIPTION = "New status of the Booking.";
    public static final String MESSAGE = "Free text content of a message. The message can contain explanations on the status change.";


    private GatewayApiPathDict() {
        throw new IllegalStateException("Utility class");
    }
}
