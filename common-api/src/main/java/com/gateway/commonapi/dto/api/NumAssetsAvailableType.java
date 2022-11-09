package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Schema(description = "Number of available assets by type in the station")
public class NumAssetsAvailableType {
    /**
     * vehicleTypeId
     */
    @Schema(
            name = "vehicleTypeId",
            example = "Mechanical",
            required = true,
            description = "REQUIRED if the vehicle_types_available is defined. The vehicle_type_id of each vehicle type at the station as described in vehicle_types.json.")
    @JsonProperty("vehicleTypeId")
    private String vehicleTypeId;

    /**
     * The total number of available vehicles of the corresponding vehicleTypeId
     */
    @Schema(
            name = "count",
            example = "8",
            required = true,
            description = "The total number of available vehicles of the corresponding vehicle_type_id.")
    @JsonProperty("count")
    private Integer count;

}
