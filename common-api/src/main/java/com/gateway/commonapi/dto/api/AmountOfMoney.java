package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * TOMP-API
 * AmountOfMoney
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class AmountOfMoney {
  @JsonProperty("amount")
  @NotNull
  private Float amount = null;

  @JsonProperty("amountExVat")
  private Float amountExVat = null;

  @JsonProperty("currencyCode")
  private String currencyCode = null;

  @JsonProperty("vatRate")
  private Float vatRate = null;

  @JsonProperty("vatCountryCode")
  private String vatCountryCode = null;

  public AmountOfMoney amount(Float amount) {
    this.amount = amount;
    return this;
  }

  /**
   * This should be in the base unit as defined by the ISO 4217 currency code with the appropriate number of decimal places and omitting the currency symbol. e.g. if the price is in US Dollars the price would be 9.95. This is inclusive VAT
   * minimum: 0
   * @return amount
   **/
  @Schema(example = "9.95", description = "This should be in the base unit as defined by the ISO 4217 currency code with the appropriate number of decimal places and omitting the currency symbol. e.g. if the price is in US Dollars the price would be 9.95. This is inclusive VAT")
  
  @DecimalMin("0")  public Float getAmount() {
    return amount;
  }

  public void setAmount(Float amount) {
    this.amount = amount;
  }

  public AmountOfMoney amountExVat(Float amountExVat) {
    this.amountExVat = amountExVat;
    return this;
  }

  /**
   * Get amountExVat
   * minimum: 0
   * @return amountExVat
   **/
  @Schema(example = "8.95", description = "")
  
  @DecimalMin("0")  public Float getAmountExVat() {
    return amountExVat;
  }

  public void setAmountExVat(Float amountExVat) {
    this.amountExVat = amountExVat;
  }

  public AmountOfMoney currencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
    return this;
  }

  /**
   * ISO 4217 currency code
   * @return currencyCode
   **/
  @Schema(description = "ISO 4217 currency code")
  
  @Size(min=3,max=3)   public String getCurrencyCode() {
    return currencyCode;
  }

  public void setCurrencyCode(String currencyCode) {
    this.currencyCode = currencyCode;
  }

  public AmountOfMoney vatRate(Float vatRate) {
    this.vatRate = vatRate;
    return this;
  }

  /**
   * value added tax rate (percentage of amount)
   * minimum: 0
   * @return vatRate
   **/
  @Schema(example = "21", description = "value added tax rate (percentage of amount)")
  
  @DecimalMin("0")  public Float getVatRate() {
    return vatRate;
  }

  public void setVatRate(Float vatRate) {
    this.vatRate = vatRate;
  }

  public AmountOfMoney vatCountryCode(String vatCountryCode) {
    this.vatCountryCode = vatCountryCode;
    return this;
  }

  /**
   * two-letter country codes according to ISO 3166-1
   * @return vatCountryCode
   **/
  @Schema(example = "NL", description = "two-letter country codes according to ISO 3166-1")
  
  @Size(min=2,max=2)   public String getVatCountryCode() {
    return vatCountryCode;
  }

  public void setVatCountryCode(String vatCountryCode) {
    this.vatCountryCode = vatCountryCode;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AmountOfMoney {\n");
    
    sb.append("    amount: ").append(indented(amount)).append("\n");
    sb.append("    amountExVat: ").append(indented(amountExVat)).append("\n");
    sb.append("    currencyCode: ").append(indented(currencyCode)).append("\n");
    sb.append("    vatRate: ").append(indented(vatRate)).append("\n");
    sb.append("    vatCountryCode: ").append(indented(vatCountryCode)).append("\n");
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
