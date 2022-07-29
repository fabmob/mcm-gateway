package com.gateway.api.util.enums;

public enum ZoneType {
	OPERATING("OPERATING"),
	NO_PARKING("NOPARKING"),
	SPEED_LIMIT("SPEEDLIMIT"),
	PREFERENTIAL_PARKING("PREFERENTIALPARKING");

	public final String value;

	ZoneType(String value) {
		this.value = value;
	}
}