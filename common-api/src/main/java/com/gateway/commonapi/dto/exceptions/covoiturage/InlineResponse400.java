package com.gateway.commonapi.dto.exceptions.covoiturage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Schema(name = "InlineResponse400",
        description = "Error response according to standard carpool")

/**
 * InlineResponse400
 */
@Validated
@Data
@AllArgsConstructor
public class InlineResponse400 {

    @Schema(name = "error",
            type = "string",
            example = "string")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("error")
    protected String error;
}
