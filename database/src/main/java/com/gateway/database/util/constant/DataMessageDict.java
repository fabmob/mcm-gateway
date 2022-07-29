package com.gateway.database.util.constant;

public class DataMessageDict {

	//PLACEHOLDERS PARAMETERS
	public static final String FIRST_PLACEHOLDER = "0";
	public static final String SECOND_PLACEHOLDER = "1";
	public static final String THIRD_PLACEHOLDER = "2";
	public static final String FOURTH_PLACEHOLDER = "3";

	//MSP META ERRORS
	public static final String MSP_META_WITH_ID_IS_NOT_FOUND = "MspMeta avec l'id {0}";

	//DATA MAPPER ERRORS
	public static final String DATA_MAPPER_WITH_ID_IS_NOT_FOUND = "DataMapper avec l'id {0}";
	public static final String DATA_MAPPER_MSP_ACTIONS_ID_IS_NOT_FOUND = "DataMapper avec le MspActions id {0}";
	public static final String DATA_MAPPER_ALREADY_EXISTS_IN_DB_USE_PUT_INSTEAD = "DataMapper with actionId : {0} and champInterne : {1} already exists in DB, use PUT instead";

	//MSP ACTION ERRORS
	public static final String MSP_ACTION_WITH_ID_IS_NOT_FOUND = "MspAction avec l'id {0}";
	public static final String MSP_ACTION_WITH_MSP_META_ID_IS_NOT_FOUND = "MspAction avec le MspMeta id {0}";

	//MSP CALL ERRORS
	public static final String MSP_CALLS_WITH_ID_IS_NOT_FOUND = "MspCalls avec l'id {0}";
	public static final String MSP_CALLS_WITH_MSP_ACTIONS_ID_IS_NOT_FOUND = "MspCalls avec le MspActions id {0}";

	//TOKEN ERRORS
	public static final String TOKEN_WITH_ID_IS_NOT_FOUND = "Token avec l'id {0}";
	public static final String TOKEN_WITH_MSP_META_ID_IS_NOT_FOUND= "Token avec le MspMeta id {0}";

	//SELECTOR ERRORS
	public static final String SELECTOR_WITH_ID_SHOULD_NOT_BE_PROVIDED = "Selector avec l'id {0}";


	//PARAMS ERRORS
	public static final String PARAMS_WITH_ID= "Params avec l'id {0}";
	public static final String PARAMS_MULTI_CALLS_WITH_ID = "ParamsMultiCalls avec l'id {0}";

	//MSP STANDARD ERRORS
	public static final String MSP_STANDARD_WITH_ID_IS_NOT_FOUND = "MspStandard avec l'id {0}";
	public static final String MSP_STANDARD_WITH_MSP_META_ID_MSP_ACTIONS_ID_IS_NOT_FOUND = "MspStandard avec le MspMeta id {0} et le MspActions id {1}";
	public static final String MSP_STANDARD_WITH_CRITERIA_NOT_FOUND = "MspStandard avec (MspMeta id {0} ,MspActions id {1},MspActions name {2}, Version Standard {3}, Version Datamapping {4},Active {5})";
	public static final String NO_MSP_STANDARD_FOUND = "No msp standard found with these parameters. Msp ID : {0} ; Action ID : {1} ; Version Datamapping {2} ; Version Standard : {3}";
	public static final String THERE_IS_ALREADY_MSP_STANDARD_FOUND = "Can't create new Msp Standard, there is already Msp Standard with these parameters. Msp ID : {0} ; Action ID : {1} ; Version Datamapping {2} ; Version Standard : {3}";

	//ADAPTER ERRORS
	public static final String ADAPTER_WITH_ID_IS_NOT_FOUND = "Adapter avec l'id {0}";
	public static final String UNABLE_TO_DELETE_THIS_ADAPTER_ID_BECAUSE_IT_IS_USED_IN_ONE_OR_SEVERAL_STANDARDS = "Unable to delete this Adapter id {0} because it is used by one or several Standards : {1}";
	public static final String DUPLICATE_VALUE_OF_ADAPTER_NAME = "could not add a duplicate value of adapter-name : {0}";

	//CACHE PARAM ERRORS
	public static final String DATA_ALREADY_EXISTS_IN_DB_USE_PUT_INSTEAD = "Data MspId : {0} and ActionType : {1} already exists in DB, use PUT instead";
	public static final String SOMETHING_WENT_WRONG_DURING_SAVE_OPERATION = "Something went wrong during save operation";
	public static final String SOFT_TTL_VALUE_MUST_BE_LESS_THAN_HARD_TTL_AND_REFRESH_DELAY_MUST_BE_5 = "softTTL value must be less than or equal hardTTL, and refreshCacheDelay must be > 5";
	public static final String NOT_FOUND_CACHE_PARAM_PK = "Not found Cache Param : MspId : {0} ActionType : {1} is not found";
	public static final String NOT_FOUND_CACHE_PARAM_ID = "Cache Param with id  : Cache Param Id : {0}";
	public static final String MSP_META_WITH_ID_IS_NOT_FOUND2 = "Le Msp avec l'id {0} est introuvable";
	public static final String NO_RESULTS = "No results with the given datas. MspId : {0} ; ActionType : {1}";

	//OTHER ERRORS
	public static final String BODY_WITH_ID_SHOULD_NOT_BE_PROVIDED = "Body avec l'id {0}";
	public static final String HEADERS_WITH_ID_SHOULD_NOT_BE_PROVIDED = "Headers avec l'id {0}";

	public static final String MSPID_MUST_AND_ACTION_TYPE_MUST_BE_NOT_NULL = "mspId and actionType must be not null";
	public static final String SOFT_TTL_VALUE_HARD_TTL_AND_REFRESH_DELAY_MUST_BE_NOT_NULL = "Soft TTL, Hard TTL and RefreshCacheDelay must be not null";

	private DataMessageDict() {
	}
}
