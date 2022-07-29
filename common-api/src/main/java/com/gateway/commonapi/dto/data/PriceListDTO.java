package com.gateway.commonapi.dto.data;


import java.util.List;
import java.util.UUID;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonComponent
@Data
@Schema(description = "Information about a price list")
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"priceListId", "comment", "duration", "distance", "parkingForbiddenFee", "outOfBoundFee", "duration", "distance"})
public class PriceListDTO {

    /**
     * price list DTO identifier
     */
    @Schema(
            name = "priceListId",
            description = "pricelist identifier",
            example = "b814c97e-df56-4651-ac50-115255379666",
            required = true)
    @JsonProperty("priceListId")
    private UUID priceListId;

    /**
     * Comment on the price list.
     */
    @Schema(
            name = "comment",
            description = "Comment on the price list",
            example = "Trotti price list")
    @JsonProperty("comment")
    private String comment;

    /**
     * Price list based on duration.
     */
    @Schema(
            name = "duration",
            description = "Price list according to duration")
    @JsonProperty("duration")
    private List<DurationDTO> duration;

    /**
     * Price list based on distance.
     */
    @Schema(
            name = "distance",
            description = "Price list according to distance")
    @JsonProperty("distance")
    private List<DistanceDTO> distance;

    /**
     * Extra fare if the vehicle is parked in a forbidden area.
     */
    @Schema(
            name = "parkingForbiddenFee",
            description = "Extra fare (in cents). Applied if the vehicle is parked in a forbidden area at the end of the trip",
            example = "5000")
    @JsonProperty("parkingForbiddenFee")
    private Long parkingForbiddenFee;

    /**
     * Extra fare if the vehicle is parked out of bounds.
     */
    @Schema(
            name = "outOfBoundFee",
            description = "Extra fare (in cents). Applied if the vehicle is parked out of bounds at the end of the trip",
            example = "50000")
    @JsonProperty("outOfBoundFee")
    private Long outOfBoundFee;


}
