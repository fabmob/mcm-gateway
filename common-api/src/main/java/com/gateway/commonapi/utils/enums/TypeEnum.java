package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.utils.ExceptionUtils;

import java.util.Objects;

public enum TypeEnum {
    PUBLIC_TRANSPORT("PUBLIC_TRANSPORT"),
    CARPOOLING("CARPOOLING"),
    PARKING("PARKING"),
    EV_CHARGING("EV_CHARGING"),
    SELF_SERVICE_BICYCLE("SELF_SERVICE_BICYCLE"),
    CAR_SHARING("CAR_SHARING"),
    FREE_FLOATING("FREE_FLOATING"),
    TAXI_VTC("TAXI_VTC"),
    MAAS_APPLICATION("MAAS_APPLICATION"),
    MAAS_EDITOR("MAAS_EDITOR");


    private final String value;

    TypeEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
        for (TypeEnum b : TypeEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new BadRequestException(ExceptionUtils.getBadEnumValueMessage(TypeEnum.class, text));
    }

    /**
     * @param value of the typeEnum
     * @return true if the value is one of MAAS possible values
     */
    public static boolean isMaasType(String value) {
        return (Objects.equals(value, TypeEnum.MAAS_APPLICATION.value) || Objects.equals(value, TypeEnum.MAAS_EDITOR.value));
    }
}
