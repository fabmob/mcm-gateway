package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.PartnerCallsDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.dataapi.service.PartnerCallsService;
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

@RequestMapping(PARTNER_CALLS_BASE_PATH)
@RestController
@Slf4j
public class PartnerCallsController {
    private static final String ADD_PARTNER_CALL = "************* Add PartnerCall ************* ";
    private static final String GET_PARTNER_CALL_BY_ID = "************* Get PartnerCall By Id ************* ";
    private static final String DELETE_PARTNER_CALL = "************* Delete PartnerCall ************* ";
    private static final String GET_ALL_PARTNER_CALL_OR_BY_PARTNER_ACTION_ID = "************* Get All PartnerCalls Or By partnerActionId ************* ";
    private static final String UPDATE_PARTNER_CALL = "************* Update PartnerCall ************* ";

    PartnerCallsService callService;

    public PartnerCallsController(PartnerCallsService callService) {
        super();
        this.callService = callService;
    }

    @Operation(summary = "Create specified Partner Call", description = "Description Create the specified Partner Calls", tags = {
            PARTNER_CALLS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = PARTNER_CALLS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PartnerCallsDTO> addCall(@RequestBody PartnerCallsDTO call) {
        log.info(ADD_PARTNER_CALL);
        PartnerCallsDTO addCall = callService.addPartnerCalls(call);
        return new ResponseEntity<>(addCall, HttpStatus.CREATED);
    }

    @Operation(summary = "Get the specified Partner Call", description = "Description Get the specified Partner Calls", tags = {
            PARTNER_CALLS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = PARTNER_CALL_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerCallsDTO> getPartnerCallFromId(@PathVariable("id") UUID id) {
        log.info(GET_PARTNER_CALL_BY_ID);
        PartnerCallsDTO partnerCall = callService.getPartnerCallsFromId(id);
        return ResponseEntity.ok(partnerCall);

    }

    @Operation(summary = "Remove specified Partner Call", description = "Description Delete the specified Partner Call", tags = {
            PARTNER_CALLS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response Ok", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = PARTNER_CALL_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteCall(@PathVariable(name = "id") UUID id) {
        log.info(DELETE_PARTNER_CALL);
        callService.deletePartnerCalls(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all Partner Calls", description = "Returns the list of Partner Calls", tags = {PARTNER_CALLS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = PARTNER_CALLS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PartnerCallsDTO>> getAllCalls(
            @RequestParam(name = "partnerActionId", required = false) UUID partnerActionId) {
        log.info(GET_ALL_PARTNER_CALL_OR_BY_PARTNER_ACTION_ID);

        if (partnerActionId != null) {
            List<PartnerCallsDTO> calls = callService.getByActionId(partnerActionId);
            return new ResponseEntity<>(calls, HttpStatus.OK);
        }
        List<PartnerCallsDTO> calls = callService.getAllPartnerCalls();
        return new ResponseEntity<>(calls, HttpStatus.OK);

    }

    @Operation(summary = "Update specified Partner Call", description = "Description update the specified Partner Call", tags = {
            PARTNER_CALLS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response OK", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = PARTNER_CALL_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePartnerCalls(@PathVariable("id") UUID id, @RequestBody PartnerCallsDTO body) {
        log.info(UPDATE_PARTNER_CALL);
        callService.updatePartnerCalls(id, body);
        return ResponseEntity.noContent().build();

    }
}
