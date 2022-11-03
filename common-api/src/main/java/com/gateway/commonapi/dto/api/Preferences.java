package com.gateway.commonapi.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Validated
public class Preferences implements Serializable {
    /**
     * If driver journey, specifies if the driver allows smoking in the car.
     */
    @Schema(name = "smoking",
            example = "true",
            description = "If driver journey, specifies if the driver allows smoking in the car.")
    @JsonProperty("smoking")
    public Boolean smoking = null;

    /**
     * If driver journey, specifies if the driver allows animals in the car.
     */
    @Schema(name = "animals",
            example = "true",
            description = "If driver journey, specifies if the driver allows animals in the car.")
    @JsonProperty("animals")
    public Boolean animals = null;

    /**
     * If driver journey, specifies if the driver allows music in the car.
     */
    @Schema(name = "music",
            example = "true",
            description = "If driver journey, specifies if the driver enjoys music in the car.")
    @JsonProperty("music")
    public Boolean music = null;

    /**
     * If driver journey, specifies if the driver enjoys talking with passengers.
     */
    @Schema(name = "isTalker",
            example = "true",
            description = "If driver journey, specifies if the driver enjoys talking with passengers.")
    @JsonProperty("isTalker")
    public Boolean isTalker = null;

    /**
     * If driver journey, specifies the size of allowed luggage. From very small (1) to very big (5). (min:1, max:5).
     */
    @Schema(name = "luggageSize",
            example = "3",
            description = "If driver journey, specifies the size of allowed luggage. From very small (1) to very big (5). (min:1, max:5)")
    @JsonProperty("luggageSize")
    @Min(1)
    @Max(5)
    public Integer luggageSize = null;
}
