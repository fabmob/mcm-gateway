package com.gateway.commonapi.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "A multipolygon following the GeoJSON standard")
public class MultiPolygon {

    private static final long serialVersionUID = 1L;

    /**
     * Type of geometry, constant description "Polygon"
     */
    @Schema(
            name = "type",
            example = "MultiPolygon",
            description = "Type of geometry: Polygon",
            required = true)
    private String type;

    /**
     * Coordinates of a Polygon are an array of linear rings coordinate arrays. The
     * first element in the array represents the exterior ring. Any subsequent
     * elements represent interior rings (or holes).
     * <p>
     * Each linear ring coordinates are in WGS84, in one of the following format:
     * Longitude, latitude Longitude, latitude, altitude The last coordinates in the
     * linear ring MUST be equal to the first coordinates.
     */
    @Schema(
            name = "coordinates",
            example = "coordinates: [[ [ [-122.655775,45.516445],[-122.655705,45.516445],[-122.655705,45.516495],[-122.655775,45.516495]] ]]",
            description = "Coordinates of each linear ring: each point has longitude, latitude, and optional altitude.\n" +
                    "The last coordinates of each linear ring MUST be equal to the first coordinates.\n" +
                    "The first linear ring defines the outer boundary of the polygon, following ones define inner boundaries (holes).",
            required = true)
    @Setter
    private double[][][][] coordinates;


}
