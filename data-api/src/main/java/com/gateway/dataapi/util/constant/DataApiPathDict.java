package com.gateway.dataapi.util.constant;

public final class DataApiPathDict {

    public static final String ID = "id";

    public static final String MSP_METAS_BASE_PATH = "/msp-metas";
    public static final String MSP_META_PATH = "/{id}";
    public static final String MSP_METAS_PATH = "";

    public static final String MSP_ACTIONS_BASE_PATH = "/msp-actions";
    public static final String MSP_ACTION_PATH = "/{id}";
    public static final String MSP_ACTIONS_PATH = "";

    public static final String MSP_CALLS_BASE_PATH = "/msp-calls";
    public static final String MSP_CALL_PATH = "/{id}";
    public static final String MSP_CALLS_PATH = "";

    public static final String TOKENS_BASE_PATH = "/tokens";
    public static final String TOKEN_PATH = "/{id}";
    public static final String TOKENS_PATH = "";

    public static final String DATA_MAPPER_BASE_PATH = "/data-mappers";
    public static final String DATA_MAPPER_PATH = "/{id}";
    public static final String DATA_MAPPERS_PATH = "";

    public static final String MSP_STANDARD_BASE_PATH= "/msp-standards";
    public static final String MSP_STANDARD_PATH = "/{id}";
    public static final String MSP_STANDARDS_PATH = "";

    public static final String ADAPTERS_BASE_PATH= "/adapters";
    public static final String ADAPTER_PATH = "/{id}";
    public static final String ADAPTERS_PATH = "";

    public static final String DATA_MAPPER_TAG = "dataMappers";
    public static final String MSP_ACTIONS_TAG = "mspActions";
    public static final String MSP_CALLS_TAG = "mspCalls";
    public static final String MSP_METAS_TAG = "mspMetas";
    public static final String TOKENS_TAG = "tokens";
    public static final String MSP_STANDARDS_TAG = "standards";
    public static final String ADAPTERS_TAG = "adapters";
    public static final String CACHE_PARAMTAG = "cacheParam";



    public static final String CACHE_PARAM_BASE_PATH = "/cache-params";
    public static final String CACHE_PARAM_PATH = "/{cacheParamId}";


    private DataApiPathDict() {}
}
