package com.gateway.commonapi.cache;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.GatewayParamsDTO;
import com.gateway.commonapi.restConfig.RestConfig;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.gateway.commonapi.constants.DataApiPathDict.CACHE_ACTIVATION_BASE_PATH;
import static com.gateway.commonapi.constants.DataApiPathDict.GATEWAY_PARAMS_BASE_PATH;
import static com.gateway.commonapi.constants.GlobalConstants.CACHE_ACTIVATION;
import static com.gateway.commonapi.constants.GlobalConstants.DEFAULT_VALUE_CACHE_ACTIVATION;

/**
 * Cache Manager for GatewayParamStatus operation
 */
@Slf4j
@Component
public class GatewayParamStatusManager extends CacheManager<GatewayParamsDTO> {

    @Autowired
    private CacheUtil<String, GatewayParamsDTO> gatewayParamsDTOCacheUtil;
    public static final int ONE_DAY_IN_SECONDS = 86400;

    @Value("${gateway.service.dataapi.baseUrl}")
    private String uri;

    RestConfig restConfig = new RestConfig();
    RestTemplate restTemplate = restConfig.restTemplate();


    /**
     * Retrieve cache status in cache or synchronize with database if not found
     *
     * @return true or false
     */
    public Boolean getCacheStatus() {
        Boolean cacheStatus = null;
        String cacheStatusCache = null;
        String keyPrefixEnvironment = keyPrefixEnv + GlobalConstants.SEPARATOR;
        String keyPrefix = keyPrefixEnvironment.concat(CACHE_ACTIVATION);

        GatewayParamsDTO gatewayParamsDTO;
        gatewayParamsDTO = gatewayParamsDTOCacheUtil.getValue(keyPrefix, GatewayParamsDTO.class);

        if (gatewayParamsDTO != null && gatewayParamsDTO.getParamValue() != null) {
            cacheStatusCache = gatewayParamsDTO.getParamValue();
        }

        if (cacheStatusCache != null) {
            cacheStatus = Boolean.valueOf(cacheStatusCache);
        } else {
            log.info(CACHE_ACTIVATION + " key not found in cache... try to synchronize with database");
            this.synchronizeCacheStatus();
            gatewayParamsDTO = gatewayParamsDTOCacheUtil.getValue(keyPrefix, GatewayParamsDTO.class);
            if (gatewayParamsDTO != null) {
                cacheStatus = Boolean.valueOf(gatewayParamsDTO.getParamValue());
            }
            return cacheStatus;
        }

        return cacheStatus;
    }


    /**
     * Synchronize cache_activation value in the cache with the one in database. Or create values if not present in both
     */
    public void synchronizeCacheStatus() {

        String urlGetGatewayParamStatus = uri + GATEWAY_PARAMS_BASE_PATH + CACHE_ACTIVATION_BASE_PATH;
        ResponseEntity<GatewayParamsDTO> gatewayParamStatus;
        try {
            gatewayParamStatus = restTemplate.exchange(urlGetGatewayParamStatus, HttpMethod.GET, CommonUtils.setHeaders(), GatewayParamsDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.info(CACHE_ACTIVATION + " key not found in database ... creating one");
            String urlPostGatewayParamStatus = uri + GATEWAY_PARAMS_BASE_PATH;
            GatewayParamsDTO cacheStatus = new GatewayParamsDTO(CACHE_ACTIVATION, DEFAULT_VALUE_CACHE_ACTIVATION);
            gatewayParamStatus = restTemplate.postForEntity(urlPostGatewayParamStatus, cacheStatus, GatewayParamsDTO.class);
        }

        try {
            GatewayParamsDTO gatewayParam = Objects.requireNonNull(gatewayParamStatus.getBody());
            String value = gatewayParam.getParamValue();
            log.info("Pushing Cache Status with value " + value + " in cache");
            this.populateCacheActivation(gatewayParam, ONE_DAY_IN_SECONDS);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * Populate Cache with a cache activation status using CACHE_ACTIVATION key
     *
     * @param gatewayParamsDTO
     * @param hardTTL
     */
    public void populateCacheActivation(GatewayParamsDTO gatewayParamsDTO, Integer hardTTL) {
        try {
            // convert dto as string to store it in cache
            String gatewayParamsDTOStringyfied = objectMapper.writeValueAsString(gatewayParamsDTO);
            // add element to the cache
            gatewayParamsDTOCacheUtil.putValue(CACHE_ACTIVATION, gatewayParamsDTOStringyfied, hardTTL);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Clear cache status in cache
     *
     * @param keyPattern
     */
    public void clearCache(String keyPattern) {
        this.clearCache(keyPattern, gatewayParamsDTOCacheUtil);
    }

}



