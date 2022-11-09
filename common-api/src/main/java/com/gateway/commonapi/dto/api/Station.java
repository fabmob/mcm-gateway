package com.gateway.commonapi.dto.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.commonapi.dto.api.geojson.Coordinates;
import com.gateway.commonapi.utils.enums.RentalMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Bean for static information about a station.
 * Based on TOMP-API format
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Static information about a station")
@JsonPropertyOrder({"partnerId", "stationId", "name", "shortName", "coordinates", "physicalAddress", "crossStreet", "contactPhone", "regionId", "rentalMethods", "isVirtualStation", "stationArea", "isParkingLot", "isStreetParking", "isUnderground", "isSidewalkParking", "isParkingHoop", "isParkingHoop", "vehicleCapacity", "vehicleTypeCapacity", "isValet", "isChargingStation", "rentalAndroidUrl", "rentalIosUrl", "rentalWebUrl", "_links"})
public class Station implements Serializable {


    /**
     * partner identifier.
     */
    @Schema(
            name = "partnerId",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            description = "Identifier of the partner",
            required = true)
    @JsonProperty("partnerId")
    @NotNull
    private UUID partnerId;

    /**
     * Station identifier.
     */
    @Schema(
            name = "stationId",
            example = "'XX:Y:12345678",
            description = "Identifier of the station",
            required = true)
    @JsonProperty("stationId")
    @NotNull
    private String stationId;

    /**
     * Station name.
     */
    @Schema(
            name = "name",
            example = "'Island Central",
            description = "Name of the station",
            required = true)
    @JsonProperty("name")
    @NotNull
    private String name;


    /**
     * Short name or other type of identifier.
     */
    @Schema(
            name = "shortName",
            example = "'Island Central",
            description = "Short name or other type of identifier")
    @JsonProperty("shortName")
    @NotNull
    private String shortName;


    /**
     * Coordinates of the station.
     */
    @Schema(
            description = "GPS coordinates of the station",
            required = true)
    @JsonProperty("coordinates")
    @NotNull
    private Coordinates coordinates;


    /**
     * Physical address of the station.
     */
    @Schema(
            name = "physicalAddress",
            description = "Address of the station")
    @JsonProperty("physicalAddress")
    @NotNull
    private PhysicalAddress physicalAddress;

    /**
     * Cross street of where the station is located.
     */
    @Schema(
            name = "crossStreet",
            example = "on the corner with Secondary Road",
            description = "Cross street of where the station is located")
    @JsonProperty("crossStreet")
    private String crossStreet;


    /**
     * Contact phone of the station.
     */
    @Schema(
            name = "contactPhone",
            example = "+33102030405",
            description = "Contact phone of the station")
    @JsonProperty("contactPhone")
    private String contactPhone;

    /**
     * Identifier of the region where station is located.
     */
    @Schema(
            name = "regionId",
            example = "Region1",
            description = "Identifier of the region where the station is located")
    @JsonProperty("regionId")
    private String regionId;

    /**
     * Payment methods accepted at this station.
     */
    @Schema(
            name = "rentalMethods",
            example = "CREDITCARD",
            description = "Payment methods accepted at this station")
    @JsonProperty("rentalMethods")
    private List<RentalMethod> rentalMethods;

    /**
     * Check if the station is location with physical infrastructure.
     */
    @Schema(
            name = "isVirtualStation",
            example = "True",
            description = "True if the station is a location without physical infrastructure, defined by a point (lat/lon) and/or station_area (below). " +
                    " False is the station consists of physical infrastructure (docks).")
    @JsonProperty("isVirtualStation")
    private Boolean isVirtualStation;

    /**
     * A GeoJSON MultiPolygon that describes the area of a virtual station.
     */
    @Schema(
            name = "stationArea",
            description = "A GeoJSON MultiPolygon that describes the area of a virtual station")
    @JsonProperty("stationArea")
    private MultiPolygon stationArea;

