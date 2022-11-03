package com.gateway.api.rest;


import com.gateway.api.model.PartnerMeta;
import com.gateway.api.model.PriceList;
import com.gateway.api.service.bookingservice.BookingService;
import com.gateway.api.service.ivservice.IVService;
import com.gateway.api.service.partnerservice.PartnerService;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.exceptions.CarpoolError;
import com.gateway.commonapi.dto.exceptions.TompError;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.BookingStatus;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.StandardEnum;
import com.gateway.commonapi.utils.enums.ZoneType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static com.gateway.api.util.constant.GatewayMessageDict.*;
import static com.gateway.commonapi.constants.ControllerMessageDict.*;
import static com.gateway.commonapi.constants.GatewayApiPathDict.*;

@Slf4j
@Validated
@RestController
@RequestMapping(GET_PARTNERS_PATH)
public class APIController {

    @Autowired
    private PartnerService partnerService;
    @Autowired
    private IVService ivService;
    @Autowired
    private BookingService bookingService;

    /**
     * Return metadata for all managed Partner.
     *
     * @return List of Partner Metadata
     */


    @Operation(summary = "Get metadata for all managed Partner", description = "Endpoint used to get all metadata for all managed Partner and useful links", tags = "Partners Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(value = "",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PartnerMeta> getPartnersMeta(@RequestParam(required = false) PartnerTypeEnum partnerType) {
        log.info("Call of service getPartnersMeta");
        CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 500));
        if (partnerType != null) {
            return partnerService.getPartnersMetaByType(partnerType);
        }
        return partnerService.getPartnersMeta();
    }


    /**
     * Return metadata for a Partner.
     *
     * @param partnerId Identifier of the Partner.
     * @return Partner Metadata object.
     */

    @Operation(summary = "Get metadata for a given Partner", description = "Endpoint used to get metadata for a Partner and useful links", tags = "Partners Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(value = GET_PARTNER_BY_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerMeta> getPartnerMeta(@PathVariable UUID partnerId) {
        log.info("Call of service getPartnerMeta for Partner {}", partnerId);
        CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 500));
        return new ResponseEntity<>(partnerService.getPartnerMeta(partnerId), HttpStatus.OK);


    }


    /**
     * Return a global view.
     *
     * @return Global view.
     */

    @Operation(summary = "Get a global view  (standard: TOMP v1.3.0)", description = "Endpoint used to retrieve a global view", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(
            value = GET_PARTNERS_GLOBAL_VIEW_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlobalView> getGlobalView() {
        log.info("Call of service getGlobalView");
        CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 500));
        return new ResponseEntity<>(ivService.getGlobalView(), HttpStatus.OK);
    }


    /**
     * Return a global view for around me.
     *
     * @param partnerAroundMeRequest
     * @return Global view.
     */

    @Operation(summary = "Get a global view for around me  (standard: TOMP v1.3.0)", description = "Endpoint used to retrieve a global view for around me", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @PostMapping(
            value = GET_PARTNERS_AROUND_ME_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GlobalView> getAroundMe(@RequestBody(required = true) PartnerAroundMeRequest partnerAroundMeRequest) {
        log.info("Call of service aroundMe");
        CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 500));
        return new ResponseEntity<>(ivService.getAroundMe(partnerAroundMeRequest), HttpStatus.OK);
    }


    /**
     * Return a specific area information for a Partner.
     *
     * @param partnerId Identifier of the Partner.
     * @param areaType  Area type
     * @return object {@link PartnerZone}} for a Partner area.
     */
    @Operation(summary = "Get specific area information for a Partner  (standard: TOMP v1.3.0)", description = "Endpoint used to get a specific area information for a Partner", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(value = GET_PARTNER_AREAS_TYPE_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PartnerZone> getPartnerAreas(@PathVariable UUID partnerId, @PathVariable ZoneType areaType) {
        log.info("Call of service getPartnerAreas for Partner {} and zone {}", partnerId, areaType);
        CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 500));
        return new ResponseEntity<>(partnerService.getPartnerZone(partnerId, areaType), HttpStatus.OK);

    }


    /**
     * Return all stations for a Partner.
     *
     * @param partnerId Identifier of the Partner.
     * @return List of stations for the Partner.
     */

    @Operation(summary = "Get all stations for a Partner  (standard: TOMP v1.3.0)", description = "Endpoint used to retrieve stations for a Partner", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFUL_OPERATION, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(
            value = GET_PARTNER_STATIONS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Station>> getPartnerStations(@PathVariable UUID partnerId, @RequestParam(required = false) Float lon, @RequestParam(required = false) Float lat, @RequestParam(required = false) Float radius) {
        log.info("Call of service getPartnerStations for Partner {}", partnerId);
        CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 500));
        return new ResponseEntity<>(ivService.getStations(partnerId, lon, lat, radius), HttpStatus.OK);


    }

    /**
     * Return status for all stations for a Partner.
     *
     * @param partnerId Identifier of the Partner.
     * @return List of stations for the Partner.
     */
    @Operation(summary = "Get all stations status for a Partner  (standard: TOMP v1.3.0)", description = "Endpoint used to retrieve status of stations for a Partner", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFUL_OPERATION, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(
            value = GET_PARTNER_STATIONS_STATUS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StationStatus>> getPartnerStationsStatus(@PathVariable UUID partnerId, @RequestParam(required = false) String stationId) {
        log.info("Call of service getPartnerStationsStatus for Partner {}", partnerId);
        CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 500));
        return new ResponseEntity<>(ivService.getStationStatus(partnerId, stationId), HttpStatus.OK);

    }

    /**
     * Return all assets for a Partner.
     *
     * @param partnerId Identifier of the Partner.
     * @return List of assets for the Partner.
     */
    @Operation(summary = "Get all assets for a Partner  (standard: TOMP v1.3.0)", description = "Endpoint used to retrieve assets for a Partner", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "403", description = TOMP_UNAUTHORIZED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "404", description = TOMP_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(
            value = GET_PARTNER_ASSETS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Asset>> getPartnerAssets(@PathVariable UUID partnerId) {

        try {
            log.info("Call of service getPartnerAssets for Partner {}", partnerId);
            CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 403, 404, 500));
            return new ResponseEntity<>(ivService.getAssets(partnerId), HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(List.of(404));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                return new ResponseEntity<>(ivService.getAssets(partnerId), HttpStatus.OK);
            }
        }


    }

    /**
     * Return available assets for a Partner.
     *
     * @param partnerId Identifier of the Partner.
     * @return List of available assets for the Partner.
     */
    @Operation(summary = "Get all available assets for a Partner  (standard: TOMP v1.3.0)", description = "Endpoint used to retrieve available assets for a Partner", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "403", description = TOMP_UNAUTHORIZED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "404", description = TOMP_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(
            value = GET_PARTNER_AVAILABLE_ASSETS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AssetType>> getPartnerAvailableAssets(@PathVariable UUID partnerId, @RequestParam(required = false) String stationId, @RequestParam(required = false) Float lon, @RequestParam(required = false) Float lat, @RequestParam(required = false) Float radius) {

        try {
            log.info("Call of service getPartnerAvailableAssets for Partner {}", partnerId);
            CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 403, 404, 500));
            return new ResponseEntity<>(ivService.getAvailableAssets(partnerId, stationId, lon, lat, radius), HttpStatus.OK);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(List.of(404));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                return new ResponseEntity<>(ivService.getAvailableAssets(partnerId, stationId, lon, lat, radius), HttpStatus.OK);
            }
        }


    }

    @Operation(summary = "Get all types of vehicle from a Partner  (standard: TOMP v1.3.0)", description = "Endpoint used to retrieve all types of vehicle for a Partner", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(
            value = GET_PARTNER_VEHICLE_TYPES_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VehicleTypes>> getPartnerVehicleTypes(@PathVariable UUID partnerId) {
        log.info("Call of service getPartnerVehicleTypes for Partner {}", partnerId);
        CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        CallUtils.saveValidCodesInCallThread(String.valueOf(new ArrayList<>(Arrays.asList(200, 400, 401, 500))));
        CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 500));
        return new ResponseEntity<>(ivService.getVehicleTypes(partnerId), HttpStatus.OK);

    }

    @Operation(summary = "Get all pricing plan from a Partner  (standard: TOMP v1.3.0)", description = "Endpoint used to retrieve all pricing plan for a Partner", tags = "Traveler Information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RESPONSE_OK_PRICING_PLAN, headers = {
                    @Header(name = "Content-Language", description = TOMP_HEADERS_DESCRIPTION, schema = @Schema(type = "string")),
            }),
            @ApiResponse(responseCode = "400", description = TOMP_BAD_REQUEST, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "403", description = TOMP_UNAUTHORIZED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "500", description = INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(
            value = GET_PARTNER_PRICING_PLAN_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PriceList>> getPartnerPricingPlan(@PathVariable UUID partnerId, @RequestParam(required = false) String stationId) {
        log.info("Call of service getPartnerPricingPlan for Partner {}", partnerId);
        CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 403, 500));
        return new ResponseEntity<>(ivService.getPricingPlan(partnerId, stationId), HttpStatus.OK);

    }


    //------------------------------------------CARPOOLING END-POINTS------------------------------------------//

    /**
     * DriverJourneys End-point
     **/
    @Operation(summary = "Search for matching punctual planned outward driver journeys.  (standard: COVOITURAGE)", description = "Route used to retrieve a collection of punctual planned outward driver journeys matching the provided criteria.", tags = {"Carpooling", "Traveler Information"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_IV_RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS, content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = COVOITURAGE_HEADERS_DESCRIPTION, schema = @Schema(type = "integer")),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = CarpoolError.class)))})
    @GetMapping(
            value = GET_PARTNER_DRIVER_JOURNEY_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DriverJourney>> getDriverJourneys(@PathVariable UUID partnerId,
                                                                 @RequestParam(required = true) @Parameter(description = DEPARTURE_LAT) Float departureLat,
                                                                 @RequestParam(required = true) @Parameter(description = DEPARTURE_LNG) Float departureLng,
                                                                 @RequestParam(required = true) @Parameter(description = ARRIVAL_LAT) Float arrivalLat,
                                                                 @RequestParam(required = true) @Parameter(description = DEPARTURE_LNG) Float arrivalLng,
                                                                 @RequestParam(required = true) @Parameter(description = DEPARTURE_DATE) Integer departureDate,
                                                                 @RequestParam(required = false, defaultValue = "900") @Parameter(description = TIME_DELTA) Integer timeDelta,
                                                                 @RequestParam(required = false, defaultValue = "1") @Parameter(description = DEPARTURE_RADIUS) Float departureRadius,
                                                                 @RequestParam(required = false, defaultValue = "1") @Parameter(description = ARRIVAL_RADIUS) Float arrivalRadius,
                                                                 @RequestParam(required = false) @Parameter(description = COUNT) Integer count) {
        try {
            log.info("Call of service getDriverJourneys for Partner {}", partnerId);
            CallUtils.saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 429, 500));
            return new ResponseEntity<>(ivService.getDriverJourneys(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count), HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(Arrays.asList(401, 429));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                return new ResponseEntity<>(ivService.getDriverJourneys(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count), HttpStatus.OK);
            }
        }


    }


    /**
     * Passengerjourneys End-point
     **/
    @Operation(summary = "Search for matching punctual planned outward passenger journeys.  (standard: COVOITURAGE)", description = "Route used to retrieve a collection of punctual planned outward passenger journeys matching the provided criteria.", tags = {"Carpooling", "Traveler Information"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_IV_RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS, content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = COVOITURAGE_HEADERS_DESCRIPTION, schema = @Schema(type = "integer")),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = CarpoolError.class)))})
    @GetMapping(
            value = GET_PARTNER_PASSENGER_JOURNEY_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PassengerJourney>> getPassengerJourneys(@PathVariable UUID partnerId,
                                                                       @RequestParam(required = true) @Parameter(description = DEPARTURE_LAT) Float departureLat,
                                                                       @RequestParam(required = true) @Parameter(description = DEPARTURE_LNG) Float departureLng,
                                                                       @RequestParam(required = true) @Parameter(description = ARRIVAL_LAT) Float arrivalLat,
                                                                       @RequestParam(required = true) @Parameter(description = DEPARTURE_LNG) Float arrivalLng,
                                                                       @RequestParam(required = true) @Parameter(description = DEPARTURE_DATE) Integer departureDate,
                                                                       @RequestParam(required = false, defaultValue = "900") @Parameter(description = TIME_DELTA) Integer timeDelta,
                                                                       @RequestParam(required = false, defaultValue = "1") @Parameter(description = DEPARTURE_RADIUS) Float departureRadius,
                                                                       @RequestParam(required = false, defaultValue = "1") @Parameter(description = ARRIVAL_RADIUS) Float arrivalRadius,
                                                                       @RequestParam(required = false) @Parameter(description = COUNT) Integer count) {
        try {
            log.info("Call of service getDriverJourneys for Partner {}", partnerId);
            CallUtils.saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 429, 500));
            return new ResponseEntity<>(ivService.getPassengerJourneys(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count), HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(Arrays.asList(401, 429));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                return new ResponseEntity<>(ivService.getPassengerJourneys(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count), HttpStatus.OK);
            }
        }

    }

    /**
     * PassengerRegularTrips End-point
     **/
    @Operation(summary = "Search for matching passenger regular trips.  (standard: COVOITURAGE)", description = "Route used to retrieve a collection of passenger regular trips matching the provided criteria.", tags = {"Carpooling", "Traveler Information"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_IV_RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS, content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = COVOITURAGE_HEADERS_DESCRIPTION, schema = @Schema(type = "integer")),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = CarpoolError.class)))})
    @GetMapping(
            value = GET_PARTNER_PASSENGER_REGULAR_TRIPS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PassengerRegularTrip>> getPassengerRegularTrips(@PathVariable UUID partnerId,
                                                                               @RequestParam(required = true) @Parameter(description = DEPARTURE_LAT) Float departureLat,
                                                                               @RequestParam(required = true) @Parameter(description = DEPARTURE_LNG) Float departureLng,
                                                                               @RequestParam(required = true) @Parameter(description = ARRIVAL_LAT) Float arrivalLat,
                                                                               @RequestParam(required = true) @Parameter(description = DEPARTURE_LNG) Float arrivalLng,
                                                                               @RequestParam(required = true) @Parameter(description = DEPARTURE_TIME_OF_DAY, example = "07:30:00") String departureTimeOfDay,
                                                                               @RequestParam(required = false, defaultValue = "MON,TUE,WED,THU,FRI,SAT,SUN") @Parameter(description = DEPARTURE_WEEK_DAYS) List<String> departureWeekdays,
                                                                               @RequestParam(required = false, defaultValue = "900") @Parameter(description = TIME_DELTA) Integer timeDelta,
                                                                               @RequestParam(required = false, defaultValue = "1") @Parameter(description = DEPARTURE_RADIUS) Float departureRadius,
                                                                               @RequestParam(required = false, defaultValue = "1") @Parameter(description = ARRIVAL_RADIUS) Float arrivalRadius,
                                                                               @RequestParam(required = false) @Parameter(description = MIN_DEPARTURE_DAY) Integer minDepartureDate,
                                                                               @RequestParam(required = false) @Parameter(description = MAX_DEPARTURE_DAY) Integer maxDepartureDate,
                                                                               @RequestParam(required = false) @Parameter(description = COUNT) Integer count) {
        try {
            log.info("Call of service getPassengerRegularTrips ");
            CallUtils.saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 429, 500));
            return new ResponseEntity<>(ivService.getPassengerRegularTrips(partnerId, departureLat, departureLng, arrivalLat, arrivalLng,
                    departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius,
                    minDepartureDate, maxDepartureDate, count), HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(Arrays.asList(401, 429));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                return new ResponseEntity<>(ivService.getPassengerRegularTrips(partnerId, departureLat, departureLng, arrivalLat, arrivalLng,
                        departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius,
                        minDepartureDate, maxDepartureDate, count), HttpStatus.OK);
            }
        }

    }


    /**
     * DriverRegularTrips End-point
     **/
    @Operation(summary = "Search for matching regular driver trips.  (standard: COVOITURAGE)", description = "Route used to retrieve a collection of driver regular trips matching the provided criteria.", tags = {"Carpooling", "Traveler Information"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_IV_RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS, content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = COVOITURAGE_HEADERS_DESCRIPTION, schema = @Schema(type = "integer")),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = CarpoolError.class)))})
    @GetMapping(
            value = GET_PARTNER_DRIVER_REGULAR_TRIPS_PATH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DriverRegularTrip>> getDriverRegularTrips(@PathVariable UUID partnerId,
                                                                         @RequestParam(required = true) @Parameter(description = DEPARTURE_LAT) Float departureLat,
                                                                         @RequestParam(required = true) @Parameter(description = DEPARTURE_LNG) Float departureLng,
                                                                         @RequestParam(required = true) @Parameter(description = ARRIVAL_LAT) Float arrivalLat,
                                                                         @RequestParam(required = true) @Parameter(description = DEPARTURE_LNG) Float arrivalLng,
                                                                         @RequestParam(required = true) @Parameter(description = DEPARTURE_TIME_OF_DAY, example = "07:30:00") String departureTimeOfDay,
                                                                         @RequestParam(required = false, defaultValue = "MON,TUE,WED,THU,FRI,SAT,SUN") @Parameter(description = DEPARTURE_WEEK_DAYS) List<String> departureWeekdays,
                                                                         @RequestParam(required = false, defaultValue = "900") @Parameter(description = TIME_DELTA) Integer timeDelta,
                                                                         @RequestParam(required = false, defaultValue = "1") @Parameter(description = DEPARTURE_RADIUS) Float departureRadius,
                                                                         @RequestParam(required = false, defaultValue = "1") @Parameter(description = ARRIVAL_RADIUS) Float arrivalRadius,
                                                                         @RequestParam(required = false) @Parameter(description = MIN_DEPARTURE_DAY) Integer minDepartureDate,
                                                                         @RequestParam(required = false) @Parameter(description = MAX_DEPARTURE_DAY) Integer maxDepartureDate,
                                                                         @RequestParam(required = false) @Parameter(description = COUNT) Integer count) {
        try {
            log.info("Call of service getDriverRegularTrips ");
            CallUtils.saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 429, 500));
            return new ResponseEntity<>(ivService.getDriverRegularTrips(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count), HttpStatus.OK);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(Arrays.asList(401, 429));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                return new ResponseEntity<>(ivService.getDriverRegularTrips(partnerId, departureLat, departureLng, arrivalLat, arrivalLng, departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count), HttpStatus.OK);
            }
        }

    }

    /**
     * Send messages MaaS -> Msp
     **/
    @Operation(summary = "Send a message to the owner of a retrieved journey.  (standard: COVOITURAGE)", description = "Route used to allow a user to connect back to the owner of a retrieved journey through a text message.", tags = {"Carpooling"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = COVOITURAGE_MESSAGE_RESPONSE_OK, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = COVOITURAGE_MESSAGE_NOT_FOUND, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS, content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = COVOITURAGE_HEADERS_DESCRIPTION, schema = @Schema(type = "integer")),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = CarpoolError.class)))})
    @PostMapping(
            value = POST_MESSAGE_MAAS_PARTNER,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postMessage(@PathVariable UUID partnerId, @RequestBody Message message) {
        try {
            log.info("Post Message");
            CallUtils.saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(201, 400, 401, 404, 429, 500));

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Message>> violations = validator.validate(message);
            if (!violations.isEmpty()) {
                throw new BadRequestException(CommonUtils.placeholderFormat(INVALID_BODY, FIRST_PLACEHOLDER, MESSAGE_OBJECT));
            }

            ivService.postMessage(partnerId, message);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(Arrays.asList(201, 401, 404, 429));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                ivService.postMessage(partnerId, message);
                return ResponseEntity.status(HttpStatus.CREATED).build();
            }
        }

    }


    @Operation(summary = "Create a punctual outward Booking request.  (standard: COVOITURAGE)", description = "Route used to synchronize a Booking request initiated by a platform to the second platform involved in the shared punctual outward journey. While posting a new Booking, its status must always be set first as status=WAITING_CONFIRMATION. Reminder: In case of booking without deeplink, the sender platform MUST also store the Booking object, and be ready to receive modifications of it through the PATCH /bookings API endpoint.", tags = {"Carpooling", "Booking"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = COVOITURAGE_POST_BOOKING_RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS, content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = COVOITURAGE_HEADERS_DESCRIPTION, schema = @Schema(type = "integer")),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = CarpoolError.class)))})
    @PostMapping(
            value = POST_CARPOOLING_BOOKING,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Booking> postBooking(@PathVariable UUID partnerId, @RequestBody(required = true) Booking booking) {
        try {
            log.info("Call of service postBooking");
            CallUtils.saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(201, 400, 401, 429, 500));

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Booking>> violations = validator.validate(booking);
            if (!violations.isEmpty()) {
                throw new BadRequestException(CommonUtils.placeholderFormat(INVALID_BODY, FIRST_PLACEHOLDER, BOOKING_OBJECT));
            }


            return new ResponseEntity<>(bookingService.postBooking(partnerId, booking), HttpStatus.CREATED);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(Arrays.asList(401, 429));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                return new ResponseEntity<>(bookingService.postBooking(partnerId, booking), HttpStatus.CREATED);
            }
        }

    }

    @Operation(summary = "Retrieves an existing Booking request.  (standard: COVOITURAGE)", description = "Route used to retrieve the details of an existing Booking request. Can only be used by the operator having created the Booking request. This route is provided to check if the Booking object state is similar between two operators, but its usage should be required to handle the full use case of a booking.", tags = {"Carpooling", "Booking"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_GET_BOOKING_RESPONSE_OK),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = COVOITURAGE_GET_BOOKING_NOT_FOUND, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS, content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = COVOITURAGE_HEADERS_DESCRIPTION, schema = @Schema(type = "integer")),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = CarpoolError.class)))})
    @GetMapping(
            value = GET_CARPOOLING_BOOKING,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Booking> getBooking(@PathVariable UUID partnerId, @PathVariable UUID bookingId) {
        try {
            log.info("Call of service getBooking");
            CallUtils.saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 404, 429, 500));
            return new ResponseEntity<>(bookingService.getBooking(partnerId, bookingId), HttpStatus.OK);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(Arrays.asList(401, 429));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                return new ResponseEntity<>(bookingService.getBooking(partnerId, bookingId), HttpStatus.OK);
            }
        }

    }

    @Operation(summary = "Updates status of an existing Booking request.  (standard: COVOITURAGE)", description = "Route used to update the status of an existing Booking request. Should be used usually just to confirm, cancel, etc. an existing Booking.", tags = {"Carpooling", "Booking"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_GET_BOOKING_RESPONSE_OK, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = COVOITURAGE_PATCH_BOOKING_NOT_FOUND, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "409", description = COVOITURAGE_CONFLICT, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS, content = @Content(schema = @Schema(hidden = true)),
                    headers = {
                            @Header(name = "Retry-After", description = COVOITURAGE_HEADERS_DESCRIPTION, schema = @Schema(type = "integer")),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = CarpoolError.class)))})
    @PatchMapping(
            value = PATCH_CARPOOLING_BOOKING,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> patchBooking(@PathVariable UUID partnerId,
                                               @PathVariable UUID bookingId,
                                               @RequestParam(defaultValue = "WAITING_CONFIRMATION") @Parameter(description = STATUS_DESCRIPTION) BookingStatus status,
                                               @RequestParam(required = false) @Parameter(description = MESSAGE) String message) {
        try {
            log.info("Call of service patchBooking");
            CallUtils.saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 404, 429, 500));
            bookingService.patchBooking(partnerId, bookingId, status, message);
            return ResponseEntity.ok().build();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(Arrays.asList(200, 401, 429));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                bookingService.patchBooking(partnerId, bookingId, status, message);
                return ResponseEntity.ok().build();
            }
        }

    }

    @Operation(summary = "Sends booking information of a user connected with a third-party provider back to the provider.  (standard: COVOITURAGE)", description = "Route used to allow a carpool operator to send booking information to a third-party provider. This can be used in the context of a booking flow with deepLinking and when a passenger is using the Connect specification to link their accounts of a third-party service (e.g., a MaaS) and the operator.", tags = {"Carpooling", "Booking"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_POST_BOOKING_RESPONSE_OK, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "400", description = COVOITURAGE_BAD_REQUEST, content = @Content(schema = @Schema(implementation = CarpoolError.class))),
            @ApiResponse(responseCode = "401", description = COVOITURAGE_UNAUTHORIZED, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS, content = @Content(schema = @Schema(hidden = true)),

                    headers = {
                            @Header(name = "Retry-After", description = COVOITURAGE_HEADERS_DESCRIPTION, schema = @Schema(type = "integer")),
                    }),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = CarpoolError.class)))})
    @PostMapping(
            value = POST_BOOKING_EVENTS,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> postBookingEvents(@PathVariable UUID partnerId, @RequestBody CarpoolBookingEvent carpoolBookingEvent) {

        try {
            log.info("Call of service postBookingEvent");
            CallUtils.saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 400, 401, 429, 500));

            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<CarpoolBookingEvent>> violations = validator.validate(carpoolBookingEvent);
            if (!violations.isEmpty()) {
                throw new BadRequestException(CommonUtils.placeholderFormat(INVALID_BODY, FIRST_PLACEHOLDER, BOOKING_EVENT_OBJECT));
            }

            bookingService.postBookingEvents(partnerId, carpoolBookingEvent);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(Arrays.asList(401, 429));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                bookingService.postBookingEvents(partnerId, carpoolBookingEvent);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }
    }


    /*******      PING     *******/

    @Operation(summary = "Give health status of the webservice.", description = "", tags = {"Carpooling", "Partners Status"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = COVOITURAGE_STATUS_RESPONSE_OK, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "429", description = COVOITURAGE_MANY_REQUESTS, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = COVOITURAGE_INTERNAL_SERVER_ERROR, content = @Content(schema = @Schema(implementation = CarpoolError.class)))})
    @GetMapping(value = GET_STATUS,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getStatusForCarpool(@PathVariable UUID partnerId) {
        try {
            log.info("Call of service status of Carpooling MSP");
            CallUtils.saveOutputStandardInCallThread(StandardEnum.COVOITURAGE_STANDARD);
            CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 429, 500));
            partnerService.getStatus(partnerId);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            ArrayList<Integer> hiddenCodes = new ArrayList<>(Arrays.asList(429));
            if (hiddenCodes.contains(e.getRawStatusCode())) {
                return new ResponseEntity<>(null, HttpStatus.valueOf(e.getRawStatusCode()));
            } else {
                partnerService.getStatus(partnerId);
                return ResponseEntity.status(HttpStatus.OK).build();

            }
        }
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @Operation(summary = "Give a health status of webservice of TOMP standard.", description = "", tags = {"Partners Status"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = SUCCESSFUL_OPERATION, content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = TOMP_UNAUTHENTICATED, content = @Content(schema = @Schema(implementation = TompError.class))),
            @ApiResponse(responseCode = "500", description = NOT_EVERY_ENDPOINT_FUNCTIONS_PROPERLY, content = @Content(schema = @Schema(implementation = TompError.class)))})
    @GetMapping(value = GET_PING,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Object> ping(@PathVariable UUID partnerId) {
        log.info("Call of service status of TOMP MSP");
        CallUtils.saveOutputStandardInCallThread(StandardEnum.TOMP_1_3_0);
        CallUtils.saveValidCodesInCallThread(Arrays.asList(200, 401, 500));
        partnerService.ping(partnerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

