package com.gateway.api.model;


import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.api.model.geojson.Point;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Bean for static information about a car park.
 */
@Data
@AllArgsConstructor
@Validated
@Schema(description = "Static information about a parking")
@JsonPropertyOrder({
        "mspId", "parkingId", "name", "url", "location", "address",
        "description", "characteristics", "pictures", "accessTypes", "assetTypes", "actions" , "_links"})
public class Parking  implements Serializable {
    

    /**
     * Default constructor
     */
    public Parking() {
        actions = new ActionList();
    }

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
     * Car park identifier.
     */
    @Schema(
            name="parkingId",
            example="95cae659-d225-454e-9d3b-32a982f73074",
            description="Identifier of the car park",
            required=true)
    @JsonProperty("parkingId")
    @NotNull
    private String parkingId;

    /**
     * Car park name.
     */
    @Schema(
            name="name",
            example="Andrésy - Gare de Maurecourt - Sébastien Rouault",
            description="Name of the car park",
            required=true)
    @JsonProperty("name")
    @NotNull
    private String parkingName;

    /**
     * URL presenting the car park.
     */
    @Schema(
            name="url",
            example="https://zenpark.com/parkings/parking-andresy-gare-de-maurecourt-sebastien-rouault\"",
            description="URL presenting the car park")
    @JsonProperty("url")
    private String url;

    /**
     * Location of the car park.
     */
    @Schema(
            name="location",
            description="Location of the car park")
    @JsonProperty("location")
    private Point location;

    /**
     * Address of the car park.
     */
    @Schema(
            name="address",
            description="Address of the car park")
    @JsonProperty("address")
    private Address address;

    /**
     * Description of the car park.
     */
    @Schema(
            name="description",
            example="<p>Ce parking est situé à proximité de la gare de Maurecourt à Andrésy.</p>",
            description="Description of the car park")
    @JsonProperty("description")
    private String description;

    /**
     * Textual information about the price.
     */
    @Schema(
            name="priceInfo",
            example="À partir de 1,30 €/h",
            description="Textual information about the price")
    @JsonProperty("priceInfo")
    private String priceInfo;

    /**
     * Characteristics of the car park.
     */
    @Schema(
            name="characteristics",
            description="Characteristics of the car park")
    @JsonProperty("characteristics")
    private ParkingCharacteristics characteristics;

    /**
     * Pictures of the car park.
     */
    @Schema(
            name="pictures",
            description="Pictures of the car park")
    @JsonProperty("pictures")
    private List<String> pictures;

    /**
     * Subscriptions of the car park.
     */
    @Schema(
            name="subscriptions",
            description="Subscriptions of the car park")
    @JsonProperty("subscriptions")
    private ParkingSubscriptions subscriptions;

    /**
     * Access types of the car park.
     */
    @Schema(
            name="accessTypes",
            example="[ \"Ticket\", \"External\" ]",
            description="Access types to the car park")
    @JsonProperty("accessTypes")
    private List<String> accessTypes;

    /**
     * Asset types allowed in the car park.
     */
    @Schema(
            name="assetTypes",
            example="[ \"Small\", \"Tall\" ]",
            description="Asset types allowed in the car park")
    @JsonProperty("assetTypes")
    private List<String> assetTypes;

    /**
     * Actions.
     */
    @Schema(
            name="actions",
            description="Actions that can be performed on the parking")
    @JsonProperty("actions")
    @Setter(AccessLevel.NONE)
    private ActionList actions;

    /**
     * Availability of the car park.
     */
    @Schema(
            name="availability",
            description="Availability of the car park")
    @JsonProperty("availability")
    private ParkingAvailability availability;

    public Parking(UUID mspId, String parkingId, String parkingName) {
        this.mspId = mspId;
        this.parkingId = parkingId;
        this.parkingName = parkingName;
    }
}

