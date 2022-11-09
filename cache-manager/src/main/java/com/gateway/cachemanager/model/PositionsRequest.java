package com.gateway.cachemanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gateway.commonapi.dto.api.geojson.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Slf4j
@Jacksonized
@Data
@NoArgsConstructor
@Validated
@Schema(description = "Body for request on positions")
public class PositionsRequest implements Serializable {

    /**
     * List of coordinates to scan to retrieve all data
     */
    @Schema(
            name = "positions",
            description = "List of coordinates to scan to retrieve all data",
            required = true)
    @NotNull
    @JsonProperty("positions")
    private List<Position> positions;
}
