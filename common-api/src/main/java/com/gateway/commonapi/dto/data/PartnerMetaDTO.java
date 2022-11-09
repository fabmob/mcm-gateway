package com.gateway.commonapi.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.TypeEnum;
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
@Schema(description =  "Information about PartnerMeta")
@JsonPropertyOrder({"partnerId", "partnerType", "hasVehicle", "hasStation", "hasStationStatus", "hasOperationZone", "hasNoParkingZone", "hasPrefParkingZone", "hasSpeedLimitZone", "hasParking", "hasHold", "priceListId", "urlWebview", "isEnabled", "name", "operator", "type", "url", "priceListText", "logo", "logoUrl", "logoFormat", "primaryColor", "secondaryColor"})
public class PartnerMetaDTO {

    /**
     * PartnerMetaDTO identifier
     */
    @Schema(
            name = "partnerId",
            description = "PartnerMeta identifier",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            accessMode = Schema.AccessMode.READ_ONLY,
            required = true)
    @JsonProperty("partnerId")
    private UUID partnerId;


    /**
     * PartnerMetaDTO identifier
     */
    @Schema(
            name = "partnerType",
            description = "Type of the partner \"MSP\" or \"MAAS\"",
            example = "MSP",
            required = true)
    @JsonProperty("partnerType")
    private PartnerTypeEnum partnerType;

    /**
     * PartnerMeta name
     */
    @Schema(
            name = "name",
            description = "PartnerMeta name",
            example = "Trotti")
    @JsonProperty("name")
    private String name = null;

    /**
     * PartnerMeta operator
     */
    @Schema(
            name = "operator",
            description = "PartnerMeta operator name",
            example = "Voi Trotti")
    @JsonProperty("operator")
    private String operator = null;

    /**
     * PartnerMeta type
     */
    @Schema(
            name = "type",
            description = "ENUM:\"PUBLIC_TRANSPORT\", \"CARPOOLING\", \"PARKING\", \"EV_CHARGING\", \"SELF_SERVICE_BICYCLE\", \"CAR_SHARING\", \"FREE_FLOATING\", \"TAXI_VTC\", \"MAAS_APPLICATION\", \"MAAS_EDITOR\"\n" +
                    "With \"MAAS_APPLICATION\" or \"MAAS_EDITOR\" only for partnerType \"MAAS\", other values for partnerType \"Partner\"",
            example = "CARPOOLING",
            required = true)
    @JsonProperty("type")
    private TypeEnum type = null;

