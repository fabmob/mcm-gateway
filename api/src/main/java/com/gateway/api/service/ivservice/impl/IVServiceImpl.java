package com.gateway.api.service.ivservice.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.api.model.PartnerMeta;
import com.gateway.api.model.PriceList;
import com.gateway.api.model.mapper.PriceListDTOMapper;
import com.gateway.api.service.ivservice.IVService;
import com.gateway.api.service.partnerservice.PartnerService;
import com.gateway.api.util.ValidityUtils;
import com.gateway.commonapi.cache.*;
import com.gateway.commonapi.constants.GatewayApiPathDict;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.data.*;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.cache.CacheService;
import com.gateway.commonapi.utils.enums.PartnerTypeEnum;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.gateway.api.util.constant.GatewayMessageDict.*;
import static com.gateway.api.util.constant.GatewayParamDict.*;
import static com.gateway.commonapi.constants.ErrorCodeDict.MAX_RESULT_CODE;
import static com.gateway.commonapi.constants.ErrorCodeDict.RADIUS_CODE;
import static com.gateway.commonapi.constants.GatewayApiPathDict.*;
import static com.gateway.commonapi.constants.GatewayErrorMessage.*;
import static com.gateway.commonapi.utils.enums.ActionsEnum.*;


@Service
@Slf4j
public class IVServiceImpl implements IVService {
    @Value("${gateway.service.routingapi.baseurl}")
    private String routingApiUri;

    @Value("${gateway.service.dataapi.baseUrl}")
    private String dataApiUri;

    private RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    @Autowired
    private PartnerService partnerService;


    private PriceListDTOMapper priceMapper = Mappers.getMapper(PriceListDTOMapper.class);

    @Autowired
    private ErrorMessages errorMessages;

    @Autowired
    private CacheService cacheService;

    @Autowired
    AssetCacheManager assetCacheManager;

    @Autowired
    AssetTypeCacheManager assetTypeCacheManager;

    @Autowired
    VehicleTypesCacheManager vehicleTypesCacheManager;

    @Autowired
    StationStatusCacheManager stationStatusCacheManager;

    @Autowired
    StationCacheManager stationCacheManager;

    @Autowired
    PriceListCacheManager priceListCacheManager;

    @Autowired
    ValidityUtils validityUtils;


    /**
     * check if the partner necessarily expects the latitude , longitude and radius parameters based on the data in the params table
     *
     * @param params database params
     * @param lon    longitude
     * @param lat    latitude
     * @param rad    radius
     */
    private void checkRequirementOfParams(List<ParamsDTO> params, Float lon, Float lat, Float rad) {

        Map<String, String> paramsValue = new HashMap<>();

        if (lon != null) {
            paramsValue.put(LON, String.valueOf(lon));
        }
        if (lat != null) {
            paramsValue.put(LAT, String.valueOf(lat));
        }
        if (rad != null) {
            paramsValue.put(RAD, String.valueOf(rad));
        }

        List<String> missingFields = new ArrayList<>();

        for (ParamsDTO param : params) {
            String value;

            if (StringUtils.isNotBlank(param.getKeyMapper())) {
                String keyMapper = param.getKeyMapper();

                value = findKeyInParamsMap(keyMapper, paramsValue);

                if (!StringUtils.isNotBlank(value)) {
                    //if the value of the param was not passed as input but there is String 'null' in the 'value' column
                    // then the param is optional so its absence does not throw an error
                    if (StringUtils.isNotBlank(param.getValue()) && param.getValue().equals("null")) {

                        continue;
                    } else {
                        // the requiredFields are required for the partner, so an error has to be thrown
                        missingFields.add(param.getKeyMapper());

                    }
                }
            }
        }
        if (!missingFields.isEmpty()) {
            throw new NotFoundException(CommonUtils.placeholderFormat(MISSING_FIELD, FIRST_PLACEHOLDER, missingFields.toString()));
        }

    }

    private List<PartnerCallsDTO> getPartnerCalls(UUID partnerId, String actionName) {
        PartnerStandardDTO standardDTO = this.getStandard(partnerId, actionName);
        return this.getCalls(standardDTO.getPartnerActionsId());
    }

    /**
     * Check lat, lon , rad and max result for Tomp endpoint
     *
     * @param lon
     * @param lat
     * @param rad
     * @param maxResult
     */
    private void checkPositionParams(Float lon, Float lat, Float rad, Integer maxResult) {
        List<String> titles = new ArrayList<>();
        List<String> messages = new ArrayList<>();
        List<Integer> errorCodes = new ArrayList<>();

        if (lat != null) {
            ValidityUtils.checkLatitude(messages, titles, errorCodes, lat.doubleValue());
        }
        if (lon != null) {
            ValidityUtils.checkLongitude(messages, titles, errorCodes, lon.doubleValue());
        }

        if (rad != null) {
            ValidityUtils.checkIfParameterIsPositive(messages, titles, errorCodes, rad, INVALID_RAD_MESSAGE, INVALID_RAD_TITLE, RADIUS_CODE);
        }
        if (maxResult != null) {
            ValidityUtils.checkIfParameterIsPositive(messages, titles, errorCodes, Float.valueOf(maxResult), INVALID_MAX_RESULT, INVALID_MAX_RESULT_TITLE, MAX_RESULT_CODE);
        }
        if (!messages.isEmpty()) {
            String globalMessage = StringUtils.join(messages, " ");
            String globalTitle = StringUtils.join(titles, ". ");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            GenericError genericError = new GenericError();
            genericError.setErrorCode(errorCodes.get(0));
            genericError.setTimestamp(simpleDateFormat.format(new Date()));
            genericError.setDescription(globalMessage);
            genericError.setLabel(globalTitle);

            throw new BadRequestException(genericError);
        }
    }


