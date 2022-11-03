package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.Parking;
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
 * Cache Manager for parking operation
 * Not used for the moment because no parking endpoint needed
 */
@Slf4j
@Component
public class ParkingCacheManager extends CacheManager<Parking> {

    public static final String GET_PARKING_SERVICE_KEY_PREFIX = "getParking";

    @Autowired
    private CacheUtil<String, Parking> parkingCacheUtil;

    /**
     * Retrieve parking by id from cache
     *
     * @return List of stations
     */
    public Parking getFromCache(UUID id) {
        return (Parking) this.getFromCache(id, GET_PARKING_SERVICE_KEY_PREFIX, Parking.class);
    }

    /**
     * Search parking elements from cache
     *
     * @param partnerId    partner id
     * @param latitude     latitude of the center of the circle of research
     * @param longitude    longitude of the center of the circle of research
     * @param radius       radius of the search
     * @param distanceUnit distance unit of the radius
     * @return list of assetType from the cache cache
     */
    public List<Parking> getAllParkingsFromCacheByGeoParams(UUID partnerId, Double longitude, Double latitude, Float radius, RedisGeoCommands.DistanceUnit distanceUnit) {

        if (longitude == null || latitude == null || radius == null) {
            throw new InternalException(CommonUtils.placeholderFormat(LAT_OR_LNG_IS_NULL));
        }

        // retrieve parkings
        List<Parking> parkings = new ArrayList<>();
        List<Pair<Distance, Parking>> resListParking = parkingCacheUtil.searchAndRetrieveByDistance(partnerId.toString(), GET_PARKING_SERVICE_KEY_PREFIX,
                latitude, longitude,
                radius, distanceUnit, Parking.class);
        for (Pair<Distance, Parking> geoRes : resListParking) {
            if (geoRes.getValue() != null) {
                log.info("distance {} {} for {}", geoRes.getKey().getValue(), geoRes.getKey().getMetric().getAbbreviation(), geoRes.getValue().toString());
                parkings.add(geoRes.getValue());
            }
        }
        return parkings;
    }


    /**
     * populate Cache regarding a keyCache for the map (e.g : partnerId+service)
     */
    public void populateParkingCacheGeoData(List<Parking> parkings, UUID partnerId, CacheParamDTO cacheParamDTO) {

        log.debug("{} assets added", parkings.size());
        parkings.forEach(parking -> {
            try {
                String assetsStringyfied = objectMapper.writeValueAsString(parking);

                String elementCacheKey = GET_PARKING_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + partnerId.toString() + GlobalConstants.SEPARATOR + parking.getParkingId();

                log.debug("Adding element with key {}", elementCacheKey);
                parkingCacheUtil.putValue(elementCacheKey, assetsStringyfied, cacheParamDTO.getHardTTL());

                parkingCacheUtil.addGeoMetadata(GET_PARKING_SERVICE_KEY_PREFIX + GlobalConstants.SEPARATOR + parking.getPartnerId().toString(),
                        parking.getLocation().getCoordinates()[1],
                        parking.getLocation().getCoordinates()[0], parking.getParkingId(), cacheParamDTO.getHardTTL());

                log.info("Adding assetId {}  to the cache ", parking.getParkingId());
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
        this.clearCache(keyPattern, parkingCacheUtil);
    }
}
