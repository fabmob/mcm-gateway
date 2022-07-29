package com.gateway.requestrelay.utils.constant;

public class RequestRelayMessageDict {

    public static final String CONTENT_TYPE_HEADER = "Content-type";
    public static final String UTF_16_VALUE = "UTF-16";
    public static final String REGEX = "\\P{Print}";
    public static final String CONVERSION_ERROR_MSG = "Error converting to UTF-8";

    private RequestRelayMessageDict() {
        throw new IllegalStateException("Utility class");
    }
}
