package com.gateway.commonapi.dto.api;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Bean for information about an address.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Information about an address")
@JsonPropertyOrder({ "full", "houseNumber", "street", "city", "postCode", "country" })
public class Address implements Serializable {
    
    /**
     * Full address.
     */
    @Schema(
            name="full",
            example="40, rue du Louvre, 75001, Paris, France",
            description="Full address (format can vary depending on the provider)")
    @JsonProperty("full")
    private String fullAddress;

    /**
     * House number.
     */
    @Schema(
            name="houseNumber",
            example="40",
            description="House number")
    @JsonProperty("houseNumber")
    private String houseNumber;

    /**
     * Street name.
     */
    @Schema(
            name="street",
            example="rue du Louvre",
            description="Street name")
    @JsonProperty("street")
    private String street;

    /**
     * City.
     */
    @Schema(
            name="city",
            example="Paris",
            description="City")
    @JsonProperty("city")
    private String city;

    /**
     * Post code.
     */
    @Schema(
            name="postCode",
            example="75001",
            description="Postal code")
    @JsonProperty("postCode")
    private String postCode;

    /**
     * Country.
     */
    @Schema(
            name="country",
            example="France",
            description="Country")
    @JsonProperty("country")
    private String country;
}
