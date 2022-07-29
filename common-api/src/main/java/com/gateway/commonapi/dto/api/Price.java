package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.utils.enums.TypeEnumPrice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

/**
 * Price
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Price implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Either « FREE », « PAYING » or « UNKNOWN ». « UNKNOWN » is given when it should be « PAYING » but we cannot set the price yet.
     *
     * @return type
     **/
    @Schema(name = "type",
            example = "PAYING",
            description = "Either « FREE », « PAYING » or « UNKNOWN ». « UNKNOWN » is given when it should be « PAYING » but we cannot set the price yet.")
    @JsonProperty("type")
    public TypeEnumPrice type = null;

    /**
     * Carpooling passenger cost estimate. In the case of integrated booking by API, amount expected by the carpooling operator.
     **/
    @Schema(name = "amount",
            example = "15.30",
            description = "Carpooling passenger cost estimate. In the case of integrated booking by API, amount expected by the carpooling operator.")
    @JsonProperty("amount")
    public Float amount = null;

    /**
     * ISO 4217 code representing the currency of the price.
     **/
    @Schema(name = "currency",
            example = "EUR",
            description = "ISO 4217 code representing the currency of the price.")
    @JsonProperty("currency")
    public String currency = null;
}
