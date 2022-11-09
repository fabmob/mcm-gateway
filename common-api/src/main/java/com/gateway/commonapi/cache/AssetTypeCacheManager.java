package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.AssetType;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gateway.commonapi.constants.GlobalConstants.LAT_OR_LNG_IS_NULL;


/**
 * Cache Manager for the Available assets operation
 */
@Slf4j
@Component
public class AssetTypeCacheManager extends CacheManager<AssetType> {

    public static final String GET_ASSET_TYPE_SERVICE_KEY_PREFIX = "getAssetType";

    @Autowired
    private CacheUtil<String, AssetType> assetTypeCacheUtil;

    /**
     * Retrieve all assets from cache
     *
     * @return List of partnerMetaDTO
     */
    public AssetType getFromCache(UUID id) {
        return (AssetType) this.getFromCache(id, GET_ASSET_TYPE_SERVICE_KEY_PREFIX, AssetType.class);
    }


    /**
     * Search Available assets elements from cache
     *
     * @param partnerId    partner id
     * @param lat          latitude of the center of the circle of research
     * @param lon          longitude of the center of the circle of research
     * @param rad          radius of the search
     * @param distanceUnit distance unit of the radius
     * @return list of assetType from the cache
     */
    public List<AssetType> getAllAssetTypeFromCacheByGeoParams(UUID partnerId, String stationId, Double lon, Double lat, Float rad, RedisGeoCommands.DistanceUnit distanceUnit) {

        if (lon == null || lat == null || rad == null) {
            throw new InternalException(CommonUtils.placeholderFormat(LAT_OR_LNG_IS_NULL));
        }

        List<AssetType> assetTypes = new ArrayList<>();

        List<Pair<Distance, AssetType>> resListAvailableAsset = assetTypeCacheUtil.searchAndRetrieveByDistance(partnerId.toString(), GET_ASSET_TYPE_SERVICE_KEY_PREFIX,
                lat, lon, rad, distanceUnit, AssetType.class);

        if (stationId != null) {
            resListAvailableAsset = resListAvailableAsset.stream().filter(c -> c.getValue().getStationId().equals(stationId)).collect(Collectors.toList());
        }

        for (Pair<Distance, AssetType> geoRes : resListAvailableAsset) {
            log.info("distance {} {} for {}", geoRes.getKey().getValue(), geoRes.getKey().getMetric().getAbbreviation(), geoRes.getValue().toString());
            assetTypes.add(geoRes.getValue());
        }

        return assetTypes;
    }


    public List<AssetType> getAllAssetTypeFromCache(UUID partnerId, String stationId) {
        List<AssetType> assetTypes;
        assetTypes = this.getAllFromCache(partnerId, GET_ASSET_TYPE_SERVICE_KEY_PREFIX, AssetType.class);
        if (stationId != null) {
            assetTypes = assetTypes.stream().filter(c -> c.getStationId().equals(stationId)).collect(Collectors.toList());
        }
        return assetTypes;
    }

    /**
     * populate Cache regarding a keyCache for the map (e.g : partnerId+service)
     */
    public void populateAssetTypeCache(List<AssetType> assetTypes, CacheParamDTO cacheParamDTO) {

        log.debug("{} assets added", assetTypes.size());
        assetTypes.forEach(assetType -> {
            try {
                String assetsStringyfied = objectMapper.writeValueAsString(assetType);

                String elementCacheKey = GET_ASSET_TYPE_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + assetType.getPartnerId() + GlobalConstants.SEPARATOR + assetType.getStationId();
                log.debug("Adding element with key {}", elementCacheKey);
                assetTypeCacheUtil.putValue(elementCacheKey, assetsStringyfied, cacheParamDTO.getHardTTL());

                assetTypeCacheUtil.addGeoMetadata(GET_ASSET_TYPE_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + assetType.getPartnerId().toString(),
                        assetType.getAssets().get(0).getOverriddenProperties().getLocation().getCoordinates().getLat(),
                        assetType.getAssets().get(0).getOverriddenProperties().getLocation().getCoordinates().getLng(), assetType.getStationId(), cacheParamDTO.getHardTTL());

                log.info("Adding assetId {}  to the cache ", assetType.getAssetTypeId());
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
        this.clearCache(keyPattern, assetTypeCacheUtil);
    }
}
