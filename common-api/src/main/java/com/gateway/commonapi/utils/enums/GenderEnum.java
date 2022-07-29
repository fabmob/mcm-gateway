package com.gateway.commonapi.utils.enums;

/**
 * User's gender. 'O' stands for 'Other'.
 */
public enum GenderEnum {
    F("F"),

    M("M"),

    O("O");

    private String value;

    GenderEnum(String value) {
        this.value = value;
    }
}
