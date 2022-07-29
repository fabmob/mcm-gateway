package com.gateway.commonapi.dto.api.geojson;


import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * Class for a GeoJSON polygon.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7946">RFC 7946</a>
 */
@Getter
@AllArgsConstructor
@Validated
@Schema(description = "A polygon following the GeoJSON standard")
public class Polygon implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Type of geometry, constant description "Polygon"
     */
    @Schema(
            name = "type",
            example = "Polygon",
            description="Type of geometry: Polygon",
            required=true)
    private String type ;

    /**
     * Coordinates of a Polygon are an array of linear rings coordinate arrays. The
     * first element in the array represents the exterior ring. Any subsequent
     * elements represent interior rings (or holes).
     *
     * Each linear ring coordinates are in WGS84, in one of the following format:
     * Longitude, latitude Longitude, latitude, altitude The last coordinates in the
     * linear ring MUST be equal to the first coordinates.
     */
    @Schema(
            name = "coordinates",
            example = "[[ [2.275725, 48.865983], [2.3390958085656166, 48.85375581057431], [2.3648982158072323, 48.867872484748744], [2.275725, 48.865983] ]]",
            description="Coordinates of each linear ring: each point has longitude, latitude, and optional altitude.\n" +
                    "The last coordinates of each linear ring MUST be equal to the first coordinates.\n" +
                    "The first linear ring defines the ounter boundary of the polygon, following ones define inner boundaries (holes).",
            required=true)
    @Setter
    private double[][][] coordinates;

    /**
     * Default constructor: creates a single array for one polygon.
     */
    public Polygon () {
        coordinates = new double[1][][];
    }

    /**
     * Add a single polygon (outer boundary).
     *
     * @param points Coordinates of the polygon. The last coordinates MUST be equal to the first coordinates.
     */
    public void addCoordinates(double[][] points) {
        coordinates[0] = points;
    }
}

