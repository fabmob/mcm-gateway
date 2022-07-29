package com.gateway.adapter.rest;

import com.gateway.adapter.utils.constant.AdapterPathDict;
import com.gateway.adapter.service.DefaultAdapterService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.gateway.adapter.utils.constant.AdapterMessageDict.*;
import static com.gateway.adapter.utils.constant.AdapterPathDict.*;


@Slf4j
@Validated
@RestController
@RequestMapping(GLOBAL_PATH)
public class DefaultAdapterController {

    @Autowired
    private DefaultAdapterService defaultAdapterService;

    @Operation(summary = "Default GET request adapter ", description = "Convert a maas request regarding datamapping into the msp format", tags = {
            "adapter"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = AdapterPathDict.ADAPTER_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> adaptGet(@RequestParam(name = ACTION_ID_PARAM) @NotNull UUID mspActionId, @RequestParam(name = MSP_ID_PARAM) @NotNull UUID mspId, @RequestParam(required = false) Map<String, String> params) throws IOException, InterruptedException {
        log.info("Call of service default GET adapter");

        log.debug(String.format("mspActionId param found %s", params.get(ACTION_ID_PARAM)));
        if (log.isDebugEnabled()) {
            params.forEach((key, value) -> log.debug(String.format("param [%s]=%s", key, value)));
        }
        return new ResponseEntity<>(defaultAdapterService.adaptOperation(params, mspActionId, mspId, null), HttpStatus.OK);
    }



    @Operation(summary = "Default POST request adapter ", description = "Convert a maas request regarding datamapping into the msp format", tags = {
            "adapter"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = AdapterPathDict.ADAPTER_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> adaptPost(@RequestParam(name = ACTION_ID_PARAM) @NotNull UUID mspActionId, @RequestParam(name = MSP_ID_PARAM) @NotNull UUID mspId, @RequestParam(required = false) Map<String, String> params, @RequestBody Optional<Map<String, Object>> body) throws IOException, InterruptedException {
        log.info("Call of service default POST adapter");

        log.debug(String.format("mspActionId param found %s", params.get(ACTION_ID_PARAM)));
        if (log.isDebugEnabled()) {
            params.forEach((key, value) -> log.debug(String.format("param [%s]=%s", key, value)));
        }

        if(body.isPresent()){
            return new ResponseEntity<>(defaultAdapterService.adaptOperation(params, mspActionId, mspId, body.get()), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(defaultAdapterService.adaptOperation(params, mspActionId, mspId,null), HttpStatus.OK);

        }
    }
}
