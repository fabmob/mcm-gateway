package com.gateway.commonapi.dto.api.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@JsonPropertyOrder({"lon", "lat", "radius"})
public class Position implements Serializable {

    /**
     * Longitude
     */
    @Schema(

            example = "6.169639",
            description = "Longitude")
    @JsonProperty("lon")
    @NotNull
    private Float lon;

    /**
     * Latitude
     */
    @Schema(
            description = "Latitude",
            example = "'52.253279")
    @JsonProperty("lat")
    @NotNull
    private Float lat;

    /**
     * Radius
     */
    @Schema(description = "Radius in meters",
            example = "5")
    @JsonProperty("radius")
    private Float radius;


}
