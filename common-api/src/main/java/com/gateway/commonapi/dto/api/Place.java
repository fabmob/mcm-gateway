package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.dto.api.geojson.Coordinates;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * a origin or destination of a leg, 3D. lon/lat in WGS84.
 */
@Schema(description = "a origin or destination of a leg, 3D. lon/lat in WGS84.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-02-17T17:07:12.550Z[GMT]")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place implements Serializable {

  @Schema(
          name="name",
          description="Human readable name of the place",
          example = "Place de la République")
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("stopReference")
  @Valid
  private List<StopReference> stopReference = null;

  @Schema(
          name="stationId",
          description="Station Id",
          example = "XX:Y:12345678")
  @JsonProperty("stationId")
  @NotNull
  private String stationId = null;

  @Schema(
          name="crowflyDistance",
          description="Distance to reach the vehicule (in meters)",
          example = "50")
  @JsonProperty("crowflyDistance")
  @NotNull
  private String crowflyDistance;

  @Schema(
          name="coordinates",
          description="GPS coordinates")
  @JsonProperty("coordinates")
  @NotNull
  private Coordinates coordinates = null;

  @JsonProperty("physicalAddress")
  @NotNull
  private Address physicalAddress = null;

  @JsonProperty("extraInfo")
  @Valid
  private Map<String, Object> extraInfo = null;

  public Place name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Human readable name of the place, could match Content-Language
   * @return name
   **/
  @Schema(description = "Human readable name of the place, could match Content-Language")
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Place stopReference(List<StopReference> stopReference) {
    this.stopReference = stopReference;
    return this;
  }

  public Place addStopReferenceItem(StopReference stopReferenceItem) {
    if (this.stopReference == null) {
      this.stopReference = new ArrayList<StopReference>();
    }
    this.stopReference.add(stopReferenceItem);
    return this;
  }

  /**
   * Get stopReference
   * @return stopReference
   **/
  @Schema(description = "")
      @Valid
    public List<StopReference> getStopReference() {
    return stopReference;
  }

  public void setStopReference(List<StopReference> stopReference) {
    this.stopReference = stopReference;
  }

  public Place stationId(String stationId) {
    this.stationId = stationId;
    return this;
  }

  /**
   * reference to /operator/stations
   * @return stationId
   **/
  @Schema(description = "reference to /v1/msps/{mspId}/stations")
  
    public String getStationId() {
    return stationId;
  }

  public void setStationId(String stationId) {
    this.stationId = stationId;
  }

  public Place coordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  /**
   * Get coordinates
   * @return coordinates
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public Place physicalAddress(Address physicalAddress) {
    this.physicalAddress = physicalAddress;
    return this;
  }

  /**
   * Get physicalAddress
   * @return physicalAddress
   **/
  @Schema(description = "")
  
    @Valid
    public Address getPhysicalAddress() {
    return physicalAddress;
  }

  public void setPhysicalAddress(Address physicalAddress) {
    this.physicalAddress = physicalAddress;
  }

  public Place extraInfo(Map<String, Object> extraInfo) {
    this.extraInfo = extraInfo;
    return this;
  }

  public Place putExtraInfoItem(String key, Object extraInfoItem) {
    if (this.extraInfo == null) {
      this.extraInfo = new HashMap<String, Object>();
    }
    this.extraInfo.put(key, extraInfoItem);
    return this;
  }

  /**
   * Get extraInfo
   * @return extraInfo
   **/
  @Schema(description = "")
  
    public Map<String, Object> getExtraInfo() {
    return extraInfo;
  }

  public void setExtraInfo(Map<String, Object> extraInfo) {
    this.extraInfo = extraInfo;
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Place {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    stopReference: ").append(toIndentedString(stopReference)).append("\n");
    sb.append("    stationId: ").append(toIndentedString(stationId)).append("\n");
    sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
    sb.append("    physicalAddress: ").append(toIndentedString(physicalAddress)).append("\n");
    sb.append("    extraInfo: ").append(toIndentedString(extraInfo)).append("\n");
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
