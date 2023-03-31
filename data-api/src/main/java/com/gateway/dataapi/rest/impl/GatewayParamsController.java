package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.GatewayParamsDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.dataapi.service.GatewayParamsService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gateway.commonapi.constants.ControllerMessageDict.*;
import static com.gateway.commonapi.constants.DataApiPathDict.*;

@RequestMapping(GATEWAY_PARAMS_BASE_PATH)
@RestController
@Slf4j
public class GatewayParamsController {

    private static final String DELETE_GATEWAY_PARAMS = "*************Delete GATEWAY_PARAM *************";
    private static final String ADD_NEW_GATEWAY_PARAMS = "************* ADD NEW GATEWAY_PARAM *************";
    private static final String GET_GATEWAY_PARAMS_BY_ID = "************* Get GATEWAY_PARAM By Id *************";
    private static final String GET_ALL_GATEWAY_PARAMS = "************* Get All GATEWAY_PARAMS *************";
    private static final String UPDATE_GATEWAY_PARAMS = "************* Update GATEWAY_PARAM *************";

    @Autowired
    private final GatewayParamsService gatewayParamsService;


    public GatewayParamsController(GatewayParamsService gatewayParamsService) {
        super();
        this.gatewayParamsService = gatewayParamsService;
    }

    @Operation(summary = "Create a specified gateway parameter.", description = "Route used to create a gateway parameter specified by a key and a value.", tags = {
            GATEWAY_PARAMS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = GATEWAY_PARAMS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GatewayParamsDTO> addGatewayParam(@RequestBody GatewayParamsDTO gatewayParamsDTO) {
        log.info(ADD_NEW_GATEWAY_PARAMS);
        GatewayParamsDTO response = gatewayParamsService.addGatewayParamsDTO(gatewayParamsDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve the value of a specified gateway parameter.", description = "Route used to retrieve the value of a gateway parameter specified by its key.", tags = {
            GATEWAY_PARAMS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = GATEWAY_PARAM_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GatewayParamsDTO> getGatewayParamById(@PathVariable("paramKey") String paramKey) {
        log.debug(GET_GATEWAY_PARAMS_BY_ID);
        GatewayParamsDTO gatewayParamsDTO = gatewayParamsService.findGatewayParamsDTOByParamKey(paramKey);
        return ResponseEntity.ok(gatewayParamsDTO);

    }

    @Operation(summary = "Delete a specified gateway parameter.", description = "Route used to delete a gateway parameter specified by its key.", tags = {
            GATEWAY_PARAMS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response Ok", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = GATEWAY_PARAM_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteGatewayParams(@PathVariable(name = "paramKey") String paramKey) {
        log.info(DELETE_GATEWAY_PARAMS);
        gatewayParamsService.deleteGatewayParamsDTO(paramKey);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve the key and value of all gateway parameters.", description = "Route used to retrieve the key and value of all gateway parameters.", tags = {
            GATEWAY_PARAMS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response Ok"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = GATEWAY_PARAMS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GatewayParamsDTO>> getAllGatewayParams() {
        log.info(GET_ALL_GATEWAY_PARAMS);
        List<GatewayParamsDTO> gatewayParams = gatewayParamsService.getAllGatewayParamsDTO();
        return new ResponseEntity<>(gatewayParams, HttpStatus.OK);

    }

    @Operation(summary = "Update the value of a specified gateway parameter.", description = "Route used to update the value of a gateway parameter specified by its key.", tags = {
            GATEWAY_PARAMS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response Ok", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = GATEWAY_PARAM_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateGatewayParams(@PathVariable("paramKey") String paramKey, @RequestBody GatewayParamsDTO body) {
        log.info(UPDATE_GATEWAY_PARAMS);
        gatewayParamsService.updateGatewayParamsDTO(paramKey, body);
        return ResponseEntity.noContent().build();
    }

}
