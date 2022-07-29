package com.gateway.api.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

/**
 * Bean for a around-me request body.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "A request for msp around me")
@JsonPropertyOrder({ "lon", "lat", "radius", "maxResult", "mspIds"})
public class MSPAroundMeRequest {

    @Schema(name="lon",
            description="Longitude of the geographical position",
            example = "6.169639")
    @JsonProperty("lon")
    private Float lon;

    @Schema(name="lat",
            description="Latitude of the geographical position",
            example = "52.253279")
    @JsonProperty("lat")
    private Float lat;

    @Schema(name="radius",
            description="Limit distance between msp and initial position",
            example = "100")
    @JsonProperty("radius")
    private Float radius;

    @Schema(name="maxResult",
            description="Max result to return",
            example = "200")
    @JsonProperty("maxResult")
    private Integer maxResult;

    @Schema(name="mspIds",
            description="List of msp ids to retrieve",
            required = true)
    @JsonProperty("mspIds")
    private List<UUID> mspIds;
}
