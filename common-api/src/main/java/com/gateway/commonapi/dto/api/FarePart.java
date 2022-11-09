package com.gateway.commonapi.dto.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * TOMP-API Standard
 * this describes a part of the fare (or discount). It contains a for instance the startup costs (fixed) or the flex part (e.g. 1.25 EUR per 2.0 MILES). The amount is tax included. In case of discounts, the values are negative. With &#x27;MAX&#x27; you can specify e.g. a maximum of 15 euro per day. Percentage is mainly added for discounts. The &#x60;scale&#x60; properties create the ability to communicate scales (e.g. the first 4 kilometers you&#x27;ve to pay EUR 0.35 per kilometer, the kilometers 4 until 8 EUR 0.50 and above it EUR 0.80 per kilometer).
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "this describes a part of the fare (or discount). It contains a for instance the startup costs (fixed) or the flex part (e.g. 1.25 EUR per 2.0 MILES). The amount is tax included. In case of discounts, the values are negative. With 'MAX' you can specify e.g. a maximum of 15 euro per day. Percentage is mainly added for discounts. The `scale` properties create the ability to communicate scales (e.g. the first 4 kilometers you've to pay EUR 0.35 per kilometer, the kilometers 4 until 8 EUR 0.50 and above it EUR 0.80 per kilometer).")
@Validated
public class FarePart extends AmountOfMoney implements Serializable {

  @JsonProperty("type")
  private FareTypeEnum type = null;


  @JsonProperty("kind")
  private KindEnum kind = null;


  @JsonProperty("unitType")
  private UnitFareTypeEnum unitType = null;

  @JsonProperty("units")
  private Float units = null;

  @JsonProperty("scaleFrom")
  private Float scaleFrom = null;

  @JsonProperty("scaleTo")
  private Float scaleTo = null;


  @JsonProperty("scaleType")
  private ScaleFareTypeEnum scaleType = null;

  @JsonProperty("name")
  private String name = null;


  @JsonProperty("class")
  private PropertyClassEnum propertyClass = PropertyClassEnum.FARE;

  @JsonProperty("minimumAmount")
  private Float minimumAmount = null;

  @JsonProperty("maximumAmount")
  private Float maximumAmount = null;

  @JsonProperty("meta")
  @Valid
  private Map<String, Object> meta = null;

