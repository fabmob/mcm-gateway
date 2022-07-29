package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Vehicle air quality certificate")
@Validated
public class EcoLabel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(
            name = "countryCode",
            example = "FR",
            description = "REQUIRED if eco_label is defined. Country where the eco_sticker applies.",
            required = true)
    @JsonProperty("countryCode")
    private Integer countryCode;

    @Schema(
            name = "ecoSticker",
            example = "critair_2",
            description = "REQUIRED if eco_label is defined. Country where the eco_sticker applies.",
            required = true)
    @JsonProperty("ecoSticker")
    private Integer ecoSticker;
}
