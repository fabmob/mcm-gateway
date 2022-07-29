package com.gateway.api.util.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This indicates that this set of rental hours applies to either members or non-members only.
 */
public enum UserTypeEnum {
    MEMBER("MEMBER"),

    NON_MEMBERS("NON_MEMBERS");

    private String value;

    UserTypeEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static UserTypeEnum fromValue(String text) {
        for (UserTypeEnum userType : UserTypeEnum.values()) {
            if (String.valueOf(userType.value).equals(text)) {
                return userType;
            }
        }
        return null;
    }
}
