package com.gateway.commonapi.cache;

import com.gateway.commonapi.dto.api.*;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.gateway.commonapi.constants.GlobalConstants.LAT_OR_LNG_IS_NULL;

/**
 * Cache Manager for the around-me operation
 */
@Slf4j
@Component
public class AroundMeCacheManager {
    @Autowired
    private CacheUtil<String, Asset> aroundMeAssetCacheUtil;

    @Autowired
    private StationCacheManager stationCacheManager;

    @Autowired
    private ParkingCacheManager parkingCacheManager;

    @Autowired
    private AssetCacheManager assetCacheManager;

    @Autowired
    private StationStatusCacheManager stationStatusCacheManager;


    /**
     * Search GlobalView elements from cache
     *
     * @param partnerIds   list of partner ids
     * @param latitude     latitude of the center of the circle of research
     * @param longitude    longitude of the center of the circle of research
     * @param radius       radius of the search
     * @param distanceUnit distance unit of the radius
     * @return
     */
    public GlobalView searchElementsFromGeoCache(List<UUID> partnerIds, Double latitude, Double longitude, Float radius,
                                                 RedisGeoCommands.DistanceUnit distanceUnit) {

        if (longitude == null || latitude == null || radius == null) {
            throw new InternalException(CommonUtils.placeholderFormat(LAT_OR_LNG_IS_NULL));
        }

        GlobalView globalView = new GlobalView();
        globalView.setAssets(new ArrayList<>());

        // iterate over all partner to get all assets.
        for (UUID partnerId : partnerIds) {

            List<Asset> assets = assetCacheManager.getAllAssetsFromCacheByGeoParams(partnerId, longitude, latitude, radius, RedisGeoCommands.DistanceUnit.METERS);
            globalView.getAssets().addAll(assets);


            // retrieve parkings
            List<Parking> parkingsList = parkingCacheManager.getAllParkingsFromCacheByGeoParams(partnerId, longitude, latitude, radius, RedisGeoCommands.DistanceUnit.METERS);
            globalView.getParkings().addAll(parkingsList);


            // retrieve stations
            List<Station> stationsList = stationCacheManager.getAllStationFromCacheByGeoParams(partnerId, longitude, latitude, radius, RedisGeoCommands.DistanceUnit.METERS);
            globalView.getStations().addAll(stationsList);


            // // retrieve station status
            List<StationStatus> stationStatuses = stationStatusCacheManager.getAllStationStatusFromCache(partnerId, null);
            globalView.getStationsStatus().addAll(stationStatuses);
        }
        return globalView;
    }


    /**
     * Clear the cache based on a pattern passed. Env prefix will be automatically added to the key.
     *
     * @param keyPattern
     */
    public void clearCache(String keyPattern) {
        aroundMeAssetCacheUtil.clearCache(keyPattern, false);
    }

}

