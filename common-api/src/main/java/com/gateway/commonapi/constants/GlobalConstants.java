package com.gateway.commonapi.constants;

public class GlobalConstants {
    public static final String GATEWAY_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String CORRELATION_ID_HEADER = "Correlation-Id";
    public static final String UUID_REGEXP = "\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b";
    public static final String ISO8601_DATE_REGEXP = "\\d{4}-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d(\\.\\d+)?(([+-]\\d\\d:\\d\\d)|Z)?";
    public static final int NO_ERROR_CODE = -1;

    private GlobalConstants() {}
}
