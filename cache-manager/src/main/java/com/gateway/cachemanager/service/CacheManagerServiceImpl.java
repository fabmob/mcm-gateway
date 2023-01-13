package com.gateway.cachemanager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.cache.*;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.dto.api.geojson.Position;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.dto.data.GatewayParamsDTO;
import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import com.gateway.commonapi.dto.data.PriceListDTO;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.*;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.cache.CacheService;
import com.gateway.commonapi.utils.enums.ActionsEnum;
import com.gateway.commonapi.utils.enums.ZoneType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.*;

import static com.gateway.commonapi.cache.AssetCacheManager.GET_ASSET_SERVICE_KEY_PREFIX;
import static com.gateway.commonapi.cache.AssetTypeCacheManager.GET_ASSET_TYPE_SERVICE_KEY_PREFIX;
import static com.gateway.commonapi.cache.ParkingCacheManager.GET_PARKING_SERVICE_KEY_PREFIX;
import static com.gateway.commonapi.cache.PartnerMetaCacheManager.GET_META_SERVICE_KEY_PREFIX;
import static com.gateway.commonapi.cache.PriceListCacheManager.GET_PRICE_LIST_KEY_PREFIX;
import static com.gateway.commonapi.cache.StationCacheManager.GET_STATION_SERVICE_KEY_PREFIX;
import static com.gateway.commonapi.cache.StationStatusCacheManager.GET_STATION_STATUS_SERVICE_KEY_PREFIX;
import static com.gateway.commonapi.cache.VehicleTypesCacheManager.GET_VEHICLE_TYPE_SERVICE_KEY_PREFIX;
import static com.gateway.commonapi.cache.ZoneCacheManager.GET_ZONE_SERVICE_KEY_PREFIX;
import static com.gateway.commonapi.constants.CacheManagerDict.NOT_CACHE_ELEMENT;
import static com.gateway.commonapi.constants.DataApiPathDict.*;
import static com.gateway.commonapi.constants.GatewayApiPathDict.*;
import static com.gateway.commonapi.constants.GlobalConstants.BASE_ERROR_MESSAGE;
import static com.gateway.commonapi.constants.GlobalConstants.*;


@Slf4j
@Service
public class CacheManagerServiceImpl implements CacheManagerService {

    private RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    @Value("${gateway.service.dataapi.baseUrl}")
    private String dataApiUri;
    @Value("${gateway.service.routingapi.baseurl}")
    private String routingApiUri;
    @Autowired
    private ErrorMessages errorMessages;

    @Autowired
    private CacheService cacheService;

    @Autowired
    PartnerMetaCacheManager partnerMetaCache;

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
    ParkingCacheManager parkingCacheManager;

    @Autowired
    private ZoneCacheManager zoneCacheManager;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private GatewayParamStatusManager gatewayParamStatusManager;

    private static final String STAR_PREFIX = "*";


    @Override
    public void clearCache(List<UUID> partners) {

        if (partners == null || partners.isEmpty()) {
            partnerMetaCache.clearCache(STAR_PREFIX);
        } else {
            for (UUID partner : partners) {
                partnerMetaCache.clearCacheById(partner.toString());
            }
        }
    }

    @Override
    public boolean getCacheStatus() {
        return gatewayParamStatusManager.getCacheStatus();
    }

    @Override
    public boolean putCacheStatus(boolean isEnabled) {
        this.updateCacheGatewayParams(CACHE_ACTIVATION, String.valueOf(isEnabled));
        gatewayParamStatusManager.synchronizeCacheStatus();
        return gatewayParamStatusManager.getCacheStatus();
    }

    @Override
    public void refreshPartners() {
        List<PartnerMetaDTO> partnerMetaDTOsFromDB = this.getPartners();
        partnerMetaCache.clearCache(GET_META_SERVICE_KEY_PREFIX + SEPARATOR + STAR_PREFIX);
        partnerMetaCache.populateCache(partnerMetaDTOsFromDB);
    }

