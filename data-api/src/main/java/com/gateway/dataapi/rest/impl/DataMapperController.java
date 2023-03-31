package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.DataMapperDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.dataapi.service.DataMapperService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.gateway.commonapi.constants.ControllerMessageDict.*;
import static com.gateway.commonapi.constants.DataApiPathDict.*;

@RequestMapping(DATA_MAPPER_BASE_PATH)
@RestController
@Slf4j
public class DataMapperController {

    private static final String DELETE_DATAMAPPER = "*************Delete DataMapper *************";
    private static final String ADD_NEW_DATAMAPPER = "************* ADD NEW DataMapper *************";
    private static final String GET_DATAMAPPER_BY_ID = "************* Get DataMapper By Id *************";
    private static final String GET_ALL_DATAMAPPER_OR_BY_PARTNERACTION_ID = "************* Get All DataMapper Or Get By PartnerActionsId *************";
    private static final String UPDATE_DATAMAPPER = "************* Update DataMapper *************";

    @Autowired
    private final DataMapperService dataMapperService;


    public DataMapperController(DataMapperService datamapperService) {
        super();
        this.dataMapperService = datamapperService;
    }

    @Operation(summary = "Create data mappers for partner actions.", description = "Route used to create one or several data mappers for one or several partner actions.", tags = {
            DATA_MAPPER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = DATA_MAPPERS_PATH, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<DataMapperDTO>> addDataMapper(@RequestBody List<DataMapperDTO> mapperListDTO) {
        log.info(ADD_NEW_DATAMAPPER);
        List<DataMapperDTO> mapperList = new ArrayList<>();
        for (DataMapperDTO mapperDTO: mapperListDTO) {
            mapperList.add(dataMapperService.addDataMapper(mapperDTO));
        }
        return new ResponseEntity<>(mapperList, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve a specified data mapper.", description = "Route used to retrieve a specified data mapper.", tags = {
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

    @Operation(summary = "Delete a specified data mapper.", description = "Route used to delete a specified data mapper.", tags = {
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

    @Operation(summary = "Retrieve all data mappers for all or a specified partner action.", description = "Route used to retrieve all data mappers for all partner actions or for a specified partner action.", tags = {
            DATA_MAPPER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response Ok"),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = DATA_MAPPERS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DataMapperDTO>> getAllDataMapper(
            @RequestParam(name = "partnerActionId", required = false) UUID partnerActionId) {
        log.info(GET_ALL_DATAMAPPER_OR_BY_PARTNERACTION_ID);
        if (partnerActionId != null) {
            List<DataMapperDTO> mappers = dataMapperService.getByPartnerActionId(partnerActionId);
            return new ResponseEntity<>(mappers, HttpStatus.OK);
        }
        List<DataMapperDTO> mappers = dataMapperService.getAllDataMappers();
        return new ResponseEntity<>(mappers, HttpStatus.OK);


    }

    @Operation(summary = "Update a specified data mapper.", description = "Route used to update a specified data mapper.", tags = {
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
