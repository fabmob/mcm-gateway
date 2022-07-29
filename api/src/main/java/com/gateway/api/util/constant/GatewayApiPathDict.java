package com.gateway.api.util.constant;

public class GatewayApiPathDict {

    public static final String GET_MSP_BY_ID_PATH = "/{mspId}";

    public static final String GET_AREA_TYPE_PATH = "/{areaType}";

    public static final String GET_MSP_ID_PATH = "?mspId={mspId}";

    public static final String GET_ACTION_NAME_PATH = "&actionName={actionName}";

    public static final String GET_STATION_ID_PATH = "&stationId={stationId}";

    public static final String GET_MSP_STATIONS_STATUS_PATH = GET_MSP_BY_ID_PATH + "/stations-status";

    public static final String GET_MSP_STATIONS_PATH = GET_MSP_BY_ID_PATH + "/stations";


    public static final String GET_MSP_AVAILABLE_ASSETS_PATH = GET_MSP_BY_ID_PATH + "/available-assets";

    public static final String GET_MSP_VEHICLE_TYPES_PATH = GET_MSP_BY_ID_PATH + "/vehicle-types";

    public static final String GET_MSP_PRICING_PLAN_PATH = GET_MSP_BY_ID_PATH + "/system-pricing-plan";


    public static final String GET_CARPOOLING_BY_MSP_ID = GET_MSP_BY_ID_PATH + "/carpooling";

    public static final String GET_MSP_ASSETS_PATH = GET_MSP_BY_ID_PATH + "/assets";

    public static final String GET_MSP_AREAS_TYPE_PATH = GET_MSP_BY_ID_PATH + "/areas" + GET_AREA_TYPE_PATH;

    public static final String GET_MSPS_GLOBAL_VIEW_PATH = "/global-view";

    public static final String GET_MSPS_AROUND_ME_PATH = "/around-me";

    public static final String MSP_META_ENDPOINT = "/msp-metas/";

    public static final String GET_ASSETS_BY_MSP_ID = "/msps/{mspId}/assets";

    /********Carpooling**********/

    public static final String GET_MSP_DRIVER_JOURNEY_PATH = GET_CARPOOLING_BY_MSP_ID + "/driver_journeys";
    public static final String GET_MSP_PASSENGER_JOURNEY_PATH = GET_CARPOOLING_BY_MSP_ID + "/passenger_journeys";
    public static final String GET_MSP_DRIVER_REGULAR_TRIPS_PATH = GET_CARPOOLING_BY_MSP_ID + "/driver_regular_trips";
    public static final String GET_MSP_PASSENGER_REGULAR_TRIPS_PATH = GET_CARPOOLING_BY_MSP_ID + "/passenger_regular_trips";


    public static final String GET_MSPS_PATH = "/msps";
    public static final String MSP_ID = "mspId";
    public static final String ACTION_NAME = "actionName";
    public static final String STATION_ID = "stationId";


    public static final String BASE_ERROR_MESSAGE = "CallId: {0}, {1}";
    public static final String GET_MSP_ZONE_BY_MSP_ID_AND_AREA_TYPE = "/v1/msps/{mspId}/areas/{areaType}";


    private GatewayApiPathDict() {
        throw new IllegalStateException("Utility class");
    }
}
