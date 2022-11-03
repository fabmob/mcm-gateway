package com.gateway.commonapi.utils.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.utils.ExceptionUtils;

import java.util.Arrays;
import java.util.List;


public enum ActionsEnum {

    ASSET_SEARCH("ASSET_SEARCH"),
    AROUND_ME_SEARCH("AROUND_ME_SEARCH"),
    MSP_ZONE_SEARCH("MSP_ZONE_SEARCH"),
    STATION_STATUS_SEARCH("STATION_STATUS_SEARCH"),
    STATION_SEARCH("STATION_SEARCH"),
    AVAILABLE_ASSET_SEARCH("AVAILABLE_ASSET_SEARCH"),
    PARKING_SEARCH("PARKING_SEARCH"),
    VEHICLE_TYPES_SEARCH("VEHICLE_TYPES_SEARCH"),
    PRICING_PLAN_SEARCH("PRICING_PLAN_SEARCH"),
    DRIVER_JOURNEYS_SEARCH("DRIVER_JOURNEYS_SEARCH"),
    PASSENGER_JOURNEYS_SEARCH("PASSENGER_JOURNEYS_SEARCH"),
    PASSENGER_REGULAR_TRIPS_SEARCH("PASSENGER_REGULAR_TRIPS_SEARCH"),
    DRIVER_REGULAR_TRIPS_SEARCH("DRIVER_REGULAR_TRIPS_SEARCH"),
    CARPOOLING_BOOK("CARPOOLING_BOOK"),
    CARPOOLING_PATCH_BOOKING("CARPOOLING_PATCH_BOOKING"),
    CARPOOLING_BOOKING_SEARCH("CARPOOLING_BOOKING_SEARCH"),
    SEND_MESSAGE("SEND_MESSAGE"),
    BOOKING_EVENT("BOOKING_EVENT"),
    STATUS("STATUS"),
    PING("PING");


    public final String value;

    ActionsEnum(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static ActionsEnum fromValue(String text) {
        for (ActionsEnum b : ActionsEnum.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new BadRequestException(ExceptionUtils.getBadEnumValueMessage(ActionsEnum.class, text));
    }

    /**
     * Verify if given action is allowed to be cached
     *
     * @param actionsEnum
     */
    public static boolean isCacheableAction(ActionsEnum actionsEnum) {
        boolean isCacheable = true;
        List<ActionsEnum> cacheableActions = Arrays.asList(ASSET_SEARCH, PARKING_SEARCH, STATION_SEARCH, PRICING_PLAN_SEARCH, VEHICLE_TYPES_SEARCH, STATION_STATUS_SEARCH, AVAILABLE_ASSET_SEARCH, MSP_ZONE_SEARCH);
        if (!cacheableActions.contains(actionsEnum)) {
            isCacheable = false;
        }

        return isCacheable;
    }
}
