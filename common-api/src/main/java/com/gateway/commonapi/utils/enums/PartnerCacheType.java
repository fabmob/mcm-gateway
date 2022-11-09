package com.gateway.commonapi.utils.enums;

public enum PartnerCacheType {

    VEHICLE("Vehicle"),
    PARKING("Parking"),
    STATION_STATUS("Station status"),
    STATION_INFORMATION("Station information"),
    ZONE_OPERATING("Operating zone"),
    ZONE_SPEED_LIMIT("Speed limit zone"),
    ZONE_NO_PARKING("No parking zone"),
    ZONE_PREFERENTIAL_PARKING("Preferential parking zone");

    private String value;

    private PartnerCacheType(String value) {
        this.value = value;
    }

    public String getFormattedValue() {
        return value;
    }

}
