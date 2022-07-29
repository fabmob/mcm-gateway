package com.gateway.routingapi.rest;


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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.gateway.routingapi.util.constant.RoutingDict.PARAMS;
import static com.gateway.routingapi.util.constant.RoutingMessageDict.*;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Validated
@RestController
public class RoutingApiController {

    @Autowired
    private RoutingService adapterRouterService;

    @Operation(summary = "Forward Get operation", description = "", tags = ROUTING_TAG)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = REPONSE_OK),
            @ApiResponse(responseCode = "400", description = REQUETE_MAL_FORMEE_OU_NON_VALIDE, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = REQUETE_NON_AUTORISEE_JETON_OAUTH_2_KO_OU_ABSENT, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = DONNEE_NON_TROUVEE, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = ERREUR_INTERNE, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = LE_SERVEUR_DISTANT_EST_INDISPONIBLE, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = RoutingDict.ROUTE_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> routeOperation(@RequestParam @NotNull UUID mspId, @RequestParam @NotNull String actionName, @RequestBody Optional<Map<String, Object>> body, @RequestParam(required = false) Map<String, String> params) throws IOException, InterruptedException {
        log.info("Call of service routeGetOperation");
        if (log.isDebugEnabled()) {
            params.forEach((key, value) -> log.debug(String.format(PARAMS, key, value)));
        }
        Object response = adapterRouterService.routeOperation(params, mspId, actionName, body);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(null,null, HttpStatus.OK);
    }
}
