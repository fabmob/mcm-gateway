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
            description = "Maximum time in minutes that a vehicle can be reserved before a rental begins")
    @JsonProperty("iconUrl")
    private String iconUrl;


    @Schema(
            name = "iconUrlDark",
            description = "Maximum time in minutes that a vehicle can be reserved before a rental begins")
    @JsonProperty("iconUrlDark")
    private String iconUrlDark;


    @Schema(
            name = "iconLastModified",
            description = "Maximum time in minutes that a vehicle can be reserved before a rental begins")
    @JsonProperty("iconLastModified")
    private Date iconLastModified;
}