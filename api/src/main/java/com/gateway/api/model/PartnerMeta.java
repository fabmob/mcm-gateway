package com.gateway.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.api.common.HrefLink;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.TypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Validated
@Schema(description = "Metadata of a Partner")
@JsonPropertyOrder({
        "partnerId", "partnerType", "name", "operator", "type",
        "url", "urlWebview",
        "logo", "logoUrl", "logoFormat", "primaryColor", "secondaryColor",
        "priceList", "_links"})
public class PartnerMeta implements Serializable {

    /**
     * Partner identifier
     */
    @Schema(
            name = "partnerId",
            description = "partner identifier",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            required = true)
    @NotNull
    @JsonProperty("partnerId")
    private UUID partnerId;

    /**
     * partner type
     */
    @Schema(
            name = "partnerType",
            description = "Type of the partner \"MSP\" or \"MAAS\"",
            example = "MAAS",
            required = true)
    @JsonProperty("partnerType")
    private PartnerTypeEnum partnerType;

    /**
     * Partner name
     */
    @Schema(
            name = "name",
            description = "The operator trade name",
            example = "Voi")
    @JsonProperty("name")
    private String name;

    /**
     * Partner operator
     */
    @Schema(
            name = "operator",
            description = "The operator identifier. MUST be a Root Domain (example operator.org) owned by the operator or a Fully Qualified Domain Name (example carpool.mycity.com) exclusively operated by the operator",
            example = "Voi Trottinettes")
    @JsonProperty("operator")
    private String operator;

    /**
     * Partner type
     */
    @Schema(
            name = "type",
            description = "ENUM:\"PUBLIC_TRANSPORT\", \"CARPOOLING\", \"PARKING\", \"EV_CHARGING\", \"SELF_SERVICE_BICYCLE\", \"CAR_SHARING\", \"FREE_FLOATING\", \"TAXI_VTC\", \"MAAS_APPLICATION\", \"MAAS_EDITOR\"\n" +
                    "With \"MAAS_APPLICATION\" or \"MAAS_EDITOR\" only for partnerType \"MAAS\", other values for partnerType \"Partner\"",
            example = "CARPOOLING",
            required = true)
    @NotNull
    @JsonProperty("type")
    private TypeEnum type;

    /**
     * Price list.
     */
    @Schema(
            name = "priceList",
            description = "Price list")
    @JsonProperty("priceList")
    private PriceList priceList;

    /**
     * URL for the Partner.
     */
    @Schema(
            name = "url",
            description = "URL to know more about the Partner",
            example = "https://Partner_example_Trotti.com/logo")
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
     * Logo for the Partner.
     */
    @Schema(
            name = "logo",
            description = "Logo for the Partner")
    @JsonProperty("logo")
    private byte[] logo;

    /**
     * URL of the logo.
     */
    @Schema(
            name = "logoUrl",
            description = "URL of the logo",
            example = "https://Partner_example_Trotti.com/logo")
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
     * Primary color of the Partner.
     */
    @Schema(
            name = "primaryColor",
            description = "Primary color of the Partner",
            example = "RED")
    @JsonProperty("primaryColor")
    private String primaryColor;

    /**
     * Secondary color of the Partner.
     */
    @Schema(
            name = "secondaryColor",
            description = "Secondary color of the Partner",
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
    private final Map<String, HrefLink> links = new HashMap<>();

    /**
     * Add a link to the Partner.
     *
     * @param rel  Relation of the link.
     * @param link URL.
     */
    public void add(String rel, HrefLink link) {
        Assert.notNull(link, "Link must not be null!");
        this.links.put(rel, link);
    }

    /**
     * Add a link to the Partner.
     *
     * @param link HATEOAS link.
     */
    public void addHateoasLink(Link link) {
        Assert.notNull(link, "Link must not be null!");
        this.links.put(link.getRel().value(), HrefLink.getLinkFromHateoasLink(link));
    }


    /**
     * flag for enabling or disabling the Partner
     */
    @JsonIgnore
    private boolean isEnabled;

    /**
     * flag for Partner exposing Vehicles information
     */
    @JsonIgnore
    private boolean hasVehicle;

    /**
     * flag for Partner exposing Stations information
     */
    @JsonIgnore
    private boolean hasStation;

    /**
     * flag for Partner exposing Stations status
     */
    @JsonIgnore
    private boolean hasStationStatus;

    /**
     * flag for Partner having an operating zone
     */
    @JsonIgnore
    private boolean hasOperatingZone;

    /**
     * flag for Partner having a no parking zone
     */
    @JsonIgnore
    private boolean hasNoParkingZone;

    /**
     * flag for Partner having a preferential parking zone
     */
    @JsonIgnore
    private boolean hasPrefParkingZone;

    /**
     * flag for Partner having a speed limited zone
     */
    @JsonIgnore
    private boolean hasSpeedLimitZone;

    /**
     * flag for Partner having car parks
     */
    @JsonIgnore
    private boolean hasParking;

    /**
     * flag for Partner having to do hold
     */
    @JsonIgnore
    private boolean hasHold;

    @JsonIgnore
    private boolean hasVehicleTypes;

    @JsonIgnore
    private boolean hasPricingPlan;
    @JsonIgnore
    private boolean hasPing;
    @JsonIgnore
    private boolean hasCarpoolingBookingPost;
    @JsonIgnore
    private boolean hasCarpoolingBookingPatch;
    @JsonIgnore
    private boolean hasCarpoolingBookingGet;
    @JsonIgnore
    private boolean hasCarpoolingBookingEvent;
    @JsonIgnore
    private boolean hasCarpoolingDriverJourney;
    @JsonIgnore
    private boolean hasCarpoolingPassengerJourney;
    @JsonIgnore
    private boolean hasCarpoolingDriverTrip;
    @JsonIgnore
    private boolean hasCarpoolingPassengerTrip;
    @JsonIgnore
    private boolean hasCarpoolingMessages;
    @JsonIgnore
    private boolean hasCarpoolingStatus;
    @JsonIgnore
    private boolean hasAroundMe;


}
