package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.TypeEnumDriver;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class UserJourneys implements Serializable {

    /**
     * The operator identifier.
     **/
    @Schema(name = "operator",
            example = "carpool.mycity.com",
            description = "The operator identifier.",
            required = true)
    @JsonProperty("operator")
    public String operator = null;

    /**
     * Latitude of the passenger pick-up point.
     **/
    @Schema(name = "passengerPickupLat",
            example = "2.11111",
            description = "Latitude of the passenger pick-up point",
            required = true)
    @JsonProperty("passengerPickupLat")
    public Double passengerPickupLat = null;

    /**
     * Longitude of the passenger pick-up point.
     **/
    @Schema(name = "passengerPickupLng",
            example = "12.11111",
            description = "Longitude of the passenger pick-up point",
            required = true)
    @JsonProperty("passengerPickupLng")
    public Double passengerPickupLng = null;

    /**
     * Latitude of the passenger drop-off point.
     **/
    @Schema(name = "passengerDropLat",
            example = "48.858997",
            description = "Latitude of the passenger drop-off point",
            required = true)
    @JsonProperty("passengerDropLat")
    public Double passengerDropLat = null;

    /**
     * Longitude of the passenger drop-off point
     **/
    @Schema(name = "passengerDropLng",
            example = "8.158997",
            description = "Longitude of the passenger drop-off point",
            required = true)
    @JsonProperty("passengerDropLng")
    public Double passengerDropLng = null;

    /**
     * String representing the pickup-up address
     **/
    @Schema(name = "passengerPickupAddress",
            example = "Pont d'Iéna, Paris",
            description = "String representing the pickup-up address")
    @JsonProperty("passengerPickupAddress")
    public String passengerPickupAddress = null;

    /**
     * String representing the drop-off address
     **/
    @Schema(name = "passengerDropAddress",
            example = "Place du Capitole, Toulouse",
            description = "String representing the drop-off address")
    @JsonProperty("passengerDropAddress")
    public String passengerDropAddress = null;

    /**
     * Carpooling distance in meters
     **/
    @Schema(name = "distance",
            example = "700000",
            description = "Carpooling distance in meters")
    @JsonProperty("distance")
    public Integer distance = null;

    /**
     * Latitude of the departure
     **/
    @Schema(name = "driverDepartureLat",
            example = "2.293444",
            description = "Latitude of the departure")
    @JsonProperty("driverDepartureLat")
    public Double driverDepartureLat = null;

    /**
     * Longitude of the departure
     **/
    @Schema(name = "driverDepartureLng",
            example = "42.293444",
            description = "Longitude of the departure")
    @JsonProperty("driverDepartureLng")
    public Double driverDepartureLng = null;

    /**
     * Latitude of the arrival
     **/
    @Schema(name = "driverDepartureLng",
            example = "32.293444",
            description = "Latitude of the arrival.")
    @JsonProperty("driverArrivalLat")
    public Double driverArrivalLat = null;

    /**
     * Longitude of the arrival
     **/
    @Schema(name = "driverArrivalLng",
            example = "52.293444",
            description = "Longitude of the arrival")
    @JsonProperty("driverArrivalLng")
    public Double driverArrivalLng = null;

    /**
     * String representing the departure address of the driver.
     **/
    @Schema(name = "driverDepartureAddress",
            example = "Pont d'Iéna, Paris",
            description = "String representing the departure address of the driver")
    @JsonProperty("driverDepartureAddress")
    public String driverDepartureAddress = null;

    /**
     * String representing the arrival address of the driver.
     **/
    @Schema(name = "driverArrivalAddress",
            example = "Place du Capitole, Toulouse",
            description = "String representing the arrival address of the driver")
    @JsonProperty("driverArrivalAddress")
    public String driverArrivalAddress = null;

    /**
     * Carpooling duration in seconds.
     **/
    @Schema(name = "duration",
            example = "Carpooling duration in seconds",
            description = "3400")
    @JsonProperty("duration")
    public Integer duration = null;

    /**
     * Carpooling journey itinerary as a Google Encoded Polyline, compressed at level 5.
     **/
    @Schema(name = "journeyPolyline",
            example = "Carpooling journey itinerary as a Google Encoded Polyline, compressed at level 5",
            description = "wweiH_|~L`dmQjjdEx`sLkg^")
    @JsonProperty("journeyPolyline")
    public String journeyPolyline = null;

    /**
     * Preferences
     **/
    @Schema(name = "preferences")
    @JsonProperty("preferences")
    public Preferences preferences = null;

    /**
     * URL of the journey on the webservice provider platform. Required to support booking by deeplink.
     **/
    @Schema(name = "webUrl",
            example = "URL of the journey on the webservice provider platform. Required to support booking by deeplink",
            description = "carpool.mycity.com/driverJourney/12345678")
    @JsonProperty("webUrl")
    public String webUrl = null;


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
     * Type of journey. A dynamic journey is happening in real time.
     */
    @Schema(name = "type",
            description = "Type of journey. A dynamic journey is happening in real time",
            example = "PLANNED",
            required = true)
    @JsonProperty("type")
    public TypeEnumDriver type = null;
}
