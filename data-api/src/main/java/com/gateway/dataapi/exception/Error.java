package com.gateway.dataapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(name="Error", description = "Structure d'erreur si problème")
@Data
@AllArgsConstructor
public class Error {

    @Schema(name = "code", description = "code erreur  ", required = true, example = "3")
    private int code;

    @Schema(name = "message", description = "message d'erreur")
    private String message;
}
