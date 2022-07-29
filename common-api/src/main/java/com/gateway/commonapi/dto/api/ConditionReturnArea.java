package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.dto.api.geojson.Coordinates;
import com.gateway.commonapi.dto.api.geojson.GeojsonPolygon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * TOMP-API Standard
 * a return area. In the condition list there can be multiple return area&#x27;s.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "a return area. In the condition list there can be multiple return area's.")
@Validated
public class ConditionReturnArea extends Condition implements OneOfassetTypeConditionsItems, OneOflegConditionsItems {
  @JsonProperty("stationId")
  private String stationId = null;

  @JsonProperty("returnArea")
  private GeojsonPolygon returnArea = null;

  @JsonProperty("coordinates")
  private Coordinates coordinates = null;

  @JsonProperty("returnHours")
  @Valid
  private List<SystemHours> returnHours = null;

  public ConditionReturnArea stationId(String stationId) {
    this.stationId = stationId;
    return this;
  }

  /**
   * station to which the asset should be returned
   * @return stationId
   **/
  @Schema(description = "station to which the asset should be returned")
  
    public String getStationId() {
    return stationId;
  }

  public void setStationId(String stationId) {
    this.stationId = stationId;
  }

  public ConditionReturnArea returnArea(GeojsonPolygon returnArea) {
    this.returnArea = returnArea;
    return this;
  }

  /**
   * Get returnArea
   * @return returnArea
   **/
  @Schema(description = "")
  
    @Valid
    public GeojsonPolygon getReturnArea() {
    return returnArea;
  }

  public void setReturnArea(GeojsonPolygon returnArea) {
    this.returnArea = returnArea;
  }

  public ConditionReturnArea coordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  /**
   * Get coordinates
   * @return coordinates
   **/
  @Schema(description = "")
  
    @Valid
    public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public ConditionReturnArea returnHours(List<SystemHours> returnHours) {
    this.returnHours = returnHours;
    return this;
  }

  public ConditionReturnArea addReturnHoursItem(SystemHours returnHoursItem) {
    if (this.returnHours == null) {
      this.returnHours = new ArrayList<>();
    }
    this.returnHours.add(returnHoursItem);
    return this;
  }

  /**
   * the return hours of the facility (if different from operating-hours)
   * @return returnHours
   **/
  @Schema(description = "the return hours of the facility (if different from operating-hours)")
      @Valid
    public List<SystemHours> getReturnHours() {
    return returnHours;
  }

  public void setReturnHours(List<SystemHours> returnHours) {
    this.returnHours = returnHours;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConditionReturnArea {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    stationId: ").append(toIndentedString(stationId)).append("\n");
    sb.append("    returnArea: ").append(toIndentedString(returnArea)).append("\n");
    sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
    sb.append("    returnHours: ").append(toIndentedString(returnHours)).append("\n");
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
