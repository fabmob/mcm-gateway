package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.PartnerStandardDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.dataapi.service.PartnerStandardService;
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
import static com.gateway.commonapi.constants.DataApiPathDict.PARTNER_STANDARDS_TAG;
import static com.gateway.commonapi.constants.DataApiPathDict.PARTNER_STANDARD_BASE_PATH;
import static com.gateway.dataapi.util.constant.DataApiMessageDict.RESPONSE_OK;

@Slf4j
@RequestMapping(PARTNER_STANDARD_BASE_PATH)
@RestController
public class PartnerStandardController {
    private static final String ADD_PARTNER_STANDARD = "************* Add PartnerStandard ************* ";
    private static final String GET_PARTNER_STANDARD_BY_ID = "************* Get PartnerStandard By Id ************* ";
    private static final String DELETE_PARTNER_STANDARD = "************* Delete PartnerStandard ************* ";
    private static final String GET_ALL_PARTNER_STANDARD_OR_BY_CRITERIA = "************* Get All PartnerStandards Or Get By Specific Criteria(PartnerMetaId,PartnerActionsId,VersionStandard,VersionDatamapping) ************* ";
    private static final String UPDATE_PARTNER_STANDARD = "************* Update PartnerStandard ************* ";

    PartnerStandardService partnerStandardService;

    public PartnerStandardController(PartnerStandardService partnerStandardService) {
        super();
        this.partnerStandardService = partnerStandardService;
    }

    @Operation(summary = "Create specified Partner Standard", description = "Description Create the specified Partner Call", tags = {
            PARTNER_STANDARDS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "409", description = CONFLICT, content = @Content(schema = @Schema(implementation = Conflict.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PartnerStandardDTO> createPartnerStandard(@RequestBody PartnerStandardDTO standard) {
        log.info(ADD_PARTNER_STANDARD);
        PartnerStandardDTO addStandard = partnerStandardService.addPartnerStandard(standard);
        return new ResponseEntity<>(addStandard, HttpStatus.CREATED);
    }

    @Operation(summary = "Get the specified Partner Standard", description = "Description Get the specified Partner Standard", tags = {
            PARTNER_STANDARDS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerStandardDTO> getPartnerStandardFromId(@PathVariable("id") UUID id) {
        log.info(GET_PARTNER_STANDARD_BY_ID);
        PartnerStandardDTO partnerStandard = partnerStandardService.getPartnerStandardFromId(id);
        return ResponseEntity.ok(partnerStandard);

    }

    @Operation(summary = "Update Partner Standard", description = "Description the update Partner Standard", tags = {PARTNER_STANDARDS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = RESPONSE_OK, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "409", description = CONFLICT, content = @Content(schema = @Schema(implementation = Conflict.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePartnerStandard(@PathVariable("id") UUID id, @RequestBody PartnerStandardDTO body) {
        log.info(UPDATE_PARTNER_STANDARD);
        partnerStandardService.updatePartnerStandard(id, body);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get All Partner Standards Or Get By Specific Criteria(PartnerMetaId,PartnerActionsId,VersionStandard,VersionDatamapping,Active) ", description = "Description list of Partner Standard", tags = {
            PARTNER_STANDARDS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PartnerStandardDTO>> getAllPartnerStandards(@RequestParam(name = "partnerId", required = false) UUID partnerId,
                                                                       @RequestParam(name = "partnerActionsId", required = false) UUID partnerActionsId,
                                                                       @RequestParam(name = "partnerActionsName", required = false) String partnerActionsName,
                                                                       @RequestParam(name = "versionStandard", required = false) String versionStandard,
                                                                       @RequestParam(name = "versionDatamapping", required = false) String versionDatamapping,
                                                                       @RequestParam(name = "isActive", required = false) Boolean isActive) {
        log.info(GET_ALL_PARTNER_STANDARD_OR_BY_CRITERIA);
        if (partnerId == null && partnerActionsId == null && partnerActionsName == null && versionStandard == null && versionDatamapping == null && isActive == null) {
            List<PartnerStandardDTO> partnerStandards = partnerStandardService.getAllPartnerStandards();
            return new ResponseEntity<>(partnerStandards, HttpStatus.OK);
        }
        List<PartnerStandardDTO> partnerStandards = partnerStandardService.getByCriteria(partnerId, partnerActionsId, partnerActionsName, versionStandard, versionDatamapping, isActive);
        return new ResponseEntity<>(partnerStandards, HttpStatus.OK);
    }

    @Operation(summary = "Delete specified Partner Standard", description = "Description delete the specified Partner Standard", tags = {
            PARTNER_STANDARDS_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = RESPONSE_OK, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deletePartnerStandard(@PathVariable(name = "id") UUID id) {
        log.info(DELETE_PARTNER_STANDARD);
        partnerStandardService.deletePartnerStandard(id);
        return ResponseEntity.noContent().build();
    }
}
