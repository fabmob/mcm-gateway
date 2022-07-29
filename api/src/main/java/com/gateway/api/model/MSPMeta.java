package com.gateway.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.api.common.HrefLink;
import com.gateway.commonapi.utils.enums.MSPType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Jacksonized
@Data
@AllArgsConstructor
@Validated
@Schema(description = "Metadata of an MSP")
@JsonPropertyOrder({
        "mspId", "name", "operator", "type",
        "url", "urlWebview",
        "logo", "logoUrl", "logoFormat", "primaryColor", "secondaryColor",
        "priceList", "_links"})
public class MSPMeta implements Serializable {

    /**
     * MSP identifier
     */
    @Schema(
            name = "mspId",
            description = "MSP identifier",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            required = true)
    @NotNull
    @JsonProperty("mspId")
    private UUID mspId;

    /**
     * MSP name
     */
    @Schema(
            name = "name",
            description = "MSP name",
            example = "Voi")
    @JsonProperty("name")
    private String name;

    /**
     * MSP operator
     */
    @Schema(
            name = "operator",
            description = "MSP operator name",
            example = "Voi Trottinettes")
    @JsonProperty("operator")
    private String operator;

    /**
     * MSP type
     */
    @Schema(
            name = "type",
            description = "MSP type",
            example = "TROTTINETTE",
            required = true)
    @NotNull
    @JsonProperty("type")
    private MSPType type;

    /**
     * Price list.
     */
    @Schema(
            name="priceList",
            description="Price list")
    @JsonProperty("priceList")
    private PriceList priceList;

    /**
     * URL for the MSP.
     */
    @Schema(
            name = "url",
            description = "URL to know more about the MSP",
            example = "https://MSP_example_Trotti.com/logo")
    @JsonProperty("url")
    private String url;

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
     * Logo for the MSP.
     */
    @Schema(
            name = "logo",
            description = "Logo for the MSP")
    @JsonProperty("logo")
    private byte[] logo;

    /**
     * URL of the logo.
     */
    @Schema(
            name = "logoUrl",
            description = "URL of the logo",
            example = "https://MSP_example_Trotti.com/logo")
    @JsonProperty("logoUrl")
    private String logoUrl;

    /**
     * Format of the logo.
     */
    @Schema(
            name = "logoFormat",
            description = "Format of the logo (SVG, PNG...)",
            example = "PNG")
    @JsonProperty("logoFormat")
    private String logoFormat;

    /**
     * Primary color of the MSP.
     */
    @Schema(
            name = "primaryColor",
            description = "Primary color of the MSP",
            example = "RED")
    @JsonProperty("primaryColor")
    private String primaryColor;

    /**
     * Secondary color of the MSP.
     */
    @Schema(
            name = "secondaryColor",
            description = "Secondary color of the MSP",
            example = "Yellow")
    @JsonProperty("secondaryColor")
    private String secondaryColor;


    /**
     * Links
     */
    @Schema(
            name = "_links",
            example = "{ \"self\": { \"href\": \"https://example.com/api/resource/{id}\" }, \"action\": { \"href\": \"https://example.com/api/resource/{id}/action\" } }",
            description = "Useful links to external resources")
    @JsonProperty("_links")
    private final Map<String, HrefLink> links;

    /**
     * Create a MSP without any links.
     */
    public MSPMeta() {
        this.links = new HashMap<>();
    }

    /**
     * Add a link to the MSP.
     *
     * @param rel  Relation of the link.
     * @param link URL.
     */
    public void add(String rel, HrefLink link) {
        Assert.notNull(link, "Link must not be null!");
        this.links.put(rel, link);
    }

    /**
     * Add a link to the MSP.
     *
     * @param link HATEOAS link.
     */
    public void addHateoasLink(Link link) {
        Assert.notNull(link, "Link must not be null!");
        this.links.put(link.getRel(), HrefLink.getLinkFromHateoasLink(link));
    }



    /**
     * flag for enabling or disabling the MSP
     */
    @JsonIgnore
    private boolean isEnabled;

    /**
     * flag for MSP exposing Vehicles information
     */
    @JsonIgnore
    private boolean hasVehicle;

    /**
     * flag for MSP exposing Stations information
     */
    @JsonIgnore
    private boolean hasStation;

    /**
     * flag for MSP exposing Stations status
     */
    @JsonIgnore
    private boolean hasStationStatus;

    /**
     * flag for MSP having an operating zone
     */
    @JsonIgnore
    private boolean hasOperatingZone;

    /**
     * flag for MSP having a no parking zone
     */
    @JsonIgnore
    private boolean hasNoParkingZone;

    /**
     * flag for MSP having a preferential parking zone
     */
    @JsonIgnore
    private boolean hasPrefParkingZone;

    /**
     * flag for MSP having a speed limited zone
     */
    @JsonIgnore
    private boolean hasSpeedLimitZone;

    /**
     * flag for MSP having car parks
     */
    @JsonIgnore
    private boolean hasParking;

    /**
     * flag for MSP having to do hold
     */
    @JsonIgnore
    private boolean hasHold;

    // Constructor used for mocks
    public MSPMeta(UUID mspId, String name, MSPType type, Boolean hasStation, Boolean hasStationStatus, Boolean hasVehicle) {
        this.mspId = mspId;
        this.name = name;
        this.type = type;
        this.hasStation = hasStation;
        this.hasStationStatus = hasStationStatus;
        this.hasVehicle = hasVehicle;
        this.links = new HashMap<>();
    }
}
