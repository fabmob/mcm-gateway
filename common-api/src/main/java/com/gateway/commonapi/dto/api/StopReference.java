package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.ReferenceTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

/**
 * reference to a stop (can be nation specific). This can help to specific pinpoint a (bus) stop. Extra information about the stop is not supplied; you should find it elsewhere.
 */
@Schema(description = "reference to a stop (can be nation specific). This can help to specific pinpoint a (bus) stop. Extra information about the stop is not supplied; you should find it elsewhere.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-02-17T17:07:12.550Z[GMT]")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StopReference {

  @Schema(
          name="type",
          example = "GTFS_STOP_ID")
  @JsonProperty("type")
  private ReferenceTypeEnum type = null;

  @Schema(
          name="id",
          example = "FR:S:99990000")
  @JsonProperty("id")
  private String id = null;

  @Schema(
          name="country",
          example = "FR")
  @JsonProperty("country")
  private String country = null;

  public StopReference type(ReferenceTypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * type of external reference (GTFS, CHB).
   * @return type
   **/
  @Schema(description = "type of external reference (GTFS, CHB).")

    public ReferenceTypeEnum getType() {
    return type;
  }

  public void setType(ReferenceTypeEnum type) {
    this.type = type;
  }

  public StopReference id(String id) {
    this.id = id;
    return this;
  }

  /**
   * this field should contain the complete ID. E.g. NL:S:13121110 or BE:S:79640040
   * @return id
   **/
  @Schema( description = "this field should contain the complete ID. E.g. NL:S:13121110 or BE:S:79640040")

    public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public StopReference country(String country) {
    this.country = country;
    return this;
  }

  /**
   * two-letter country codes according to ISO 3166-1
   * @return country
   **/
  @Schema(example = "NL", description = "two-letter country codes according to ISO 3166-1")

  @Size(min=2,max=2)   public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StopReference {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
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
