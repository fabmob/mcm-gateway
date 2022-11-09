package com.gateway.adapter.utils.constant;

public class AdapterPathDict {

    public static final String GLOBAL_PATH = "";

    public static final String ADAPTER_PATH = "/adapt";

    public static final String GET_TOKEN_PATH = "/tokens";

    public static final String GET_CALLS_PATH = "/partner-calls";

    public static final String PARTNER_ACTIONS_BASE_PATH = "/partner-actions";

    public static final String GET_ACTION_BY_ID_PATH = "/partner-actions/{actionId}";

    public static final String GET_PARTNER_META_BY_ID_PATH = "/partner-metas/{id}";

    public static final String GET_DATA_MAPPER_BY_ID_PATH = "/data-mappers";

    public static final String GET_BY_ACTIONS_ID_PATH = "?partnerActionId={actionId}";

    public static final String REST_PROTOCOL = "?protocol=REST";

    public static final String GET_BY_PARTNER_META_ID_PATH = "?partnerMetaId={partnerId}";

    public static final String DEFAULT_ADAPTER_GET_BY_ACTIONS_ID_PATH = "?actionId={actionId}";

    public static final String DEFAULT_ADAPTER_PARTNER_ID_PATH = "&partnerId={partnerId}";

    private AdapterPathDict() {
    }
}
