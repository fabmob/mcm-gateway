package com.gateway.database.util.enums;

public enum Msp {
    NAME ("name"),
    HASHOLD ("hasHold"),
    HASNOPARKINGZONE("hasNoParkingZone"),
    HASOPERATINGZONE("hasOperatingZone"),
    HASPARKING("hasParking"),
    HASPREFPARKINGZONE("hasPrefParkingZone"),
    HASSPEEDLIMITZONE("hasSpeedLimitZone"),
    HASSTATION("hasStation"),
    HASSTATIONSTATUS("hasStationStatus"),
    HASVEHICLE("hasVehicle"),
    ISENABLED("isEnabled"),
    LOGOFORMAT("logoFormat"),
    LOGOURL("logoUrl"),
    OPERATOR("operator"),
    PRIMARYCOLOR("primaryColor"),
    SECONDARYCOLOR("secondaryColor"),
    TYPE("type"),
    URL("url"),
    URLWEBVIEW("urlWebView"),
    PRICELIST("priceList");

    private String value;

    public final String getValue() {
        return value;
    }

    Msp(String value) {
        this.value=value;
    }

}