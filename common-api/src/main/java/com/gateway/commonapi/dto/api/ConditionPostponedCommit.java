package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

/**
 * TOMP-API Standard
 * ConditionPostponedCommit
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class ConditionPostponedCommit extends Condition implements OneOfassetTypeConditionsItems, OneOflegConditionsItems {
  @JsonProperty("ultimateResponseTime")
  private OffsetDateTime ultimateResponseTime = null;

  public ConditionPostponedCommit ultimateResponseTime(OffsetDateTime ultimateResponseTime) {
    this.ultimateResponseTime = ultimateResponseTime;
    return this;
  }

  /**
   * Get ultimateResponseTime
   * @return ultimateResponseTime
   **/
  @Schema(required = true, description = "")
      @NotNull

    @Valid
    public OffsetDateTime getUltimateResponseTime() {
    return ultimateResponseTime;
  }

  public void setUltimateResponseTime(OffsetDateTime ultimateResponseTime) {
    this.ultimateResponseTime = ultimateResponseTime;
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConditionPostponedCommit {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    ultimateResponseTime: ").append(toIndentedString(ultimateResponseTime)).append("\n");
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
