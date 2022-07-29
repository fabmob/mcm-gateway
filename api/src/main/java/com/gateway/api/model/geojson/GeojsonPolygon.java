package com.gateway.api.model.geojson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;

/**
 * TOMP-API
 * geojson representation of a polygon. First and last point must be equal. See also https://geojson.org/geojson-spec.html#polygon and example https://geojson.org/geojson-spec.html#id4. The order should be lon, lat [[[lon1, lat1], [lon2,lat2], [lon3,lat3], [lon1,lat1]]], the first point should match the last point.
 */

@Data
@AllArgsConstructor
@Schema(description = "geojson representation of a polygon. First and last point must be equal. See also https://geojson.org/geojson-spec.html#polygon and example https://geojson.org/geojson-spec.html#id4. The order should be lon, lat [[[lon1, lat1], [lon2,lat2], [lon3,lat3], [lon1,lat1]]], the first point should match the last point.")
@Validated
public class GeojsonPolygon extends ArrayList<GeojsonLine>  {


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeojsonPolygon {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
