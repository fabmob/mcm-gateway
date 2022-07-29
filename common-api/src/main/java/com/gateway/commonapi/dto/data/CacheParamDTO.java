package com.gateway.commonapi.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Schema(name = "CacheParamDTO", description = "Information about CacheParam")
public class CacheParamDTO {

    @Schema(name = "cacheParamId",
            description = "Cache param id",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            accessMode = Schema.AccessMode.READ_ONLY,
            required = true)
    @JsonProperty("cacheParamId")
    private UUID cacheParamId;

    @Schema(name = "mspId",
            description = "Msp id",
            example = "b814c97e-df56-4651-ac50-11525537964f",
            required = true)
    @JsonProperty("mspId")
    private UUID mspId;

    @Schema(name = "actionType",
            description = "Type of action to cache",
            example = "STATION_SEARCH")
    @JsonProperty("actionType")
    private String actionType;

    @Schema(name = "softTTL",
            description = "Time limit, in seconds, that say the parameter needs to be refreshed",
            example = "300",
            required = true)
    @JsonProperty("softTTL")
    private Integer softTTL;

    @Schema(name = "hardTTL",
            description = "Time limit, in seconds, that say the parameter is outdated",
            example = "600",
            required = true)
    @JsonProperty("hardTTL")
    private Integer hardTTL;

    @Schema(name = "refreshCacheDelay",
            description = "delay in seconds showing the wished delay before refresh the cache",
            example = "500",
            required = true)
    @JsonProperty("refreshCacheDelay")
    private Integer refreshCacheDelay;

}