    @Override
    public void refresh(UUID partnerId, ActionsEnum actionName, List<Position> positions) {
        if (!ActionsEnum.isCacheableAction(actionName)) {
            throw new BadRequestException(NOT_CACHE_ELEMENT + actionName.value);
        }
        CacheParamDTO cacheParamDTO = cacheService.getCacheParam(partnerId, actionName.value);

        if (Objects.equals(actionName, ActionsEnum.MSP_ZONE_SEARCH)) {
            this.refreshPartnerArea(partnerId, cacheParamDTO);
        } else {
            List<Object> partnerResponse = new ArrayList<>();
            if (positions == null || positions.isEmpty()) {
                partnerResponse = (List<Object>) this.getRouting(partnerId, actionName.value, Optional.empty(), null);
            } else {
                for (Position position : positions) {
                    partnerResponse.addAll((List<Object>) this.getRouting(partnerId, actionName.value, Optional.empty(), this.paramsToMap(position)));
                }
            }

            ObjectMapper objectMapper = new ObjectMapper();
            switch (actionName) {
                case ASSET_SEARCH:
                    List<Asset> assets = objectMapper.convertValue(partnerResponse, new TypeReference<>() {
                    });
                    String assetsCacheKey = GET_ASSET_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString();
                    assetCacheManager.clearCache(assetsCacheKey + STAR_PREFIX);
                    assetCacheManager.populateCache(assets, cacheParamDTO);
                    break;
                case PARKING_SEARCH:
                    List<Parking> parkings = objectMapper.convertValue(partnerResponse, new TypeReference<>() {
                    });
                    String parkingsCacheKey = GET_PARKING_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString();
                    parkingCacheManager.clearCache(parkingsCacheKey + STAR_PREFIX);
                    parkingCacheManager.populateParkingCacheGeoData(parkings, partnerId, cacheParamDTO);
                    break;
                case STATION_SEARCH:
                    List<Station> stations = objectMapper.convertValue(partnerResponse, new TypeReference<>() {
                    });
                    String stationsCacheKey = GET_STATION_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString();
                    stationCacheManager.clearCache(stationsCacheKey + STAR_PREFIX);
                    stationCacheManager.populateStationsCache(stations, cacheParamDTO);
                    break;
                case PRICING_PLAN_SEARCH:
                    List<PriceListDTO> prices = objectMapper.convertValue(partnerResponse, new TypeReference<>() {
                    });
                    String priceCacheKey = GET_PRICE_LIST_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString();
                    priceListCacheManager.clearCache(priceCacheKey + STAR_PREFIX);
                    priceListCacheManager.populateCache(prices, partnerId, "", cacheParamDTO);
                    break;
                case VEHICLE_TYPES_SEARCH:
                    List<VehicleTypes> vehicleTypes = objectMapper.convertValue(partnerResponse, new TypeReference<>() {
                    });
                    String vehicleTypesCacheKey = GET_VEHICLE_TYPE_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString();
                    vehicleTypesCacheManager.clearCache(vehicleTypesCacheKey + STAR_PREFIX);
                    vehicleTypesCacheManager.populateCache(vehicleTypes, partnerId, cacheParamDTO);
                    break;
                case STATION_STATUS_SEARCH:
                    List<StationStatus> stationStatus = objectMapper.convertValue(partnerResponse, new TypeReference<>() {
                    });
                    String stationsStatusCacheKey = GET_STATION_STATUS_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString();
                    stationStatusCacheManager.clearCache(stationsStatusCacheKey + STAR_PREFIX);
                    stationStatusCacheManager.populateStationStatusCache(stationStatus, cacheParamDTO);
                    break;
                case AVAILABLE_ASSET_SEARCH:
                    List<AssetType> assetTypes = objectMapper.convertValue(partnerResponse, new TypeReference<>() {
                    });
                    String assetTypesCacheKey = GET_ASSET_TYPE_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString();
                    assetTypeCacheManager.clearCache(assetTypesCacheKey + STAR_PREFIX);
                    assetTypeCacheManager.populateAssetTypeCache(assetTypes, cacheParamDTO);
                    break;
                default:
                    throw new BadRequestException(NOT_CACHE_ELEMENT + actionName.value);

            }
        }

    }


    private void updateCacheGatewayParams(String key, String value) {
        log.info("Updating Gateway Param " + key + " in database and cache");
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);

        GatewayParamsDTO gatewayParamsDTO = new GatewayParamsDTO(key, value);
        HttpEntity<GatewayParamsDTO> entity = new HttpEntity<>(gatewayParamsDTO, httpHeaders);

        String urlPutGtwCacheParam = dataApiUri + GATEWAY_PARAMS_BASE_PATH + CommonUtils.placeholderFormat(GATEWAY_PARAM_PATH, PARAM_KEY, key);

