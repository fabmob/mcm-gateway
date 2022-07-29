package com.gateway.commonapi.dto.data;

import java.util.UUID;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import java.util.UUID;

@JsonComponent
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Information about a price list item")
@JsonPropertyOrder({"priceListItemId","typePriceListItem","lowerPriceLimit","upperPriceLimit","fixedFare","farePerUnit","unit" })
public class PriceListItemDTO {

    /**
     * price list Item DTO identifier
     */
    @Schema(
            name = "priceListItemId",
            description = "priceListItem identifier",
            example = "b814c97e-df56-4651-ac50-115255379601",
            required = true)
    @JsonProperty("priceListItemId")
    private UUID priceListItemId;

    /**
     * Lower boundary of the price list item.
     *
     * For example, for prices depending on duration, it's the minimum duration.
     */
    @Schema(
            name="lowerPriceLimit",
            example="15",
            description="Lower boundary of the price list item (for example, if price is depending on duration, minimum duration in minutes)",
            required=true)
    @JsonProperty("lowerPriceLimit")
    private Long lowerPriceLimit;

    /**
     * Upper boundary of the price list item.
     *
     * For example, for prices depending on duration, it's the maximum duration.
     */
    @Schema(
            name="upperPriceLimit",
            example="240",
            description="Upper boundary of the price list item (for example, if price is depending on duration, maximum duration in minutes)")
    @JsonProperty("upperPriceLimit")
    private Long upperPriceLimit;

    /**
     * Fixed fare (in cents). Applied if the price list item is concerned.
     */
    @Schema(
            name="fixedFare",
            example="1",
            description="Fixed fare (in cents). Applied if the price list item is concerned")
    @JsonProperty("fixedFare")
    private Long fixedFare;

    /**
     * Fare per unit (in cents, per unit of time or distance). Applied for each unit.
     */
    @Schema(
            name="farePerUnit",
            example="1",
            description="Fare per unit (in cents, per unit of time or distance). Applied for each unit.")
    @JsonProperty("farePerUnit")
    private Long farePerUnit;

    /**
     * Unit (of time in minutes or distance in kilometers) used to apply the fare per unit.
     */
    @Schema(
            name="unit",
            example="1",
            description="Unit (of time in minutes or distance in kilometers) used to apply the fare per unit.")
    @JsonProperty("unit")
    private Long unit;


}
