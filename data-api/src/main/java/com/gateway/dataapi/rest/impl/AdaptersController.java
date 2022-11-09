package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.AdaptersDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.dataapi.service.AdaptersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.gateway.commonapi.constants.ControllerMessageDict.*;
import static com.gateway.commonapi.constants.DataApiPathDict.*;

@RequestMapping(ADAPTERS_BASE_PATH)
@RestController
@Slf4j
public class AdaptersController {
    private static final String ADD_ADAPTER = "************* Add Adapter ************* ";
    private static final String GET_ADAPTER_BY_ID = "************* Get Adapter By Id ************* ";
    private static final String DELETE_ADAPTER = "************* Delete Adapter ************* ";
    private static final String GET_ALL_ADAPTERS = "************* Get All Adapters ************* ";

    private final AdaptersService adaptersService;

    public AdaptersController(AdaptersService adaptersService) {
        this.adaptersService = adaptersService;
    }

    @Operation(summary = "Create specified Adapter", description = "Description Create the specified Adapter", tags = {
            ADAPTERS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = ADAPTERS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AdaptersDTO> addAdapter(@RequestBody AdaptersDTO adapter) {
        log.info(ADD_ADAPTER);
        AdaptersDTO adapters = adaptersService.addAdapters(adapter);
        return new ResponseEntity<>(adapters, HttpStatus.CREATED);
    }

    @Operation(summary = "Get the specified Adapter", description = "Description Get the specified Adapter", tags = {
            ADAPTERS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response Ok"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = ADAPTER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdaptersDTO> geAdapterFromId(@PathVariable("id") UUID id) {
        log.info(GET_ADAPTER_BY_ID);
        AdaptersDTO adapter = adaptersService.getAdaptersFromId(id);
        return ResponseEntity.ok(adapter);
    }

    @Operation(summary = "Remove specified Adapter", description = "Description delete the specified Adapter", tags = {
            ADAPTERS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response Ok", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = ADAPTER_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteAction(@PathVariable(name = "id") UUID id) {
        log.info(DELETE_ADAPTER);
        adaptersService.deleteAdapters(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Get all Adapters", description = "Returns the list of Adapters", tags = {
            ADAPTERS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = ADAPTERS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdaptersDTO>> getAllAdapters() {
        log.info(GET_ALL_ADAPTERS);
        List<AdaptersDTO> adapters = adaptersService.getAllAdapters();
        return new ResponseEntity<>(adapters, HttpStatus.OK);

    }

}
