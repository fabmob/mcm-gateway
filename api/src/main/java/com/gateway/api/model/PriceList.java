package com.gateway.api.model;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Bean for information about a price list.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Information about a price list")
@JsonPropertyOrder({ "comment", "duration", "distance", "parkingForbiddenFee", "outOfBoundFee" })
public class PriceList implements Serializable {

    

    /**
     * Comment on the price list.
     */
    @Schema(
            name="comment",
            description="Comment on the price list")
    @JsonProperty("comment")
    private String comment;

    /**
     * Price list based on duration.
     */
    @Schema(
            name="duration",
            description="Price list based on duration")
    @JsonProperty("duration")
    private List<Duration> duration;

    /**
     * Price list based on distance.
     */
    @Schema(
            name="distance",
            description="Price list based on distance")
    @JsonProperty("distance")
    private List<Distance> distance;

    /**
     * Extra fare if the vehicle is parked in a forbidden area.
     */
    @Schema(
            name="parkingForbiddenFee",
            description="Extra fare (in cents). Applied if the vehicle is parked in a forbidden area at the end of the trip")
    @JsonProperty("parkingForbiddenFee")
    private Long parkingForbiddenFee;

    /**
     * Extra fare if the vehicle is parked out of bounds.
     */
    @Schema(
            name="outOfBoundFee",
            description="Extra fare (in cents). Applied if the vehicle is parked out of bounds at the end of the trip")
    @JsonProperty("outOfBoundFee")
    private Long outOfBoundFee;
}

