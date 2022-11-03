package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gateway.commonapi.dto.api.Station;
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
import static com.gateway.commonapi.constants.GlobalConstants.SEPARATOR;


/**
 * Cache Manager for the Station Types operation
 */
@Slf4j
@Component
public class StationCacheManager extends CacheManager<Station> {

    public static final String GET_STATION_SERVICE_KEY_PREFIX = "getStation";

    @Autowired
    private CacheUtil<String, Station> stationCacheUtil;

    /**
     * Retrieve all stations from cache
     *
     * @return List of stations
     */
    public Station getStationFromCache(UUID id) {
        return (Station) this.getFromCache(id, GET_STATION_SERVICE_KEY_PREFIX, Station.class);

    }


    /**
     * Search stations elements from cache
     *
     * @param partnerId partner id
     * @param lat       latitude of the center of the circle of research
     * @param lon       longitude of the center of the circle of research
     * @param rad       radius of the search
     * @param meters    distance unit of the radius
     * @return list of assetType from the cache
     */
    public List<Station> getAllStationFromCacheByGeoParams(UUID partnerId, Double lon, Double lat, Float rad, RedisGeoCommands.DistanceUnit meters) {

        if (lon == null || lat == null || rad == null) {
            throw new InternalException(CommonUtils.placeholderFormat(LAT_OR_LNG_IS_NULL));
        }

        List<Station> stations = new ArrayList<>();
        // need to retrieve all type of objects used in globalview ("stations", "stationsStatus", "assets", "parkings")
        List<Pair<Distance, Station>> resListStations = stationCacheUtil.searchAndRetrieveByDistance(partnerId.toString(), GET_STATION_SERVICE_KEY_PREFIX,
                lat, lon, rad, meters, Station.class);

        for (Pair<Distance, Station> geoRes : resListStations) {
            log.info("distance {} {} for {}", geoRes.getKey().getValue(), geoRes.getKey().getMetric().getAbbreviation(), geoRes.getValue().toString());
            stations.add(geoRes.getValue());
        }
        return stations;
    }


    /**
     * populate Cache regarding a keyCache for the map (e.g : partnerId+service)
     */
    public void populateStationsCache(List<Station> stations, CacheParamDTO cacheParamDTO) {

        log.debug("{} stations added", stations.size());
        stations.forEach(station -> {
            try {
                String assetsStringyfied = objectMapper.writeValueAsString(station);

                String elementCacheKey = GET_STATION_SERVICE_KEY_PREFIX + SEPARATOR + station.getPartnerId().toString() + SEPARATOR + station.getStationId();

                log.debug("Adding element with key {}", elementCacheKey);
                stationCacheUtil.putValue(elementCacheKey, assetsStringyfied, cacheParamDTO.getHardTTL());

                if (station.getCoordinates() != null && station.getCoordinates().getLat() != null && station.getCoordinates().getLng() != null) {
                    stationCacheUtil.addGeoMetadata(GET_STATION_SERVICE_KEY_PREFIX + SEPARATOR + station.getPartnerId().toString(),
                            station.getCoordinates().getLat(),
                            station.getCoordinates().getLng(), station.getStationId(), cacheParamDTO.getHardTTL());
                }
                log.info("Adding stationId {}  to the cache ", station.getStationId());
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
            }
        });
    }


    public List<Station> getAllStationFromCache(UUID partnerId) {
        return this.getAllFromCache(partnerId, GET_STATION_SERVICE_KEY_PREFIX, Station.class);
    }

    /**
     * Clear the cache based on a pattern passed. Env prefix will be automatically added to the key.
     *
     * @param keyPattern
     */
    public void clearCache(String keyPattern) {
        this.clearCache(keyPattern, stationCacheUtil);
    }


}