    @Override
    public List<Station> getStations(UUID partnerId, Float lon, Float lat, Float rad) {

        validityUtils.checkPartnerId(partnerId);
        this.checkPositionParams(lon, lat, rad, null);

        List<Station> stations = new ArrayList<>();

        String msg = CommonUtils.placeholderFormat(GET_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, "stations");

        CacheParamDTO cacheParam = cacheService.getCacheParam(partnerId, STATION_SEARCH.value);

        if ((Boolean.TRUE.equals(cacheService.useCache())) && (cacheParam != null)) {

            if (lat != null && lon != null && rad != null) {
                try {
                    stations = stationCacheManager.getAllStationFromCacheByGeoParams(partnerId, (double) lon, (double) lat, rad, RedisGeoCommands.DistanceUnit.METERS);
                    log.debug("get stations from cache by geo params ");
                } catch (Exception e) {
                    this.exceptionHandler(e, msg, partnerId);
                }

            } else {

                List<PartnerCallsDTO> calls = this.getPartnerCalls(partnerId, STATION_SEARCH.value);

                List<ParamsDTO> params;
                for (PartnerCallsDTO callDto : calls) {

                    params = new ArrayList<>(callDto.getParams());

                    if (!CollectionUtils.isEmpty(params)) {

                        this.checkRequirementOfParams(params, lon, lat, rad);

                        if (lat == null && lon == null && rad == null) {
                            try {
                                stations.addAll(stationCacheManager.getAllStationFromCache(partnerId));
                                log.debug("get all stations from cache ");
                            } catch (Exception e) {
                                this.exceptionHandler(e, msg, partnerId);
                            }

                        } else {
                            throw new NotFoundException(CommonUtils.placeholderFormat(IMPOSSIBLE_GEOLOCALISATION));

                        }
                    }
                }
            }

        } else {
            Map<String, String> params = new HashMap<>();
            if (lon != null) {
                params.put(LON, String.valueOf(lon));
            }
            if (lat != null) {
                params.put(LAT, String.valueOf(lat));
            }
            if (rad != null) {
                params.put(RAD, String.valueOf(rad));
            }

            List<Object> response = (List<Object>) this.getRooting(partnerId, STATION_SEARCH.value, Optional.empty(), params);
            if (response == null) {
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_PARTNER_STATIONS_STATUS_PATH, PARTNER_ID, String.valueOf(partnerId))));
            }

            try {

                ObjectMapper mapper = new ObjectMapper();
                List<Station> convertedResponse = mapper.convertValue(response, new TypeReference<List<Station>>() {
                });
                for (Station station : convertedResponse) {
                    stations.add(station);
                }
            } catch (Exception e) {
                this.exceptionHandler(e, GatewayApiPathDict.GET_PARTNER_STATIONS_PATH, partnerId);
            }
        }
        return stations;
    }

    @Override
    public List<StationStatus> getStationStatus(UUID partnerId, String stationId) {
        validityUtils.checkPartnerId(partnerId);
        List<StationStatus> stationStatuses = new ArrayList<>();
        CacheParamDTO cacheParam = cacheService.getCacheParam(partnerId, STATION_STATUS_SEARCH.value);

        if (Boolean.TRUE.equals(cacheService.useCache()) && cacheParam != null) {
            try {
                stationStatuses = stationStatusCacheManager.getAllStationStatusFromCache(partnerId, stationId);
                log.debug("get stations status from cache");

            } catch (Exception e) {
                String msg = CommonUtils.placeholderFormat(GET_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, "stationsStatus");
                this.exceptionHandler(e, msg, partnerId);
            }
        } else {

            Map<String, String> params = new HashMap<>();
            if (StringUtils.isNotBlank(stationId)) {
                params.put(STATION_ID, stationId);
            }

            List<Object> response = (List<Object>) this.getRooting(partnerId, STATION_STATUS_SEARCH.value, Optional.empty(), params);
            if (response == null) {
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_PARTNER_STATIONS_STATUS_PATH, PARTNER_ID, String.valueOf(partnerId))));
            }


            try {
                ObjectMapper mapper = new ObjectMapper();
                List<StationStatus> convertedResponse = mapper.convertValue(response, new TypeReference<List<StationStatus>>() {
                });
                for (StationStatus stationStatus : convertedResponse) {
                    if (stationStatus.getStationId().equals(stationId) || stationId == null) {
                        stationStatuses.add(stationStatus);
                    }
                }
            } catch (Exception e) {
                this.exceptionHandler(e, GET_PARTNER_STATIONS_STATUS_PATH, partnerId);
            }
        }
        return stationStatuses;
    }

    @Override
    public List<Asset> getAssets(UUID partnerId) {
        validityUtils.checkPartnerId(partnerId);
        List<Asset> assets = new ArrayList<>();
        CacheParamDTO cacheParam = cacheService.getCacheParam(partnerId, ASSET_SEARCH.value);
        if (Boolean.TRUE.equals(cacheService.useCache()) && cacheParam != null) {
            try {
                assets = assetCacheManager.getAllAssetFromCache(partnerId);
                log.debug("get assets from cache");
            } catch (Exception e) {
                String msg = CommonUtils.placeholderFormat(GET_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, "assets");
                this.exceptionHandler(e, msg, partnerId);
            }
        } else {
            List<Object> response = (List<Object>) this.getRooting(partnerId, ASSET_SEARCH.value, Optional.empty(), null);
            if (response == null) {
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_PARTNER_STATIONS_STATUS_PATH, PARTNER_ID, String.valueOf(partnerId))));
            }

            try {
                ObjectMapper mapper = new ObjectMapper();
                List<Asset> convertedResponse = mapper.convertValue(response, new TypeReference<List<Asset>>() {
                });
                for (Asset asset : convertedResponse) {
                    assets.add(asset);
                }
            } catch (Exception e) {
                this.exceptionHandler(e, GET_ASSETS_BY_PARTNER_ID, partnerId);
            }
        }
        return assets;

    }


    /**
     * prepare the list of calls
     *
     * @param actionId action identifier
     * @return list of partnerCallsDTO in the database related to this action
     */
    private List<PartnerCallsDTO> getCalls(UUID actionId) {
        String initialOutputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));
        
        List<PartnerCallsDTO> partnerBusinessCalls;
        String urlGetCallsByActionId = dataApiUri + CommonUtils.placeholderFormat(GET_CALLS_PATH + GET_BY_ACTIONS_ID_PATH, ACTION_ID_PARAM, String.valueOf(actionId));

        try {
            ResponseEntity<PartnerCallsDTO[]> partnerCallsDTO = restTemplate.exchange(urlGetCallsByActionId, HttpMethod.GET, CommonUtils.setHeaders(), PartnerCallsDTO[].class);
            partnerBusinessCalls = Arrays.asList(Objects.requireNonNull(partnerCallsDTO.getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } finally {
            CallUtils.saveOutputStandardInCallThread(initialOutputStandard);
        }
        return partnerBusinessCalls;
    }


    /**
     * prepare the standard
     *
     * @param actionName action name
     * @param partnerId  action identifier
     * @return list of partnerCallsDTO in the database related to this action
     */
    private PartnerStandardDTO getStandard(UUID partnerId, String actionName) {
        String initialOutputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));

        PartnerStandardDTO partnerStandard;
        String urlGetStandard = dataApiUri + ("/partner-standards?partnerId=" + partnerId + "&partnerActionsName=" + actionName);

        try {
            ResponseEntity<PartnerStandardDTO[]> standard = restTemplate.exchange(urlGetStandard, HttpMethod.GET, CommonUtils.setHeaders(), PartnerStandardDTO[].class);
            partnerStandard = Objects.requireNonNull(standard.getBody())[0];
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetStandard));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetStandard));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetStandard));
        } finally {
            CallUtils.saveOutputStandardInCallThread(initialOutputStandard);
        }
        return partnerStandard;
    }


    private String findKeyInParamsMap(String key, Map<String, String> paramsValue) {

        String foundedValue = null;
        if (paramsValue != null) {
            for (Map.Entry<String, String> entry : paramsValue.entrySet()) {
                if (entry.getKey().equals(key)) {
                    foundedValue = entry.getValue();
                    break;
                }
            }
        }

        return foundedValue;
    }


    @Override
    public List<AssetType> getAvailableAssets(UUID partnerId, String stationId, Float lon, Float lat, Float rad) {
        validityUtils.checkPartnerId(partnerId);
        this.checkPositionParams(lon, lat, rad, null);

        String msg = CommonUtils.placeholderFormat(GET_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, "AvailableAsset");
        List<AssetType> assetTypes = new ArrayList<>();
        CacheParamDTO cacheParam = cacheService.getCacheParam(partnerId, AVAILABLE_ASSET_SEARCH.value);

        if (Boolean.TRUE.equals(cacheService.useCache()) && cacheParam != null) {
            if (lat != null && lon != null && rad != null) {
                try {
                    assetTypes = assetTypeCacheManager.getAllAssetTypeFromCacheByGeoParams(partnerId, stationId, (double) lon, (double) lat, rad, RedisGeoCommands.DistanceUnit.METERS);
                    log.debug("get Available assets from cache vy geo params");
                } catch (Exception e) {
                    this.exceptionHandler(e, msg, partnerId);
                }
            } else {

                List<PartnerCallsDTO> calls = this.getPartnerCalls(partnerId, AVAILABLE_ASSET_SEARCH.value);

                List<ParamsDTO> params;
                for (PartnerCallsDTO callDto : calls) {

                    params = new ArrayList<>(callDto.getParams());

                    if (!CollectionUtils.isEmpty(params)) {

                        this.checkRequirementOfParams(params, lon, lat, rad);

                        if (lat == null && lon == null && rad == null) {
                            try {
                                assetTypes = assetTypeCacheManager.getAllAssetTypeFromCache(partnerId, stationId);
                                log.debug("get Available assets from cache");
                            } catch (Exception e) {
                                this.exceptionHandler(e, msg, partnerId);
                            }

                        } else {
                            throw new BadRequestException(CommonUtils.placeholderFormat(IMPOSSIBLE_GEOLOCALISATION));

                        }
                    }
                }
            }
        } else {

            Map<String, String> params = new HashMap<>();
            if (StringUtils.isNotBlank(stationId)) {
                params.put(STATION_ID, (stationId));
            }
            if (lon != null) {
                params.put(LON, String.valueOf(lon));
            }
            if (lat != null) {
                params.put(LAT, String.valueOf(lat));
            }
            if (rad != null) {
                params.put(RAD, String.valueOf(rad));
            }

            List<Object> response = (List<Object>) this.getRooting(partnerId, AVAILABLE_ASSET_SEARCH.value, Optional.empty(), params);
            if (response == null) {
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_PARTNER_AVAILABLE_ASSETS_PATH, PARTNER_ID, String.valueOf(partnerId))));
            }

            try {

                ObjectMapper mapper = new ObjectMapper();
                List<AssetType> convertedResponse = mapper.convertValue(response, new TypeReference<List<AssetType>>() {
                });
                for (AssetType assetType : convertedResponse) {
                    if (assetType.getStationId().equals(stationId) || stationId == null) {
                        assetTypes.add(assetType);
                    }
                }
            } catch (Exception e) {
                this.exceptionHandler(e, GET_PARTNERS_PATH + GET_PARTNER_AVAILABLE_ASSETS_PATH, partnerId);
            }
        }
        return assetTypes;
    }

    @Override
    public GlobalView getGlobalView() {
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        List<PartnerMeta> partnerMetas = partnerService.getPartnersMetaByType(PartnerTypeEnum.MSP);

        GlobalView globalView = new GlobalView();
        List<UUID> partnerIds = new ArrayList<>();
        partnerMetas.stream().forEach(partnerMeta -> partnerIds.add(partnerMeta.getPartnerId()));

        for (UUID partnerId : partnerIds) {

            try {
                CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
                List<Station> stations = this.getStations(partnerId, null, null, null);
                if (stations != null) {
                    globalView.getStations().addAll(stations);
                }
            } catch (Exception e) {
                log.error(IGNORED + e.getMessage());
            } finally {
                CallUtils.saveOutputStandardInCallThread(outputStandard);
            }

            try {
                CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
                List<StationStatus> stationsStatus = this.getStationStatus(partnerId, null);
                if (stationsStatus != null) {
                    globalView.getStationsStatus().addAll(stationsStatus);
                }
            } catch (Exception e) {
                log.error(IGNORED + e.getMessage());
            } finally {
                CallUtils.saveOutputStandardInCallThread(outputStandard);
            }

            try {
                CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
                List<Asset> assets = this.getAssets(partnerId);
                if (assets != null) {
                    globalView.getAssets().addAll(assets);
                }
            } catch (Exception e) {
                log.error(IGNORED + e.getMessage());
            } finally {
                CallUtils.saveOutputStandardInCallThread(outputStandard);
            }

            //TODO : same way, add list of parkings when PARKINGS_SEARCH action will exist

            // for msp directly providing around-me action
            try {
                CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
                PartnerAroundMeRequest partnerAroundMeRequest = new PartnerAroundMeRequest();
                partnerAroundMeRequest.setPartnersIds(partnerIds);
                Map<String, Object> body = this.validateBodyRequest(partnerAroundMeRequest);
                List<Object> response = (List<Object>) this.getRooting(partnerId, AROUND_ME_SEARCH.value, Optional.of(body), null);
                ObjectMapper mapper = new ObjectMapper();

                List<GlobalView> globalViews = mapper.convertValue(response, new TypeReference<List<GlobalView>>() {
                });
                globalViews.stream().forEach(element -> {
                    globalView.getParkings().addAll(element.getParkings());
                    globalView.getAssets().addAll(element.getAssets());
                    globalView.getStationsStatus().addAll(element.getStationsStatus());
                    globalView.getStations().addAll(element.getStations());
                });
            } catch (Exception e) {
                log.error(IGNORED + e.getMessage());
            } finally {
                CallUtils.saveOutputStandardInCallThread(outputStandard);
            }


        }

        CallUtils.saveOutputStandardInCallThread(outputStandard);
        return globalView;
    }

    /**
     * Validate Body request
     *
     * @param partnerAroundMeRequest
     * @return
     */
    private Map<String, Object> validateBodyRequest(PartnerAroundMeRequest partnerAroundMeRequest) {
        Map<String, Object> body = new HashMap<>();
        if (partnerAroundMeRequest.getPartnersIds() != null) {
            body.put("partnerIds", partnerAroundMeRequest.getPartnersIds());
        }
        if (partnerAroundMeRequest.getLat() != null) {
            body.put("lat", partnerAroundMeRequest.getLat());

        }
        if (partnerAroundMeRequest.getLon() != null) {
            body.put("lon", partnerAroundMeRequest.getLon());
        }
        if (partnerAroundMeRequest.getRadius() != null) {
            body.put("radius", partnerAroundMeRequest.getRadius());
        }
        if (partnerAroundMeRequest.getMaxResult() != null) {
            body.put("maxResult", partnerAroundMeRequest.getMaxResult());
        }
        return body;
    }

    @Override
    public GlobalView getAroundMe(PartnerAroundMeRequest partnerAroundMeRequest) {
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        if (partnerAroundMeRequest.getPartnersIds() != null) {
            for (UUID partnerId : partnerAroundMeRequest.getPartnersIds()) {
                validityUtils.checkPartnerId(partnerId);
            }
        }
        this.checkPositionParams(partnerAroundMeRequest.getLon(), partnerAroundMeRequest.getLat(), partnerAroundMeRequest.getRadius(), partnerAroundMeRequest.getMaxResult());

        Map<String, Object> body = this.validateBodyRequest(partnerAroundMeRequest);
        GlobalView globalView = new GlobalView();
        //Infos around me for selected msps

        List<UUID> partnerIds = partnerAroundMeRequest.getPartnersIds();

        List<UUID> mspIds = new ArrayList<>();

        if (partnerIds != null && !partnerIds.isEmpty()) {
            for (UUID partnerId : partnerIds) {
                // retrieve the partner
                PartnerMeta partnerMeta = partnerService.getPartnerMeta(partnerId);
                // check if the partner is a msp
                if (partnerMeta.getPartnerType() != null && partnerMeta.getPartnerType().equals(PartnerTypeEnum.MSP)) {
                    mspIds.add(partnerMeta.getPartnerId());
                } else {
                    throw new NotFoundException(CommonUtils.placeholderFormat(PARTNER_META_DOES_NOT_CORRESPOND_TO_PARTNER, FIRST_PLACEHOLDER, partnerMeta.getPartnerId().toString()));
                }
            }
        } else {
            List<PartnerMeta> allMsps = partnerService.getPartnersMetaByType(PartnerTypeEnum.MSP);
            for (PartnerMeta msp : allMsps) {
                mspIds.add(msp.getPartnerId());
            }
        }

        if (!mspIds.isEmpty()) {
            for (UUID partnerId : mspIds) {

                // for partner directly providing AROUND_ME action
                try {
                    CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
                    List<Object> response = (List<Object>) this.getRooting(partnerId, AROUND_ME_SEARCH.value, Optional.of(body), null);
                    ObjectMapper mapper = new ObjectMapper();

                    List<GlobalView> globalViews = mapper.convertValue(response, new TypeReference<List<GlobalView>>() {
                    });
                    globalViews.stream().forEach(element -> {
                        globalView.getParkings().addAll(element.getParkings());
                        globalView.getAssets().addAll(element.getAssets());
                        globalView.getStationsStatus().addAll(element.getStationsStatus());
                        globalView.getStations().addAll(element.getStations());
                    });
                } catch (Exception e) {
                    if (e.getMessage().contains(NO_ACTIVE_ACTION_FOUND)) {
                        log.error(IGNORED + e.getMessage());
                    } else {
                        this.exceptionHandler(e, GET_PARTNERS_PATH + GET_PARTNERS_AROUND_ME_PATH + FOR_PARTNER_WITH_ID_PARTNER_ID, partnerId);
                    }
                } finally {
                    CallUtils.saveOutputStandardInCallThread(outputStandard);
                }


                // concat alls stations, stationsStatus and assets of partners around
                List<Station> stations = new ArrayList<>();
                try {
                    CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
                    stations = this.getStations(partnerId, partnerAroundMeRequest.getLon(), partnerAroundMeRequest.getLat(), partnerAroundMeRequest.getRadius());
                    if (stations != null) {
                        globalView.getStations().addAll(stations);
                    }
                } catch (Exception e) {
                    if (e.getMessage().contains(NO_ACTIVE_ACTION_FOUND)) {
                        log.error(IGNORED + e.getMessage());
                    } else {
                        this.exceptionHandler(e, GET_PARTNERS_PATH + GET_PARTNERS_AROUND_ME_PATH + FOR_PARTNER_WITH_ID_PARTNER_ID, partnerId);
                    }
                } finally {
                    CallUtils.saveOutputStandardInCallThread(outputStandard);
                }

                List<String> stationsIds = new ArrayList<>();
                if (stations != null && !stations.isEmpty()) {
                    stations.stream().forEach(station -> stationsIds.add(station.getStationId()));
                }

                try {
                    CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
                    if (!stationsIds.isEmpty()) {
                        for (String stationId : stationsIds) {
                            List<StationStatus> stationsStatus = this.getStationStatus(partnerId, stationId);
                            if (stationsStatus != null) {
                                globalView.getStationsStatus().addAll(stationsStatus);
                            }
                        }
                    }
                } catch (Exception e) {
                    if (e.getMessage().contains(NO_ACTIVE_ACTION_FOUND)) {
                        log.error(IGNORED + e.getMessage());
                    } else {
                        this.exceptionHandler(e, GET_PARTNERS_PATH + GET_PARTNERS_AROUND_ME_PATH + FOR_PARTNER_WITH_ID_PARTNER_ID, partnerId);
                    }
                } finally {
                    CallUtils.saveOutputStandardInCallThread(outputStandard);
                }

                try {
                    CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
                    List<Asset> assets = this.getAssets(partnerId);
                    List<Asset> filteredAssets = new ArrayList<>();
                    for (Asset asset : assets) {
                        if (stationsIds.contains(asset.getHomeStationId())) {
                            filteredAssets.add(asset);
                        }
                    }
                    if (!filteredAssets.isEmpty()) {
                        globalView.getAssets().addAll(filteredAssets);
                    }
                } catch (Exception e) {
                    if (e.getMessage().contains(NO_ACTIVE_ACTION_FOUND)) {
                        log.error(IGNORED + e.getMessage());
                    } else {
                        this.exceptionHandler(e, GET_PARTNERS_PATH + GET_PARTNERS_AROUND_ME_PATH + FOR_PARTNER_WITH_ID_PARTNER_ID, partnerId);
                    }
                } finally {
                    CallUtils.saveOutputStandardInCallThread(outputStandard);
                }

                //TODO : same way, add list of parkings when PARKINGS_SEARCH action will exist

            }
        }

        CallUtils.saveOutputStandardInCallThread(outputStandard);
        return globalView;

    }

    @Override
    public List<VehicleTypes> getVehicleTypes(UUID partnerId) {
        validityUtils.checkPartnerId(partnerId);
        List<VehicleTypes> vehicleTypes = new ArrayList<>();
        CacheParamDTO cacheParam = cacheService.getCacheParam(partnerId, VEHICLE_TYPES_SEARCH.value);

        if (Boolean.TRUE.equals(cacheService.useCache()) && cacheParam != null) {
            try {
                vehicleTypes = vehicleTypesCacheManager.getAllVehicleTypesFromCache(partnerId);
                log.debug("get vehicle type from cache");
            } catch (Exception e) {
                String msg = CommonUtils.placeholderFormat(GET_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, "VehicleTypes");
                this.exceptionHandler(e, msg, partnerId);
            }
        } else {
            List<Object> response = (List<Object>) this.getRooting(partnerId, VEHICLE_TYPES_SEARCH.value, Optional.empty(), null);

            try {
                if (response == null) {
                    throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_PARTNERS_PATH + GET_PARTNER_VEHICLE_TYPES_PATH, PARTNER_ID, String.valueOf(partnerId))));
                }
                ObjectMapper mapper = new ObjectMapper();
                List<VehicleTypes> responseList = mapper.convertValue(response, new TypeReference<List<VehicleTypes>>() {
                });
                for (VehicleTypes types : responseList) {
                    vehicleTypes.add(types);
                }

            } catch (NotFoundException e) {
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_PARTNERS_PATH + GET_PARTNER_VEHICLE_TYPES_PATH, PARTNER_ID, String.valueOf(partnerId))));
            } catch (Exception e) {
                this.exceptionHandler(e, GET_PARTNERS_PATH + GET_PARTNER_VEHICLE_TYPES_PATH, partnerId);
            }
        }

        return vehicleTypes;

    }

    @Override
    public List<PriceList> getPricingPlan(UUID partnerId, String stationId) {
        validityUtils.checkPartnerId(partnerId);
        List<PriceList> priceList = new ArrayList<>();
        List<PriceListDTO> priceListDTO;

        CacheParamDTO cacheParam = cacheService.getCacheParam(partnerId, PRICING_PLAN_SEARCH.value);

        if (Boolean.TRUE.equals(cacheService.useCache()) && cacheParam != null) {
            try {
                priceListDTO = priceListCacheManager.getAllPriceListFromCache(partnerId, stationId);
                priceList = priceMapper.mapDataApiDtoToApiDto(priceListDTO);
                log.debug("get price list from cache");
            } catch (Exception e) {
                String msg = CommonUtils.placeholderFormat(GET_FROM_CACHE_ERREUR, FIRST_PLACEHOLDER, "priceList");
                this.exceptionHandler(e, msg, partnerId);
            }
        } else {

            Map<String, String> params = new HashMap<>();
            if (StringUtils.isNotBlank(stationId)) {
                params.put(STATION_ID, stationId);
            }

            List<Object> response = (List<Object>) this.getRooting(partnerId, PRICING_PLAN_SEARCH.value, Optional.empty(), params);

            String msg = GET_PARTNERS_PATH + GET_PARTNER_PRICING_PLAN_PATH + (stationId != null ? CommonUtils.placeholderFormat(GET_STATION_ID_PATH, STATION_ID, stationId) : StringUtils.EMPTY);
            if (response == null) {
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(msg, PARTNER_ID, String.valueOf(partnerId))));
            }
            try {

                ObjectMapper mapper = new ObjectMapper();
                priceListDTO = mapper.convertValue(response, new TypeReference<List<PriceListDTO>>() {
                });
                priceList = priceMapper.mapDataApiDtoToApiDto(priceListDTO);

            } catch (Exception e) {
                this.exceptionHandler(e, msg, partnerId);
            }
        }
        return priceList;
    }

    /**
     * Call routing
     *
     * @param partnerId
     * @param actionName
     * @param body
     * @param params
     * @return
     */
    private Object getRooting(UUID partnerId, String actionName, Optional<Map<String, Object>> body, Map<String, String> params) {
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));

        String partnerMetaIdValue = partnerId != null ? partnerId.toString() : null;
        Object partnerBusinessResponse = null;
        String urlCall = routingApiUri + CommonUtils.placeholderFormat(GET_PARTNER_ID_PATH, PARTNER_ID, partnerMetaIdValue + GET_ACTION_NAME_PATH, ACTION_NAME, actionName);
        String urlTemplate = CommonUtils.constructUrlTemplate(urlCall, params);
        HttpEntity<Optional<Map<String, Object>>> entity = new HttpEntity<>(body, CommonUtils.setHeaders().getHeaders());
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
     * Return a customed exception with initial error message if it exists or a customed global one
     *
     * @param e
     * @param serviceCalledMsg
     * @param partnerId
     */
    private void exceptionHandler(Exception e, String serviceCalledMsg, UUID partnerId) {
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));
        if (e.getMessage() != null) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new InternalException(e.getMessage());
        } else if (StringUtils.isNoneEmpty(serviceCalledMsg)) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(serviceCalledMsg, PARTNER_ID, String.valueOf(partnerId))));
        } else {
            throw new InternalException(errorMessages.getTechnicalGenericDescription());
        }
    }

    private Map<String, String> getJourneysParams(Float departureLat, Float departureLng, Float arrivalLat, Float
            arrivalLng, Integer departureDate, Integer timeDelta, Float departureRadius, Float arrivalRadius, Integer count) {
        Map<String, String> journeysParams = new HashMap<>();
        if (departureLat != null) {
            journeysParams.put("departureLat", String.valueOf(departureLat));
        }
        if (departureLng != null) {
            journeysParams.put("departureLng", String.valueOf(departureLng));
        }
        if (arrivalLat != null) {
            journeysParams.put("arrivalLat", String.valueOf(arrivalLat));
        }
        if (arrivalLng != null) {
            journeysParams.put("arrivalLng", String.valueOf(arrivalLng));
        }
        if (departureDate != null) {
            journeysParams.put("departureDate", String.valueOf(departureDate));
        }
        if (timeDelta != null) {
            journeysParams.put("timeDelta", String.valueOf(timeDelta));
        }
        if (departureRadius != null) {
            journeysParams.put("departureRadius", String.valueOf(departureRadius));
        }
        if (arrivalRadius != null) {
            journeysParams.put("arrivalRadius", String.valueOf(arrivalRadius));
        }
        if (count != null) {
            journeysParams.put("count", String.valueOf(count));
        }
        return journeysParams;
    }


    /**
     * check the validity of input params for carpool iv endpoints
     */
    private void checkIvCarpoolingParams(Float departureLat, Float arrivalLat,
                                         Float departureLng, Float arrivalLng,
                                         Float departureRadius, Float arrivalRadius,
                                         Integer maxResult, String departureTimeOfDay,
                                         List<String> departureWeekdays,
                                         Integer minDepartureDate, Integer maxDepartureDate) {

        List<String> messages = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<Integer> errorCodes = new ArrayList<>();

        if (departureLat != null) {
            ValidityUtils.checkLatitude(messages, titles, errorCodes, Double.valueOf(departureLat));
        }
        if (arrivalLat != null) {
            ValidityUtils.checkLatitude(messages, titles, errorCodes, Double.valueOf(arrivalLat));
        }
        if (departureLng != null) {
            ValidityUtils.checkLongitude(messages, titles, errorCodes, Double.valueOf(departureLng));
        }
        if (arrivalLng != null) {
            ValidityUtils.checkLongitude(messages, titles, errorCodes, Double.valueOf(arrivalLng));
        }

        ValidityUtils.checkIfParameterIsPositive(messages, titles, errorCodes, departureRadius, INVALID_RAD_MESSAGE_COVOIT, null, RADIUS_CODE);
        ValidityUtils.checkIfParameterIsPositive(messages, titles, errorCodes, arrivalRadius, INVALID_RAD_MESSAGE_COVOIT, null, RADIUS_CODE);

        if (maxResult != null) {
            ValidityUtils.checkIfParameterIsPositive(messages, titles, errorCodes, Float.valueOf(maxResult), INVALID_MAX_RESULT_COVOIT, null, MAX_RESULT_CODE);
        }

        ValidityUtils.checkTimeOfDay(messages, null, errorCodes, departureTimeOfDay);
        ValidityUtils.checkDayOfWeek(messages, null, errorCodes, departureWeekdays);
        ValidityUtils.checkMinAndMaxDepartureDate(messages, null, errorCodes, minDepartureDate, maxDepartureDate);

        if (!messages.isEmpty()) {
            String globalMessage = StringUtils.join(messages, " ");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            GenericError genericError = new GenericError();
            genericError.setErrorCode(errorCodes.get(0));
            genericError.setTimestamp(simpleDateFormat.format(new Date()));
            genericError.setDescription(globalMessage);
            genericError.setLabel(String.valueOf(titles));

            throw new BadRequestException(genericError);
        }
    }

    @Override
    public List<DriverJourney> getDriverJourneys(UUID partnerId, Float departureLat, Float departureLng,
                                                 Float arrivalLat, Float arrivalLng, Integer departureDate, Integer timeDelta,
                                                 Float departureRadius, Float arrivalRadius, Integer count) {

        validityUtils.checkPartnerId(partnerId);
        this.checkIvCarpoolingParams(departureLat, arrivalLat, departureLng, arrivalLng, departureRadius, arrivalRadius, count, null, null, null, null);

        Map<String, String> journeysParams = this.getJourneysParams(departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count);

        List<Object> response = (List<Object>) this.getRooting(partnerId, DRIVER_JOURNEYS_SEARCH.value, Optional.empty(), journeysParams);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_PARTNER_DRIVER_JOURNEY_PATH, PARTNER_ID, String.valueOf(partnerId))));
        }
        List<DriverJourney> driverJourneys = new ArrayList<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<DriverJourney> convertedResponse = mapper.convertValue(response, new TypeReference<List<DriverJourney>>() {
            });
            for (DriverJourney driverJourney : convertedResponse) {
                driverJourneys.add(driverJourney);
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GatewayApiPathDict.GET_PARTNER_DRIVER_JOURNEY_PATH, partnerId);
        }
        return driverJourneys;
    }


    @Override
    public List<PassengerJourney> getPassengerJourneys(UUID partnerId, Float departureLat, Float
            departureLng, Float arrivalLat, Float arrivalLng, Integer departureDate, Integer timeDelta, Float departureRadius, Float arrivalRadius, Integer count) {

        validityUtils.checkPartnerId(partnerId);
        this.checkIvCarpoolingParams(departureLat, arrivalLat, departureLng, arrivalLng, departureRadius, arrivalRadius, count, null, null, null, null);

        Map<String, String> journeysParams = this.getJourneysParams(departureLat, departureLng, arrivalLat, arrivalLng, departureDate, timeDelta, departureRadius, arrivalRadius, count);

        List<Object> response = (List<Object>) this.getRooting(partnerId, PASSENGER_JOURNEYS_SEARCH.value, Optional.empty(), journeysParams);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_PARTNER_DRIVER_JOURNEY_PATH, PARTNER_ID, String.valueOf(partnerId))));
        }
        List<PassengerJourney> passengerJourneys = new ArrayList<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<PassengerJourney> convertedResponse = mapper.convertValue(response, new TypeReference<List<PassengerJourney>>() {
            });

            for (PassengerJourney passengerJourneys1 : convertedResponse) {
                passengerJourneys.add(passengerJourneys1);
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GatewayApiPathDict.GET_PARTNER_PASSENGER_JOURNEY_PATH, partnerId);
        }
        return passengerJourneys;
    }


    /**
     * check validity of params in body message
     *
     * @param message
     */
    private void checkCarpoolingMessageParams(Message message) {
        List<String> messages = new ArrayList<>();
        List<Integer> errorCodes = new ArrayList<>();

        ValidityUtils.checkGrade(messages, null, errorCodes, message.getFrom().getGrade());
        ValidityUtils.checkGrade(messages, null, errorCodes, message.getTo().getGrade());
        if (!messages.isEmpty()) {
            String globalMessage = StringUtils.join(messages, " ");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            GenericError genericError = new GenericError();
            genericError.setErrorCode(errorCodes.get(0));
            genericError.setTimestamp(simpleDateFormat.format(new Date()));
            genericError.setDescription(globalMessage);
            throw new BadRequestException(genericError);

        }
    }


    @Override
    public void postMessage(UUID partnerId, Message message) {
        validityUtils.checkPartnerId(partnerId);
        this.checkCarpoolingMessageParams(message);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> body = mapper.convertValue(message, new TypeReference<Map<String, Object>>() {
        });
        this.getRooting(partnerId, SEND_MESSAGE.value, Optional.of(body), null);

    }


    private Map<String, String> getRegularTripsParams(Float departureLat, Float departureLng,
                                                      Float arrivalLat, Float arrivalLng,
                                                      String departureTimeOfDay,
                                                      List<String> departureWeekdays, Integer timeDelta,
                                                      Float departureRadius, Float arrivalRadius,
                                                      Integer minDepartureDate, Integer maxDepartureDate, Integer count) {

        Map<String, String> regularTripsParams = new HashMap<>();
        if (departureLat != null) {
            regularTripsParams.put("departureLat", String.valueOf(departureLat));
        }
        if (departureLng != null) {
            regularTripsParams.put("departureLng", String.valueOf(departureLng));
        }
        if (arrivalLat != null) {
            regularTripsParams.put("arrivalLat", String.valueOf(arrivalLat));
        }
        if (arrivalLng != null) {
            regularTripsParams.put("arrivalLng", String.valueOf(arrivalLng));
        }
        if (departureTimeOfDay != null) {
            regularTripsParams.put("departureTimeOfDay", String.valueOf(departureTimeOfDay));
        }

        if (departureWeekdays != null) {
            regularTripsParams.put("departureWeekdays", String.join(",", departureWeekdays));
        }
        if (timeDelta != null) {
            regularTripsParams.put("timeDelta", String.valueOf(timeDelta));
        }
        if (departureRadius != null) {
            regularTripsParams.put("departureRadius", String.valueOf(departureRadius));
        }
        if (arrivalRadius != null) {
            regularTripsParams.put("arrivalRadius", String.valueOf(arrivalRadius));
        }
        if (minDepartureDate != null) {
            regularTripsParams.put("minDepartureDate", String.valueOf(minDepartureDate));
        }
        if (maxDepartureDate != null) {
            regularTripsParams.put("maxDepartureDate", String.valueOf(maxDepartureDate));
        }
        if (count != null) {
            regularTripsParams.put("count", String.valueOf(count));
        }
        return regularTripsParams;
    }

    @Override
    public List<PassengerRegularTrip> getPassengerRegularTrips(UUID partnerId, Float departureLat, Float
            departureLng,
                                                               Float arrivalLat, Float arrivalLng,
                                                               String departureTimeOfDay,
                                                               List<String> departureWeekdays, Integer timeDelta,
                                                               Float departureRadius, Float arrivalRadius,
                                                               Integer minDepartureDate, Integer maxDepartureDate, Integer count) {

        validityUtils.checkPartnerId(partnerId);
        this.checkIvCarpoolingParams(departureLat, arrivalLat, departureLng, arrivalLng, departureRadius, arrivalRadius, count, departureTimeOfDay, departureWeekdays, minDepartureDate, maxDepartureDate);

        Map<String, String> regularTripsParams = this.getRegularTripsParams(departureLat, departureLng, arrivalLat, arrivalLng, departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count);

        List<Object> response = (List<Object>) this.getRooting(partnerId, PASSENGER_REGULAR_TRIPS_SEARCH.value, Optional.empty(), regularTripsParams);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_PARTNER_DRIVER_JOURNEY_PATH, PARTNER_ID, String.valueOf(partnerId))));
        }
        List<PassengerRegularTrip> passengerRegularTrips = new ArrayList<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<PassengerRegularTrip> convertedResponse = mapper.convertValue(response, new TypeReference<List<PassengerRegularTrip>>() {
            });
            for (PassengerRegularTrip passengerRegularTrip : convertedResponse) {
                passengerRegularTrips.add(passengerRegularTrip);
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GET_PARTNER_PASSENGER_REGULAR_TRIPS_PATH, partnerId);
        }
        return passengerRegularTrips;
    }


    @Override
    public List<DriverRegularTrip> getDriverRegularTrips(UUID partnerId, Float departureLat, Float departureLng,
                                                         Float arrivalLat, Float arrivalLng,
                                                         String departureTimeOfDay, List<String> departureWeekdays,
                                                         Integer timeDelta,
                                                         Float departureRadius, Float arrivalRadius,
                                                         Integer minDepartureDate, Integer maxDepartureDate, Integer count) {

        validityUtils.checkPartnerId(partnerId);
        this.checkIvCarpoolingParams(departureLat, arrivalLat, departureLng, arrivalLng, departureRadius, arrivalRadius, count, departureTimeOfDay, departureWeekdays, minDepartureDate, maxDepartureDate);

        Map<String, String> regularTripsParams = this.getRegularTripsParams(departureLat, departureLng, arrivalLat, arrivalLng, departureTimeOfDay, departureWeekdays, timeDelta, departureRadius, arrivalRadius, minDepartureDate, maxDepartureDate, count);

        List<Object> response = (List<Object>) this.getRooting(partnerId, DRIVER_REGULAR_TRIPS_SEARCH.value, Optional.empty(), regularTripsParams);
        if (response == null) {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(GET_PARTNER_DRIVER_JOURNEY_PATH, PARTNER_ID, String.valueOf(partnerId))));
        }
        List<DriverRegularTrip> driverRegularTrips = new ArrayList<>();
        try {

            ObjectMapper mapper = new ObjectMapper();
            List<DriverRegularTrip> convertedResponse = mapper.convertValue(response, new TypeReference<List<DriverRegularTrip>>() {
            });
            for (DriverRegularTrip driverRegularTrip : convertedResponse) {
                driverRegularTrips.add(driverRegularTrip);
            }
        } catch (Exception e) {
            this.exceptionHandler(e, GET_PARTNER_DRIVER_REGULAR_TRIPS_PATH, partnerId);
        }
        return driverRegularTrips;
    }


}
