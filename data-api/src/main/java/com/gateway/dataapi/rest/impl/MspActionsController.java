package com.gateway.dataapi.rest.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.commonapi.dto.data.MspActionDTO;
import com.gateway.commonapi.dto.exceptions.BadGateway;
import com.gateway.commonapi.dto.exceptions.BadRequest;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.NotFound;
import com.gateway.commonapi.dto.exceptions.Unauthorized;
import com.gateway.dataapi.service.MspActionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import static com.gateway.commonapi.utils.ControllerMessageDict.*;
import static com.gateway.dataapi.util.constant.DataApiPathDict.*;

@RequestMapping(MSP_ACTIONS_BASE_PATH)
@RestController
@Slf4j
public class MspActionsController {
    private static final String ADD_MSP_ACTION = "************* Add MspAction ************* ";
    private static final String GET_MSP_ACTION_BY_ID = "************* Get MspAction By Id ************* ";
    private static final String DELETE_MSP_ACTION = "************* Delete MspAction ************* ";
    private static final String GET_ALL_MSP_ACTION_OR_BY_MSP_META_ID = "************* Get All MspActions Or Get By MspMetaId ************* ";
    private static final String UPDATE_MSP_ACTION = "************* Update MspAction ************* ";

    private final MspActionsService actionService;

    public MspActionsController(MspActionsService actionService) {
        super();
        this.actionService = actionService;
    }

    @Operation(summary = "Create specified Msp Action", description = "Description Create the specified MspAction", tags = {
            MSP_ACTIONS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = MSP_ACTIONS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MspActionDTO> addAction(@RequestBody MspActionDTO action) {
        log.info(ADD_MSP_ACTION);
        MspActionDTO actions = actionService.addMspAction(action);
        return new ResponseEntity<>(actions, HttpStatus.CREATED);
    }

    @Operation(summary = "Get the specified Msp Action", description = "Description Get the specified Msp Action", tags = {
            MSP_ACTIONS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = MSP_ACTION_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MspActionDTO> geMspActionFromId(@PathVariable("id") UUID id) {
        log.info(GET_MSP_ACTION_BY_ID);
        MspActionDTO mspAction = actionService.getMspActionFromId(id);
        return ResponseEntity.ok(mspAction);
    }

    @Operation(summary = "Delete specified MspAction", description = "Description delete the specified MspAction", tags = {
            MSP_ACTIONS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = MSP_ACTION_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteAction(@PathVariable(name = "id") UUID id) {
        log.info(DELETE_MSP_ACTION);
        actionService.deleteMspAction(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all MspAction or Get By MspMetaId", description = "Description the liste of MspAction", tags = {
            MSP_ACTIONS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = MSP_ACTIONS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MspActionDTO>> getAllActions(
            @RequestParam(name = "mspMetaId", required = false) UUID mspMetaId) {
        log.info(GET_ALL_MSP_ACTION_OR_BY_MSP_META_ID);
        if (mspMetaId != null) {
            List<MspActionDTO> actions = actionService.getMspActionFromIdMspMetaId(mspMetaId);
            return new ResponseEntity<>(actions, HttpStatus.OK);
        }
        List<MspActionDTO> actions = actionService.getAllMspActions();
        return new ResponseEntity<>(actions, HttpStatus.OK);

    }

    @Operation(summary = "Update specified MspAction", description = "Description update the specified MspAction", tags = {
            MSP_ACTIONS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response OK", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = MSP_ACTION_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMspAction(@PathVariable("id") UUID id, @RequestBody MspActionDTO body) {
        log.info(UPDATE_MSP_ACTION);
        actionService.updateMspAction(id, body);
        return ResponseEntity.noContent().build();
    }
}
