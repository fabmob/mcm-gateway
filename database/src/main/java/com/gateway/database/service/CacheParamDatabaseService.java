package com.gateway.database.service;

import com.gateway.database.model.CacheParam;
import com.gateway.database.model.CacheParamPK;

import java.util.List;
import java.util.UUID;

public interface CacheParamDatabaseService {
    /**
     * Add a new CacheParam
     *
     * @param cacheParam CacheParam object
     * @return CacheParam information for the CacheParam added
     */

    CacheParam addCacheParam(CacheParam cacheParam);

    /**
     * Retrieve a list of CacheParam transported into CacheParam
     *
     * @return List of CacheParam
     */
    List<CacheParam> getAllCacheParams();


    /**
     * Delete a CacheParam
     *
     * @param cacheParamPK Identifier of the CacheParam
     */
    void deleteCacheParam(CacheParamPK cacheParamPK);

    /**
     * Delete cacheParam using its UUID
     *
     * @param cacheParamId cacheParam Id
     */
    void deleteCacheParam(UUID cacheParamId);



    /**
     * Retrieve a CacheParam information.
     *
     * @param cacheParamPK Identifier of the CacheParam
     * @return CacheParam information for the CacheParam
     */
    CacheParam findCacheParamByPK(CacheParamPK cacheParamPK);

    /**
     * Retrieve a CacheParam information.
     *
     * @param cacheParamId Identifier of the CacheParam
     * @return CacheParam information for the CacheParam
     */
    CacheParam findCacheParamByID(UUID cacheParamId);


    /**
     *
     * @param cacheParamPK cacheParam PK
     * @param cacheParam cacheParam
     * @return CacheParam
     */
    CacheParam updateCacheParam(CacheParamPK cacheParamPK, CacheParam cacheParam);

    /**
     *
     * @param cacheParamId cacheParam Id
     * @param cacheParam cacheParam
     * @return CacheParam
     */
    CacheParam updateCacheParam(UUID cacheParamId, CacheParam cacheParam);

    /**
     *
     * @param mspId msp Id
     * @param actionType actionType
     * @return List of Cache param using filter
     */
    List<CacheParam> getAllCacheParamByCriteria(UUID mspId, String actionType);
}
