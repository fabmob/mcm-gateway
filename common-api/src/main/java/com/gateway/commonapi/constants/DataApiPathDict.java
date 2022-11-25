package com.gateway.commonapi.constants;

public final class DataApiPathDict {

    public static final String ID = "id";
    public static final String PARAM_KEY = "paramKey";

    public static final String PARTNER_METAS_BASE_PATH = "/partner-metas";
    public static final String PARTNER_META_PATH = "/{id}";
    public static final String PARTNER_METAS_PATH = "";

    public static final String PARTNER_ACTIONS_BASE_PATH = "/partner-actions";
    public static final String PARTNER_ACTION_PATH = "/{id}";
    public static final String PARTNER_ACTIONS_PATH = "";

    public static final String PARTNER_CALLS_BASE_PATH = "/partner-calls";
    public static final String PARTNER_CALL_PATH = "/{id}";
    public static final String PARTNER_CALLS_PATH = "";

    public static final String TOKENS_BASE_PATH = "/tokens";
    public static final String TOKEN_PATH = "/{id}";
    public static final String TOKENS_PATH = "";

    public static final String DATA_MAPPER_BASE_PATH = "/data-mappers";
    public static final String DATA_MAPPER_PATH = "/{id}";
    public static final String DATA_MAPPERS_PATH = "";

    public static final String PARTNER_STANDARD_BASE_PATH= "/partner-standards";
    public static final String PARTNER_STANDARD_PATH = "/{id}";
    public static final String PARTNER_STANDARDS_PATH = "";

    public static final String ADAPTERS_BASE_PATH= "/adapters";
    public static final String ADAPTER_PATH = "/{id}";
    public static final String ADAPTERS_PATH = "";

    public static final String DATA_MAPPER_TAG = "DataMappers";
    public static final String PARTNER_ACTIONS_TAG = "PartnerActions";
    public static final String PARTNER_CALLS_TAG = "PartnerCalls";
    public static final String PARTNER_METAS_TAG = "PartnerMetas";
    public static final String TOKENS_TAG = "Tokens";
    public static final String PARTNER_STANDARDS_TAG = "Standards";
    public static final String ADAPTERS_TAG = "Adapters";
    public static final String CACHE_PARAMTAG = "CacheParam";
    public static final String GATEWAY_PARAMS_TAG = "GatewayParams";

    public static final String CACHE_PARAM_BASE_PATH = "/cache-params";
    public static final String CACHE_PARAM_PATH = "/{cacheParamId}";

    public static final String GATEWAY_PARAMS_BASE_PATH = "/gateway-params";
    public static final String GATEWAY_PARAM_PATH = "/{paramKey}";
    public static final String GATEWAY_PARAMS_PATH = "";

    public static final String CACHE_ACTIVATION_BASE_PATH = "/CACHE_ACTIVATION";


    private DataApiPathDict() {
    }
}
