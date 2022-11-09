package com.gateway.requestrelay.utils.constant;

public class RequestRelayPathDict {

    public static final String REQUEST_RELAY_PATH = "/request-relay";
    public static final String REQUEST_RELAY_FULL_PATH = "/request-relay?protocol=";

    private RequestRelayPathDict() {
        throw new IllegalStateException("Utility class");
    }
}
