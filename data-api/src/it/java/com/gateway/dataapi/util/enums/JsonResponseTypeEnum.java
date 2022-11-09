package com.gateway.dataapi.util.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum JsonResponseTypeEnum {
    JSON_ARRAY("JSON_ARRAY"),
    JSON_OBJECT("JSON_OBJECT"),
    EMPTY("empty");

    private final String value;
}