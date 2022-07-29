package com.gateway.database.util.enums;

public enum PriceList {
    COMMENT("comment"),
    OUTOFBOUNDFEE("outOfBoundFee"),
    PARKINGFORBIDDENFEE("parkingForbiddenFee"),
    DURATION("duration"),
    DISTANCE("distance");

    private String value;

    public final String getValue() {
        return value;
    }

    PriceList (String value) {
        this.value=value;
    }

}