    /**
     * URL for the PartnerMeta.
     */
    @Schema(
            name = "url",
            description = "URL to know more about the PartnerMeta",
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
     * Primary color of the PartnerMeta.
     */
    @Schema(
            name = "primaryColor",
            description = "Primary color of the PartnerMeta",
            example = "RED")
    @JsonProperty("primaryColor")
    private String primaryColor = null;

    /**
     * Secondary color of the PartnerMeta.
     */
    @Schema(
            name = "secondaryColor",
            description = "Secondary color of the PartnerMeta",
            example = "Yellow")
    @JsonProperty("secondaryColor")
    private String secondaryColor = null;

    /**
     * flag for PartnerMeta exposing Vehicles information
     */
    @Schema(
            name = "hasVehicle",
            description = "flag for PartnerMeta exposing Vehicles information",
            example = "true")
    @JsonProperty("hasVehicle")
    private Boolean hasVehicle = null;

    /**
     * flag for PartnerMeta exposing Stations information
     */
    @Schema(
            name = "hasStation",
            description = "flag for PartnerMeta exposing Stations information",
            example = "true")
    @JsonProperty("hasStation")
    private Boolean hasStation = null;

    /**
     * flag for PartnerMeta exposing Stations status
     */
    @Schema(
            name = "hasStationStatus",
            description = "flag for PartnerMeta exposing Stations status",
            example = "true")
    @JsonProperty("hasStationStatus")
    private Boolean hasStationStatus = null;

    /**
     * flag for PartnerMeta having an operating zone
     */
    @Schema(
            name = "hasOperatingZone",
            description = "flag for PartnerMeta having an operating zone",
            example = "true")
    @JsonProperty("hasOperatingZone")
    private Boolean hasOperatingZone = null;

    /**
     * flag for PartnerMeta having a no parking zone
     */
    @Schema(
            name = "hasNoParkingZone",
            description = "flag for PartnerMeta having a no parking zone",
            example = "false")
    @JsonProperty("hasNoParkingZone")
    private Boolean hasNoParkingZone = null;

    /**
     * flag for PartnerMeta having a preferential parking zone
     */
    @Schema(
            name = "hasPrefParkingZone",
            description = "flag for PartnerMeta having a preferential parking zone",
            example = "false")
    @JsonProperty("hasPrefParkingZone")
    private Boolean hasPrefParkingZone = null;

    /**
     * flag for PartnerMeta having a speed limited zone
     */
    @Schema(
            name = "hasSpeedLimitZone",
            description = "flag for PartnerMeta having a speed limited zone",
            example = "false")
    @JsonProperty("hasSpeedLimitZone")
    private Boolean hasSpeedLimitZone = null;

    /**
     * flag for PartnerMeta having car parks
     */
    @Schema(
            name = "hasParking",
            description = "flag for PartnerMeta having car parks",
            example = "true")
    @JsonProperty("hasParking")
    private Boolean hasParking = null;

    /**
     * flag for PartnerMeta having to do hold
     */
    @Schema(
            name = "hasHold",
            description = "flag for PartnerMeta having to do hold",
            example = "true")
    @JsonProperty("hasHold")
    private Boolean hasHold = null;

    @Schema(
            name = "hasVehicleTypes",
            description = "flag for PartnerMeta having vehicle types",
            example = "true")
    @JsonProperty("hasVehicleTypes")
    private Boolean hasVehicleTypes = null;

    @Schema(
            name = "hasPricingPlan",
            description = "flag for PartnerMeta having pricing plan",
            example = "true")
    @JsonProperty("hasPricingPlan")
    private Boolean hasPricingPlan = null;

    @Schema(
            name = "hasPing",
            description = "flag for PartnerMeta having ping system",
            example = "true")
    @JsonProperty("hasPing")
    private Boolean hasPing = null;

    @Schema(
            name = "hasCarpoolingBookingPost",
            description = "flag for PartnerMeta handling carpooling booking post",
            example = "true")
    @JsonProperty("hasCarpoolingBookingPost")
    private Boolean hasCarpoolingBookingPost = null;

    @Schema(
            name = "hasCarpoolingBookingPatch",
            description = "flag for PartnerMeta handling carpooling booking patch action",
            example = "true")
    @JsonProperty("hasCarpoolingBookingPatch")
    private Boolean hasCarpoolingBookingPatch = null;

    @Schema(
            name = "hasCarpoolingBookingGet",
            description = "flag for PartnerMeta handling carpooling booking get action",
            example = "true")
    @JsonProperty("hasCarpoolingBookingGet")
    private Boolean hasCarpoolingBookingGet = null;

    @Schema(
            name = "hasCarpoolingBookingEvent",
            description = "flag for PartnerMeta handling carpooling booking event",
            example = "true")
    @JsonProperty("hasCarpoolingBookingEvent")
    private Boolean hasCarpoolingBookingEvent = null;

    @Schema(
            name = "hasCarpoolingDriverJourney",
            description = "flag for PartnerMeta handling carpooling driver journey endpoint",
            example = "true")
    @JsonProperty("hasCarpoolingDriverJourney")
    private Boolean hasCarpoolingDriverJourney = null;

    @Schema(
            name = "hasCarpoolingPassengerJourney",
            description = "flag for PartnerMeta handling carpooling passenger journey endpoint",
            example = "true")
    @JsonProperty("hasCarpoolingPassengerJourney")
    private Boolean hasCarpoolingPassengerJourney = null;

    @Schema(
            name = "hasCarpoolingDriverTrip",
            description = "flag for PartnerMeta handling carpooling driver trip endpoint",
            example = "true")
    @JsonProperty("hasCarpoolingDriverTrip")
    private Boolean hasCarpoolingDriverTrip = null;

    @Schema(
            name = "hasCarpoolingPassengerTrip",
            description = "flag for PartnerMeta handling carpooling passenger trip endpoint",
            example = "true")
    @JsonProperty("hasCarpoolingPassengerTrip")
    private Boolean hasCarpoolingPassengerTrip = null;

    @Schema(
            name = "hasCarpoolingMessages",
            description = "flag for PartnerMeta handling messages",
            example = "true")
    @JsonProperty("hasCarpoolingMessages")
    private Boolean hasCarpoolingMessages = null;

    @Schema(
            name = "hasCarpoolingStatus",
            description = "flag for PartnerMeta handling status action (same as ping)",
            example = "true")
    @JsonProperty("hasCarpoolingStatus")
    private Boolean hasCarpoolingStatus = null;

    @Schema(
            name = "hasAroundMe",
            description = "flag for PartnerMeta handling aroundMe action",
            example = "true")
    @JsonProperty("hasAroundMe")
    private Boolean hasAroundMe = null;



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

