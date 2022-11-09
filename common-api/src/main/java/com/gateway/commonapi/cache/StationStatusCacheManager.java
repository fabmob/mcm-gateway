package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.StationStatus;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Cache Manager for the Station status Types operation
 */
@Slf4j
@Component
public class StationStatusCacheManager extends CacheManager<StationStatus> {

    public static final String GET_STATION_STATUS_SERVICE_KEY_PREFIX = "getStationStatus";

    @Autowired
    private CacheUtil<String, StationStatus> cacheUtil;

    /**
     * Retrieve all stations from cache
     *
     * @return List of stations
     */
    public StationStatus getStationStatusFromCache(UUID id) {
        return (StationStatus) this.getFromCache(id, GET_STATION_STATUS_SERVICE_KEY_PREFIX, StationStatus.class);
    }

    /**
     * Retrieve all Station Status from cache
     *
     * @return List of stationStatus
     */
    public List<StationStatus> getAllStationStatusFromCache(UUID partnerId, String stationId) {
        List<StationStatus> stationStatus;
        Set<String> keys = cacheUtil.getKeysByPrefix(GET_STATION_STATUS_SERVICE_KEY_PREFIX);
        String keyPrefixEnvironment = keyPrefixEnv + GlobalConstants.SEPARATOR;
        if (stationId != null) {
            List<String> keysBypartnerAndStationId = keys.stream().filter(c -> c.equals(keyPrefixEnv + GlobalConstants.SEPARATOR + GET_STATION_STATUS_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString() + GlobalConstants.SEPARATOR + stationId)).collect(Collectors.toList());

            List<String> keysByIdWithoutPrefix = new ArrayList<>();
            for (String key : keysBypartnerAndStationId) {
                keysByIdWithoutPrefix.add(key.substring(keyPrefixEnvironment.length()));
            }
            stationStatus = new ArrayList<>(cacheUtil.getValues(keyPrefixEnvironment, keysByIdWithoutPrefix, StationStatus.class).values());

        } else {
            Set<String> keysById = cacheUtil.getKeysByPrefix(GET_STATION_STATUS_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString());

            List<String> keysByIdWithoutPrefix = new ArrayList<>();
            for (String key : keysById) {
                keysByIdWithoutPrefix.add(key.substring(keyPrefixEnvironment.length()));
            }
            stationStatus = new ArrayList<>(cacheUtil.getValues(keyPrefixEnvironment, keysByIdWithoutPrefix, StationStatus.class).values());
        }
        return stationStatus;
    }


    /**
     * Populate Cache regarding a keyCache for the map (e.g : PartnerId+service)
     *
     * @param stationsStatus list of elements to add
     */
    public void populateStationStatusCache(List<StationStatus> stationsStatus, CacheParamDTO cacheParamDTO) {
        stationsStatus.forEach(stationStatus -> {
            try {
                // convert dto as string to store it in cache
                String assetStringyfied = objectMapper.writeValueAsString(stationStatus);
                // build the key cache
                String elementCacheKey = GET_STATION_STATUS_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + stationStatus.getPartnerId().toString() + GlobalConstants.SEPARATOR + stationStatus.getStationId();
                // add element to the cache
                cacheUtil.putValue(elementCacheKey, assetStringyfied, cacheParamDTO.getHardTTL());
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        });
    }


    /**
     * Clear the cache based on a pattern passed. Env prefix will be automatically added to the key.
     *
     * @param keyPattern
     */
    public void clearCache(String keyPattern) {
        this.clearCache(keyPattern, cacheUtil);
    }
}
