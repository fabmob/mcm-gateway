package com.gateway.dataapi.rest.impl;

import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.dataapi.service.CacheParamService;
import com.gateway.dataapi.util.constant.DataApiMessageDict;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.gateway.commonapi.constants.ControllerMessageDict.*;
import static com.gateway.commonapi.constants.DataApiPathDict.*;
import static com.gateway.dataapi.util.constant.DataApiMessageDict.CREATED;

@RequestMapping(CACHE_PARAM_BASE_PATH)
@RestController
@Slf4j
@AllArgsConstructor
public class CacheParamController {

    private static final String DELETE_CACHEPARAM = "*************Delete CacheParam *************";
    private static final String ADD_NEW_CACHEPARAM = "************* ADD NEW CacheParam *************";
    private static final String GET_CACHEPARAM_BY_ID = "************* Get CacheParam By Id *************";
    private static final String GET_ALL_CACHEPARAM = "************* Get All CacheParam *************";
    private static final String UPDATE_CACHEPARAM = "************* Update CacheParam *************";


    @Autowired
    private final CacheParamService cacheParamService;


    @Operation(summary = "Create cache parameters for a specified partner.", description = "Route used to create cache parameters for a specified partner. The parameters described the action type, soft and hard TTL, and refresh cache delay.", tags = {CACHE_PARAMTAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = CREATED),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CacheParamDTO> addCacheParam(@RequestBody CacheParamDTO cacheParamRequest) {
        log.info(ADD_NEW_CACHEPARAM);
        CacheParamDTO cacheParamResponse = cacheParamService.addCacheParam(cacheParamRequest);
        return new ResponseEntity<>(cacheParamResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve a specified cache parameters.", description = "Route used to retrieve a specified cache parameters.", tags = {
            CACHE_PARAMTAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = DataApiMessageDict.RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = CACHE_PARAM_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CacheParamDTO> getCacheParamFromId(@PathVariable("cacheParamId") UUID cacheParamId) {
        log.info(GET_CACHEPARAM_BY_ID);
        CacheParamDTO cacheParam = cacheParamService.getCacheParamFromID(cacheParamId);
        return ResponseEntity.ok(cacheParam);
    }


    @Operation(summary = "Update a specified cache parameters.", description = "Route used to update a specified cache parameters.", tags = {CACHE_PARAMTAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = DataApiMessageDict.RESPONSE_OK, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = CACHE_PARAM_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateCacheParam(@PathVariable("cacheParamId") UUID cacheParamId, @RequestBody CacheParamDTO body) {
        log.info(UPDATE_CACHEPARAM);
        cacheParamService.updateCacheParam(cacheParamId, body);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Delete a specified cache parameters.", description = "Route used to delete a specified cache parameters.", tags = {CACHE_PARAMTAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = DataApiMessageDict.RESPONSE_OK, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = CACHE_PARAM_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> deleteCacheParam(@PathVariable(name = "cacheParamId") UUID cacheParamId) {
        log.info(DELETE_CACHEPARAM);
        cacheParamService.deleteCacheParamById(cacheParamId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retrieve cache parameters for all or a specified partner and action type.", description = "Route used to retrieve cache parameters for all or a specified partner and action type.", tags = {CACHE_PARAMTAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = DataApiMessageDict.RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CacheParamDTO>> getAllCacheParam(@RequestParam(name = "partnerId", required = false) UUID partnerId,
                                                                @RequestParam(name = "actionType", required = false) String actionType) {
        log.info(GET_ALL_CACHEPARAM);
        List<CacheParamDTO> mappers;
        if (partnerId == null && actionType == null) {
            mappers = cacheParamService.getAllCacheParams();
        } else {
            mappers = cacheParamService.getAllCacheParamByCriteria(partnerId, actionType);
        }
        return new ResponseEntity<>(mappers, HttpStatus.OK);
    }


}
