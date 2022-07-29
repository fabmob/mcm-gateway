package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.MspCallsDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.dataapi.service.MspCallsService;
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

import static com.gateway.commonapi.utils.ControllerMessageDict.*;
import static com.gateway.dataapi.util.constant.DataApiPathDict.*;

@RequestMapping(MSP_CALLS_BASE_PATH)
@RestController
@Slf4j
public class MspCallsController {
    private static final String ADD_MSP_CALL = "************* Add MspCall ************* ";
    private static final String GET_MSP_CALL_BY_ID = "************* Get MspCall By Id ************* ";
    private static final String DELETE_MSP_CALL = "************* Delete MspCall ************* ";
    private static final String GET_ALL_MSP_CALL_OR_BY_MSP_ACTION_ID = "************* Get All MspCalls Or By MspActionId ************* ";
    private static final String UPDATE_MSP_CALL = "************* Update MspCall ************* ";

    MspCallsService callService;

    public MspCallsController(MspCallsService callService) {
        super();
        this.callService = callService;
    }

    @Operation(summary = "Create specified MspCall", description = "Description Create the specified MspCalls", tags = {
            MSP_CALLS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = MSP_CALLS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MspCallsDTO> addCall(@RequestBody MspCallsDTO call) {
        log.info(ADD_MSP_CALL);
        MspCallsDTO addCall = callService.addMspCalls(call);
        return new ResponseEntity<>(addCall, HttpStatus.CREATED);
    }

    @Operation(summary = "Get the specified Msp Call", description = "Description Get the specified MspCalls", tags = {
            MSP_CALLS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = MSP_CALL_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MspCallsDTO> geMspCallFromId(@PathVariable("id") UUID id) {
        log.info(GET_MSP_CALL_BY_ID);
        MspCallsDTO mspCall = callService.getMspCallsFromId(id);
        return ResponseEntity.ok(mspCall);

    }

    @Operation(summary = "Remove specified MspCall", description = "Description Delete the specified MspCall", tags = {
            MSP_CALLS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response Ok", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = MSP_CALL_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteCall(@PathVariable(name = "id") UUID id) {
        log.info(DELETE_MSP_CALL);
        callService.deleteMspCalls(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all MspCalls", description = "Description the liste of MspCall", tags = {MSP_CALLS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = MSP_CALLS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MspCallsDTO>> getAllCalls(
            @RequestParam(name = "mspActionId", required = false) UUID mspActionId) {
        log.info(GET_ALL_MSP_CALL_OR_BY_MSP_ACTION_ID);

        if (mspActionId != null) {
            List<MspCallsDTO> calls = callService.getByActionId(mspActionId);
            return new ResponseEntity<>(calls, HttpStatus.OK);
        }
        List<MspCallsDTO> calls = callService.getAllMspCalls();
        return new ResponseEntity<>(calls, HttpStatus.OK);

    }

    @Operation(summary = "Update specified MspCall", description = "Description update the specified MspCall", tags = {
            MSP_CALLS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response OK", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = MSP_CALL_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMspCalls(@PathVariable("id") UUID id, @RequestBody MspCallsDTO body) {
        log.info(UPDATE_MSP_CALL);
        callService.updateMspCalls(id, body);
        return ResponseEntity.noContent().build();

    }
}