  public FarePart type(FareTypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * type of fare part. If there is only one farepart and this field is missing, it should be assumed it is 'FIXED'. In all other situations this field is mandatory.
   * @return type
   **/
  @Schema(description = "type of fare part. If there is only one farepart and this field is missing, it should be assumed it is 'FIXED'. In all other situations this field is mandatory.")
  
    public FareTypeEnum getType() {
    return type;
  }

  public void setType(FareTypeEnum type) {
    this.type = type;
  }

  public FarePart kind(KindEnum kind) {
    this.kind = kind;
    return this;
  }

  /**
   * is this the default price or is this an additional part (discount, price surge). In case of a DISCOUNT, the amount must always be negative and in case of SURGE it must be positive. This also means, that when you're working with discounts or surges, you have to deliver 2 fareparts, one for the default price and one for the discount/surge. This can be used in combination with as well the fixed price parts as with the flex price parts.
   * @return kind
   **/
  @Schema(description = "is this the default price or is this an additional part (discount, price surge). In case of a DISCOUNT, the amount must always be negative and in case of SURGE it must be positive. This also means, that when you're working with discounts or surges, you have to deliver 2 fareparts, one for the default price and one for the discount/surge. This can be used in combination with as well the fixed price parts as with the flex price parts.")
  
    public KindEnum getKind() {
    return kind;
  }

  public void setKind(KindEnum kind) {
    this.kind = kind;
  }

  public FarePart unitType(UnitFareTypeEnum unitType) {
    this.unitType = unitType;
    return this;
  }

  /**
   * in case of 'FLEX' mandatory, otherwise not allowed. E.g. 0.5 EUR per HOUR
   * @return unitType
   **/
  @Schema(description = "in case of 'FLEX' mandatory, otherwise not allowed. E.g. 0.5 EUR per HOUR")
  
    public UnitFareTypeEnum getUnitType() {
    return unitType;
  }

  public void setUnitType(UnitFareTypeEnum unitType) {
    this.unitType = unitType;
  }

  public FarePart units(Float units) {
    this.units = units;
    return this;
  }

  /**
   * the number of km, seconds etc. Mandatory when the type is 'FLEX', otherwise not allowed. In case of 0.5 EUR per 15 MINUTES, `units` should contain 15 and `unitType` MINUTES.
   * minimum: 0
   * @return units
   **/
  @Schema(description = "the number of km, seconds etc. Mandatory when the type is 'FLEX', otherwise not allowed. In case of 0.5 EUR per 15 MINUTES, `units` should contain 15 and `unitType` MINUTES.")
  
  @DecimalMin("0")  public Float getUnits() {
    return units;
  }

  public void setUnits(Float units) {
    this.units = units;
  }

  public FarePart scaleFrom(Float scaleFrom) {
    this.scaleFrom = scaleFrom;
    return this;
  }

  /**
   * in case of scaling, this is the bottom value (f.x. in the first hour 3 CAD, the `scaleFrom` should contain 0 and the `scaleType` HOUR). When `scaleTo` is used, but this field is missing, it should be assumed it is a 0.
   * minimum: 0
   * @return scaleFrom
   **/
  @Schema(description = "in case of scaling, this is the bottom value (f.x. in the first hour 3 CAD, the `scaleFrom` should contain 0 and the `scaleType` HOUR). When `scaleTo` is used, but this field is missing, it should be assumed it is a 0.")
  
  @DecimalMin("0")  public Float getScaleFrom() {
    return scaleFrom;
  }

  public void setScaleFrom(Float scaleFrom) {
    this.scaleFrom = scaleFrom;
  }

  public FarePart scaleTo(Float scaleTo) {
    this.scaleTo = scaleTo;
    return this;
  }

  /**
   * the upper value of the scale (f.x. 3 CAD in the first hour, this field should contain 1, `scaleFrom` 0 and `scaleType` HOUR)
   * minimum: 0
   * @return scaleTo
   **/
  @Schema(description = "the upper value of the scale (f.x. 3 CAD in the first hour, this field should contain 1, `scaleFrom` 0 and `scaleType` HOUR)")
  
  @DecimalMin("0")  public Float getScaleTo() {
    return scaleTo;
  }

  public void setScaleTo(Float scaleTo) {
    this.scaleTo = scaleTo;
  }

  public FarePart scaleType(ScaleFareTypeEnum scaleType) {
    this.scaleType = scaleType;
    return this;
  }

  /**
   * Get scaleType
   * @return scaleType
   **/
  @Schema(description = "")
  
    public ScaleFareTypeEnum getScaleType() {
    return scaleType;
  }

  public void setScaleType(ScaleFareTypeEnum scaleType) {
    this.scaleType = scaleType;
  }

  public FarePart name(String name) {
    this.name = name;
    return this;
  }

  /**
   * an optional description of this fare part.
   * @return name
   **/
  @Schema(description = "an optional description of this fare part.")
  
    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FarePart propertyClass(PropertyClassEnum propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * class of this fare part. Could be FARE or ANCILLARY
   * @return propertyClass
   **/
  @Schema(description = "class of this fare part. Could be FARE or ANCILLARY")
  
    public PropertyClassEnum getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(PropertyClassEnum propertyClass) {
    this.propertyClass = propertyClass;
  }

  public FarePart minimumAmount(Float minimumAmount) {
    this.minimumAmount = minimumAmount;
    return this;
  }

  /**
   * The minimum price, in the same currency as amount. Place in `amount` the most likely value.
   * minimum: 0
   * @return minimumAmount
   **/
  @Schema(example = "9", description = "The minimum price, in the same currency as amount. Place in `amount` the most likely value.")
  
  @DecimalMin("0")  public Float getMinimumAmount() {
    return minimumAmount;
  }

  public void setMinimumAmount(Float minimumAmount) {
    this.minimumAmount = minimumAmount;
  }

  public FarePart maximumAmount(Float maximumAmount) {
    this.maximumAmount = maximumAmount;
    return this;
  }

  /**
   * The minimum price, in the same currency as amount. Place in `amount` the most likely value.
   * minimum: 0
   * @return maximumAmount
   **/
  @Schema(example = "11", description = "The minimum price, in the same currency as amount. Place in `amount` the most likely value.")
  
  @DecimalMin("0")  public Float getMaximumAmount() {
    return maximumAmount;
  }

  public void setMaximumAmount(Float maximumAmount) {
    this.maximumAmount = maximumAmount;
  }

  public FarePart meta(Map<String, Object> meta) {
    this.meta = meta;
    return this;
  }

  public FarePart putMetaItem(String key, Object metaItem) {
    if (this.meta == null) {
      this.meta = new HashMap<>();
    }
    this.meta.put(key, metaItem);
    return this;
  }

  /**
   * Get meta
   * @return meta
   **/
  @Schema(description = "")
  
    public Map<String, Object> getMeta() {
    return meta;
  }

  public void setMeta(Map<String, Object> meta) {
    this.meta = meta;
  }



  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FarePart {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
    sb.append("    unitType: ").append(toIndentedString(unitType)).append("\n");
    sb.append("    units: ").append(toIndentedString(units)).append("\n");
    sb.append("    scaleFrom: ").append(toIndentedString(scaleFrom)).append("\n");
    sb.append("    scaleTo: ").append(toIndentedString(scaleTo)).append("\n");
    sb.append("    scaleType: ").append(toIndentedString(scaleType)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    minimumAmount: ").append(toIndentedString(minimumAmount)).append("\n");
    sb.append("    maximumAmount: ").append(toIndentedString(maximumAmount)).append("\n");
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
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
