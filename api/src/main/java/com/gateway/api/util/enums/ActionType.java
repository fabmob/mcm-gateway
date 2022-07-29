package com.gateway.api.util.enums;

/**
 * Enumeration for action types.
 */
public enum ActionType {

	// Actions to be performed on a vehicle
	BOOK_VEHICLE,
	HOLD_VEHICLE,
	INFO_VEHICLE,
	LOCK_VEHICLE,
	REPORT_DEFECT_VEHICLE,
	USE_VEHICLE,

	// Actions to be performed on a station
	INFO_STATION,
	REPORT_DEFECT_STATION,

	// Actions to be performed on a parking
	BOOK_PARKING,
	INFO_PARKING,

	// Actions to be performed globally
	SUBSCRIBE,
}
