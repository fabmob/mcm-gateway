package com.gateway.adapter.utils.constant;

public class AdapterMessageDict {

    public static final String FIRST_PLACEHOLDER = "0";

    public static final String MSP_ID_PARAM = "mspId";

    public static final String ACTION_ID_PARAM = "actionId";

    public static final String PROCESSING_HEADERS = "Processing Headers...";

    public static final String ARGUMENTS_HEADER_PATTERN = "\\{(.*?)\\}";

    public static final String PRE_POCESS = "Pre process value : {finalValue}";

    public static final String CALL_IN_PROGRESS = "call in progress with parameters : {finalValue}";

    public static final String MSP_CALLS_DTO_WITH_THIS_METHOD_IS_NOT_FOUND = "Call with this method : {method}";

    public static final String BASE_ERROR_MESSAGE = "CallId: {0}, {1}";

    public static final String MSP_ACTION_WITH_THIS_MSP_META_ID_IS_NOT_FOUND = "MspAction with MspMeta id {0} ";

    public static final String MISSING_BODY_FIELD = "Field {0} need to be filled in the request body";

    public static final String HIDDEN_TEXT = "******";

    public static final String FORMATTING_DATE_FAILED = "Formatting date failed";

    public static final String ADDING_TIME_FAILED = "Adding time failed";

    public static final String BAD_TIMEZONE_FORMAT_OR_CAN_T_RETRIEVE_IT = "Bad timezone format or can't retrieve it";

    public AdapterMessageDict() {
    }
}
