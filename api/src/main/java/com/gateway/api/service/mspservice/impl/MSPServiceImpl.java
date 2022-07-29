package com.gateway.api.service.mspservice.impl;

import com.gateway.api.model.MSPMeta;
import com.gateway.api.model.MSPZone;
import com.gateway.api.model.mapper.MSPMetaDTOMapper;
import com.gateway.api.rest.APIController;
import com.gateway.api.service.mspservice.MSPService;
import com.gateway.api.util.enums.MSPType;
import com.gateway.api.util.enums.ZoneStatus;
import com.gateway.api.util.enums.ZoneType;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.monitoring.UserContext;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@Service
public class MSPServiceImpl implements MSPService {


    @Value("${gateway.service.dataapi.baseUrl}")
    private String uri;

    private static final String MSP_META_ENDPOINT = "/msp-metas/";

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ErrorMessages errorMessages;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private MSPMetaDTOMapper mapper = Mappers.getMapper(MSPMetaDTOMapper.class);

    private static final String NO_PARKING_ZONE = "noParkingZone";
    private static final String OPERATING_ZONE = "operatingZone";
    private static final String PREFERENTIAL_PARKING_ZONE = "preferentialParkingZone";
    private static final String SPEED_LIMIT_ZONE = "speedLimitZone";
    private static final String STATIONS = "stations";
    private static final String STATIONS_STATUS = "stationsStatus";
    private static final String ASSETS = "assets";


    //MOCKED response for getMSPZone( )

    MSPMeta msp1 = new MSPMeta((UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759")), "msp1", MSPType.TROTTINETTE, true, true, true);
    MSPMeta msp2 = new MSPMeta((UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b735")), "msp2", MSPType.AUTOPARTAGE, true, true, true);
    MSPMeta msp3 = new MSPMeta((UUID.fromString("14390fdf-34c1-41c9-885e-6ce669264483")), "msp3", MSPType.VELO, true, true, false);
    List<MSPMeta> liste = List.of(msp1, msp2, msp3);


    MSPZone mspZone1 = new MSPZone(msp1.getMspId(), "msp1", ZoneStatus.AVAILABLE);
    MSPZone mspZone2 = new MSPZone(msp2.getMspId(), "msp2", ZoneStatus.AVAILABLE);
    MSPZone mspZone3 = new MSPZone(msp3.getMspId(), "msp3", ZoneStatus.AVAILABLE);
    List<MSPZone> mspZones = List.of(mspZone1, mspZone2, mspZone3);


    @Override
    public MSPZone getMSPZone(UUID mspId, ZoneType areaType)  {
        MSPMeta mspMeta = null;

        for (MSPMeta msp : liste) {
            if (msp.getMspId().equals(mspId)) {
                mspMeta = msp;
            }
        }
        if (mspMeta != null) {
            MSPZone resp = null;
            for (MSPZone zone : mspZones) {
                if (zone.getMspId().equals(mspId)) {
                    resp = zone;
                }
            }
            return resp;
        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),CommonUtils.placeholderFormat("/v1/msps/{mspId}/areas/{areaType}", "mspId", mspId.toString(),"areaType",areaType.name())));
        }
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
                        log.error("No metadata for MSP identifier {}", msp.getMspId(), e);
                    }
                }
            }
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),urlGetMetas));
        } catch (RestClientException e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),urlGetMetas));
        } catch (Exception e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),urlGetMetas));
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
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),urlGetMeta));
        } catch (RestClientException e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),urlGetMeta));
        } catch (Exception e) {
            log.error(MessageFormat.format("CallId: {0}, {1}", correlationId, e.getMessage()),e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(),urlGetMeta));
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
            log.error("No metadata for MSP identifier {}", mspId, e);
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
                log.error("No operating area for MSP identifier {}", mspId, e);
            }
        }
        if (mspMeta.isHasNoParkingZone()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPZone(mspId, (ZoneType.NO_PARKING))).
                        withRel(NO_PARKING_ZONE));
            } catch (NotFoundException e) {
                log.error("No prohibited parking area for MSP identifier {}", mspId, e);
            }
        }
        if (mspMeta.isHasPrefParkingZone()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPZone(mspId, (ZoneType.PREFERENTIAL_PARKING))).
                        withRel(PREFERENTIAL_PARKING_ZONE));
            } catch (NotFoundException e) {
                log.error("No preferential parking area for MSP identifier {}", mspId, e);
            }
        }
        if (mspMeta.isHasSpeedLimitZone()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPZone(mspId, (ZoneType.SPEED_LIMIT))).
                        withRel(SPEED_LIMIT_ZONE));
            } catch (NotFoundException e) {
                log.error("No Speed limit area for MSP identifier {}", mspId, e);
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
                log.error("No Vehicules for MSP identifier {}", mspId, e);
            }
        }

        // Station information about MSP
        if (mspMeta.isHasStation()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPStations(mspId)).
                        withRel(STATIONS));
            } catch (NotFoundException e) {
                log.error("No stations for MSP identifier {}", mspId, e);
            }
        }
        if (mspMeta.isHasStationStatus()) {
            try {
                mspMeta.addHateoasLink(linkTo(
                        methodOn(APIController.class).
                                getMSPStationsStatus(mspId)).
                        withRel(STATIONS_STATUS));
            } catch (NotFoundException e) {
                log.error("No stations status for MSP identifier {}", mspId, e);
            }
        }
    }
}
