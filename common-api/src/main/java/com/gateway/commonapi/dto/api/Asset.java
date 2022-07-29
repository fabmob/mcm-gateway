package com.gateway.commonapi.dto.api;


import java.io.Serializable;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * Asset
 * <p>
 * TOMP-API Standard
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Information about an asset")
@JsonPropertyOrder({
        "mspId", "assetId", "assetType", "isReserved",
        "isReservedFrom", "isReservedTo", "isDisabled", "mileage", "licensePlate","homeStationId","lastReported","planId","overriddenProperties","rentalAndroidUrl","rentalIosUrl","rentalWebUrl"})
public class Asset implements Serializable {

    @JsonProperty("mspId")
    @NotNull
    @Schema(description = "MSP Identifier",
            example = "b814c97e-df56-4651-ac50-11525537964f")
    private UUID mspId;

    @JsonProperty("assetId")
    @NotNull
    @Schema(description = "Asset Identifier",
            example= "Bike1111")
    private String assetId = null;

    @JsonProperty("assetType")
    @NotNull
    @Schema(description = "asset Type (example\":\"bike\", \"car\"...)",
            example = "bike")
    private String assetType = null;

    @JsonProperty("isReserved")
    @Schema(description = "True indicates the bike is currently reserved for someone else",
            example = "true")
    private Boolean isReserved = null;

    @JsonProperty("isReservedFrom")
    @Schema(description = "Beginning date-time of the reservation",
            example = "2022-03-01T10:35:49.196Z")
    private OffsetDateTime isReservedFrom = null;

    @JsonProperty("isReservedTo")
    @Schema(description = "Ending date-time of the reservation",
            example = "2022-03-01T10:36:49.196Z")
    private OffsetDateTime isReservedTo = null;

    public void setIsReservedFrom(OffsetDateTime isReservedFrom) {
        this.isReservedFrom = isReservedFrom;
    }

    public void setIsReservedTo(OffsetDateTime isReservedTo) {
        this.isReservedTo = isReservedTo;
    }

    public void setIsReservedFrom(String isReservedFrom) {
        this.isReservedFrom = StringUtils.isNotBlank(isReservedFrom) ? OffsetDateTime.parse(isReservedFrom) : null;
    }

    public void setIsReservedTo(String isReservedTo) {
        this.isReservedTo = StringUtils.isNotBlank(isReservedTo) ? OffsetDateTime.parse(isReservedTo) : null;
    }

    @JsonProperty("isDisabled")
    @Schema(description = "True indicates the asset is currently disabled (broken)",
            example = "false")
    private Boolean isDisabled = null;


    @JsonProperty("mileage")
    @Schema(description = "The current mileage of the asset",
            example = "1000")
    private Float mileage = null;

    @JsonProperty("licensePlate")
    @Schema(description = "The license plate of the asset",
            example = "BK 1111")
    private String licensePlate = null;

    @JsonProperty("homeStationId")
    @Schema(description = "The station_id of the station this vehicle must be returned to as defined in station_information.json.",
            example = "Sta00010")
    private String homeStationId = null;

    @JsonProperty("lastReported")
    @Schema(description = "The last time this vehicle reported its status to the operator's backend",
            example = "1609866109")
    private Timestamp lastReported = null;

    @JsonProperty("planId")
    @Schema(description = "The plan_id of the pricing plan this vehicle is eligible for as described in system_pricing_plans.json",
            example = "plan3")
    private String planId = null;

    @JsonProperty("overriddenProperties")
    private AssetProperties overriddenProperties = null;

    @Schema(
            example = "https://www.example.com/app?sid=1234567890&platform=android")
    @JsonProperty("rentalAndroidUrl")
    private String rentalAndroidUrl = null;

    @Schema(
            example = "https://www.example.com/app?sid=1234567890&platform=ios")
    @JsonProperty("rentalIosUrl")
    private String rentalIosUrl = null;

    @Schema(
            example = "https://www.example.com/app?sid=1234567890")
    @JsonProperty("rentalWebUrl")
    private String rentalWebUrl = null;

    /**
     * Deep link for booking a asset
     *
     * Use getActions().getAction(ActionType.BOOK_asset) to getBookDeeplink.
     */
    @Schema(
            name="bookDeeplink",
            description="Deep link to book a asset in the station",
            example = "https://book/Bike1111")
    @JsonProperty("bookDeeplink")
    private String bookDeeplink;

    /**
     * Deep link for using a
     *
     *  Use getActions().getAction(ActionType.USE_asset) to getUseDeeplink.
     */
    @Schema(
            name="useDeeplink",
            description="Deep link to use a asset in the station",
            example = "https://use/Bike1111")
    @JsonProperty("useDeeplink")
    private String useDeeplink;

    public Asset id(String id) {
        this.assetId = id;
        return this;
    }


    public Asset(UUID mspId, String assetId, String assetType, Boolean isReserved) {
        this.mspId = mspId;
        this.assetId = assetId;
        this.assetType = assetType;
        this.isReserved = isReserved;
    }
}








