package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Details about a Passenger journeys
 */

@EqualsAndHashCode
@JsonPropertyOrder({"operator", "passengerPickupLat", "passengerPickupLng", "passengerDropLat",
        "passengerDropLng", "passengerPickupAddress", "passengerDropAddress", "distance", "driverDepartureLat"
        , "driverDepartureLng", "driverArrivalLat", "driverArrivalLng",
        "driverDepartureAddress", "driverArrivalAddress", "duration",
        "journeyPolyline", "preferences", "webUrl", "passenger", "id", "passengerPickupDate",
        "driverDepartureDate", "type", "requestedSeats"})
public class PassengerJourneys extends UserJourneys implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Description of a user
     **/
    @Schema(required = true, description = "Description of a user")
    @JsonProperty("passenger")
    public User passenger = null;

    /**
     * Requested seats by the passenger.
     */
    @Schema(name = "requestedSeats",
            description = "Available seats in the car",
            example = "2")
    @JsonProperty("requestedSeats")
    public Integer requestedSeats = null;
}
