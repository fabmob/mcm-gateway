package com.gateway.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.api.util.enums.RequiredFieldsEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * TOMP-API Standard
 * ConditionRequireBookingData
 */
@Validated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionRequireBookingData extends Condition implements OneOfassetTypeConditionsItems, OneOflegConditionsItems {

  @JsonProperty("requiredFields")
  @Valid
  private List<RequiredFieldsEnum> requiredFields = new ArrayList<>();

  @JsonProperty("claims")
  @Valid
  private List<String> claims = null;

  public ConditionRequireBookingData requiredFields(List<RequiredFieldsEnum> requiredFields) {
    this.requiredFields = requiredFields;
    return this;
  }

  public ConditionRequireBookingData addRequiredFieldsItem(RequiredFieldsEnum requiredFieldsItem) {
    this.requiredFields.add(requiredFieldsItem);
    return this;
  }

  /**
   * Get requiredFields
   * @return requiredFields
   **/
  @Schema(required = true, description = "")
      @NotNull

    public List<RequiredFieldsEnum> getRequiredFields() {
    return requiredFields;
  }

  public void setRequiredFields(List<RequiredFieldsEnum> requiredFields) {
    this.requiredFields = requiredFields;
  }

  public ConditionRequireBookingData claims(List<String> claims) {
    this.claims = claims;
    return this;
  }

  public ConditionRequireBookingData addClaimsItem(String claimsItem) {
    if (this.claims == null) {
      this.claims = new ArrayList<>();
    }
    this.claims.add(claimsItem);
    return this;
  }

  /**
   * when in the 'requiredFields' array 'BLOCKCHAIN_CLAIMS' is specified, in this array claims can be specified. On the WIKI page, the known ones are enlisted, but this list isn't finalized yet. https://github.com/TOMP-WG/TOMP-API/wiki/Blockchain---Verifiable-credentials
   * @return claims
   **/
  @Schema(description = "when in the 'requiredFields' array 'BLOCKCHAIN_CLAIMS' is specified, in this array claims can be specified. On the WIKI page, the known ones are enlisted, but this list isn't finalized yet. https://github.com/TOMP-WG/TOMP-API/wiki/Blockchain---Verifiable-credentials")
  
    public List<String> getClaims() {
    return claims;
  }

  public void setClaims(List<String> claims) {
    this.claims = claims;
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConditionRequireBookingData {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    requiredFields: ").append(toIndentedString(requiredFields)).append("\n");
    sb.append("    claims: ").append(toIndentedString(claims)).append("\n");
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
