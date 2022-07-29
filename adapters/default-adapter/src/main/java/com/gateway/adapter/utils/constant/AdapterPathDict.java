package com.gateway.adapter.utils.constant;

public class AdapterPathDict {

    public static final String GLOBAL_PATH = "";

    public static final String ADAPTER_PATH = "/adapt";

    public static final String GET_TOKEN_PATH = "/tokens";

    public static final String GET_CALLS_PATH = "/msp-calls";

    public static final String GET_ACTION_BY_ID_PATH = "/msp-actions/{actionId}";

    public static final String GET_BY_ACTIONS_ID_PATH = "?mspActionId={actionId}";

    public static final String GET_BY_MSP_META_ID_PATH = "?mspMetaId={mspId}";

    public static final String DEFAULT_ADAPTER_GET_BY_ACTIONS_ID_PATH = "?actionId={actionId}";

    public static final String DEFAULT_ADAPTER_MSP_ID_PATH = "&mspId={mspId}";

    public static final String DEFAULT_ADAPTER_PARAMS_PATH = "&params={params}";

    public AdapterPathDict() {
    }
}
