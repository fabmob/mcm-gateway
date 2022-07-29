package com.gateway.commonapi.dto.data;

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
@Schema(description =  "Information about MspMeta")
@JsonPropertyOrder({"mspId", "hasVehicle", "hasStation", "hasStationStatus", "hasOperationZone", "hasNoParkingZone", "hasPrefParkingZone", "hasSpeedLimitZone", "hasParking", "hasHold", "priceListId", "urlWebview", "isEnebled", "name", "operator", "type", "url", "priceListText", "logo", "logoUrl", "logoFormat", "primaryColor", "secondaryColor"})
public class MspMetaDTO {

    /**
     * MspMetaDTO identifier
     */
    @Schema(
            name = "mspId",
            description = "MspMeta identifier",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            accessMode = Schema.AccessMode.READ_ONLY,
            required = true)
    @JsonProperty("mspId")
    private UUID mspId;

    /**
     * MspMeta name
     */
    @Schema(
            name = "name",
            description = "MspMeta name",
            example = "Trotti")
    @JsonProperty("name")
    private String name = null;

    /**
     * MspMeta operator
     */
    @Schema(
            name = "operator",
            description = "MspMeta operator name",
            example = "Voi Trotti")
    @JsonProperty("operator")
    private String operator = null;

    /**
     * MspMeta type
     */
    @Schema(
            name = "type",
            description = "MspMeta type",
            example = "TROTTINETTE",
            required = true)
    @JsonProperty("type")
    private MSPType type = null;

    /**
     * URL for the MspMeta.
     */
    @Schema(
            name = "url",
            description = "URL to know more about the MspMeta",
            example = "https://MSP_example_Trotti.com")
    @JsonProperty("url")
    private String url = null;

    /**
     * PriceListDTO.
     */
    @Schema(
            name = "priceList",
            description = "Price list")
    @JsonProperty("priceList")
    private PriceListDTO priceList = null;

    /**
     * URL of the logo.
     */
    @Schema(
            name = "logoUrl",
            description = "URL of the logo",
            example = "https://MSP_example_Trotti.com/logo")
    @JsonProperty("logoUrl")
    private String logoUrl = null;

    /**
     * Format of the logo.
     */
    @Schema(
            name = "logoFormat",
            description = "Format of the logo (SVG, PNG...)",
            example = "PNG")
    @JsonProperty("logoFormat")
    private String logoFormat = null;

    /**
     * Primary color of the MspMeta.
     */
    @Schema(
            name = "primaryColor",
            description = "Primary color of the MspMeta",
            example = "RED")
    @JsonProperty("primaryColor")
    private String primaryColor = null;

    /**
     * Secondary color of the MspMeta.
     */
    @Schema(
            name = "secondaryColor",
            description = "Secondary color of the MspMeta",
            example = "Yellow")
    @JsonProperty("secondaryColor")
    private String secondaryColor = null;

    /**
     * flag for MspMeta exposing Vehicles information
     */
    @Schema(
            name = "hasVehicle",
            description = "flag for MspMeta exposing Vehicles information",
            example = "true")
    @JsonProperty("hasVehicle")
    private Boolean hasVehicle = null;

    /**
     * flag for MspMeta exposing Stations information
     */
    @Schema(
            name = "hasStation",
            description = "flag for MspMeta exposing Stations information",
            example = "true")
    @JsonProperty("hasStation")
    private Boolean hasStation = null;

    /**
     * flag for MspMeta exposing Stations status
     */
    @Schema(
            name = "hasStationStatus",
            description = "flag for MspMeta exposing Stations status",
            example = "true")
    @JsonProperty("hasStationStatus")
    private Boolean hasStationStatus = null;

    /**
     * flag for MspMeta having an operating zone
     */
    @Schema(
            name = "hasOperatingZone",
            description = "flag for MspMeta having an operating zone",
            example = "true")
    @JsonProperty("hasOperatingZone")
    private Boolean hasOperatingZone = null;

    /**
     * flag for MspMeta having a no parking zone
     */
    @Schema(
            name = "hasNoParkingZone",
            description = "flag for MspMeta having a no parking zone",
            example = "false")
    @JsonProperty("hasNoParkingZone")
    private Boolean hasNoParkingZone = null;

    /**
     * flag for MspMeta having a preferential parking zone
     */
    @Schema(
            name = "hasPrefParkingZone",
            description = "flag for MspMeta having a preferential parking zone",
            example = "false")
    @JsonProperty("hasPrefParkingZone")
    private Boolean hasPrefParkingZone = null;

    /**
     * flag for MspMeta having a speed limited zone
     */
    @Schema(
            name = "hasSpeedLimitZone",
            description = "flag for MspMeta having a speed limited zone",
            example = "false")
    @JsonProperty("hasSpeedLimitZone")
    private Boolean hasSpeedLimitZone = null;

    /**
     * flag for MspMeta having car parks
     */
    @Schema(
            name = "hasParking",
            description = "flag for MspMeta having car parks",
            example = "true")
    @JsonProperty("hasParking")
    private Boolean hasParking = null;

    /**
     * flag for MspMeta having to do hold
     */
    @Schema(
            name = "hasHold",
            description = "flag for MspMeta having to do hold",
            example = "true")
    @JsonProperty("hasHold")
    private Boolean hasHold = null;

    /**
     * True if the URL can be displayed in a web view.
     */
    @Schema(
            name = "urlWebview",
            description = "True if the URL can be displayed in a web view",
            example = "true")
    @JsonProperty("urlWebview")
    private Boolean urlWebview;

    /**
     * flag for enabling or disabling the MSP
     */
    @JsonProperty("isEnabled")
    private Boolean isEnabled = null;

    @Override
    public String toString() {
        return "";
    }
}

