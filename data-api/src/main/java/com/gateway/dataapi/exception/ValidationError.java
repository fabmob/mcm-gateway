package com.gateway.dataapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Schema(name="ValidationError", description = "Structure d'erreur si erreur de validation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationError {

    @Schema(name = "status", description = "code statut", example = "400")
    private int status;

    @Schema(name = "timestamp", description = "heure d'appel")
    private Date timestamp;

    @Schema(name = "errors", description = "liste des erreurs de validation")
    private List<String> errors;

}
