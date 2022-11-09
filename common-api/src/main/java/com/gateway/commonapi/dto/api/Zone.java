package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.ZoneType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

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
            example = "OPERATING")
    @JsonProperty("zoneType")
    private ZoneType zoneType;

    /**
     * “Feature” (as per IETF RFC 7946).
     */
    @Schema(
            name = "type",
            description = "“Feature” (as per IETF RFC 7946).",
            example = "Feature")
    @JsonProperty("type")
    private String type;

    /**
     * Public name of the geofencing zone.
     */
    @Schema(
            name = "name",
            description = "Public name of the geofencing zone",
            example= "NE 24th/NE Knott")
    @JsonProperty("name")
    private String name;
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

    public void setValidityStartDate(ZonedDateTime validityStartDate) {
        this.validityStartDate = validityStartDate;
    }

    public void setValidityEndDate(ZonedDateTime validityEndDate) {
        this.validityEndDate = validityEndDate;
    }

    public void setValidityStartDate(String validityStartDate) {
        this.validityStartDate = StringUtils.isNotBlank(validityStartDate) ? ZonedDateTime.parse(validityStartDate) : null;
    }

    public void setValidityEndDate(String validityEndDate) {
        this.validityEndDate = StringUtils.isNotBlank(validityEndDate) ? ZonedDateTime.parse(validityEndDate) : null;
    }

    /**
     * Rules
     */
    @Schema(
            name = "rules")
    @JsonProperty("rules")
    private List<Rules> rules;

    /**
     * Polygon defining the area.
     */
    @Schema(
            name = "geometry",
            description = "Polygon in GeoJson format defining the geographical area",
            required = true)
    @JsonProperty("geometry")
    @NotNull
    private MultiPolygon geometry;
}

