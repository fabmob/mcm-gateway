package com.gateway.api.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


/**
 * Bean for information about an address according to TOMP-API standards.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Information about an address")
@JsonPropertyOrder({"streetAddress", "street", "houseNumber", "houseNumberAddition", "addressAdditionalInfo", "areaReference", "city", "province", "state", "postalCode", "country"})
public class PhysicalAddress implements Serializable {

    /**
     * Street address.
     */
    @Schema(
            name = "streetAddress",
            example = "example street 18, 2nd floor, 18-B33")
    @JsonProperty("streetAddress")
    private String streetAddress;

    /**
     * Street name
     */
    @Schema(
            name = "street",
            example = "example street")
    @JsonProperty(value = "street")
    private String street;

    /**
     * House number.
     */
    @Schema(
            name = "houseNumber",
            example = "18",
            description = "House number")
    @JsonProperty("houseNumber")
    private int houseNumber;

    /**
     * House number addition
     */
    @Schema(
            name = "houseNumberAddition",
            example = "18-B33",
            description = "House number addition")
    @JsonProperty("houseNumberAddition")
    private String houseNumberAddition;

    /**
     * Address additional infos
     */
    @Schema(
            name = "addressAdditionalInfo",
            example = "2nd floor")
    @JsonProperty("addressAdditionalInfo")
    private String addressAdditionalInfo;

    /**
     * Area reference
     */
    @Schema(
            name = "areaReference",
            example = "Smallcity, Pinetree county")
    @JsonProperty("areaReference")
    private String areaReference;

    /**
     * City.
     */
    @Schema(
            name = "city",
            example = "Smallcity",
            description = "City")
    @JsonProperty("city")
    private String city;

    /**
     * Province.
     */
    @Schema(
            name = "province",
            description = "province",
            example = "Pinetree county")
    @JsonProperty("province")
    private String province;

    /**
     * State.
     */
    @Schema(
            name = "state",
            description = "state",
            example = "Tree state")
    @JsonProperty("state")
    private String state;

    /**
     * Postal code.
     */
    @Schema(
            name = "postalCode",
            example = "TR55555",
            description = "Postal code")
    @JsonProperty("postalCode")
    private String postalCode;

    /**
     * Country.
     */
    @Schema(
            name = "country",
            example = "FR",
            description = "Country")
    @JsonProperty("country")
    private String country;
}

