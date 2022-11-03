package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.PartnerZone;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.utils.enums.ZoneType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Cache Manager for partnerZone operation
 */
@Slf4j
@Component
public class ZoneCacheManager extends CacheManager<PartnerZone> {

    public static final String GET_ZONE_SERVICE_KEY_PREFIX = "getZone";

    @Autowired
    private CacheUtil<String, PartnerZone> cacheUtil;

    /**
     * Retrieve all partner zone from cache
     *
     * @return List of partnerMetaDTO
     */
    public PartnerZone getPartnerZoneFromCache(UUID id, ZoneType areaType) {
        String key = keyPrefixEnv + GlobalConstants.SEPARATOR + GET_ZONE_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + id + GlobalConstants.SEPARATOR + areaType.toString();
        return cacheUtil.getValue(key, PartnerZone.class);
    }

    /**
     * Populate Cache regarding a keyCache for the map (e.g : PartnerId+service)
     *
     * @param zones list of elements to add
     */
    public void populateCache(List<PartnerZone> zones, ZoneType areaType, CacheParamDTO cacheParamDTO) {
        zones.forEach(zone -> {
            try {
                // convert dto as string to store it in cache
                String zoneStringyfied = objectMapper.writeValueAsString(zone);
                // build the key cache
                String elementCacheKey = GET_ZONE_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + zone.getPartnerId().toString() + GlobalConstants.SEPARATOR + areaType.toString();

                // add element to the cache
                cacheUtil.putValue(elementCacheKey, zoneStringyfied, cacheParamDTO.getHardTTL());
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
