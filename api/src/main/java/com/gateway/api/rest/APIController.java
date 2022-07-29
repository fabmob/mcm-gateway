package com.gateway.api.rest;


import com.gateway.api.model.*;
import com.gateway.api.service.ivservice.IVService;
import com.gateway.api.service.mspservice.MSPService;
import com.gateway.api.util.enums.ZoneType;
import com.gateway.commonapi.dto.exceptions.*;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.gateway.api.util.constant.GatewayApiPathDict.*;
import static com.gateway.commonapi.utils.ControllerMessageDict.*;

@Slf4j
@Validated
@RestController
@RequestMapping(GET_MSPS_PATH)
public class APIController {

    @Autowired
    private MSPService mspService;
    @Autowired
    private IVService ivService;

    /**
     * Return metadatas for all managed MSP.
     *
     * @return List of MSP Metadata
     */


    @Operation(summary = "Get metadatas for all managed MSP", description = "Endpoint used to get all metadatas for all managed MSP and useful links", tags = "MSPs Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = "",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MSPMeta> getMSPsMeta() throws IOException, InterruptedException {
        log.info("Call of service getMSPsMeta");
        return mspService.getMSPsMeta();

    }



    /**
     * Return metadatas for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @return MSP Metadata object.
     *
     */

    @Operation(summary = "Get metadatas for a given MSP", description = "Endpoint used to get metadatas for a MSP and useful links", tags = "MSPs Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = GET_MSP_BY_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MSPMeta> getMSPMeta(@PathVariable UUID mspId) {
        log.info("Call of service getMSPMeta for MSP {}", mspId);
        return new ResponseEntity<>(mspService.getMSPMeta(mspId), HttpStatus.OK);


    }



    /**
     * Return a global view.
     *
     * @return Global view.
     */

    @Operation(summary = "Get a global view", description = "Endpoint used to retrieve a global view", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(
            value = GET_MSPS_GLOBAL_VIEW_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlobalView> getGlobalView()  {
        log.info("Call of service getGlobalView");
        return new ResponseEntity<>(ivService.getGlobalView(), HttpStatus.OK);
    }



    /**
     * Return a global view for around me.
     * @param mspAroundMeRequest
     * @return Global view.
     */

    @Operation(summary = "Get a global view for around me", description = "Endpoint used to retrieve a global view for around me", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @PostMapping(
            value = GET_MSPS_AROUND_ME_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlobalView> getAroundMe(@RequestBody(required = true) MSPAroundMeRequest mspAroundMeRequest)  {
        log.info("Call of service aroundMe");
        return new ResponseEntity<>(ivService.getAroundMe(mspAroundMeRequest), HttpStatus.OK);
    }



    /**
     * Return a specific area informations for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @param areaType Area type
     * @return object {@link MSPZone}} for a MSP area.
     */
    @Operation(summary = "Get specific area informations for a MSP", description = "Endpoint used to get a specific area informations for a MSP", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(value = GET_MSP_AREAS_TYPE_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MSPZone> getMSPZone(@PathVariable UUID mspId, @PathVariable ZoneType areaType)  {
        log.info("Call of service getMSPZone for MSP {} and zone {}", mspId, areaType);
        return new ResponseEntity<>(mspService.getMSPZone(mspId, areaType), HttpStatus.OK);

    }



    /**
     * Return all stations for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @return List of stations for the MSP.
     */

    @Operation(summary = "Get all stations for a MSP", description = "Endpoint used to retrieve stations for a MSP", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(
            value = GET_MSP_STATIONS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Station>> getMSPStations(@PathVariable UUID mspId) {
        log.info("Call of service getMSPStations for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getStations(mspId), HttpStatus.OK);


    }

    /**
     * Return status for all stations for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @return List of stations for the MSP.
     */
    @Operation(summary = "Get all stations status for a MSP", description = "Endpoint used to retrieve status of stations for a MSP", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(
            value = GET_MSP_STATIONS_STATUS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationStatus>> getMSPStationsStatus(@PathVariable UUID mspId) {
        log.info("Call of service getMSPStationsStatus for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getStationStatus(mspId), HttpStatus.OK);

    }

    /**
     * Return all assets for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @return List of assets for the MSP.
     */
    @Operation(summary = "Get all assets for a MSP", description = "Endpoint used to retrieve assets for a MSP", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(
            value = GET_MSP_ASSETS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Asset>> getMSPAssets(@PathVariable UUID mspId) {
        log.info("Call of service getMSPAssets for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getAssets(mspId), HttpStatus.OK);

    }

    /**
     * Return available assets for a MSP.
     *
     * @param mspId Identifier of the MSP.
     * @return List of available assets for the MSP.
     */
    @Operation(summary = "Get all available assets for a MSP", description = "Endpoint used to retrieve available assets for a MSP", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(
            value = GET_MSP_AVAILABLE_ASSETS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AssetType>> getMSPAvailableAssets(@PathVariable UUID mspId, @RequestParam(required = false) String stationId) {
        log.info("Call of service getMSPAvailableAssets for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getAvailableAssets(mspId, stationId), HttpStatus.OK);

    }
}
