package com.gateway.database.util.enums;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public enum PriceListItemType {
    DISTANCE("distance"),
    DURATION("duration");

    private String value;

    public String getValue() {
        return this.value;
    }
}