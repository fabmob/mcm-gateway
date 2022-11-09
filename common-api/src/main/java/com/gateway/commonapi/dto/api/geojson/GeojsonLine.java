package com.gateway.commonapi.dto.api.geojson;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;

/**
 * TOMP-API
 * An array  of WGS84 coordinate pairs
 */

@Data
@AllArgsConstructor
@Schema(description = "An array  of WGS84 coordinate pairs")
@Validated
public class GeojsonLine extends ArrayList<GeojsonPoint>  {


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeojsonLine {\n");
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
