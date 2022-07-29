package com.gateway.database.util.constant;

public class DataMessageDict {
	public static final String FIRST_PLACEHOLDER = "0";

	public static final String MSP_META_WITH_ID_IS_NOT_FOUND = "MspMeta avec l'id {0}";
	public static final String DATA_MAPPER_WITH_ID_IS_NOT_FOUND = "DataMapper avec l'id {0}";
	public static final String DATA_MAPPER_MSP_ACTIONS_ID_IS_NOT_FOUND = "DataMapper avec le MspActions id {0}";

	public static final String MSP_ACTION_WITH_ID_IS_NOT_FOUND = "MspAction avec l'id {0}";
	public static final String MSP_ACTION_WITH_MSP_META_ID_IS_NOT_FOUND = "MspAction avec le MspMeta id {0}";

	public static final String MSP_CALLS_WITH_ID_IS_NOT_FOUND = "MspCalls avec l'id {0}";
	public static final String MSP_CALLS_WITH_MSP_ACTIONS_ID_IS_NOT_FOUND = "MspCalls avec le MspActions id {0}";
	
	public static final String TOKEN_WITH_ID_IS_NOT_FOUND = "Token avec l'id {0}";
	public static final String TOKEN_WITH_MSP_META_ID_IS_NOT_FOUND= "Token avec le MspMeta id {0}";

	public static final String SELECTOR_WITH_ID_SHOULD_NOT_BE_PROVIDED = "Selector avec l'id {0}";

	public static final String BODY_WITH_ID_SHOULD_NOT_BE_PROVIDED = "Body avec l'id {0}";

	public static final String HEADERS_WITH_ID_SHOULD_NOT_BE_PROVIDED = "Headers avec l'id {0}";

	public static final String PARAMS_WITH_ID= "Params avec l'id {0}";
	public static final String PARAMS_MULTI_CALLS_WITH_ID = "ParamsMultiCalls avec l'id {0}";

	public static final String MSP_STANDARD_WITH_ID_IS_NOT_FOUND = "MspStandard avec l'id {0}";
	public static final String MSP_STANDARD_WITH_MSP_META_ID_MSP_ACTIONS_ID_IS_NOT_FOUND = "MspStandard avec le MspMeta id {0} et le MspActions id {1}";
	public static final String MSP_STANDARD_WITH_CRITERIA_NOT_FOUND = "MspStandard avec (MspMeta id {0} ,MspActions id {1}, Version Standard {2}, Version Datamapping {3})";

	private DataMessageDict() {
	}
}
