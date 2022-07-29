package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Details about a Driver Regular Trip
 */
@Schema(allOf = DriverTrip.class)
@Validated
@JsonPropertyOrder({"operator", "passengerPickupLat", "passengerPickupLng", "passengerDropLat", "passengerDropLng", "passengerPickupAddress", "passengerDropAddress", "distance", "driverDepartureLat"
        , "driverDepartureLng", "driverArrivalLat","driverArrivalLng" ,"driverDepartureLng", "driverDepartureAddress", "driverArrivalAddress",  "duration", "journeyPolyline",
        "preferences", "webUrl", "driver", "departureToPickupWalkingDistance", "departureToPickupWalkingDuration",
        "departureToPickupWalkingPolyline", "dropoffToArrivalWalkingDistance","dropoffToArrivalWalkingDuration",
        "dropoffToArrivalWalkingPolyline","car", "schedules"})
public class DriverRegularTrip extends DriverTrip {
    @Schema(name = "schedules")
    @JsonProperty("schedules")
    private List<Schedule> schedules;


}
