package com.gateway.adapter.rest;

import com.gateway.adapter.service.CustomAdapterService;
import com.gateway.adapter.utils.constant.AdapterPathDict;
import com.gateway.commonapi.dto.exceptions.*;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static com.gateway.adapter.utils.constant.AdapterMessageDict.*;
import static com.gateway.adapter.utils.constant.AdapterPathDict.*;


@Slf4j
@Validated
@RestController
@RequestMapping(GLOBAL_PATH)
public class CustomAdapterController {


    @Autowired
    private CustomAdapterService customAdapterService;

    @Operation(summary = "Custom Post request adapter ", description = "Convert a maas request regarding datamapping into the partner format", tags = {
            ADAPTER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = REPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = AdapterPathDict.ADAPTER_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> adaptGet(@RequestParam(name = ACTION_ID_PARAM) @NotNull UUID partnerActionId, @RequestParam(name = PARTNER_ID_PARAM) @NotNull UUID partnerId, @RequestParam(required = false) Map<String, String> params) throws IOException, InterruptedException, IntrospectionException {
        log.info("Call of service custom Post adapter");

        log.debug(String.format(PARTNER_ACTION_ID_PARAM, params.get(ACTION_ID_PARAM)));
        if (log.isDebugEnabled()) {
            params.forEach((key, value) -> log.debug(String.format(PARAM, key, value)));
        }
        return new ResponseEntity<>(customAdapterService.adaptOperation(params, partnerActionId, partnerId, null), HttpStatus.OK);
    }


}
