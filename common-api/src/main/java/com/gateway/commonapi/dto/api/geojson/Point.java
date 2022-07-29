package com.gateway.commonapi.dto.api.geojson;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

/**
 * Class for a GeoJSON point.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946">RFC 7946</a>
 */
@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "A point following the GeoJSON standard")
@JsonIgnoreProperties({ "latitude", "longitude", "altitude" })
public class Point implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Type of geometry, constant description "Point"
     */
    @Schema(
            name="type",
            example="Point",
            description="Type of geometry: Point",
            required=false)
    private String type = "Point";

    /**
     * Coordinates of the point in WGS84, in one of the following format:
     *   Longitude, latitude
     *   Longitude, latitude, altitude
     */
    @Schema(
            name="coordinates",
            example="[2.275725, 48.865983]",
            description="Coordinates of the point with longitude, latitude, and optional altitude",
            required=true)
    @NotNull
    private double[] coordinates;

    /**
     * Constructor for a point without altitude.
     *
     * @param longitude Longitude of the point.
     * @param latitude Latitude of the point.
     */
    public Point(double longitude, double latitude) {
        setCoordinates(longitude, latitude);
    }

    /**
     * Constructor for a point with altitude.
     *
     * @param longitude Longitude of the point.
     * @param latitude Latitude of the point.
     * @param altitude Altitude of the point.
     */
    public Point(double longitude, double latitude, double altitude) {
        setCoordinates(longitude, latitude, altitude);
    }

    /**
     * Set coordinates for a point without altitude.
     *
     * @param longitude Longitude of the point.
     * @param latitude Latitude of the point.
     */
    public void setCoordinates(double longitude, double latitude) {
        coordinates = new double[2];
        coordinates[0] = longitude;
        coordinates[1] = latitude;
    }

    /**
     * Set coordinates for a point with altitude.
     *
     * @param longitude Longitude of the point.
     * @param latitude Latitude of the point.
     * @param altitude Altitude of the point.
     */
    public void setCoordinates(double longitude, double latitude, double altitude) {
        coordinates = new double[3];
        coordinates[0] = longitude;
        coordinates[1] = latitude;
        coordinates[2] = altitude;
    }

    /**
     * @return Longitude of the point.
     */
    public Double getLongitude() {
        if ((coordinates != null) && (coordinates.length > 0)) {
            return coordinates[0];
        }
        return null;
    }

    /**
     * @return Latitude of the point.
     */
    public Double getLatitude() {
        if ((coordinates != null) && (coordinates.length > 1)) {
            return coordinates[1];
        }
        return null;
    }

    /**
     * @return Altitude of the point.
     */
    public Double getAltitude() {
        if ((coordinates != null) && (coordinates.length > 2)) {
            return coordinates[2];
        }
        return null;
    }
}
