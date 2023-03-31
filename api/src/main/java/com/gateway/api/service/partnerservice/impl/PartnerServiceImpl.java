package com.gateway.api.service.partnerservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.api.model.PartnerMeta;
import com.gateway.api.model.mapper.PartnerMetaDTOMapper;
import com.gateway.api.rest.APIController;
import com.gateway.api.service.partnerservice.PartnerService;
import com.gateway.api.util.ValidityUtils;
import com.gateway.commonapi.cache.PartnerMetaCacheManager;
import com.gateway.commonapi.cache.ZoneCacheManager;
import com.gateway.commonapi.constants.AttributeDict;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.PartnerZone;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.restConfig.RestConfig;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.cache.CacheService;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.PartnerTypeRequestHeader;
import com.gateway.commonapi.utils.enums.StandardEnum;
import com.gateway.commonapi.utils.enums.ZoneType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.gateway.api.util.constant.GatewayMessageDict.*;
import static com.gateway.commonapi.constants.ErrorCodeDict.PARTNER_ID_CODE;
import static com.gateway.commonapi.constants.GatewayApiPathDict.*;
import static com.gateway.commonapi.constants.GatewayErrorMessage.UNKNOWN_PARTNER_ID;
import static com.gateway.commonapi.constants.GatewayErrorMessage.UNKNOWN_PARTNER_ID_MESSAGE;
import static com.gateway.commonapi.utils.CommonUtils.setHeaders;
import static com.gateway.commonapi.utils.enums.ActionsEnum.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Service
public class PartnerServiceImpl implements PartnerService {

    @Value("${gateway.service.dataapi.baseUrl}")
    private String uri;
    @Value("${gateway.service.routingapi.baseurl}")
    private String routingApiUri;
    private static final String SEPARATOR = ": ";

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ErrorMessages errorMessages;

    @Autowired
    private PartnerMetaCacheManager partnerMetaCache;

    @Autowired
    private ZoneCacheManager zoneCacheManager;

    private final PartnerMetaDTOMapper mapper = Mappers.getMapper(PartnerMetaDTOMapper.class);

    @Autowired
    ValidityUtils validityUtils;

    RestConfig restConfig = new RestConfig();
    RestTemplate restTemplate = restConfig.restTemplate();

