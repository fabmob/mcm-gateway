package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.MspStandardDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.dataapi.service.MspStandardService;
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

import static com.gateway.dataapi.util.constant.DataApiPathDict.MSP_STANDARDS_TAG;
import static com.gateway.dataapi.util.constant.DataApiPathDict.MSP_STANDARD_BASE_PATH;

@Slf4j
@RequestMapping(MSP_STANDARD_BASE_PATH)
@RestController
public class MspStandardController {
    private static final String ADD_MSP_STANDARD = "************* Add MspStandard ************* ";
    private static final String GET_MSP_STANDARD_BY_ID = "************* Get MspStandard By Id ************* ";
    private static final String DELETE_MSP_STANDARD = "************* Delete MspStandard ************* ";
    private static final String GET_ALL_MSP_STANDARD_OR_BY_CRITERIA = "************* Get All MspStandards Or Get By Specific Criteria(MspMetaId,MspActionsId,VersionStandard,VersionDatamapping) ************* ";
    private static final String UPDATE_MSP_STANDARD = "************* Update MspStandard ************* ";

    MspStandardService mspStandardService;

    public MspStandardController(MspStandardService mspStandardService) {
        super();
        this.mspStandardService = mspStandardService;
    }

    @Operation(summary = "Create specified MspStandard", description = "Description Create the specified MspCall", tags = {
            MSP_STANDARDS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MspStandardDTO> createMspStandard(@RequestBody MspStandardDTO standard) {
        log.info(ADD_MSP_STANDARD);
        MspStandardDTO addStandard = mspStandardService.addMspStandard(standard);
        return new ResponseEntity<>(addStandard, HttpStatus.CREATED);
    }

    @Operation(summary = "Get the specified mspStandard", description = "Description Get the specified mspStandard", tags = {
            MSP_STANDARDS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MspStandardDTO> geMspStandardFromId(@PathVariable("id") UUID id) {
        log.info(GET_MSP_STANDARD_BY_ID);
        MspStandardDTO mspStandard = mspStandardService.getMspStandardFromId(id);
        return ResponseEntity.ok(mspStandard);

    }

    @Operation(summary = "Update MspStandard", description = "Description the update MspStandard", tags = {MSP_STANDARDS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response OK", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateMspStandard(@PathVariable("id") UUID id, @RequestBody MspStandardDTO body) {
        log.info(UPDATE_MSP_STANDARD);
        mspStandardService.updateMspStandard(id, body);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get All MspStandards Or Get By Specific Criteria(MspMetaId,MspActionsId,VersionStandard,VersionDatamapping,Active) ", description = "Description list of MspStandard", tags = {
            MSP_STANDARDS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MspStandardDTO>> getAllMspStandards(@RequestParam(name = "mspId", required = false) UUID mspId,
                                                                   @RequestParam(name = "mspActionsId", required = false) UUID mspActionsId,
                                                                   @RequestParam(name = "mspActionsName", required = false) String mspActionsName,
                                                                   @RequestParam(name = "versionStandard", required = false) String versionStandard,
                                                                   @RequestParam(name = "versionDatamapping", required = false) String versionDatamapping,
                                                                   @RequestParam(name = "isActive", required = false) Boolean isActive) {
        log.info(GET_ALL_MSP_STANDARD_OR_BY_CRITERIA);
        if (mspId == null && mspActionsId == null && mspActionsName == null && versionStandard == null && versionDatamapping == null && isActive == null) {
            List<MspStandardDTO> mspStandards = mspStandardService.getAllMspStandards();
            return new ResponseEntity<>(mspStandards, HttpStatus.OK);
        }
        List<MspStandardDTO> mspStandards = mspStandardService.getByCriteria(mspId, mspActionsId, mspActionsName, versionStandard, versionDatamapping, isActive);
        return new ResponseEntity<>(mspStandards, HttpStatus.OK);
    }

    @Operation(summary = "Delete specified MspStandard", description = "Description delete the specified MspStandard", tags = {
            MSP_STANDARDS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Response OK", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = "Internal sever error", content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = "Bad Gateway", content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteMspStandard(@PathVariable(name = "id") UUID id) {
        log.info(DELETE_MSP_STANDARD);
        mspStandardService.deleteMspStandard(id);
        return ResponseEntity.noContent().build();
    }
}
