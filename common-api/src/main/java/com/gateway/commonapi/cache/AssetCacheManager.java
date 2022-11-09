package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.Asset;
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

import static com.gateway.commonapi.constants.GlobalConstants.LAT_OR_LNG_IS_NULL;


/**
 * Cache Manager for the Asset operation
 */
@Slf4j
@Component
public class AssetCacheManager extends CacheManager<Asset> {

    public AssetCacheManager() {
        super();
    }

    public static final String GET_ASSET_SERVICE_KEY_PREFIX = "getAsset";

    @Autowired
    private CacheUtil<String, Asset> assetCacheUtil;


    /**
     * Retrieve all assets from cache
     *
     * @return List of partnerMetaDTO
     */
    public Asset getFromCache(UUID id) {
        return (Asset) this.getFromCache(id, GET_ASSET_SERVICE_KEY_PREFIX, Asset.class);
    }

    /**
     * Retrieve all assets from cache
     *
     * @return List of assets
     */
    public List<Asset> getAllAssetFromCache(UUID id) {
        return this.getAllFromCache(id, GET_ASSET_SERVICE_KEY_PREFIX, Asset.class);
    }


    /**
     * Populate Cache regarding a keyCache for the map (e.g : PartnerId+service)
     *
     * @param assets list of elements to add
     */
    public void populateCache(List<Asset> assets, CacheParamDTO cacheParamDTO) {
        assets.forEach(asset -> {
            try {
                // convert dto as string to store it in cache
                String assetStringyfied = objectMapper.writeValueAsString(asset);
                // build the key cache
                String elementCacheKey = GET_ASSET_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + asset.getPartnerId().toString() + GlobalConstants.SEPARATOR + asset.getAssetId();
                // add element to the cache
                assetCacheUtil.putValue(elementCacheKey, assetStringyfied, cacheParamDTO.getHardTTL());
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        });
    }


    //************************ Geo operations for asset *********************//


    /**
     * populate Cache regarding a keyCache for the map (e.g : partnerId+service)
     */
    public void populateAssetCacheGeoData(List<Asset> assets, UUID partnerId, CacheParamDTO cacheParamDTO) {

        log.debug("{} assets added", assets.size());
        assets.forEach(asset -> {
            try {
                String assetsStringyfied = objectMapper.writeValueAsString(asset);

                String elementCacheKey = GET_ASSET_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString() + GlobalConstants.SEPARATOR + asset.getAssetId();

                log.debug("Adding element with key {}", elementCacheKey);
                assetCacheUtil.putValue(elementCacheKey, assetsStringyfied, cacheParamDTO.getHardTTL());

                assetCacheUtil.addGeoMetadata(GET_ASSET_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + asset.getPartnerId().toString(),
                        asset.getOverriddenProperties().getLocation().getCoordinates().getLat(),
                        asset.getOverriddenProperties().getLocation().getCoordinates().getLng(), asset.getAssetId(), cacheParamDTO.getHardTTL());

                log.info("Adding assetId {}  to the cache ", asset.getAssetId());
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    /**
     * Search assets elements from cache
     *
     * @param partnerId    partner id
     * @param latitude     latitude of the center of the circle of research
     * @param longitude    longitude of the center of the circle of research
     * @param radius       radius of the search
     * @param distanceUnit distance unit of the radius
     * @return list of assetType from the cache
     */
    public List<Asset> getAllAssetsFromCacheByGeoParams(UUID partnerId, Double longitude, Double latitude, Float radius, RedisGeoCommands.DistanceUnit distanceUnit) {

        if (longitude == null || latitude == null || radius == null) {
            throw new InternalException(CommonUtils.placeholderFormat(LAT_OR_LNG_IS_NULL));
        }

        // retrieve assets
        List<Asset> assets = new ArrayList<>();
        List<Pair<Distance, Asset>> resListAsset = assetCacheUtil.searchAndRetrieveByDistance(partnerId.toString(), GET_ASSET_SERVICE_KEY_PREFIX,
                latitude, longitude,
                radius, distanceUnit, Asset.class);
        for (Pair<Distance, Asset> geoRes : resListAsset) {
            if (geoRes.getValue() != null) {
                log.info("distance {} {} for {}", geoRes.getKey().getValue(), geoRes.getKey().getMetric().getAbbreviation(), geoRes.getValue().toString());
                assets.add(geoRes.getValue());
            }
        }
        return assets;
    }


    /**
     * Clear the cache based on a pattern passed. Env prefix will be automatically added to the key.
     *
     * @param keyPattern
     */
    public void clearCache(String keyPattern) {
        this.clearCache(keyPattern, assetCacheUtil);
    }


}
