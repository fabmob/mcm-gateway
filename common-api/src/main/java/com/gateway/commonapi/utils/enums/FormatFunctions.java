package com.gateway.commonapi.utils.enums;

public enum FormatFunctions {

    NUMERIC_OPERATOR("NUMERIC_OPERATOR"),
    CONVERT_LIST_TO_STRING("CONVERT_LIST_TO_STRING"),
    FORMAT_DATE("FORMAT_DATE"),
    CONVERT_STRING_TO_BOOLEAN("CONVERT_STRING_TO_BOOLEAN");

    private String value;

    public final String getValue() {
        return value;
    }

    FormatFunctions(String value) {
        this.value = value;
    }


}