        try {
            restTemplate.exchange(urlPutGtwCacheParam, HttpMethod.PUT, entity, Object.class);
            gatewayParamStatusManager.synchronizeCacheStatus();

        } catch (HttpClientErrorException.NotFound e) {
            //If corresponding Gateway param not found we create it
            log.info("Cache param not found, creating one ...");
            this.createCacheGatewayParams(key, value);
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlPutGtwCacheParam));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlPutGtwCacheParam));
        }

    }

    private void createCacheGatewayParams(String key, String value) {
        log.info("Creating Gateway Param " + key + " in database");
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);

        GatewayParamsDTO gatewayParamsDTO = new GatewayParamsDTO(key, value);
        HttpEntity<GatewayParamsDTO> entity = new HttpEntity<>(gatewayParamsDTO, httpHeaders);

        String urlPostGtwCacheParam = dataApiUri + GATEWAY_PARAMS_BASE_PATH;

        try {
            restTemplate.exchange(urlPostGtwCacheParam, HttpMethod.POST, entity, GatewayParamsDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlPostGtwCacheParam));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlPostGtwCacheParam));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlPostGtwCacheParam));
        }

    }

    private List<PartnerMetaDTO> getPartners() {

        List<PartnerMetaDTO> partnerMetaDTOList = new ArrayList<>();

        // get the correlationId of the current thread and forward as http header

        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        String urlGetMetas = dataApiUri + PARTNER_META_ENDPOINT;

        try {
            ResponseEntity<PartnerMetaDTO[]> dataApiResponse = restTemplate.exchange(urlGetMetas, HttpMethod.GET, entity, PartnerMetaDTO[].class);
            partnerMetaDTOList = Arrays.asList(Objects.requireNonNull(dataApiResponse.getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, REFRESH_CACHE_CALL_FAIL + MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(REFRESH_CACHE_CALL_FAIL + MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(REFRESH_CACHE_CALL_FAIL + MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMetas));
        }

        return partnerMetaDTOList;
    }

    private Map<String, String> paramsToMap(Position position) {
        Map<String, String> params = new HashMap<>();
        if (position != null) {
            Float lon = position.getLon();
            Float lat = position.getLat();
            Float rad = position.getRadius();
            if (lon != null) {
                params.put(LON, String.valueOf(lon));
            }
            if (lat != null) {
                params.put(LAT, String.valueOf(lat));
            }
            if (rad != null) {
                params.put(RAD, String.valueOf(rad));
            }
        }

        return params;
    }

    private void refreshPartnerArea(UUID partnerId, CacheParamDTO cacheParamDTO) {
        for (ZoneType areaType : ZoneType.values()) {
            Map<String, String> params = new HashMap<>();
            params.put(ZONE_TYPE, areaType.toString());
            List<Object> partnerResponse = (List<Object>) this.getRouting(partnerId, ActionsEnum.MSP_ZONE_SEARCH.value, Optional.empty(), params);

            ObjectMapper objectMapper = new ObjectMapper();
            List<PartnerZone> zones = objectMapper.convertValue(partnerResponse, new TypeReference<>() {
            });
            String zonesCacheKey = GET_ZONE_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString() + GlobalConstants.SEPARATOR + areaType.value;
            zoneCacheManager.clearCache(zonesCacheKey + STAR_PREFIX);
            zoneCacheManager.populateCache(zones, areaType, cacheParamDTO);

        }
    }

    /**
     * Call routing
     *
     * @param partnerId  partner unique identifier
     * @param actionName name of action
     * @param body       body
     * @param params     parameters
     * @return response
     */
    private Object getRouting(UUID partnerId, String actionName, Optional<Map<String, Object>> body, Map<String, String> params) {
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
                throw new NotFoundException(REFRESH_CACHE_CALL_FAIL + MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + error.getDescription());
            }
        } catch (HttpServerErrorException e) {
            log.error(ERROR_FOR_URL_CALL, urlTemplate, e.getMessage(), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                GenericError error = new GenericError(e.getResponseBodyAsString());
                throw new InternalException(REFRESH_CACHE_CALL_FAIL + MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + error.getDescription());
            }
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new BadGatewayException(REFRESH_CACHE_CALL_FAIL + MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + e.getMessage());
            }
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new UnavailableException(REFRESH_CACHE_CALL_FAIL + MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCall) + SEPARATOR + e.getMessage());
            }
        }

        return partnerBusinessResponse;
    }

}
