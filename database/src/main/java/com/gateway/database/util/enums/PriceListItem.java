package com.gateway.database.util.enums;

public enum PriceListItem {

    LOWERPRICELIMIT("lowerPriceLimit"),
    UPPERPRICELIMIT("upperPriceLimit"),
    FAREPERUNIT("farePerUnit"),
    FIXEDFARE("fixedFare"),
    UNIT("unit");

    private String value;

    public final String getValue() {
        return value;
    }

    PriceListItem (String value) {
        this.value=value;
    }

}
