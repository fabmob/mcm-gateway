package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * Bean representing characteristics of a car park.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Schema(description = "Characteristics of a car park")
public class ParkingCharacteristics implements Serializable {


    /**
     * Maximum height for the vehicles in the car park.
     */
    @Schema(
            name="maxHeight",
            example="1.9",
            description="Maximum height for the vehicles in the car park")
    @JsonProperty("maxHeight")
    private Double maxHeight;

    /**
     * True if the car park is indoor.
     */
    @Schema(
            name="indoor",
            description="True if the car park is indoor")
    @JsonProperty("indoor")
    private Boolean indoor;

    /**
     * True if the car park is underground.
     */
    @Schema(
            name="underground",
            description="True if the car park is underground")
    @JsonProperty("underground")
    private Boolean underground;

    /**
     * True if the car park has video monitoring.
     */
    @Schema(
            name="videoMonitoring",
            description="True if the car park has video monitoring")
    @JsonProperty("videoMonitoring")
    private Boolean videoMonitoring;

    /**
     * True if the car park has a valet service.
     */
    @Schema(
            name="valet",
            description="True if the car park has a valet service")
    @JsonProperty("valet")
    private Boolean valetService;

    /**
     * True if the car park is pedestrian friendly.
     */
    @Schema(
            name="pedestrianFriendly",
            description="True if the car park is pedestrian friendly")
    @JsonProperty("pedestrianFriendly")
    private Boolean pedestrianFriendly;

    /**
     * True if the car park is lighted.
     */
    @Schema(
            name="lighted",
            description="True if the car park is lighted")
    @JsonProperty("lighted")
    private Boolean lighted;

    /**
     * True if the car park is guarded.
     */
    @Schema(
            name="guarded",
            description="True if the car park is guarded")
    @JsonProperty("guarded")
    private Boolean guarded;
}

