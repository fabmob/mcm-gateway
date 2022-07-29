package com.gateway.api.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import javax.validation.constraints.NotNull;

/**
 * Bean for information about a price list item.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Information about a price list item")
@JsonPropertyOrder({ "from", "to", "fixedFare", "farePerUnit", "unit" })
public class PriceListItem implements Serializable {



    /**
     * Lower boundary of the price list item.
     *
     * For example, for prices depending on duration, it's the minimum duration.
     */
    @Schema(
            name="lowerPriceLimit",
            example="0",
            description="Lower boundary of the price list item (for example, if price is depending on duration, minimum duration in minutes)",
            required=true)
    @JsonProperty("lowerPriceLimit")
    @NotNull
    private Long lowerPriceLimit;

    /**
     * Upper boundary of the price list item.
     *
     * For example, for prices depending on duration, it's the maximum duration.
     */
    @Schema(
            name="upperPriceLimit",
            example="30",
            description="Upper boundary of the price list item (for example, if price is depending on duration, maximum duration in minutes)")
    @JsonProperty("upperPriceLimit")
    private Long upperPriceLimit;

    /**
     * Fixed fare (in cents). Applied if the price list item is concerned.
     */
    @Schema(
            name="fixedFare",
            example="100",
            description="Fixed fare (in cents). Applied if the price list item is concerned")
    @JsonProperty("fixedFare")
    private Long fixedFare;

    /**
     * Fare per unit (in cents, per unit of time or distance). Applied for each unit.
     */
    @Schema(
            name="farePerUnit",
            example="15",
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