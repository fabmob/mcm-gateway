package com.gateway.dataapi.service;

import com.gateway.commonapi.dto.data.CacheParamDTO;

import java.util.List;
import java.util.UUID;

public interface CacheParamService {
    /**
     * Add a new CacheParamDTO
     *
     * @param cacheParam CacheParamDTO object
     * @return CacheParamDTO information for the CacheParamDTO added
     */
    CacheParamDTO addCacheParam(CacheParamDTO cacheParam);

    /**
     * Retrieve a list of CacheParam transported into CacheParamDTO
     *
     * @return List of CacheParamDTO
     */
    List<CacheParamDTO> getAllCacheParams();


    /**
     * Delete specified cache param
     *
     * @param cacheParamId cacheParam Id
     */
    void deleteCacheParamById(UUID cacheParamId);


    /**
     * update the specified cache param
     *
     * @param cacheParamId cacheParam id
     * @param body body in the request
     */
    void updateCacheParam(UUID cacheParamId, CacheParamDTO body);

    /**
     * Get the specified cache param
     *
     * @param cacheParamId cacheParam Id
     * @return cacheParam DTO
     */
    CacheParamDTO getCacheParamFromID(UUID cacheParamId);

    /**
     *
     * @param mspId MSP ID
     * @param actionType Action type
     * @return List of Cache Param using filters
     */
    List<CacheParamDTO> getAllCacheParamByCriteria(UUID mspId, String actionType);
}
