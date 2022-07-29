package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * Details about a Passenger Regular Trip
 */
@Data
@ApiModel(parent = PassengerTrip.class)
@JsonPropertyOrder({"operator", "passengerPickupLat", "passengerPickupLng", "passengerDropLat",
        "passengerDropLng", "passengerPickupAddress", "passengerDropAddress", "distance", "driverDepartureLat"
        , "driverDepartureLng", "driverArrivalLat","driverArrivalLng" ,"driverDepartureLng",
        "driverDepartureAddress", "driverArrivalAddress",  "duration",
        "journeyPolyline", "preferences", "webUrl", "passenger", "schedules"})
@Schema(allOf = PassengerTrip.class)
public class PassengerRegularTrip extends PassengerTrip {
    @Schema(name = "schedules")
    @JsonProperty("schedules")
    private List<Schedule> schedules;
}
