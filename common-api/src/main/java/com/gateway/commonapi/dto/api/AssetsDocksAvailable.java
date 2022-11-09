package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class AssetsDocksAvailable {

    /**
     * vehicleTypeId
     */
    @Schema(
            name = "vehicleTypeIds",
            example = "\"abc123\", \"def456\"",
            required = true,
            description = "REQUIRED if vehicle_docks_available is defined. An array of strings where each string represents a vehicle_type_id that is able to use a particular type of dock at the station")
    @JsonProperty("vehicleTypeIds")
    private List<String> vehicleTypeIds;

    /**
     * The total number of available docks at the station
     */
    @Schema(
            name = "count",
            example = "8",
            required = true,
            description = "The total number of available docks at the station")
    @JsonProperty("count")
    private Integer count;

}
