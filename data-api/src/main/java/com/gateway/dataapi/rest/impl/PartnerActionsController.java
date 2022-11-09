package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.PartnerActionDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.dataapi.service.PartnerActionsService;
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

@RequestMapping(PARTNER_ACTIONS_BASE_PATH)
@RestController
@Slf4j
public class PartnerActionsController {
    private static final String ADD_PARTNER_ACTION = "************* Add PartnerAction ************* ";
    private static final String GET_PARTNER_ACTION_BY_ID = "************* Get PartnerAction By Id ************* ";
    private static final String DELETE_PARTNER_ACTION = "************* Delete PartnerAction ************* ";
    private static final String GET_ALL_PARTNER_ACTION_OR_BY_PARTNER_META_ID = "************* Get All PartnerActions Or Get By partnerMetaId ************* ";
    private static final String UPDATE_PARTNER_ACTION = "************* Update PartnerAction ************* ";

    private final PartnerActionsService actionService;

    public PartnerActionsController(PartnerActionsService actionService) {
        super();
        this.actionService = actionService;
    }

    @Operation(summary = "Create specified  Partner Action", description = "Description Create the specified Partner Action", tags = {
            PARTNER_ACTIONS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = PARTNER_ACTIONS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PartnerActionDTO> addAction(@RequestBody PartnerActionDTO action) {
        log.info(ADD_PARTNER_ACTION);
        PartnerActionDTO actions = actionService.addPartnerAction(action);
        return new ResponseEntity<>(actions, HttpStatus.CREATED);
    }

    @Operation(summary = "Get the specified Partner Action", description = "Description Get the specified Partner Action", tags = {
            PARTNER_ACTIONS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = PARTNER_ACTION_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerActionDTO> getPartnerActionFromId(@PathVariable("id") UUID id) {
        log.info(GET_PARTNER_ACTION_BY_ID);
        PartnerActionDTO partnerAction = actionService.getPartnerActionFromId(id);
        return ResponseEntity.ok(partnerAction);
    }

    @Operation(summary = "Delete specified Partner Action", description = "Description delete the specified Partner Action", tags = {
            PARTNER_ACTIONS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = PARTNER_ACTION_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteAction(@PathVariable(name = "id") UUID id) {
        log.info(DELETE_PARTNER_ACTION);
        actionService.deletePartnerAction(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all Partner Action or Get By PartnerMetaId", description = "Returns the list of Partner Actions", tags = {
            PARTNER_ACTIONS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = PARTNER_ACTIONS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PartnerActionDTO>> getAllActions(
            @RequestParam(name = "partnerMetaId", required = false) UUID partnerMetaId) {
        log.info(GET_ALL_PARTNER_ACTION_OR_BY_PARTNER_META_ID);
        if (partnerMetaId != null) {
            List<PartnerActionDTO> actions = actionService.getPartnerActionFromIdPartnerMetaId(partnerMetaId);
            return new ResponseEntity<>(actions, HttpStatus.OK);
        }
        List<PartnerActionDTO> actions = actionService.getAllPartnerActions();
        return new ResponseEntity<>(actions, HttpStatus.OK);

    }

    @Operation(summary = "Update specified Partner Action", description = "Description update the specified Partner Action", tags = {
            PARTNER_ACTIONS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response OK", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = PARTNER_ACTION_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePartnerAction(@PathVariable("id") UUID id, @RequestBody PartnerActionDTO body) {
        log.info(UPDATE_PARTNER_ACTION);
        actionService.updatePartnerAction(id, body);
        return ResponseEntity.noContent().build();
    }
}
