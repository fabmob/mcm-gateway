package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Car
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class Car {

    /**
     * Model of the car.
     **/
    @Schema(
            name = "model",
            example = "Model Y",
            description = "Model of the car")
    @JsonProperty("model")
    public String model = null;

    /**
     * Brand of the car.
     **/
    @Schema(
            name = "brand",
            example = "Tesla",
            description = "Brand of the car")
    @JsonProperty("brand")
    public String brand = null;
}
