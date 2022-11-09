package com.gateway.commonapi.dto.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Rules")
public class Rules implements Serializable {

    /**
     * Array of IDs of vehicle types for which any restrictions.
     */
    @Schema(
            name = "vehicleTypeId",
            description = "Array of IDs of vehicle types for which any restrictions",
            example = "[\"moped1\", \"car1\"]")
    @JsonProperty("vehicleTypeId")
    private List<String> vehicleTypeId;

    /**
     * Is the undocked (“free floating”) ride allowed to start and end in this zone.
     */
    @Schema(
            name = "isRideAllowed",
            description = "Is the undocked (“free floating”) ride allowed to start and end in this zone.",
            example = "true")
    @JsonProperty("isRideAllowed")
    private Boolean isRideAllowed;

    /**
     *  Is the ride allowed to travel through this zone.
     */
    @Schema(
            name = "isRideThroughAllowed",
            description = " Is the ride allowed to travel through this zone",
            example = "true")
    @JsonProperty("isRideThroughAllowed")
    private Boolean isRideThroughAllowed;
    /**
     * the maximum speed allowed.
     */
    @Schema(
            name = "maximumSpeedKph",
            description = "the maximum speed allowed",
            example = "50")
    @JsonProperty("maximumSpeedKph")
    private Integer maximumSpeedKph;
    /**
     * Can vehicles only be parked at stations defined in station_information.json within this geofence zone.
     */
    @Schema(
            name = "isStationParking",
            description = "Can vehicles only be parked at stations defined in station_information.json within this geofence zone",
            example = "true")
    @JsonProperty("isStationParking")
    private Boolean isStationParking;
}
