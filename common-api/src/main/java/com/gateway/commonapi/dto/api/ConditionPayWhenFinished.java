package com.gateway.commonapi.dto.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;


/**
 * TOMP-API Standard
 * in case the TO demands a direct payment after usage.
 */

@Data
@AllArgsConstructor
@Schema(description = "in case the TO demands a direct payment after usage.")
@Validated
public class ConditionPayWhenFinished extends Condition implements OneOfassetTypeConditionsItems, OneOflegConditionsItems {


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConditionPayWhenFinished {\n");
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
