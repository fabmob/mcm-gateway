package com.gateway.adapter.utils.constant;

public class AdapterMessageDict {

    public static final String FIRST_PLACEHOLDER = "0";

    public static final String GATEWAY_DTO = "dto";

    public static final String THERE_IS_MORE_THAN_ONE_AUTHENTICATION_ACTION = "***************** There is more than one authentication action !!! ****************";

    public static final String THE_TOKEN_IS_NOT_VALID_OR_NOT_EXIST = "the token is not valid or it not exist";

    public static final String CANNOT_PARSE_TO_GATEWAY_DTO = "Cannot parse to gatway dto : {dto}";

    public static final String UNRECOGNISED_FUNCTION = "Unrecognised formatting function : check field 'format' of dataMapper with dataMapperId = {dataMapperId}";

    public static final String DATA_MAPPER_ID = "dataMapperId";

    public static final String CANNOT_PARSE = "Error parsing MSP response: ";

    public static final String VALUE_NULL_OF_TYPE_JSONOBJECT = "Value null of jsonObject response, cannot be converted to JSONObject ";

    public static final String MISSING_CHAMP_EXTERNE = "Data mapper with id : {0} has no 'champExterne' or 'DefaultValue' ";

    public static final String MSP_ID_PARAM = "mspId";

    public static final String ID_PARAM = "id";

    public static final String ACTION_ID_PARAM = "actionId";

    public static final String MSP_CALL_ID = "mspCallId";

    public static final String NOW = "NOW";

    public static final String OFFSET = "OFFSET";

    public static final String PROCESSING_HEADERS = "Processing Headers...";

    public static final String FINAL_URL = "Final url: {}";

    public static final String FINAL_BODY = "Final body: {}";

    public static final String ORIGINAL_BODY = "ORIGINAL_BODY";

    public static final String FINAL_VALUE = "finalValue";

    public static final String ARGUMENTS_HEADER_PATTERN = "\\{(.*?)\\}";

    public static final String PRE_POCESS = "Pre process value : {finalValue}";

    public static final String CALL_IN_PROGRESS = "call in progress with parameters : {finalValue}";

    public static final String BASE_ERROR_MESSAGE = "CallId: {0}, {1}";

    public static final String MSP_ACTION_WITH_THIS_MSP_META_ID_IS_NOT_FOUND = "MspAction with MspMeta id {0} ";

    public static final String MISSING_BODY_FIELD = "Field {0} need to be filled in the request body";

    public static final String MISSING_FIELD = "Field(s) {0} need(s) to be given";

    public static final String MISSING_VALUE = "No value found for param with param_id={0} ";

    public static final String HIDDEN_TEXT = "******";

    public static final String PROCESSING_PARAMS = "Processing parameters...";

    public static final String NUMBER_OF_CALLS_MUST_BE_GREATER_THAN_ONE = "Number of calls not > 0 for {mspCallId}";

    public static final String FORMATTING_DATE_FAILED = "Formatting date failed";

    public static final String ADDING_TIME_FAILED = "Adding time failed";

    public static final String BAD_TIMEZONE_FORMAT_OR_CAN_T_RETRIEVE_IT = "Bad timezone format or can't retrieve it";
    public static final String RESPONSE_OK = "Response OK";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String RESPONSE_LOG = "Response : ";

    public static final String NOT_LIST = "Cannot apply CONVERT_LIST_TO_STRING() because {value} is not a list of string";

    public static final String FORMATTING_FUNCTION = "Formatting value of DataMapper ";

    public static final String NUMERIC_OPERATOR = "Call of function NUMERIC_OPERATOR, with params: ";

    public static final String CONVERT_LIST_TO_STRING = "Call of function CONVERT_LIST_TO_STRING, with params: \"";

    public static final String FORMAT_DATE = "Call of function FORMAT_DATE, with params: \"";

    public static final String CONVERT_STRING_TO_BOOLEAN = "Call of function CONVERT_STRING_TO_BOOLEAN, with params: ";

    public static final String UNRECOGNISED_OPERATOR = "Operator not recognized, use x, X, *, +, -, /";

    public AdapterMessageDict() {
    }
}
