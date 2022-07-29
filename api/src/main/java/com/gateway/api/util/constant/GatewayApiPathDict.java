package com.gateway.api.util.constant;

public class GatewayApiPathDict {

    public static final String GET_MSP_BY_ID_PATH = "/{mspId}";

    public static final String GET_AREA_TYPE_PATH = "/{areaType}";

    public static final String GET_MSP_STATIONS_STATUS_PATH = GET_MSP_BY_ID_PATH + "/stations-status";

    public static final String GET_MSP_STATIONS_PATH = GET_MSP_BY_ID_PATH + "/stations";

    public static final String GET_MSP_AVAILABLE_ASSETS_PATH = GET_MSP_BY_ID_PATH + "/available-assets";

    public static final String GET_MSP_ASSETS_PATH = GET_MSP_BY_ID_PATH + "/assets";

    public static final String GET_MSP_AREAS_TYPE_PATH = GET_MSP_BY_ID_PATH + "/areas" + GET_AREA_TYPE_PATH;

    public static final String GET_MSPS_GLOBAL_VIEW_PATH = "/global-view";

    public static final String GET_MSPS_AROUND_ME_PATH = "/around-me";


    public static final String GET_MSPS_PATH = "/msps";
    public static final String MSP_ID = "mspId";
    public static final String AREA_TYPE = "areaType";


    private GatewayApiPathDict() {
        throw new IllegalStateException("Utility class");
    }
}
