package com.gateway.routingapi.rest;


import com.gateway.commonapi.dto.adapter.GenericResponse;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.routingapi.service.RoutingService;
import com.gateway.routingapi.util.constant.RoutingDict;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1")
public class RoutingApiController {

    public static final String ROUTING_TAG = "Routing";
    @Autowired
    ServletContext context;
    @Autowired
    private RoutingService adapterRouterService;

    @Operation(summary = "Forward Get operation", description = "", tags = ROUTING_TAG)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Réponse OK"),
            @ApiResponse(responseCode = "400", description = "Requête mal formée ou non valide", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Requête non autorisée (jeton oauth2 KO ou absent)", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Donnée non trouvée", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Erreur interne", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Le serveur distant est indisponible", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = RoutingDict.ADAPTER_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> routeGetOperation(@RequestParam @NotNull UUID mspId, @RequestParam @NotNull String actionName, @RequestParam(required = false) Map<String, String> params) throws IOException, InterruptedException {
        log.info("Call of service routeGetOperation");

        log.debug(String.format("mspId param found %s", params.get("mspId")));
        if (log.isDebugEnabled()) {
            params.forEach((key, value) -> log.debug(String.format("param [%s]=%s", key, value)));
        }
        return new ResponseEntity<>(adapterRouterService.routeGetOperation(params, mspId, actionName), HttpStatus.OK);
    }
}
