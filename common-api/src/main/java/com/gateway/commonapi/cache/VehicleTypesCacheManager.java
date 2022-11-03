package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.VehicleTypes;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Cache Manager for the Vehicle Types operation
 */
@Slf4j
@Component
public class VehicleTypesCacheManager extends CacheManager<VehicleTypes> {

    public static final String GET_VEHICLE_TYPE_SERVICE_KEY_PREFIX = "getVehicleType";

    @Autowired
    private CacheUtil<String, VehicleTypes> cacheUtil;

    /**
     * Retrieve all assets from cache
     *
     * @return List of partnerMetaDTO
     */
    public VehicleTypes getFromCache(UUID id) {
        return (VehicleTypes) this.getFromCache(id, GET_VEHICLE_TYPE_SERVICE_KEY_PREFIX, VehicleTypes.class);
    }


    /**
     * Retrieve all vehicleTypes from cache
     *
     * @return List of vehicleTypes
     */
    public List<VehicleTypes> getAllVehicleTypesFromCache(UUID partnerId) {
        String prefix = GET_VEHICLE_TYPE_SERVICE_KEY_PREFIX ;
        return this.getAllFromCache(partnerId, prefix, VehicleTypes.class);
    }

    /**
     * Populate Cache regarding a keyCache for the map (e.g : PartnerId+service)
     *
     * @param vehicleTypes list of elements to add
     */
    public void populateCache(List<VehicleTypes> vehicleTypes, UUID partnerId, CacheParamDTO cacheParamDTO) {
        vehicleTypes.forEach(vehicleType -> {
            try {
                // convert dto as string to store it in cache
                String assetStringyfied = objectMapper.writeValueAsString(vehicleType);
                // build the key cache

                String elementCacheKey = GET_VEHICLE_TYPE_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString() + GlobalConstants.SEPARATOR + vehicleType.getVehicleTypeId();
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
