package com.gateway.commonapi.dto.api;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Details about a PassengerTrip
 */

public class PassengerTrip extends Trip {

    @Schema(required = true, name = "passenger")
    @JsonProperty("passenger")
    private User passenger;



}
