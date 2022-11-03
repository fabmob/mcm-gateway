package com.gateway.commonapi.dto.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

import javax.validation.constraints.NotBlank;


@JsonComponent
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="GatewayParamsDTO", description = "Gateway configuration's parameters")
public class GatewayParamsDTO {

    @Schema(name = "paramKey",
            description = "Parameter's name",
            example = "CACHE_ACTIVATION",
            required = true)
    @NotBlank
    @JsonProperty("paramKey")
    private String paramKey;

    @Schema(name = "paramValue",
            description = "Parameter's value",
            example = "false",
            required = true)
    @JsonProperty("paramValue")
    private String paramValue;

}
