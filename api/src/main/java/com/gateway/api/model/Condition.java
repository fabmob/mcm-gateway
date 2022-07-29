package com.gateway.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;


/**
 * TOMP-API
 * Condition
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Condition   {
  @JsonProperty("conditionType")
  private String conditionType = null;

  @JsonProperty("id")
  private String id = null;

  public Condition conditionType(String conditionType) {
    this.conditionType = conditionType;
    return this;
  }

  /**
   * The specific subclass of condition, should match the schema name exactly
   * @return conditionType
   **/
  @Schema(required = true, description = "The specific subclass of condition, should match the schema name exactly")
      @NotNull

    public String getConditionType() {
    return conditionType;
  }

  public void setConditionType(String conditionType) {
    this.conditionType = conditionType;
  }

  public Condition id(String id) {
    this.id = id;
    return this;
  }

  /**
   * An identifier for this condition that can be used to refer to this condition
   * @return id
   **/
  @Schema(example = "deposit50eu", description = "An identifier for this condition that can be used to refer to this condition")
  
    public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Condition {\n");
    
    sb.append("    conditionType: ").append(indented(conditionType)).append("\n");
    sb.append("    id: ").append(indented(id)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String indented(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
