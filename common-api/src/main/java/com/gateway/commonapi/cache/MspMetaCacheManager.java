package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.data.MspMetaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Component
public class MspMetaCacheManager {

    private static final String GETMETA_SERVICE_KEY_PREFIX = "getMeta_";
    static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CacheUtil<String, MspMetaDTO> cacheUtil;

    /**
     * Retrieve all msp from cache
     *
     * @return List of MspMetaDTO
     */
    public List<MspMetaDTO> getAllMspsFromCache() {
        List<MspMetaDTO> msps = new ArrayList<>();
        Set<String> keys = cacheUtil.getKeysByPrefix(GETMETA_SERVICE_KEY_PREFIX);
        keys.forEach(key -> msps.add(cacheUtil.getValue(key, MspMetaDTO.class)));
        return msps;
    }

    /**
     * Get MspMetaDTO from cache base on the UUID of the msp
     *
     * @param id of the msp
     * @return MspMetaDTO
     */
    public MspMetaDTO getFromCache(UUID id) {
        return cacheUtil.getValue(GETMETA_SERVICE_KEY_PREFIX + id.toString(), MspMetaDTO.class);
    }

    /**
     * Populate Cache regarding a keyCache for the map (e.g : mspId+service)
     *
     * @param msps list of elements to add
     */
    public void populateCache(List<MspMetaDTO> msps) {
        msps.forEach(msp -> {
            try {
                // convert dto as string to store it in cache
                String mspStringyfied = objectMapper.writeValueAsString(msp);
                // build the key cache
                String elementCacheKey = GETMETA_SERVICE_KEY_PREFIX + msp.getMspId().toString();
                // add element to the cache
                cacheUtil.putValue(elementCacheKey, mspStringyfied);
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
        cacheUtil.clearCache(keyPattern);
    }
}
