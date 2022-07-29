package com.gateway.api.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.api.model.geojson.Polygon;
import com.gateway.api.util.enums.ZoneType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

/**
 * Bean representing a geographical area.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Geographical zone information")
public class Zone implements Serializable {


    /**
     * Type of area.
     */
    @Schema(
            name = "zoneType",
            description = "Type of area",
            required = true,
            example = "OPERATING",
            allowableValues = "OPERATING, NO_PARKING, SPEED_LIMIT, PREFENTIAL_PARKING")
    @JsonProperty("zoneType")
    private ZoneType zoneType;

    /**
     * Validity start date.
     */
    @Schema(
            name = "validityStartDate",
            description = "Validity start date for the area",
            example = "2022-03-01T14:59:57.415Z")
    @JsonProperty("validityStartDate")
    private ZonedDateTime validityStartDate;

    /**
     * Validity end date.
     */
    @Schema(
            name = "validityEndDate",
            description = "Validity end date for the area",
            example = "2022-03-01T14:59:57.415Z")
    @JsonProperty("validityEndDate")
    private ZonedDateTime validityEndDate;

    /**
     * Polygon defining the area.
     */
    @Schema(
            name = "geometry",
            description = "Polygon in GeoJson format defining the geographical area",
            required = true)
    @JsonProperty("geometry")
    @NotNull
    private Polygon geometry;
}

