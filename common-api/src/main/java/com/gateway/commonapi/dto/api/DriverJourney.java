package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.commonapi.utils.enums.TypeEnumDriver;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;

/**
 * Details about a driver journeys
 */

@EqualsAndHashCode
@JsonPropertyOrder({"operator", "passengerPickupLat", "passengerPickupLng", "passengerDropLat",
        "passengerDropLng", "passengerPickupAddress", "passengerDropAddress", "distance", "driverDepartureLat"
        , "driverDepartureLng", "driverArrivalLat", "driverArrivalLng",
        "driverDepartureAddress", "driverArrivalAddress", "duration",
        "journeyPolyline", "preferences", "webUrl", "driver", "departureToPickupWalkingDistance",
        "departureToPickupWalkingDuration", "departureToPickupWalkingPolyline", "dropoffToArrivalWalkingDistance",
        "dropoffToArrivalWalkingDuration", "dropoffToArrivalWalkingPolyline", "car", "id",
        "passengerPickupDate", "driverDepartureDate", "type", "availableSeats", "price", "requestedSeats"})
public class DriverJourney extends DriverTrip {
    private static final long serialVersionUID = 1L;

    /**
     * Journey's id. It MUST be unique for a given operator.
     */
    @Schema(name = "id",
            description = "Journey's id. It MUST be unique for a given operator",
            example = "journey012345")
    @JsonProperty("id")
    public String id = null;

    /**
     * Passenger pickup datetime as a UNIX UTC timestamp in seconds.
     */
    @Schema(name = "passengerPickupDate",
            description = "Passenger pickup datetime as a UNIX UTC timestamp in seconds",
            example = "1655452466",
            required = true)
    @JsonProperty("passengerPickupDate")
    public Integer passengerPickupDate = null;

    /**
     * Driver departure datetime as a UNIX UTC timestamp in seconds.
     */
    @Schema(name = "driverDepartureDate",
            description = "Driver departure datetime as a UNIX UTC timestamp in seconds.",
            example = "1655452466")
    @JsonProperty("driverDepartureDate")
    public Integer driverDepartureDate = null;


    /**
     * URL of the journey on the webservice provider platform. Required to support booking by deeplink.
     **/
    @Schema(name = "webUrl",
            example = "carpool.mycity.com/driverJourney/12345678",
            description = "URL of the journey on the webservice provider platform. Required to support booking by deeplink")
    @JsonProperty("webUrl")
    public String webUrl = null;

    /**
     * Type of journey. A dynamic journey is happening in real time.
     */
    @Schema(name = "type",
            description = "Type of journey. A dynamic journey is happening in real time",
            example = "PLANNED",
            required = true)
    @JsonProperty("type")
    public TypeEnumDriver type = null;


    /**
     * Available seats in the car.
     */
    @Schema(name = "availableSeats",
            description = "Available seats in the car",
            example = "2")
    @JsonProperty("availableSeats")
    public Integer availableSeats = null;

    /**
     * Price
     */
    @Schema(name = "price")
    @JsonProperty("price")
    public Price price = null;

}