package com.gateway.routingapi.rest;


import com.gateway.commonapi.constants.ControllerMessageDict;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
import com.gateway.routingapi.service.RoutingService;
import com.gateway.routingapi.util.constant.RoutingDict;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.gateway.routingapi.util.constant.RoutingDict.PARAMS;
import static com.gateway.routingapi.util.constant.RoutingMessageDict.*;

@Slf4j
@Validated
@RestController
public class RoutingApiController {

    @Autowired
    private RoutingService adapterRouterService;

    @Operation(summary = "Create a route for a specified partner and a specified action.", description = "Route used to create a route for a specified partner and a specified action and, if specified, the standard used for output error and a valid code.", tags = ROUTING_TAG)
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
    public ResponseEntity<Object> routeOperation(@RequestParam @NotNull UUID partnerId, @RequestParam @NotNull String actionName, @RequestBody Optional<Map<String, Object>> body, @RequestParam(required = false) Map<String, String> params,
                                                 @RequestHeader(name = GlobalConstants.OUTPUT_STANDARD, required = false, defaultValue = "") @Parameter(description = ControllerMessageDict.STANDARD_HEADER_DESCRIPTION,
                                                         example = "tomp-1.3.0", array = @ArraySchema(schema = @Schema(implementation = StandardEnum.class))) String outputStandard, @RequestHeader(name = GlobalConstants.VALID_CODES, required = false, defaultValue = "") String validCodes) {
        log.info("Call of service routeGetOperation");
        CallUtils.saveOutputStandardInCallThread(outputStandard);
        CallUtils.saveValidCodesInCallThread(validCodes);
        if (log.isDebugEnabled()) {
            params.forEach((key, value) -> log.debug(String.format(PARAMS, key, value)));
        }
        Object response = adapterRouterService.routeOperation(params, partnerId, actionName, body);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, null, HttpStatus.OK);
    }
}
