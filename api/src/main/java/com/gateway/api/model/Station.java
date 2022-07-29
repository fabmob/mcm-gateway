package com.gateway.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.api.util.enums.RentalMethod;
import com.gateway.api.model.geojson.Coordinates;
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
 *
 * Based on TOMP-API format
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Static information about a station")
@JsonPropertyOrder({ "mspId", "stationId", "name", "coordinates", "physicalAddress", "crossStreet", "regionId", "rentalMethods" , "_links"})
public class Station  implements Serializable {
    

    /**
     * MSP identifier.
     */
    @Schema(
            name="mspId",
            example= "b814c97e-df56-4651-ac50-11525537964f",
            description="Identifier of the MSP",
            required=true)
    @JsonProperty("mspId")
    @NotNull
    private UUID mspId;

    /**
     * Station identifier.
     */
    @Schema(
            name="stationId",
            example="'XX:Y:12345678",
            description="Identifier of the station",
            required=true)
    @JsonProperty("stationId")
    @NotNull
    private String stationId;

    /**
     * Station name.
     */
    @Schema(
            name="name",
            example="'Island Central",
            description="Name of the station",
            required=true)
    @JsonProperty("name")
    @NotNull
    private String stationName;


    /**
     * Coordinates of the station.
     */
    @Schema(
            description="GPS coordinates of the station",
            required=true)
    @JsonProperty("coordinates")
    @NotNull
    private Coordinates coordinates;



    /**
     * Physical address of the station.
     */
    @Schema(
            name="physicalAddress",
            description="Address of the station")
    @JsonProperty("physicalAddress")
    @NotNull
    private PhysicalAddress physicalAddress;

    /**
     * Cross street of where the station is located.
     */
    @Schema(
            name="crossStreet",
            example="on the corner with Secondary Road",
            description="Cross street of where the station is located")
    @JsonProperty("crossStreet")
    private String crossStreet;

    /**
     * Identifier of the region where station is located.
     */
    @Schema(
            name="regionId",
            example="Region1",
            description="Identifier of the region where the station is located")
    @JsonProperty("regionId")
    private String regionId;



    /**
     * Payment methods accepted at this station.
     */
    @Schema(
            name="rentalMethods",
            example="CREDITCARD",
            description="Payment methods accepted at this station")
    @JsonProperty("rentalMethods")
    private List<RentalMethod> rentalMethods;


    public Station(UUID mspId, String stationId, String stationName) {
        this.mspId = mspId;
        this.stationId = stationId;
        this.stationName = stationName;
    }
}

