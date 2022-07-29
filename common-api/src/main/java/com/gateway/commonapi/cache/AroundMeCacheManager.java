package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.commonapi.dto.api.Asset;
import com.gateway.commonapi.dto.api.GlobalView;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.tests.WsTestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cache Manager for the around-me operation
 */
@Slf4j
@Component
public class AroundMeCacheManager {

    public static final String ASSETS = "ASSETS";
    public static final String SERVICE_PREFIX = "aroundMe";

    /**
     * global object mapper in order not to instantiate it each time
     */
    static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CacheUtil<String, Asset> aroundMeAssetCacheUtil;


    /**
     * retrieve Asset from cache based of mspId and assetId
     * @param mspId
     * @param assetId
     * @return Asset
     */
    public Asset getAssetFromCache(UUID mspId, String assetId) {
        return aroundMeAssetCacheUtil.getValue(aroundMeAssetCacheUtil.getKeyPrefixEnv() + mspId.toString() + "-" + ASSETS + "_" + assetId, Asset.class);
    }


    /**
     * populate Cache regarding a keyCache for the map (e.g : mspId+service)
     */
    public void populateAssetCache( List<Asset> assets, UUID mspId ) {

        log.info("{} assets mocked", assets.size());
        assets.forEach(asset -> {
            try {
                String assetsStringyfied = objectMapper.writeValueAsString(asset);
                String elementCacheKey = mspId.toString() + "-" + SERVICE_PREFIX + "-" + ASSETS + "_" + asset.getAssetId();

                log.debug("Adding element with key {}", elementCacheKey);
                aroundMeAssetCacheUtil.putValue(elementCacheKey, assetsStringyfied);

                aroundMeAssetCacheUtil.addGeoMetadata(asset.getMspId().toString() + "-" + SERVICE_PREFIX,
                        asset.getOverriddenProperties().getLocation().getCoordinates().getLat(),
                        asset.getOverriddenProperties().getLocation().getCoordinates().getLng(), asset.getAssetId());

                log.info("Adding assetId {}  to the cache ", asset.getAssetId());
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    /**
     * Search GlobalView elements from cache
     * @param mspIds list of msp ids
     * @param latitude     latitude of the center of the circle of research
     * @param longitude    longitude of the center of the circle of research
     * @param radius       radius of the search
     * @param distanceUnit distance unit of the radius
     * @return
     */
    public GlobalView searchElementsFromGeoCache(List<UUID> mspIds, double latitude, double longitude, int radius,
                                                 RedisGeoCommands.DistanceUnit distanceUnit) {
        GlobalView globalView = new GlobalView();
        globalView.setAssets(new ArrayList<>());

        // iterate over all msp to get all assets.
        for (UUID mspId :  mspIds ) {
            List<Asset> assets = new ArrayList<>();
            // need to retrieve all type of objects used in globalview ("stations", "stationsStatus", "assets", "parkings")
            List<Pair<Distance, Asset>> resList = aroundMeAssetCacheUtil.searchAndRetrieveByDistance(mspId.toString(), SERVICE_PREFIX, ASSETS,
                    latitude, longitude,
                    radius, distanceUnit, Asset.class);
            for (Pair<Distance, Asset> geoRes : resList) {
                log.info("distance {} {} for {}", geoRes.getKey().getValue(), geoRes.getKey().getMetric().getAbbreviation(), geoRes.getValue().toString());
                assets.add(geoRes.getValue());
            }
            globalView.getAssets().addAll(assets);
        }

        // later will need to get also parking, stations, ...

        return globalView;
    }


    /**
     * Clear the cache based on a pattern passed. Env prefix will be automatically added to the key.
     *
     * @param keyPattern
     */
    public void clearCache(String keyPattern) {
        aroundMeAssetCacheUtil.clearCache(keyPattern);
    }

    /**
     * Load a list of Asset from json mock file
     * Method that should be removed when real data will be unmocked.
     *
     * @return
     */
    public static List<Asset> createMockAssets() {
        try {
            String mockStringyfied = WsTestUtil.readJsonFromFilePath("./src/main/resources/assets-mock.json");
            if(mockStringyfied.isBlank()) {
                log.error("Content file is empty or file not found");
            }
            ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Asset>>() {
            });
            return objectReader.readValue(mockStringyfied);
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
