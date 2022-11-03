package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;

/**
 * Details about a Driver Trip
 */
//@Schema(allOf = Trip.class)
@EqualsAndHashCode
public class DriverTrip extends Trip {
    @Schema(required = true, name = "driver")
    @JsonProperty("driver")
    private User driver;

    @Schema(description = "Walking distance from the requested departure location to the pick-up location.",
            example = "50")
    @JsonProperty("departureToPickupWalkingDistance")
    private Integer departureToPickupWalkingDistance;

    @Schema(description = "Walking duration from the requested departure location to the pick-up location.",
            example = "60")
    @JsonProperty("departureToPickupWalkingDuration")
    private Integer departureToPickupWalkingDuration;

    @Schema(description = "Walking Google Encoded Polyline from the requested departure location to the pick-up location.",
            example = "c}eiHct~LzCgG")
    @JsonProperty("departureToPickupWalkingPolyline")
    private String departureToPickupWalkingPolyline;

    @Schema(description = "Walking distance to the requested arrival location from the drop-off location.",
            example = "50")
    @JsonProperty("dropoffToArrivalWalkingDistance")
    private Integer dropoffToArrivalWalkingDistance;

    @Schema(description = "Walking duration to the requested arrival location from the drop-off location.",
            example = "60")
    @JsonProperty("dropoffToArrivalWalkingDuration")
    private Integer dropoffToArrivalWalkingDuration;

    @Schema(description = "Walking Google Encoded Polyline to the requested arrival location from the drop-off location.",
            example = "eqciGqwxGRo@")
    @JsonProperty("dropoffToArrivalWalkingPolyline")
    private String dropoffToArrivalWalkingPolyline;

    @Schema(name = "car")
    @JsonProperty("car")
    private Car car;


}
