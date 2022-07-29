package com.gateway.commonapi.dto.api;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Bean for a global view for around me.
 */
@Data
@NoArgsConstructor
@Validated
@Schema(description = "Global view for around me")
@JsonPropertyOrder({ "stations", "stationsStatus", "assets", "parkings", "_links"})
public class GlobalView  {

    /**
     * Static informations about the stations.
     */

    @Schema(name="stations",
            description="List of stations around")
    @JsonProperty("stations")
    private List<Station> stations = new ArrayList<>();

    /**
     * Dynamic informations about the stations.
     */
    @Schema(
            name="stationsStatus",
            description="List of around stations' status")
    @JsonProperty("stationsStatus")
    private List<StationStatus> stationsStatus = new ArrayList<>();

    /**
     * Information about the vehicles.
     */
    @Schema(
            name="assets",
            description="List of assets around")
    @JsonProperty("assets")
    private List<Asset> assets = new ArrayList<>();

    /**
     * Static informations about the car parks.
     */

    @Schema(
            name="parkings",
            description="List of car parks around")
    @JsonProperty("parkings")
    private List<Parking> parkings = new ArrayList<>();


    public GlobalView(List<Station> stations, List<StationStatus> stationsStatus, List<Asset> assets, List<Parking> parkings) {
        this.stations = stations;
        this.stationsStatus = stationsStatus;
        this.assets = assets;
        this.parkings = parkings;
    }
}

