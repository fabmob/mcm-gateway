package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.dto.data.PriceListDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Cache Manager for pricing operation
 */
@Slf4j
@Component
public class PriceListCacheManager extends CacheManager<PriceListDTO> {

    public static final String GET_PRICE_LIST_KEY_PREFIX = "getPriceList";

    @Autowired
    private CacheUtil<String, PriceListDTO> cacheUtil;

    /**
     * Retrieve all PriceList  from cache
     *
     * @return List of stations
     */
    public PriceListDTO getPriceListFromCache(UUID id) {
        return (PriceListDTO) this.getFromCache(id, GET_PRICE_LIST_KEY_PREFIX, PriceListDTO.class);
    }

    /**
     * Retrieve all price List from cache
     *
     * @return List of priceList
     */
    public List<PriceListDTO> getAllPriceListFromCache(UUID partnerId, String stationId) {
        List<PriceListDTO> priceList;
        String keyPrefixEnvironment = keyPrefixEnv + GlobalConstants.SEPARATOR;

        Set<String> keysById = cacheUtil.getKeysByPrefix(GET_PRICE_LIST_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString());

        if (stationId != null) {
            List<String> keysByPartnerAndStationId = keysById.stream().filter(c -> c.contains(GlobalConstants.SEPARATOR + stationId)).collect(Collectors.toList());

            List<String> keysByIdWithoutPrefix = new ArrayList<>();
            for (String key : keysByPartnerAndStationId) {
                keysByIdWithoutPrefix.add(key.substring(keyPrefixEnvironment.length()));
            }
            priceList = new ArrayList<>(cacheUtil.getValues(keyPrefixEnvironment, keysByIdWithoutPrefix, PriceListDTO.class).values());
        } else {

            List<String> keysByIdWithoutPrefix = new ArrayList<>();
            for (String key : keysById) {
                keysByIdWithoutPrefix.add(key.substring(keyPrefixEnvironment.length()));
            }
            priceList = new ArrayList<>(cacheUtil.getValues(keyPrefixEnvironment, keysByIdWithoutPrefix, PriceListDTO.class).values());
        }
        return priceList;
    }

    /**
     * Populate Cache regarding a keyCache for the map (e.g : PartnerId+service)
     *
     * @param priceList list of elements to add
     */
    public void populateCache(List<PriceListDTO> priceList, UUID partnerId, String stationId, CacheParamDTO cacheParamDTO) {
        priceList.forEach(price -> {
            try {
                // convert dto as string to store it in cache
                String assetStringyfied = objectMapper.writeValueAsString(price);
                // build the key cache
                String stationFilter = stationId != null ? GlobalConstants.SEPARATOR + stationId : "";
                String elementCacheKey = GET_PRICE_LIST_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString() + stationFilter + GlobalConstants.SEPARATOR + price.getPriceListId();
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