    /**
     * True if GBFS parking_type is parking_lot (Off-street parking lot)
     */
    @Schema(
            name = "isParkingLot",
            example = "True",
            description = "True if GBFS parking_type is parking_lot (Off-street parking lot)")
    @JsonProperty("isParkingLot")
    private Boolean isParkingLot;

    /**
     * True if GBFS parking_type is street_parking (Curbside parking)
     */
    @Schema(
            name = "isStreetParking",
            example = "True",
            description = "True if GBFS parking_type is street_parking (Curbside parking)")
    @JsonProperty("isStreetParking")
    private Boolean isStreetParking;

    /**
     * True if GBFS parking_type is underground_parking (Parking that is below street level, station may be non-communicating)
     */
    @Schema(
            name = "isUnderground",
            example = "True",
            description = "True if GBFS parking_type is underground_parking (Parking that is below street level, station may be non-communicating)")
    @JsonProperty("isUnderground")
    private Boolean isUnderground;

    /**
     * True if GBFS parking_type is sidewalk_parking (Park vehicle on sidewalk, out of the pedestrian right of way)
     */
    @Schema(
            name = "isSidewalkParking",
            example = "True",
            description = "True if GBFS parking_type is sidewalk_parking (Park vehicle on sidewalk, out of the pedestrian right of way)")
    @JsonProperty("isSidewalkParking")
    private Boolean isSidewalkParking;

    /**
     * True if parking hoops are present at this station (Parking hoops are lockable devices that are used to secure a parking space to prevent parking of unauthorized vehicles)
     */
    @Schema(
            name = "isParkingHoop",
            example = "True",
            description = "True if parking hoops are present at this station (Parking hoops are lockable devices that are used to secure a parking space to prevent parking of unauthorized vehicles)")
    @JsonProperty("isParkingHoop")
    private Boolean isParkingHoop;

    /**
     * Number of total docking points installed at this station
     */
    @Schema(
            name = "capacity",
            example = "23",
            description = "Number of total docking points installed at this station")
    @JsonProperty("capacity")
    private Integer capacity;


    /**
     * The parking capacity of virtual stations
     */
    @Schema(
            name = "vehicleCapacity",
            description = "the parking capacity of virtual stations ")
    @JsonProperty("vehicleCapacity")
    private Object vehicleCapacity;

    /**
     * The docking capacity of a station where each key is a vehicle_type_id as described in vehicle_types.json
     */
    @Schema(
            name = "vehicleTypeCapacity",
            description = "The docking capacity of a station where each key is a vehicle_type_id as described in vehicle_types.json ")
    @JsonProperty("vehicleTypeCapacity")
    private Object vehicleTypeCapacity;

    /**
     * True if the car park has a valet service
     */
    @Schema(
            name = "isValet",
            example = "true",
            description = "True if the car park has a valet service")
    @JsonProperty("isValet")
    private Boolean isValet;

    /**
     * True if electric vehicle charging is available at this station
     */
    @Schema(
            name = "isChargingStation",
            example = "true",
            description = "True if electric vehicle charging is available at this station")
    @JsonProperty("isChargingStation")
    private Boolean isChargingStation;

    /**
     * Contains rental URIs for Android
     */
    @Schema(
            name = "rentalAndroidUrl",
            example = "https://www.example.com/app?sid=1234567890&platform=android",
            description = "Contains rental URIs for Android")
    @JsonProperty("rentalAndroidUrl")
    private String rentalAndroidUrl;

    /**
     * Contains rental URIs for Ios
     */
    @Schema(
            name = "rentalIosUrl",
            example = "https://www.example.com/app?sid=1234567890&platform=ios",
            description = "Contains rental URIs for Ios")
    @JsonProperty("rentalIosUrl")
    private String rentalIosUrl;

    /**
     * Contains rental URIs for web
     */
    @Schema(
            name = "rentalWebUrl",
            example = "https://www.example.com/app?sid=1234567890",
            description = "Contains rental URIs for web")
    @JsonProperty("rentalWebUrl")
    private String rentalWebUrl;


    public Station(UUID partnerId, String stationId, String name) {
        this.partnerId = partnerId;
        this.stationId = stationId;
        this.name = name;
    }
}

