package com.gateway.cachemanager.rest;

import com.gateway.cachemanager.model.PositionsRequest;
import com.gateway.cachemanager.service.CacheManagerService;
import com.gateway.commonapi.cache.CacheStatus;
import com.gateway.commonapi.dto.exceptions.*;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.ActionsEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.gateway.commonapi.constants.CacheManagerDict.*;
import static com.gateway.commonapi.constants.ControllerMessageDict.*;
import static com.gateway.commonapi.constants.GlobalConstants.*;

@Slf4j
@Validated
@RestController
@RequestMapping(CACHE_MANAGER_PATH)
public class CacheManagerController {

    @Autowired
    CacheManagerService cacheManagerService;

    /**
     * Clear cache
     *
     * @param partnerIds List of partners to delete from cache
     * @return no content
     */
    @Operation(summary = "Clear cache", description = "Clear all cache or only cache on specified partners", tags = {CACHE_MANAGER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = RESPONSE_OK, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @DeleteMapping(value = CACHE_MANAGER_CLEAR_PATH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Object> clearCache(@RequestParam(required = false) @Parameter(description = "List of partners to delete from cache") List<UUID> partnerIds) {
        cacheManagerService.clearCache(partnerIds);
        return ResponseEntity.noContent().build();
    }


    /**
     * Return cache status
     *
     * @return CacheStatus singleton
     */
    @Operation(summary = "Get the current cache status", description = "Indicate if cache is active or not", tags = {CACHE_MANAGER_TAG})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = CACHE_MANAGER_STATUS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CacheStatus> getCacheStatus() {
        CacheStatus cacheStatus = cacheManagerService.getCacheStatus();
        return ResponseEntity.ok().body(cacheStatus);
    }


    /***
     * Update cache status (activate or deactivate)
     *
     * @param isEnabled new cache status
     * @return CacheStatus singleton
     */
    @Operation(summary = "Update the cache status", description = "Activate or deactivate cache", tags = {CACHE_MANAGER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = UPDATE_WITH_BODY),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PutMapping(value = CACHE_MANAGER_STATUS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CacheStatus> putCacheStatus(@RequestParam(required = true) boolean isEnabled) {
        CacheStatus cacheStatus = cacheManagerService.putCacheStatus(isEnabled);
        return ResponseEntity.ok().body(cacheStatus);
    }


    /***
     * Refresh partners meta-data of cache
     */
    @Operation(summary = "Refresh partners' cache", description = "Refresh meta-data of all partners in cache", tags = {CACHE_MANAGER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = UPDATE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = CACHE_MANAGER_REFRESH_PARTNERS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> refreshPartners() {
        cacheManagerService.refreshPartners();
        return ResponseEntity.noContent().build();
    }


    /**
     * Refresh actionName data in cache for specified partner
     *
     * @param partnerId  ID of partner whose data have to be refreshed
     * @param actionName Action to refresh
     * @param positions  List of coordinates to scan to retrieve all data
     * @return
     */
    @Operation(summary = "Refresh cache", description = "Refresh specified data in cache", tags = {CACHE_MANAGER_TAG})
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = UPDATE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(value = CACHE_MANAGER_REFRESH_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> refresh(@RequestParam UUID partnerId, @RequestParam ActionsEnum actionName, @RequestBody(required = false) PositionsRequest positions) {

        if (positions != null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<PositionsRequest>> violations = validator.validate(positions);
            if (!violations.isEmpty()) {
                throw new BadRequestException(CommonUtils.placeholderFormat(INVALID_BODY, FIRST_PLACEHOLDER, POSITIONS_OBJECT));
            }

            cacheManagerService.refresh(partnerId, actionName, positions.getPositions());
        } else {
            cacheManagerService.refresh(partnerId, actionName, null);
        }

        return ResponseEntity.noContent().build();
    }

}
