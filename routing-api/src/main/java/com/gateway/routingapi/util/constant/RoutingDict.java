package com.gateway.routingapi.util.constant;

public class RoutingDict {
    private RoutingDict() {
    }

    public static final String GLOBAL_PATH = "";

    public static final String ROUTE_PATH = "/route";

    public static final String GET_VERSION_PATH = "/partner-standards";
    public static final String GET_BY_ACTIONS_NAME_PATH = "?partnerActionsName={partnerActionsName}";
    public static final String GET_BY_MSP_META_ID_PATH = "&partnerId={partnerId}";
    public static final String GET_BY_MSP_ACTIONS_ID_PATH = "?actionId={actionId}";
    public static final String GET_IS_ACTIVE_TRUE_PATH = "&isActive=true";
    public static final String BASE_ERROR_MESSAGE = "CallId: {0}, {1}";
    public static final String MSP_ID_PARAM = "partnerId";
    public static final String ACTION_ID_PARAM = "actionId";
    public static final String GET_ADAPTERS_BY_ID_PATH = "/adapters/{adaptersId}";
    public static final String ADAPTERS_ID_PARAM = "adaptersId";

    public static final String ROUTING_MSP_ID_PATH = "?partnerId={partnerId}";
    public static final String ROUTING_ACTION_NAME_ID_PATH = "&actionName={actionName}";
    public static final String PARAMS = "param [%s]=%s";


}
