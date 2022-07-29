package com.gateway.dataapi.rest.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.gateway.commonapi.dto.data.DataMapperDTO;
import com.gateway.commonapi.dto.exceptions.BadGateway;
import com.gateway.commonapi.dto.exceptions.BadRequest;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.exceptions.NotFound;
import com.gateway.commonapi.dto.exceptions.Unauthorized;
import com.gateway.dataapi.service.DataMapperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import static com.gateway.commonapi.utils.ControllerMessageDict.*;
import static com.gateway.dataapi.util.constant.DataApiPathDict.*;

@RequestMapping(DATA_MAPPER_BASE_PATH)
@RestController
@Slf4j
public class DataMapperController {

    private static final String DELETE_DATAMAPPER = "*************Delete DataMapper *************";
    private static final String ADD_NEW_DATAMAPPER = "************* ADD NEW DataMapper *************";
    private static final String GET_DATAMAPPER_BY_ID = "************* Get DataMapper By Id *************";
    private static final String GET_ALL_DATAMAPPER_OR_BY_MSPACTION_ID = "************* Get All DataMapper Or Get By MspActionsId *************";
    private static final String UPDATE_DATAMAPPER = "************* Update DataMapper *************";

    @Autowired
    private final DataMapperService dataMapperService;


    public DataMapperController(DataMapperService datamapperService) {
        super();
        this.dataMapperService = datamapperService;
    }

    @Operation(summary = "Create specified DataMapper", description = "Description Create the specified DataMapper", tags = {
            DATA_MAPPER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = DATA_MAPPERS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DataMapperDTO> addDataMapper(@RequestBody DataMapperDTO mapperDTO) {
        log.info(ADD_NEW_DATAMAPPER);
        DataMapperDTO mapper = dataMapperService.addDataMapper(mapperDTO);
        return new ResponseEntity<>(mapper, HttpStatus.CREATED);
    }

    @Operation(summary = "Get the specified Data Mapper", description = "Description Get the specified Data Mapper", tags = {
            DATA_MAPPER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = DATA_MAPPER_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataMapperDTO> getDataMapperFromId(@PathVariable("id") UUID id) {
        log.info(GET_DATAMAPPER_BY_ID);
        DataMapperDTO dataMapper = dataMapperService.getDataMapperFromId(id);
        return ResponseEntity.ok(dataMapper);

    }

    @Operation(summary = "Delete specified DataMapper", description = "Description delete the specified DataMapper", tags = {
            DATA_MAPPER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response Ok", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = DATA_MAPPER_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteDataMapper(@PathVariable(name = "id") UUID id) {
        log.info(DELETE_DATAMAPPER);
        dataMapperService.deleteDataMapper(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get All DataMapper or Get By MspActionsId", description = "Description the liste of DataMappers", tags = {
            DATA_MAPPER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response Ok"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = DATA_MAPPERS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DataMapperDTO>> getAllDataMapper(
            @RequestParam(name = "mspActionId", required = false) UUID mspActionId) {
        log.info(GET_ALL_DATAMAPPER_OR_BY_MSPACTION_ID);
        if (mspActionId != null) {
            List<DataMapperDTO> mappers = dataMapperService.getByMspActionId(mspActionId);
            return new ResponseEntity<>(mappers, HttpStatus.OK);
        }
        List<DataMapperDTO> mappers = dataMapperService.getAllDataMappers();
        return new ResponseEntity<>(mappers, HttpStatus.OK);


    }

    @Operation(summary = "Update specified DataMapper", description = "Description Update the specified DataMapper", tags = {
            DATA_MAPPER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response Ok", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = DATA_MAPPER_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateDataMapper(@PathVariable("id") UUID id, @RequestBody DataMapperDTO body) {
        log.info(UPDATE_DATAMAPPER);
        dataMapperService.updateDataMapper(id, body);
        return ResponseEntity.noContent().build();
    }

}