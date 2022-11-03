package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;


/**
 * Details about a Trip
 */

@JsonComponent
@Slf4j
@Jacksonized
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Trip implements Serializable {

    @Schema(required = true, description = "The operator identifier. MUST be a Root Domain (example operator.org) owned by the operator or a Fully Qualified Domain Name (example carpool.mycity.com) exclusively operated by the operator. A given operator SHOULD always send the same value.",
            example= "'carpool.mycity.com")
    @JsonProperty("operator")
    private String operator;

    @Schema(required = true, description = "Latitude of the passenger pick-up point.",
            example= "2.293444")
    @JsonProperty("passengerPickupLat")
    private Double passengerPickupLat;

    @Schema(required = true,description = "Longitude of the passenger pick-up point.",
            example= "48.858997")
    @JsonProperty("passengerPickupLng")
    private Double passengerPickupLng;

    @Schema(required = true,description = "Latitude of the passenger drop-off point.",
            example= "1.443332")
    @JsonProperty("passengerDropLat")
    private Double passengerDropLat;

    @Schema(required = true, description = "Longitude of the passenger drop-off point.",
            example= "43.604583")
    @JsonProperty("passengerDropLng")
    private Double passengerDropLng;

    @Schema(description = "String representing the pickup-up address.",
            example= "Pont d'Iéna, Paris")
    @JsonProperty("passengerPickupAddress")
    private String passengerPickupAddress;

    @Schema(description = "String representing the drop-off address.",
            example= "Place du Capitole, Toulouse")
    @JsonProperty("passengerDropAddress")
    private String passengerDropAddress;

    @Schema(description = "Carpooling distance in meters.",
            example= "700000")
    @JsonProperty("distance")
    private Integer distance;

    @Schema(description = "Latitude of the departure.",
            example= "2.293444")
    @JsonProperty("driverDepartureLat")
    private Double driverDepartureLat;

    @Schema(description = "Longitude of the departure.",
            example= "48.858997")
    @JsonProperty("driverDepartureLng")
    private Double driverDepartureLng;

    @Schema(description = "Latitude of the arrival.",
            example= "1.443332")
    @JsonProperty("driverArrivalLat")
    private Double driverArrivalLat;

    @Schema(description = "Longitude of the arrival.",
            example= "43.604583")
    @JsonProperty("driverArrivalLng")
    private Double driverArrivalLng;

    @Schema(description = "String representing the departure address of the driver.",
            example= "Pont d'Iéna, Paris")
    @JsonProperty("driverDepartureAddress")
    private String driverDepartureAddress;

    @Schema(description = "String representing the arrival address of the driver.",
            example= "Place du Capitole, Toulouse")
    @JsonProperty("driverArrivalAddress")
    private String driverArrivalAddress;

    @Schema(required = true, description = "Carpooling duration in seconds.",
            example= "25200")
    @JsonProperty("duration")
    private Integer duration;

    @Schema(description = "Carpooling journey itinerary as a Google Encoded Polyline, compressed at level 5.",
            example= "wweiH_|~L`dmQjjdEx`sLkg^")
    @JsonProperty("journeyPolyline")
    private String journeyPolyline;

    @Schema(name= "preferences")
    @JsonProperty("preferences")
    private Preferences preferences;

    @Schema(description = "URL of the trip on the webservice provider platform.",
            example= "'carpool.mycity.com/driverRegularTrip/12345678")
    @JsonProperty("webUrl")
    private String webUrl;


}
