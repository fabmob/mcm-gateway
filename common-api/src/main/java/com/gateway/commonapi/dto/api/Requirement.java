package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.ApplicableDaysEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * describes an (dis)ability or ancillary.
 */
@Schema(description = "describes an (dis)ability or ancillary.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-02-17T17:07:12.550Z[GMT]")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Requirement implements Serializable {
  @JsonProperty("source")
  private String source = null;

  @JsonProperty("category")
  private String category = null;

  @JsonProperty("number")
  private String number = null;

  @JsonProperty("type")
  private String type = null;

  @JsonProperty("memo")
  private String memo = null;

  @JsonProperty("variableNumber")
  private Integer variableNumber = null;


  @JsonProperty("applicable-days")
  @Valid
  private List<ApplicableDaysEnum> applicableDays = null;

  public Requirement source(String source) {
    this.source = source;
    return this;
  }

  /**
   * if obsolete, it is referencing the travelers' dictionary (https://github.com/TOMP-WG/TOMP-API/blob/master/documents/CROW%20passenger%20characteristics.xlsx)
   * @return source
   **/
  @Schema(description = "if obsolete, it is referencing the travelers' dictionary (https://github.com/TOMP-WG/TOMP-API/blob/master/documents/CROW%20passenger%20characteristics.xlsx)")
  
    public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Requirement category(String category) {
    this.category = category;
    return this;
  }

  /**
   * references to the first column of the specification initial values [ HR, AV, HV, AB, AER, K, ZR, RR ]
   * @return category
   **/
  @Schema(description = "references to the first column of the specification initial values [ HR, AV, HV, AB, AER, K, ZR, RR ]")

    public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public Requirement number(String number) {
    this.number = number;
    return this;
  }

  /**
   * references to the second column of the specification
   * @return number
   **/
  @Schema(description = "references to the second column of the specification")

  @Size(min=2,max=2)   public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Requirement type(String type) {
    this.type = type;
    return this;
  }

  /**
   * conditionally extra information, referencing to the 3th column
   * @return type
   **/
  @Schema(description = "conditionally extra information, referencing to the 3th column")
  
    public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Requirement memo(String memo) {
    this.memo = memo;
    return this;
  }

  /**
   * extra field for detailed information, not standardized
   * @return memo
   **/
  @Schema(description = "extra field for detailed information, not standardized")
  
    public String getMemo() {
    return memo;
  }

  public void setMemo(String memo) {
    this.memo = memo;
  }

  public Requirement variableNumber(Integer variableNumber) {
    this.variableNumber = variableNumber;
    return this;
  }

  /**
   * in some requirements there is references to '[variable number]' e.g. of meters (like ZR06)
   * minimum: 0
   * @return variableNumber
   **/
  @Schema(description = "in some requirements there is references to '[variable number]' e.g. of meters (like ZR06)")
  
  @Min(0)  public Integer getVariableNumber() {
    return variableNumber;
  }

  public void setVariableNumber(Integer variableNumber) {
    this.variableNumber = variableNumber;
  }

  public Requirement applicableDays(List<ApplicableDaysEnum> applicableDays) {
    this.applicableDays = applicableDays;
    return this;
  }

  public Requirement addApplicableDaysItem(ApplicableDaysEnum applicableDaysItem) {
    if (this.applicableDays == null) {
      this.applicableDays = new ArrayList<ApplicableDaysEnum>();
    }
    this.applicableDays.add(applicableDaysItem);
    return this;
  }

  /**
   * days of week that are applicable
   * @return applicableDays
   **/
  @Schema(description = "days of week that are applicable")
  
    public List<ApplicableDaysEnum> getApplicableDays() {
    return applicableDays;
  }

  public void setApplicableDays(List<ApplicableDaysEnum> applicableDays) {
    this.applicableDays = applicableDays;
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Requirement {\n");
    
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    number: ").append(toIndentedString(number)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    memo: ").append(toIndentedString(memo)).append("\n");
    sb.append("    variableNumber: ").append(toIndentedString(variableNumber)).append("\n");
    sb.append("    applicableDays: ").append(toIndentedString(applicableDays)).append("\n");
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
