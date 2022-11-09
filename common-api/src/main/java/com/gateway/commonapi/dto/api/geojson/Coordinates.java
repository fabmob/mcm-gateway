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

/**
 * Bean for information about coordinates - TOMP API Standard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Information about an address")
@JsonPropertyOrder({"lng", "lat", "alt"})
public class Coordinates implements Serializable {

    /**
     * Longitude
     */
    @Schema(

            example = "6.169639",
            description = "Longitude")
    @JsonProperty("lng")
    @NotNull
    private Float lng;

    /**
     * Latitude
     */
    @Schema(
            description = "latitude",
            example = "'52.253279")
    @JsonProperty("lat")
    @NotNull
    private Float lat;

    /**
     * Alt
     */
    @Schema(description = "altitude, in meters above sea level",
            example = "0")
    @JsonProperty("alt")
    private Float alt;


}

