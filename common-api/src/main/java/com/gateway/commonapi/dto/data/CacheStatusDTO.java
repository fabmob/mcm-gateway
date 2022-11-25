package com.gateway.commonapi.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
@Data
@NoArgsConstructor
@Schema(name = "CacheStatusDTO", description = "Information about cache status")
public class CacheStatusDTO {
    @Schema(name = "isEnabled",
            description = "cache status",
            example = "false",
            required = true)
    @JsonProperty("isEnabled")
    private boolean isEnabled = false;

    public CacheStatusDTO(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