    @Override
    public PartnerZone getPartnerZone(UUID partnerId, ZoneType areaType) {
        validityUtils.checkPartnerId(partnerId);
        PartnerZone zone = new PartnerZone();
        CacheParamDTO cacheParam = cacheService.getCacheParam(partnerId, MSP_ZONE_SEARCH.value);
        if (Boolean.TRUE.equals(cacheService.useCache()) && cacheParam != null) {
            try {
                zone = zoneCacheManager.getPartnerZoneFromCache(partnerId, areaType);
                log.debug("get partner zones from cache");
            } catch (Exception e) {
                String msg = CommonUtils.placeholderFormat(GET_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, "partnerZone");
                this.exceptionHandler(e, msg, partnerId);
            }
        } else {

            String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

            Map<String, String> params = new HashMap<>();
            params.put(ZONE_TYPE, areaType.toString());
            List<Object> response = (List<Object>) this.getRouting(partnerId, MSP_ZONE_SEARCH.value, Optional.empty(), params);

            try {

                ObjectMapper objectMapper = new ObjectMapper();
                List<PartnerZone> responseList = objectMapper.convertValue(response, new TypeReference<>() {
                });
                if (responseList != null && responseList.get(0) != null) {
                    PartnerZone respPartnerZone = responseList.get(0);
                    zone.setPartnerId(respPartnerZone.getPartnerId());
                    zone.setPartner(respPartnerZone.getPartner());
                    zone.setStatus(respPartnerZone.getStatus());
                    zone.setUpdateDate(respPartnerZone.getUpdateDate());
                    zone.setType(respPartnerZone.getType());
                    zone.setZones(respPartnerZone.getZones());
                }
            } catch (Exception e) {
                if (e.getMessage() != null) {
                    log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
                    throw new InternalException(e.getMessage());
                } else {
                    throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), CommonUtils.placeholderFormat(GET_PARTNER_ZONE_BY_PARTNER_ID_AND_AREA_TYPE, PARTNER_ID, String.valueOf(partnerId), AREA_TYPE, areaType.name())));
                }
            }
        }
        return zone;
    }


    /**
     * Retrieve a list of MSPs metadata.
     *
     * @return List of {@link PartnerMeta} MSPs metadata.
     */
    @Override
    public List<PartnerMeta> getPartnersMeta() {
        String msg = CommonUtils.placeholderFormat(GET_ALL_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, "partnersMeta");
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        List<PartnerMeta> partnerMetaList = new ArrayList<>();

        if (Boolean.TRUE.equals(cacheService.useCache())) {
            extractPartnerMetaFromCache(msg, partnerMetaList);
        } else {
            makePartnerMetaRealCall(outputStandard, partnerMetaList);
        }
        return partnerMetaList;
    }

    private void makePartnerMetaRealCall(String outputStandard, List<PartnerMeta> partnerMetaList) {
        // get the correlationId of the current thread and forward as http header
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

        String urlGetMetas = uri + PARTNER_META_ENDPOINT;

        try {
            ResponseEntity<PartnerMetaDTO[]> mspMetasDto = restTemplate.exchange(urlGetMetas, HttpMethod.GET, setHeaders(), PartnerMetaDTO[].class);
            //Convert MspMetaDTO into MSPMeta
            List<PartnerMeta> mspsMetas = mapper.mapDataApiDtoToApiDto(Arrays.asList(mspMetasDto.getBody()));
            //add _links
            generateListOfLinks(mspsMetas, partnerMetaList, NO_METADATA_FOR_PARTNER_IDENTIFIER);

        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        } catch (Exception e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        } finally {
            CallUtils.saveOutputStandardInCallThread(outputStandard);
        }
    }

    private void extractPartnerMetaFromCache(String msg, List<PartnerMeta> partnerMetaList) {

        try {
            List<PartnerMetaDTO> partnerMetaDTOList;
            List<PartnerMeta> mappedListPartners;
            partnerMetaDTOList = partnerMetaCache.getAllPartnersFromCache();
            mappedListPartners = mapper.mapDataApiDtoToApiDto(partnerMetaDTOList);
            generateListOfLinks(mappedListPartners, partnerMetaList, msg);
            log.debug("get partners meta from cache");
        } catch (Exception e) {
            this.exceptionHandler(e, msg, null);
        }
    }

    @Override
    public List<PartnerMeta> getPartnersMetaByExample(PartnerMeta partnerMetaExample, PartnerTypeRequestHeader callPartnerType) {
        String msg = CommonUtils.placeholderFormat(GET_ALL_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, PARTNER_META_BY_EXAMPLE);
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        List<PartnerMeta> partnerMetaList = new ArrayList<>();
        List<PartnerMeta> mappedListPartners;
        List<PartnerMetaDTO> partnerMetaDTOList;

        if (Boolean.TRUE.equals(cacheService.useCache())) {
            try {
                partnerMetaDTOList = partnerMetaCache.getAllPartnersFromCache();
                mappedListPartners = mapper.mapDataApiDtoToApiDto(partnerMetaDTOList);
                if (partnerMetaExample.getPartnerType() != null) {
                    mappedListPartners = mappedListPartners.stream().filter(c -> c.getPartnerType().value.contains(partnerMetaExample.getPartnerType().value)).collect(Collectors.toList());
                }
                if (partnerMetaExample.getType() != null) {
                    mappedListPartners = mappedListPartners.stream().filter(c -> c.getType().toString().contains(partnerMetaExample.getType().toString())).collect(Collectors.toList());
                }
                if (partnerMetaExample.getOperator() != null) {
                    mappedListPartners = mappedListPartners.stream().filter(c -> c.getOperator().contains(partnerMetaExample.getOperator())).collect(Collectors.toList());
                }
                if (partnerMetaExample.getName() != null) {
                    mappedListPartners = mappedListPartners.stream().filter(c -> c.getName().contains(partnerMetaExample.getName())).collect(Collectors.toList());
                }

                log.debug("get partners meta by example from cache");

                generateListOfLinks(mappedListPartners, partnerMetaList, msg);

            } catch (Exception e) {
                this.exceptionHandler(e, msg, null);
            }
        } else {
            // get the correlationId of the current thread and forward as http header
            String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            String urlGetMetas = uri + PARTNER_METAS_PATH_PARAM;


            if (partnerMetaExample.getPartnerType() != null) {
                parameters.add(AttributeDict.PARTNER_TYPE_ATTRIBUTE, partnerMetaExample.getPartnerType().value);
            }
            if (partnerMetaExample.getType() != null) {
                parameters.add(AttributeDict.TYPE_ATTRIBUTE, partnerMetaExample.getType().toString());
            }
            if (partnerMetaExample.getOperator() != null) {
                parameters.add(AttributeDict.OPERATOR_ATTRIBUTE, partnerMetaExample.getOperator());
            }
            if (partnerMetaExample.getName() != null) {
                parameters.add(AttributeDict.NAME_ATTRIBUTE, partnerMetaExample.getName());
            }

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(urlGetMetas).queryParams(parameters);
            generatePartnersList(callPartnerType, msg, outputStandard, partnerMetaList, correlationId, uriBuilder.toUriString());
        }
        return partnerMetaList;
    }


    @Override
    public List<PartnerMeta> getPartnersMetaByPartnerType(PartnerTypeEnum partnerType, PartnerTypeRequestHeader callPartnerType) {

        String msg = CommonUtils.placeholderFormat(GET_ALL_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, "partnerMetaByType");
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        List<PartnerMeta> partnerMetaList = new ArrayList<>();
        List<PartnerMeta> mappedListPartners;
        List<PartnerMetaDTO> partnerMetaDTOList;

        if (Boolean.TRUE.equals(cacheService.useCache())) {
            try {
                partnerMetaDTOList = partnerMetaCache.getAllPartnersFromCache();
                mappedListPartners = mapper.mapDataApiDtoToApiDto(partnerMetaDTOList);
                mappedListPartners = mappedListPartners.stream().filter(c -> c.getPartnerType() == partnerType).collect(Collectors.toList());
                log.debug("get partners meta by type from cache");

                generateListOfLinks(mappedListPartners, partnerMetaList, msg);

            } catch (Exception e) {
                this.exceptionHandler(e, msg, null);
            }
        } else {
            // get the correlationId of the current thread and forward as http header
            String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

            String urlGetMetas = uri + PARTNER_META_ENDPOINT + PARTNER_TYPE_FILTER + partnerType.toString();
            generatePartnersList(callPartnerType, msg, outputStandard, partnerMetaList, correlationId, urlGetMetas);
        }
        return partnerMetaList;
    }

    private void generatePartnersList(PartnerTypeRequestHeader callPartnerType, String msg, String outputStandard, List<PartnerMeta> partnerMetaList, String correlationId, String urlGetMetas) {
        try {
            ResponseEntity<PartnerMetaDTO[]> mspMetasDto = restTemplate.exchange(urlGetMetas, HttpMethod.GET, setHeaders(callPartnerType), PartnerMetaDTO[].class);
            //Convert MspMetaDTO into MSPMeta
            List<PartnerMeta> mspsMetas = mapper.mapDataApiDtoToApiDto(Arrays.asList(Objects.requireNonNull(mspMetasDto.getBody())));
            //add _links
            generateListOfLinks(mspsMetas, partnerMetaList, msg);

        } catch (HttpClientErrorException.NotFound e) {
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        } catch (Exception e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        } finally {
            CallUtils.saveOutputStandardInCallThread(outputStandard);
        }
    }


    private void generateListOfLinks(List<PartnerMeta> mappedListPartners, List<PartnerMeta> partnerMetaList, String msg) {
        if (mappedListPartners != null) {
            for (PartnerMeta partner : mappedListPartners) {
                try {
                    addLinks(partner);
                    partnerMetaList.add(partner);
                } catch (NotFoundException e) {
                    log.error(msg, partner.getPartnerId(), e);
                }
            }
        }
    }

    private GenericError getGenericErrorForNotFoundPartners(UUID partnerId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        String message = CommonUtils.placeholderFormat(UNKNOWN_PARTNER_ID_MESSAGE, FIRST_PLACEHOLDER, partnerId.toString());
        GenericError genericError = new GenericError();
        genericError.setErrorCode(PARTNER_ID_CODE);
        genericError.setTimestamp(simpleDateFormat.format(new Date()));
        genericError.setDescription(message);
        genericError.setLabel(UNKNOWN_PARTNER_ID);
        return genericError;
    }

    /**
     * Retrieve a MSP metadata information.
     *
     * @param partnerId Identifier of the MSP.
     * @return {@link PartnerMeta} Metadata information for the MSP
     * @throws NotFoundException mspMeta not found
     */
    @Override
    public PartnerMeta getPartnerMeta(UUID partnerId) {
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
        String msg = CommonUtils.placeholderFormat(GET_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, "partnerMeta");

        PartnerMetaDTO partnerMetaDTO;
        PartnerMeta partnerMeta = null;

        if (Boolean.TRUE.equals(cacheService.useCache())) {
            try {
                partnerMetaDTO = partnerMetaCache.getFromCache(partnerId);
                partnerMeta = mapper.mapDataApiDtoToApiDto(partnerMetaDTO);

                if (partnerMeta != null) {
                    addLinks(partnerMeta);
                } else {
                    throw new NotFoundException(this.getGenericErrorForNotFoundPartners(partnerId));
                }

            } catch (Exception e) {
                throw new BadRequestException(this.getGenericErrorForNotFoundPartners(partnerId));
            }
        } else {

            // get the correlationId of the current thread and forward as http header
            String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

            String urlGetMeta = uri + PARTNER_META_ENDPOINT + partnerId.toString();

            try {
                ResponseEntity<PartnerMetaDTO> mspMetasDto = restTemplate.exchange(urlGetMeta,
                        HttpMethod.GET, setHeaders(), PartnerMetaDTO.class);

                //Convert MspMetaDTO into MSPMeta
                partnerMeta = mapper.mapDataApiDtoToApiDto(mspMetasDto.getBody());
                //Add _links
                if (partnerMeta != null) {
                    addLinks(partnerMeta);
                } else {
                    throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(urlGetMeta, PARTNER_ID, String.valueOf(partnerId))));

                }


            } catch (HttpClientErrorException.NotFound e) {
                log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
                throw new BadRequestException(this.getGenericErrorForNotFoundPartners(partnerId));
            } catch (RestClientException e) {
                log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
                throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMeta) + ": Partner not found");
            } catch (Exception e) {
                log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
                throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMeta));
            } finally {
                CallUtils.saveOutputStandardInCallThread(outputStandard);
            }
        }
        return partnerMeta;
    }

    @Override
    public void getStatus(UUID mspId) {
        validityUtils.checkPartnerId(mspId);
        this.getRouting(mspId, STATUS.value, Optional.empty(), null);
    }

    @Override
    public void ping(UUID mspId) {
        validityUtils.checkPartnerId(mspId);
        this.getRouting(mspId, PING.value, Optional.empty(), null);
    }


    /**
     * Add links to MSP metadata.
     *
     * @param mspMeta MSP metadata.
     */
    void addLinks(PartnerMeta mspMeta) {
        UUID partnerId = mspMeta.getPartnerId();
        // Add links to zones
        addAreaLinks(mspMeta);
        // Various information about MSP
        addFeaturesLinks(mspMeta);
        // Self link
        try {
            mspMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPartnerMeta(partnerId)).
                    withSelfRel());
        } catch (NotFoundException e) {
            log.error(NO_METADATA_FOR_PARTNER_IDENTIFIER, partnerId, e);
        }
    }

    /**
     * Add area links to MSP metadata.
     *
     * @param mspMeta MSP metadata.
     */
    private void addAreaLinks(PartnerMeta mspMeta) {
        UUID partnerId = mspMeta.getPartnerId();

        if (mspMeta.isHasOperatingZone()) {
            mspMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPartnerAreas(partnerId, (ZoneType.OPERATING))).
                    withRel(OPERATING_ZONE));
        }
        if (mspMeta.isHasNoParkingZone()) {
            mspMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPartnerAreas(partnerId, (ZoneType.NO_PARKING))).
                    withRel(NO_PARKING_ZONE));
        }
        if (mspMeta.isHasPrefParkingZone()) {
            mspMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPartnerAreas(partnerId, (ZoneType.PREFERENTIAL_PARKING))).
                    withRel(PREFERENTIAL_PARKING_ZONE));
        }
        if (mspMeta.isHasSpeedLimitZone()) {
            mspMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPartnerAreas(partnerId, (ZoneType.SPEED_LIMIT))).
                    withRel(SPEED_LIMIT_ZONE));
        }
    }


    /**
     * Add features links to MSP metadata.
     *
     * @param partnerMeta MSP metadata.
     */
    private void addFeaturesLinks(PartnerMeta partnerMeta) {
        UUID partnerId = partnerMeta.getPartnerId();

        if (partnerMeta.isHasVehicleTypes()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPartnerVehicleTypes(partnerId)).
                    withRel(VEHICLE_TYPES));
        }

        if (partnerMeta.isHasPricingPlan()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPartnerPricingPlan(partnerId, null)).
                    withRel(PRICING_PLAN));
        }

        if (partnerMeta.isHasPing()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            ping(partnerId)).
                    withRel(PING_BIS));
        }

        if (partnerMeta.isHasCarpoolingBookingPost()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            postBooking(partnerId, null)).
                    withRel(CARPOOLING_BOOKING_POST));
        }

        if (partnerMeta.isHasCarpoolingBookingPatch()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            patchBooking(partnerId, null, null, null)).
                    withRel(CARPOOLING_BOOKING_PATCH));
        }

        if (partnerMeta.isHasCarpoolingBookingGet()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getBooking(partnerId, null)).
                    withRel(CARPOOLING_BOOKING_GET));
        }

        if (partnerMeta.isHasCarpoolingDriverJourney()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getDriverJourneys(partnerId, null, null, null, null, null, null, null, null, null)).
                    withRel(CARPOOLING_DRIVER_JOURNEY));
        }

        if (partnerMeta.isHasCarpoolingPassengerJourney()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPassengerJourneys(partnerId, null, null, null, null, null, null, null, null, null)).
                    withRel(CARPOOLING_PASSENGER_JOURNEY));
        }

        if (partnerMeta.isHasCarpoolingDriverTrip()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getDriverRegularTrips(partnerId, null, null, null, null, null, null, null, null, null, null, null, null)).
                    withRel(CARPOOLING_DRIVER_TRIP));
        }

        if (partnerMeta.isHasCarpoolingPassengerTrip()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPassengerRegularTrips(partnerId, null, null, null, null, null, null, null, null, null, null, null, null)).
                    withRel(CARPOOLING_PASSENGER_TRIP));
        }

        if (partnerMeta.isHasCarpoolingMessages()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            postMessage(partnerId, null)).
                    withRel(CARPOOLING_MESSAGES));
        }

        if (partnerMeta.isHasCarpoolingStatus()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getStatusForCarpool(partnerId)).
                    withRel(CARPOOLING_STATUS));
        }

        // Vehicle information about MSP
        if (partnerMeta.isHasVehicle()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPartnerAssets(partnerId)).
                    withRel(ASSETS));
        }

        // Station information about MSP
        if (partnerMeta.isHasStation()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPartnerStations(partnerId, null, null, null)).
                    withRel(STATIONS));
        }
        if (partnerMeta.isHasStationStatus()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getPartnerStationsStatus(partnerId, null)).
                    withRel(STATIONS_STATUS));
        }

        if (partnerMeta.isHasAroundMe()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getAroundMe(null)).
                    withRel(AROUND_ME_KEY_NAME));
        }

        if (partnerMeta.isHasStation() || partnerMeta.isHasStationStatus() || partnerMeta.isHasParking() || partnerMeta.isHasVehicle() || partnerMeta.isHasVehicleTypes()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getGlobalView()).
                    withRel(GLOBAL_VIEW_KEY_NAME));
        }

        if (partnerMeta.isHasCarpoolingBookingEvent()) {
            partnerMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            postBookingEvents(partnerId, null)).
                    withRel(BOOKING_EVENT_KEY_NAME));
        }
    }

    /**
     * Call Routing
     *
     * @param partnerId  partner Identifier
     * @param actionName Name of action
     * @param body       request body
     * @param params     parameters
     * @return
     */
    private Object getRouting(UUID partnerId, String actionName, Optional<Map<String, Object>> body, Map<String, String> params) {
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

        String partnerMetaIdValue = partnerId != null ? partnerId.toString() : null;
        Object partnerBusinessResponse = null;
        String urlCall = routingApiUri + CommonUtils.placeholderFormat(GET_PARTNER_ID_PATH, PARTNER_ID, partnerMetaIdValue
                + GET_ACTION_NAME_PATH, ACTION_NAME, actionName);

        String urlTemplate = CommonUtils.constructUrlTemplate(urlCall, params);
        HttpEntity<Optional<Map<String, Object>>> entity = new HttpEntity<>(body, setHeaders().getHeaders());
        log.debug(LOG_URL_CALL, urlTemplate);

        boolean preserveOriginalErrors = false;
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        if (StringUtils.isNotBlank(outputStandard)) {
            preserveOriginalErrors = CommonUtils.shouldPreserveResponseStatus(outputStandard);
        }

        try {
            ResponseEntity<Object> response = restTemplate.exchange(urlTemplate, HttpMethod.POST, entity, Object.class);
            if (response.getBody() != null) {
                partnerBusinessResponse = Objects.requireNonNull(response.getBody());
            }
        } catch (HttpClientErrorException e) {
            log.error(ERROR_FOR_URL_CALL, urlTemplate, e.getMessage(), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                GenericError error = new GenericError(e.getResponseBodyAsString());
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + error.getDescription());
            }
        } catch (HttpServerErrorException e) {
            log.error(ERROR_FOR_URL_CALL, urlTemplate, e.getMessage(), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                GenericError error = new GenericError(e.getResponseBodyAsString());
                throw new InternalException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + error.getDescription());
            }
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + e.getMessage());
            }
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + e.getMessage());
            }
        }
        return partnerBusinessResponse;
    }

    /**
     * Return a customed exception with initial error message if it exists or a global one
     *
     * @param e
     */
    private void exceptionHandler(Exception e, String msg, UUID partnerId) {
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

        if (e.getMessage() != null) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new InternalException(e.getMessage());
        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(msg, PARTNER_ID, String.valueOf(partnerId))));
        }
    }


}
