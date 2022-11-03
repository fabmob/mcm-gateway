package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class VehicleAssets implements Serializable {
    private static final long serialVersionUID = 1L;


    @Schema(
            name = "iconUrl",
            description = "URL pointing to the location of a graphic icon file representing the vehicle type")
    @JsonProperty("iconUrl")
    private String iconUrl;


    @Schema(
            name = "iconUrlDark",
            description = "URL pointing to the location of a graphic icon file representing this vehicle type in dark mode")
    @JsonProperty("iconUrlDark")
    private String iconUrlDark;


    @Schema(
            name = "iconLastModified",
            description = "Date that indicates the last time any included vehicle icon images were modified or updated")
    @JsonProperty("iconLastModified")
    private Date iconLastModified;
}