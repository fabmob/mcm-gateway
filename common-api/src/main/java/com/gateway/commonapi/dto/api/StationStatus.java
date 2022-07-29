package com.gateway.commonapi.dto.api;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Bean for dynamic information about a station.
 *
 * Based on GBFS format retrieved from <a href="https://github.com/NABSA/gbfs/blob/master/gbfs.md#station_statusjson">station_status.json</a>.
 */

@Slf4j
@Jacksonized
@Data
@AllArgsConstructor
@Validated
@Schema(description = "Dynamic information about a station")
@JsonPropertyOrder({
        "mspId", "stationId",
        "numAssetsAvailable", "numAssetsAvailableType", "numAssetsDisabled",
        "numDocksAvailable", "assetsDocksAvailable","numDocksDisabled",
        "isInstalled", "isRenting", "isReturning",
        "actions", "bookDeeplink", "useDeeplink",
        "lastReported", "_links"})
public class StationStatus implements Serializable {


    /**
     * Default constructor.
     */
    public StationStatus() {
        actions = new ActionList();
    }

    /**
     * MSP identifier.
     */
    @Schema(
            name = "mspId",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            description = "Identifier of the MSP",
            required = true)
    @JsonProperty("mspId")
    @NotNull
    private UUID mspId;

    /**
     * Station identifier.
     */
    @Schema(
            name = "stationId",
            example = "ST400",
            description = "Identifier of the station",
            required = true)
    @JsonProperty("stationId")
    @NotNull
    private String stationId;

    /**
     * Number of available assets in the station.
     */
    @Schema(
            name = "numAssetsAvailable",
            example = "12",
            description = "Number of available assets in the station",
            required = true)
    @JsonProperty("numAssetsAvailable")
    @NotNull
    private Long numAssetsAvailable;

    /**
     * Number of available assets by type in the station.
     */
    @Schema(
            name = "numAssetsAvailableType",
            description = "Number of available assets in the station by asset type")
    @JsonProperty("numAssetsAvailableType")
    private List<NumAssetsAvailableType> numAssetsAvailableType;

    /**
     * Number of disabled assets in the station.
     */
    @Schema(
            name = "numAssetsDisabled",
            example = "0",
            description = "Number of disabled assets in the station")
    @JsonProperty("numAssetsDisabled")
    @JsonAlias("numAssetsDisabled")
    private Long numAssetsDisabled;

    /**
     * Number of available docks in the station.
     */
    @Schema(
            name = "numDocksAvailable",
            example = "10",
            required = true,
            description = "Number of available docks in the station")
    @JsonProperty("numDocksAvailable")
    @NotNull
    private Long numDocksAvailable;

    /**
     * Number of available assets in the station by asset type.
     */
    @Schema(
            name = "assetsDocksAvailable",
            description = "Number of available assets in the station by asset type")
    @JsonProperty("assetsDocksAvailable")
    @NotNull
    private List<AssetsDocksAvailable> assetsDocksAvailable;

    /**
     * Number of disabled docks in the station.
     */
    @Schema(
            name = "numDocksDisabled",
            example = "0",
            description = "Number of disabled docks in the station")
    @JsonProperty("numDocksDisabled")
    private Long numDocksDisabled;


    /**
     * True if the station is currently on the street.
     */
    @Schema(
            name = "isInstalled",
            example = "true",
            description = "True if the station is currently on the street",
            required = true)
    @JsonProperty("isInstalled")
    @NotNull
    private Boolean isInstalled;

    /**
     * True if the station is renting assets.
     */
    @Schema(
            name = "isRenting",
            example = "true",
            description = "True if the station is renting assets")
    @JsonProperty("isRenting")
    @NotNull
    private Boolean isRenting;

    /**
     * True if the station is accepting asset returns.
     */
    @Schema(
            name = "isReturning",
            example = "true",
            description = "True if the station is accepting asset returns")
    @JsonProperty("isReturning")
    @NotNull
    private Boolean isReturning;

    /**
     * Actions.
     */
    @Schema(
            name = "actions",
            description = "Actions that can be performed on the station")
    @JsonProperty("actions")
    @Setter(AccessLevel.NONE)
    private ActionList actions;

    /**
     * Deep link for booking a asset in the station.
     *
     * Use getActions().getAction(ActionType.BOOK_asset) to getBookDeeplink.
     */
    @Schema(
            name="bookDeeplink",
            description="Deep link to book a asset in the station",
            example = "https://station/asset/book")
    @JsonProperty("bookDeeplink")
    private String bookDeeplink;

    /**
     * Deep link for using a asset in the station.
     *
     *  Use getActions().getAction(ActionType.USE_asset) to getUseDeeplink.
     */
    @Schema(
            name="useDeeplink",
            description="Deep link to use a asset in the station",
            example = "https://station/asset/use")
    @JsonProperty("useDeeplink")
    private String useDeeplink;

    /**
     * Last reported time stamp.
     */
    @Schema(
            name = "lastReported",
            description = "Last reported time stamp",
            example = "1646302112")
    @JsonProperty("lastReported")
    private Timestamp lastReported;


    public StationStatus(UUID mspId, String stationId, Long numAssetsAvailable, Boolean isInstalled, Boolean isRenting, Boolean isReturning) {
        this.mspId = mspId;
        this.stationId = stationId;
        this.numAssetsAvailable = numAssetsAvailable;
        this.isInstalled = isInstalled;
        this.isRenting = isRenting;
        this.isReturning = isReturning;
        this.actions = null;
    }
}


