package com.gateway.database.util.constant;

public class DataMessageDict {

    //PLACEHOLDERS PARAMETERS
    public static final String FIRST_PLACEHOLDER = "0";
    public static final String SECOND_PLACEHOLDER = "1";
    public static final String THIRD_PLACEHOLDER = "2";
    public static final String FOURTH_PLACEHOLDER = "3";

    //PARTNER META ERRORS
    public static final String PARTNER_TYPE_MUST_BELONGS_TO_LIST_OF_PARTNERS = "'partnerType : {0}' must belong to the internal list of partners that exists in PartnerType Enum: {1}";
    public static final String TYPE_MUST_BELONGS_TO_THIS_LIST = "'type : {0}' must belong to the internal list that exists in Type Enum: {1}";

    public static final String PARTNER_META_WITH_ID_IS_NOT_FOUND = "PartnerMeta with id {0}";
    public static final String PARTNER_TYPE_MUST_BE_NOT_NULL = "partnerType field must be not null";
    public static final String PARTNER_TYPE_IS_NOT_COMPATIBLE_WITH_THIS_TYPE = "partnerType {0} is not compatible with type {1}";


    //DATA MAPPER ERRORS
    public static final String DATA_MAPPER_WITH_ID_IS_NOT_FOUND = "DataMapper with id {0}";
    public static final String DATA_MAPPER_PARTNER_ACTIONS_ID_IS_NOT_FOUND = "DataMapper with PartnerActions id {0}";
    public static final String DATA_MAPPER_ALREADY_EXISTS_IN_DB_USE_PUT_INSTEAD = "DataMapper with actionId : {0} and internalField : {1} already exists in DB, use PUT instead";

    //PARTNER ACTION ERRORS
    public static final String PARTNER_ACTION_WITH_ID_IS_NOT_FOUND = "PartnerAction with id {0}";
    public static final String PARTNER_ACTION_WITH_PARTNER_META_ID_IS_NOT_FOUND = "PartnerAction with PartnerMeta id {0}";
    public static final String UPDATE_ACTION_NAME_IS_FORBIDDEN = "Update action name is forbidden";

    //PARTNER CALL ERRORS
    public static final String PARTNER_CALLS_WITH_ID_IS_NOT_FOUND = "PartnerCalls with id {0}";
    public static final String PARTNER_CALLS_WITH_PARTNER_ACTIONS_ID_IS_NOT_FOUND = "PartnerCalls with PartnerActions id {0}";

    //TOKEN ERRORS
    public static final String TOKEN_WITH_ID_IS_NOT_FOUND = "Token with id {0}";
    public static final String TOKEN_WITH_PARTNER_META_ID_IS_NOT_FOUND = "Token with PartnerMeta id {0}";

    //SELECTOR ERRORS
    public static final String SELECTOR_WITH_ID_SHOULD_NOT_BE_PROVIDED = "Selector with id {0}";


    //PARAMS ERRORS
    public static final String PARAMS_WITH_ID = "Params with id {0}";
    public static final String PARAMS_MULTI_CALLS_WITH_ID = "ParamsMultiCalls with id {0}";

    //PARTNER STANDARD ERRORS
    public static final String PARTNER_STANDARD_WITH_ID_IS_NOT_FOUND = "PartnerStandard with id {0}";
    public static final String PARTNER_STANDARD_WITH_CRITERIA_NOT_FOUND = "PartnerStandard with (PartnerMeta id {0} ,PartnerActions id {1},PartnerActions name {2}, Version Standard {3}, Version Datamapping {4},Active {5})";
    public static final String THERE_IS_ALREADY_PARTNER_STANDARD_FOUND = "Can't create new Partner Standard, there is already partner Standard with these parameters. partner ID : {0} ; Action ID : {1} ; Version Datamapping {2} ; Version Standard : {3}";
    public static final String THE_MSP_DOES_NOT_HANDLE_THIS_FEATURE = "The MSP with id {0} does not handle this feature : {1}";

    //ADAPTER ERRORS
    public static final String ADAPTER_WITH_ID_IS_NOT_FOUND = "Adapter with id {0}";
    public static final String UNABLE_TO_DELETE_THIS_ADAPTER_ID_BECAUSE_IT_IS_USED_IN_ONE_OR_SEVERAL_STANDARDS = "Unable to delete this Adapter id {0} because it is used by one or several Standards : {1}";
    public static final String DUPLICATE_VALUE_OF_ADAPTER_NAME = "could not add a duplicate value of adapter-name : {0}";

    //CACHE PARAM ERRORS
    public static final String DATA_ALREADY_EXISTS_IN_DB_USE_PUT_INSTEAD = "Data PartnerId : {0} and ActionType : {1} already exists in DB, use PUT instead";
    public static final String SOMETHING_WENT_WRONG_DURING_SAVE_OPERATION = "Something went wrong during save operation";
    public static final String SOFT_TTL_VALUE_MUST_BE_LESS_THAN_HARD_TTL_AND_REFRESH_DELAY_MUST_BE_5 = "softTTL value must be less than or equal hardTTL, and refreshCacheDelay must be > 5";
    public static final String NOT_FOUND_CACHE_PARAM_PK = "Not found Cache Param : PartnerId : {0} ActionType : {1} is not found";
    public static final String NOT_FOUND_CACHE_PARAM_ID = "Cache Param with id  : Cache Param Id : {0}";
    public static final String PARTNER_META_WITH_ID_IS_NOT_FOUND2 = "Le partner with id {0} est introuvable";
    public static final String NO_RESULTS = "No results with the given datas. PartnerId : {0} ; ActionType : {1}";

    //GATEWAY_PARAMS ERRORS
    public static final String GATEWAY_PARAMS_WITH_ID_IS_NOT_FOUND = "Gateway Param with paramKey {0}";
    public static final String GATEWAY_PARAMS_WITH_ID_ALREADY_EXISTS = "Gateway Param with paramKey {0} already exists";
    public static final String VALID_CACHE_ACTIVATION_VALUES = "Gateway Param CACHE_ACTIVATION allowed values are only 'true' or 'false'";
    public static final String GATEWAY_PARAMS_WITH_NULL_KEY = "Gateway Param's paramKey can not be null";

    //OTHER ERRORS
    public static final String BODY_WITH_ID_SHOULD_NOT_BE_PROVIDED = "Body with id {0}";
    public static final String HEADERS_WITH_ID_SHOULD_NOT_BE_PROVIDED = "Headers with id {0}";

    public static final String PARTNER_ID_MUST_AND_ACTION_TYPE_MUST_BE_NOT_NULL = "PartnerId and actionType must be not null";
    public static final String SOFT_TTL_VALUE_HARD_TTL_AND_REFRESH_DELAY_MUST_BE_NOT_NULL = "Soft TTL, Hard TTL and RefreshCacheDelay must be not null";

    private DataMessageDict() {
    }
}
