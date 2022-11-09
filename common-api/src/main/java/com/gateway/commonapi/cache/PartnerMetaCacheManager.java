package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.PartnerMetaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.gateway.commonapi.constants.GlobalConstants.CACHE_PARTNERS_METAS_TTL;


/**
 * Cache Manager for partner meta operation
 */
@Slf4j
@Component
public class PartnerMetaCacheManager extends CacheManager<PartnerMetaDTO> {

    public static final String GET_META_SERVICE_KEY_PREFIX = "getMeta";

    @Autowired
    private CacheUtil<String, PartnerMetaDTO> partnerCacheUtil;

    /**
     * Retrieve all partner from cache
     *
     * @return List of partnerMetaDTO
     */
    public List<PartnerMetaDTO> getAllPartnersFromCache() {

        List<PartnerMetaDTO> partners;
        Set<String> keys = partnerCacheUtil.getKeysByPrefix(GET_META_SERVICE_KEY_PREFIX);

        List<String> keysByIdWithoutPrefix = new ArrayList<>();
        String keyPrefixEnvironment = keyPrefixEnv + GlobalConstants.SEPARATOR;
        for (String key : keys) {
            keysByIdWithoutPrefix.add(key.substring(keyPrefixEnvironment.length()));
        }
        partners = new ArrayList<>(partnerCacheUtil.getValues(keyPrefixEnvironment, keysByIdWithoutPrefix, PartnerMetaDTO.class).values());

        return partners;
    }


    /**
     * Get PartnerMetaDTO from cache base on the UUID of the Partner
     *
     * @param id of the Partner
     * @return PartnerMetaDTO
     */
    public PartnerMetaDTO getFromCache(UUID id) {
        return (PartnerMetaDTO) this.getFromCache(id, GET_META_SERVICE_KEY_PREFIX, PartnerMetaDTO.class);
    }

    /**
     * Populate Cache regarding a keyCache for the map (e.g : PartnerId+service)
     *
     * @param partners list of elements to add
     */
    public void populateCache(List<PartnerMetaDTO> partners) {
        partners.forEach(partner -> {
            try {
                // convert dto as string to store it in cache
                String partnerStringyfied = objectMapper.writeValueAsString(partner);
                // build the key cache
                String elementCacheKey = GET_META_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partner.getPartnerId().toString();
                // add element to the cache
                partnerCacheUtil.putValue(elementCacheKey, partnerStringyfied, CACHE_PARTNERS_METAS_TTL);
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
        this.clearCache(keyPattern, partnerCacheUtil);
    }

}
