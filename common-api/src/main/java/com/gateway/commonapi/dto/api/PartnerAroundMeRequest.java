package com.gateway.commonapi.dto.api;


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
 * Bean for an around-me request body.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Request to retrieve a global view of stations, assets and parkings around a specified location for all or specified partners.")
@JsonPropertyOrder({"lon", "lat", "radius", "maxResult", "partnersIds"})
public class PartnerAroundMeRequest {

    @Schema(name = "lon",
            description = "Longitude of the geographical position",
            example = "6.169639")
    @JsonProperty("lon")
    private Float lon;

    @Schema(name = "lat",
            description = "Latitude of the geographical position",
            example = "52.253279")
    @JsonProperty("lat")
    private Float lat;

    @Schema(name = "radius",
            description = "Range in meters from the search location to look for stations, assets and parkings",
            example = "100")
    @JsonProperty("radius")
    private Float radius;

    @Schema(name = "maxResult",
            description = "Max result to return",
            example = "200")
    @JsonProperty("maxResult")
    private Integer maxResult;

    @Schema(name = "partnersIds",
            description = "List of partner ids to retrieve",
            required = true)
    @JsonProperty("partnersIds")
    private List<UUID> partnersIds;
}
