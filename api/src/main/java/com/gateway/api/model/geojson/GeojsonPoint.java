package com.gateway.api.model.geojson;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;

/**
 * TOMP-API
 * Geojson Coordinate
 */
@Data
@NoArgsConstructor
@Schema(description = "Geojson Coordinate")
@Validated
public class GeojsonPoint extends ArrayList<Float>  {


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GeojsonPoint {\n");
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
