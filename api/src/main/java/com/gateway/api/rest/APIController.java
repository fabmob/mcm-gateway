package com.gateway.api.rest;


import com.gateway.api.model.*;
import com.gateway.api.service.ivservice.IVService;
import com.gateway.api.service.mspservice.MSPService;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.exceptions.covoiturage.InlineResponse400;
import com.gateway.commonapi.utils.enums.ZoneType;
import com.gateway.commonapi.dto.exceptions.*;
import io.swagger.annotations.ResponseHeader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Type;
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
    public ResponseEntity<GlobalView> getGlobalView() throws IOException, InterruptedException {
        log.info("Call of service getGlobalView");
        return new ResponseEntity<>(ivService.getGlobalView(), HttpStatus.OK);
    }


    /**
     * Return a global view for around me.
     *
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
    public ResponseEntity<GlobalView> getAroundMe(@RequestBody(required = true) MSPAroundMeRequest mspAroundMeRequest) {
        log.info("Call of service aroundMe");
        return new ResponseEntity<>(ivService.getAroundMe(mspAroundMeRequest), HttpStatus.OK);
    }


    /**
     * Return a specific area informations for a MSP.
     *
     * @param mspId    Identifier of the MSP.
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
    public ResponseEntity<MSPZone> getMSPZone(@PathVariable UUID mspId, @PathVariable ZoneType areaType) {
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
    public ResponseEntity<List<Station>> getMSPStations(@PathVariable UUID mspId, @RequestParam(required = false) Float lon, @RequestParam(required = false) Float lat, @RequestParam(required = false) Float radius) {
        log.info("Call of service getMSPStations for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getStations(mspId, lon, lat, radius), HttpStatus.OK);


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
    public ResponseEntity<List<StationStatus>> getMSPStationsStatus(@PathVariable UUID mspId, @RequestParam(required = false) String stationId) {
        log.info("Call of service getMSPStationsStatus for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getStationStatus(mspId, stationId), HttpStatus.OK);

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
    public ResponseEntity<List<AssetType>> getMSPAvailableAssets(@PathVariable UUID mspId, @RequestParam(required = false) String stationId, @RequestParam(required = false) Float lon, @RequestParam(required = false) Float lat, @RequestParam(required = false) Float radius) {
        log.info("Call of service getMSPAvailableAssets for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getAvailableAssets(mspId, stationId, lon, lat, radius), HttpStatus.OK);

    }

    @Operation(summary = "Get all types of vehicle from a MSP", description = "Endpoint used to retrieve all types of vehicle for a MSP", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(
            value = GET_MSP_VEHICLE_TYPES_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleTypes>> getMSPVehicleTypes(@PathVariable UUID mspId) {
        log.info("Call of service getMSPVehicleTypes for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getVehicleTypes(mspId), HttpStatus.OK);

    }

    @Operation(summary = "Get all pricing plan from a MSP", description = "Endpoint used to retrieve all pricing plan for a MSP", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = BAD_REQUEST, content = @Content(schema = @Schema(implementation = BadRequest.class))),
            @ApiResponse(responseCode = "401", description = UNAUTHORIZED, content = @Content(schema = @Schema(implementation = Unauthorized.class))),
            @ApiResponse(responseCode = "404", description = NOT_FOUND, content = @Content(schema = @Schema(implementation = NotFound.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = GenericError.class))),
            @ApiResponse(responseCode = "502", description = BAD_GATEWAY, content = @Content(schema = @Schema(implementation = BadGateway.class)))})
    @GetMapping(
            value = GET_MSP_PRICING_PLAN_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PriceList>> getMSPPricingPlan(@PathVariable UUID mspId, @RequestParam(required = false) String stationId) {
        log.info("Call of service getMSPPricingPlan for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getPricingPlan(mspId, stationId), HttpStatus.OK);

    }

    /**  DriverJourneys End-point **/
    @Operation(summary = "Search for matching punctual planned outward driver journeys", description = "Route used to retrieve a collection of punctual planned outward driver journeys matching the provided criteria.", tags = {"Carpooling","Traveler Information"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_IV_RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = InlineResponse400.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED,content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS,content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = "How long to wait before making a new request (in seconds)." , schema =  @Schema(type = "integer") ),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR,content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping(
            value = GET_MSP_DRIVER_JOURNEY_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DriverJourney>> getDriverJourneys(@PathVariable UUID mspId, @RequestParam(required = true) Float departureLat,
                                                                 @RequestParam(required = true) Float departureLng, @RequestParam(required = true) Float arrivalLat, @RequestParam(required = true) Float arrivalLng, @RequestParam(required = true) Integer departureDate, @RequestParam(required = false) Integer timeDelta, @RequestParam(required = false) Float departureRadius,
                                                                 @RequestParam(required = false) Float arrivalRadius,
                                                                 @RequestParam(required = false) Integer count) {
        log.info("Call of service getDriverJourneys for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getDriverJourneys(mspId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count), HttpStatus.OK);
    }


    /**
     * Passengerjourneys End-point
     **/
    @Operation(summary = "Search for matching punctual planned outward passenger journeys", description = "Route used to retrieve a collection of punctual planned outward passenger journeys matching the provided criteria.", tags = {"Carpooling","Traveler Information"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_IV_RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = InlineResponse400.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED,content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS,content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = "How long to wait before making a new request (in seconds)." , schema =  @Schema(type = "integer") ),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR,content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping(
            value = GET_MSP_PASSENGER_JOURNEY_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PassengerJourney>> getPassengerJourneys(@PathVariable UUID mspId, @RequestParam(required = true) Float departureLat,
                                                                       @RequestParam(required = true) Float departureLng, @RequestParam(required = true) Float arrivalLat, @RequestParam(required = true) Float arrivalLng,
                                                                       @RequestParam(required = true) Integer departureDate, @RequestParam(required = false) Integer timeDelta, @RequestParam(required = false) Float departureRadius,
                                                                       @RequestParam(required = false) Float arrivalRadius,
                                                                       @RequestParam(required = false) Integer count) {
        log.info("Call of service getDriverJourneys for MSP {}", mspId);
        return new ResponseEntity<>(ivService.getPassengerJourneys(mspId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count), HttpStatus.OK);
    }

    /**
     * PassengerRegularTrips End-point
     **/
    @Operation(summary = "Search for matching passenger regular trips.", description = "Route used to retrieve a collection of passenger regular trips matching the provided criteria.", tags =  {"Carpooling","Traveler Information"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_IV_RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = InlineResponse400.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED,content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS,content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = "How long to wait before making a new request (in seconds)." , schema =  @Schema(type = "integer") ),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR,content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping(
            value = GET_MSP_PASSENGER_REGULAR_TRIPS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PassengerRegularTrip>> getPassengerRegularTrips(@PathVariable UUID mspId,
                                                                               @RequestParam (required = true) Float departureLat, @RequestParam (required = true) Float departureLng,
                                                                               @RequestParam (required = true) Float arrivalLat,
                                                                               @RequestParam (required = true)  Float arrivalLng, @RequestParam (required = true) String departureTimeOfDay,
                                                                               @RequestParam (required = false) List<String> departureWeekdays,
                                                                               @RequestParam(required = false) Integer timeDelta, @RequestParam(required = false) Float departureRadius,
                                                                               @RequestParam(required = false) Float arrivalRadius, @RequestParam(required = false) Integer minDepartureDate,
                                                                               @RequestParam(required = false) Integer maxDepartureDate, @RequestParam(required = false) Integer count) {
        log.info("Call of service getPassengerRegularTrips ");
        return new ResponseEntity<>(ivService.getPassengerRegularTrips(mspId,departureLat, departureLng, arrivalLat, arrivalLng,
                departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius,
                minDepartureDate, maxDepartureDate, count), HttpStatus.OK);
    }


    /**
     * DriverRegularTrips End-point
     **/
    @Operation(summary = "Search for matching regular driver trips.", description = "Route used to retrieve a collection of driver regular trips matching the provided criteria.", tags =  {"Carpooling","Traveler Information"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_IV_RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = InlineResponse400.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED,content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS,content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = "How long to wait before making a new request (in seconds)." , schema =  @Schema(type = "integer") ),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR,content = @Content(schema = @Schema(hidden = true)))})
    @GetMapping(
            value = GET_MSP_DRIVER_REGULAR_TRIPS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DriverRegularTrip>> getDriverRegularTrips (@PathVariable UUID mspId,@RequestParam (required = true) Float departureLat, @RequestParam (required = true) Float departureLng,
                                                                          @RequestParam (required = true) Float arrivalLat,
                                                                               @RequestParam (required = true)  Float arrivalLng, @RequestParam (required = true)  String departureTimeOfDay,
                                                                          @RequestParam (required = false) List<String> departureWeekdays,
                                                                               @RequestParam(required = false) Integer timeDelta, @RequestParam(required = false) Float departureRadius,
                                                                               @RequestParam(required = false) Float arrivalRadius, @RequestParam(required = false) Integer minDepartureDate,
                                                                               @RequestParam(required = false) Integer maxDepartureDate, @RequestParam(required = false) Integer count) {
        log.info("Call of service getDriverRegularTrips ");
        return new ResponseEntity<>(ivService.getDriverRegularTrips(mspId,departureLat, departureLng, arrivalLat, arrivalLng, departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count), HttpStatus.OK);
    }


}
