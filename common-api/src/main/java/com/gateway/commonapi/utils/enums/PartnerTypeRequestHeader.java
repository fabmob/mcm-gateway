package com.gateway.commonapi.utils.enums;

public enum PartnerTypeRequestHeader {
    MSP("MSP"),
    MAAS("MAAS"),
    ADMIN("ADMIN");

    public final String value;

    PartnerTypeRequestHeader(String value) {
        this.value = value;
    }

}
