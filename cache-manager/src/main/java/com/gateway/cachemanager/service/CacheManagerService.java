package com.gateway.cachemanager.service;

import com.gateway.commonapi.cache.CacheStatus;
import com.gateway.commonapi.dto.api.geojson.Position;
import com.gateway.commonapi.utils.enums.ActionsEnum;

import java.util.List;
import java.util.UUID;

public interface CacheManagerService {

    /**
     * Clear all cache or only cache on specified partner
     *
     * @param partners List of partners to delete from cache
     */
    void clearCache(List<UUID> partners);


    /**
     * Give cache status (active or not).
     *
     * @return CacheStatus singleton
     */
    CacheStatus getCacheStatus();

    /**
     * Update cache status
     *
     * @param isEnabled boolean new cache status.
     * @return CacheStatus singleton
     */
    CacheStatus putCacheStatus(boolean isEnabled);

    /**
     * Refresh partners' meta-data from cache
     */
    void refreshPartners();

    /**
     * Refresh partners' meta-data from cache
     */
    void refresh(UUID partnerId, ActionsEnum actionName, List<Position> positions);
}
