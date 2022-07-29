package com.gateway.api.service.mspservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.api.model.MSPMeta;
import com.gateway.api.model.mapper.MSPMetaDTOMapper;
import com.gateway.api.rest.APIController;
import com.gateway.api.service.mspservice.MSPService;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.MSPZone;
import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.monitoring.UserContext;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.enums.ZoneType;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

import static com.gateway.api.util.constant.GatewayApiPathDict.*;
import static com.gateway.api.util.constant.GatewayMessageDict.*;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@Service
public class MSPServiceImpl implements MSPService {


    @Value("${gateway.service.dataapi.baseUrl}")
    private String uri;
    @Value("${gateway.service.routingapi.baseurl}")
    private String routingApiUri;
    private final String correlationId = String.valueOf(CommonUtils.setHeader().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));


    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ErrorMessages errorMessages;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private ObjectMapper objectMapper;

    private MSPMetaDTOMapper mapper = Mappers.getMapper(MSPMetaDTOMapper.class);


    @Override
    public MSPZone getMSPZone(UUID mspId, ZoneType areaType) {
        Map<String, String> params = new HashMap<>();
        params.put(ZONE_TYPE, areaType.toString());
        List<Object> response = (List<Object>) this.getRooting(mspId, MSP_ZONE_SEARCH, Optional.empty(), params);
        MSPZone zone = new MSPZone();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<MSPZone> responseList = mapper.convertValue(response, new TypeReference<List<MSPZone>>() {
            });
            if (responseList != null && responseList.get(0) != null) {
                MSPZone respMspZone = responseList.get(0);
                zone.setMspId(respMspZone.getMspId());
                zone.setMsp(respMspZone.getMsp());
                zone.setStatus(respMspZone.getStatus());
                zone.setUpdateDate(respMspZone.getUpdateDate());
                zone.setType(respMspZone.getType());
                zone.setZones(respMspZone.getZones());
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
                throw new InternalException(e.getMessage());
            } else {
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), CommonUtils.placeholderFormat(GET_MSP_ZONE_BY_MSP_ID_AND_AREA_TYPE, MSP_ID, String.valueOf(mspId), AREA_TYPE, areaType.name())));
            }
        }

        return zone;
    }


    /**
     * Retrieve a list of MSPs metadata.
     *
     * @return List of {@link MSPMeta} MSPs metadata.
     */
    @Override
    public List<MSPMeta> getMSPsMeta() throws IOException, InterruptedException {
        List<MSPMeta> mspMetaList = new ArrayList<>();

        // get the correlationId of the current thread and forward as http header
        UserContext userContext = new ThreadLocalUserSession().get();
        String correlationId = userContext.getContextId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        String urlGetMetas = uri + MSP_META_ENDPOINT;

        try {
            ResponseEntity<MspMetaDTO[]> mspMetasDto = restTemplate.exchange(urlGetMetas, HttpMethod.GET, entity, MspMetaDTO[].class);
            //Convert MspMetaDTO into MSPMeta
            List<MSPMeta> mspsMetas = mapper.mapDataApiDtoToApiDto(Arrays.asList(mspMetasDto.getBody()));
            //add _links
            if (mspsMetas != null) {
                for (MSPMeta msp : mspsMetas) {
                    try {
                        addLinks(msp);
                        mspMetaList.add(msp);
                    } catch (NotFoundException e) {
                        log.error(NO_METADATA_FOR_MSP_IDENTIFIER, msp.getMspId(), e);
                    }
                }
            }
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        } catch (Exception e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        }
        return mspMetaList;
    }


    /**
     * Retrieve a MSP metadata informations.
     *
     * @param mspId Identifier of the MSP.
     * @return {@link MSPMeta} Metadata informations for the MSP
     * @throws NotFoundException mspMeta not found
     */
    @Override
    public MSPMeta getMSPMeta(UUID mspId) {

        // get the correlationId of the current thread and forward as http header
        String correlationId = new ThreadLocalUserSession().get().getContextId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        String urlGetMeta = uri + MSP_META_ENDPOINT + mspId.toString();

        try {
            ResponseEntity<MspMetaDTO> mspMetasDto = restTemplate.exchange(urlGetMeta,
                    HttpMethod.GET, entity, MspMetaDTO.class);

            //Convert MspMetaDTO into MSPMeta
            MSPMeta mspMeta = mapper.mapDataApiDtoToApiDto(mspMetasDto.getBody());
            //Add _links
            if (mspMeta != null) {
                addLinks(mspMeta);
            } else {
                throw new NotFoundException(String.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMeta));
            }
            return mspMeta;
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMeta));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMeta));
        } catch (Exception e) {
            log.error(MessageFormat.format(CALL_ID_MESSAGE_PATTERN, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMeta));
        }

    }

    /**
     * Add links to MSP metadata.
     *
     * @param mspMeta MSP metadata.
     */
    void addLinks(MSPMeta mspMeta) {
        UUID mspId = mspMeta.getMspId();
        // Add links to zones
        addAreaLinks(mspMeta);
        // Various informations about MSP
        addFeaturesLinks(mspMeta);
        // Self link
        try {
            mspMeta.addHateoasLink(linkTo(
                    methodOn(APIController.class).
                            getMSPMeta(mspId)).
                    withSelfRel());
        } catch (NotFoundException e) {
            log.error(NO_METADATA_FOR_MSP_IDENTIFIER, mspId, e);
        }
    }

    /**
     * Add area links to MSP metadata.
     *
     * @param mspMeta MSP metadata.
     */
    private void addAreaLinks(MSPMeta mspMeta) {
        UUID mspId = mspMeta.getMspId();

        if (mspMeta.isHasOperatingZone()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPZone(mspId, (ZoneType.OPERATING))).
                        withRel(OPERATING_ZONE));
            } catch (NotFoundException e) {
                log.error(NO_OPERATING_AREA_FOR_MSP_IDENTIFIER, mspId, e);
            }
        }
        if (mspMeta.isHasNoParkingZone()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPZone(mspId, (ZoneType.NO_PARKING))).
                        withRel(NO_PARKING_ZONE));
            } catch (NotFoundException e) {
                log.error(NO_PROHIBITED_PARKING_AREA_FOR_MSP_IDENTIFIER, mspId, e);
            }
        }
        if (mspMeta.isHasPrefParkingZone()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPZone(mspId, (ZoneType.PREFERENTIAL_PARKING))).
                        withRel(PREFERENTIAL_PARKING_ZONE));
            } catch (NotFoundException e) {
                log.error(NO_PREFERENTIAL_PARKING_AREA_FOR_MSP_IDENTIFIER, mspId, e);
            }
        }
        if (mspMeta.isHasSpeedLimitZone()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPZone(mspId, (ZoneType.SPEED_LIMIT))).
                        withRel(SPEED_LIMIT_ZONE));
            } catch (NotFoundException e) {
                log.error(NO_SPEED_LIMIT_AREA_FOR_MSP_IDENTIFIER, mspId, e);
            }
        }
    }

    /**
     * Add features links to MSP metadata.
     *
     * @param mspMeta MSP metadata.
     */
    private void addFeaturesLinks(MSPMeta mspMeta) {
        UUID mspId = mspMeta.getMspId();
        // Vehicle information about MSP
        if (mspMeta.isHasVehicle()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPAssets(mspId)).
                        withRel(ASSETS));
            } catch (NotFoundException e) {
                log.error(NO_VEHICULES_FOR_MSP_IDENTIFIER, mspId, e);
            }
        }

        // Station information about MSP
        if (mspMeta.isHasStation()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPStations(mspId, null, null, null)).
                        withRel(STATIONS));
            } catch (NotFoundException e) {
                log.error(NO_STATIONS_FOR_MSP_IDENTIFIER, mspId, e);
            }
        }
        if (mspMeta.isHasStationStatus()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPStationsStatus(mspId, null)).
                        withRel(STATIONS_STATUS));
            } catch (NotFoundException e) {
                log.error(NO_STATIONS_STATUS_FOR_MSP_IDENTIFIER, mspId, e);
            }
        }
    }

    /**
     * Call Routing
     *
     * @param mspId
     * @param actionName
     * @param body
     * @param params
     * @return
     */
    private Object getRooting(UUID mspId, String actionName, Optional<Map<String, Object>> body, Map<String, String> params) {
        String mspMetaIdValue = mspId != null ? mspId.toString() : null;
        Object mspBusinessResponse = null;
        String urlCall = routingApiUri + CommonUtils.placeholderFormat(GET_MSP_ID_PATH, MSP_ID, mspMetaIdValue
                + GET_ACTION_NAME_PATH, ACTION_NAME, actionName);

        String urlTemplate = CommonUtils.constructUrlTemplate(urlCall, params);

        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(urlTemplate, body, Object.class);
            if (response.getBody() != null) {
                mspBusinessResponse = Objects.requireNonNull(response.getBody());
            }
        } catch (HttpClientErrorException e) {

            GenericError error = new GenericError(e.getResponseBodyAsString());
            throw new NotFoundException(error.getDescription());

        } catch (HttpServerErrorException e) {
            log.error(ERROR_FOR_URL_CALL, urlTemplate, e.getMessage(), e);
            GenericError error = new GenericError(e.getResponseBodyAsString());
            throw new InternalException(error.getDescription());

        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall));
        }
        return mspBusinessResponse;
    }


}
