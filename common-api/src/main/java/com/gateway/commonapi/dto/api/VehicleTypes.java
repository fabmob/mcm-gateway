package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.FormFactor;
import com.gateway.commonapi.utils.enums.PropulsionType;
import com.gateway.commonapi.utils.enums.ReturnConstraint;
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
public class VehicleTypes implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * Unique identifier of a vehicle type.
     */
    @Schema(
            name = "vehicleTypeId",
            example = "abc123",
            description = "Unique identifier of a vehicle type. See Field Types above for ID field requirements.",
            required = true)
    @JsonProperty("vehicleTypeId")
    private String vehicleTypeId;

    /**
     * The vehicle's general form factor.
     */
    @Schema(
            name = "formFactor",
            example = "car",
            description = "The vehicle's general form factor",
            required = true)
    @JsonProperty("formFactor")
    private FormFactor formFactor;


    /**
     * The number of riders (driver included) the vehicle can legally accommodate..
     */
    @Schema(
            name = "riderCapacity",
            example = "12",
            description = "The number of riders (driver included) the vehicle can legally accommodate.")
    @JsonProperty("riderCapacity")
    private Integer riderCapacity;

    /**
     * Cargo volume available in the vehicle
     */
    @Schema(
            name = "cargoVolumeCapacity",
            description = "Cargo volume available in the vehicle")
    @JsonProperty("cargoVolumeCapacity")
    private Integer cargoVolumeCapacity;

    /**
     * The capacity of the vehicle cargo space (excluding passengers), expressed in kilograms.
     */
    @Schema(
            name = "cargoLoadCapacity",
            description = "The capacity of the vehicle cargo space (excluding passengers), expressed in kilograms.")
    @JsonProperty("cargoLoadCapacity")
    private Integer cargoLoadCapacity;

    /**
     * The primary propulsion type of the vehicle.
     */
    @Schema(
            name = "propulsionType",
            description = "The primary propulsion type of the vehicle.\n")
    @JsonProperty("propulsionType")
    private PropulsionType propulsionType;

    /**
     * Vehicle air quality certificate.
     */
    @Schema(
            name = "ecoLabel",
            description = "Vehicle air quality certificate.")
    @JsonProperty("ecoLabel")
    private List<EcoLabel> ecoLabel;

    /**
     * Represents the furthest distance in meters that the vehicle can travel without recharging or refueling when it has the maximum amount of energy potential .
     */
    @Schema(
            name = "maxRangeMeters",
            description = "Represents the furthest distance in meters that the vehicle can travel without recharging or refueling when it has the maximum amount of energy potential ")
    @JsonProperty("maxRangeMeters")
    private String maxRangeMeters;

    /**
     * The public name of this vehicle type
     */
    @Schema(
            name = "name",
            description = "The public name of this vehicle type")
    @JsonProperty("name")
    private String name;

    /**
     * Description of accessories available in the vehicle
     */
    @Schema(
            name = "vehicleAccessories",
            description = "Description of accessories available in the vehicle")
    @JsonProperty("vehicleAccessories")
    private List vehicleAccessories;

    /**
     * Maximum quantity of CO2, in grams, emitted per kilometer, according to the WLTP.
     */
    @Schema(
            name = "gCo2Km",
            description = "Maximum quantity of CO2, in grams, emitted per kilometer, according to the WLTP.")
    @JsonProperty("gCo2Km")
    private Integer gCo2Km;

    /**
     * URL to an image that would assist the user in identifying the vehicle.
     */
    @Schema(
            name = "vehicleImage",
            description = "URL to an image that would assist the user in identifying the vehicle")
    @JsonProperty("vehicleImage")
    private String vehicleImage;

    /**
     * The name of the vehicle manufacturer.
     */
    @Schema(
            name = "make",
            description = "The name of the vehicle manufacturer")
    @JsonProperty("make")
    private String make;


    /**
     * The name of the vehicle model.
     */
    @Schema(
            name = "model",
            description = "The name of the vehicle model")
    @JsonProperty("model")
    private String model;


    /**
     * The color of the vehicle.
     */
    @Schema(
            name = "color",
            description = "The color of the vehicle")
    @JsonProperty("color")
    private String color;

    /**
     * Number of wheels this vehicle type has.
     */
    @Schema(
            name = "wheelCount",
            description = "Number of wheels this vehicle type has")
    @JsonProperty("wheelCount")
    private Integer wheelCount;

    /**
     * The maximum speed in kilometers per hour this vehicle is permitted to reach in accordance with local permit and regulations.
     */
    @Schema(
            name = "maxPermittedSpeed",
            description = "The maximum speed in kilometers per hour this vehicle is permitted to reach in accordance with local permit and regulations.")
    @JsonProperty("maxPermittedSpeed")
    private Integer maxPermittedSpeed;

    /**
     * The rated power of the motor for this vehicle type in watts.
     */
    @Schema(
            name = "ratedPower",
            description = "The rated power of the motor for this vehicle type in watts")
    @JsonProperty("ratedPower")
    private Integer ratedPower;

    /**
     * Maximum time in minutes that a vehicle can be reserved before a rental begins.
     */
    @Schema(
            name = "defaultReserveTime",
            description = "Maximum time in minutes that a vehicle can be reserved before a rental begins")
    @JsonProperty("defaultReserveTime")
    private Integer defaultReserveTime;

    /**
     * The conditions for returning the vehicle at the end of the rental.
     */
    @Schema(
            name = "returnConstraint",
            description = "The conditions for returning the vehicle at the end of the rental")
    @JsonProperty("returnConstraint")
    private ReturnConstraint returnConstraint;

    /**
     * Icons for the vehicle type.
     */
    @Schema(
            name = "vehicleAssets",
            description = "Icons for the vehicle type")
    @JsonProperty("vehicleAssets")
    private VehicleAssets vehicleAssets;


    /**
     * Default pricing plan for this vehicle to be used by trip planning applications for purposes of calculating the cost of a single trip using this vehicle type.
     */
    @Schema(
            name = "defaultPricingPlanId",
            description = "Default pricing plan for this vehicle to be used by trip planning applications for purposes of calculating the cost of a single trip using this vehicle type")
    @JsonProperty("defaultPricingPlanId")
    private String defaultPricingPlanId;


    /**
     * Array of all pricing plan IDs.
     */
    @Schema(
            name = "pricingPlanIds",
            description = "Array of all pricing plan IDs")
    @JsonProperty("pricingPlanIds")
    private List<String> pricingPlanIds;

}