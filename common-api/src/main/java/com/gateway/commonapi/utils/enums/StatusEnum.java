package com.gateway.commonapi.utils.enums;

/**
 * Gets or Sets status
 */
public enum StatusEnum {
    WAITING_CONFIRMATION("WAITING_CONFIRMATION"),

    CONFIRMED("CONFIRMED"),

    CANCELLED("CANCELLED"),

    COMPLETED_PENDING_VALIDATION("COMPLETED_PENDING_VALIDATION"),

    VALIDATED("VALIDATED");

    private String value;

    StatusEnum(String value) {
        this.value = value;
    }

}