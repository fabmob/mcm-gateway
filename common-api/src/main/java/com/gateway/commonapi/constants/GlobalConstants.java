package com.gateway.commonapi.constants;

public class GlobalConstants {
    public static final String GATEWAY_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String CORRELATION_ID_HEADER = "Correlation-Id";
    public static final String OUTPUT_STANDARD = "output-standard";
    public static final String VALID_CODES = "valid-codes";
    public static final String UUID_REGEXP = "\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b";
    public static final String ISO8601_DATE_REGEXP = "\\d{4}-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|Z)?";
    public static final long CACHE_PARTNERS_METAS_TTL = 43200;
    public static final String SEPARATOR = ":";

    public static final String BASE_ERROR_MESSAGE = "CallId: {0}, {1}";
    public static final String UNRECOGNISED_FUNCTION = "Unrecognised formatting function : check field 'format' of dataMapper with internalField = {internalField}";
    public static final String INTERNAL_FIELD = "internalField";

    public static final String CACHE_ACTIVATION = "CACHE_ACTIVATION";
    public static final String DEFAULT_VALUE_CACHE_ACTIVATION = "false";

    public static final String REFRESH_CACHE_CALL_FAIL = "Fetching data to refresh cache failed - ";
    public static final String INVALID_BODY = "Body is not a valid {0} object";
    public static final String FIRST_PLACEHOLDER = "0";
    public static final String POSITIONS_OBJECT = "PositionsRequest";

    public static final String LAT_OR_LNG_IS_NULL = "latitude, longitude or radius is null";
    public static final String LON = "lon";
    public static final String LAT = "lat";
    public static final String RAD = "radius";

    public static final String ZONE_TYPE = "ZoneType";
    public static final String AREA_TYPE = "areaType";

    public static final String ERROR_FOR_URL_CALL = "Error calling url {}. {}";
    public static final String LOG_URL_CALL = "Calling {}";

    private GlobalConstants() {
    }
}
