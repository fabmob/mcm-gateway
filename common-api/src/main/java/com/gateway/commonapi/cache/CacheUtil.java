package com.gateway.commonapi.cache;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.api.Asset;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Getter
@Slf4j
@Configuration
public class CacheUtil<CacheStoredType extends String, DtoGenericType extends Object> {

    private final ValueOperations<String, CacheStoredType> valueOperations;
    private final RedisTemplate<String, CacheStoredType> redisTemplate;
    private final GeoOperations<String, CacheStoredType> geoOperations;

    /**
     * Key Prefix representing the env name
     */
    @Value("${spring.redis.keys.prefix:local}")
    private String keyPrefixEnv;

    private static final String GEO_PREFIX = "geo" + GlobalConstants.SEPARATOR;

    private static final String STAR_PREFIX = "*";

    static ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    CacheUtil(RedisTemplate<String, CacheStoredType> redisTemplate) {
        this.valueOperations = redisTemplate.opsForValue();
        this.redisTemplate = redisTemplate;
        this.geoOperations = redisTemplate.opsForGeo();
    }

    /**
     * Put element in the cache regarding the key provided that will be prefixed with env
     *
     * @param key     The key of the element in cache
     * @param value   object to store in cache
     * @param hardTTL time to live ih the cache, in seconds
     */
    public void putValue(String key, CacheStoredType value, long hardTTL) {
        String cacheKey = this.getKeyPrefixEnv() + GlobalConstants.SEPARATOR + key;
        valueOperations.set(cacheKey, value, Duration.ofSeconds(hardTTL));
        redisTemplate.expire(cacheKey, hardTTL, TimeUnit.SECONDS);
    }

    /**
     * Add geolocalized element in cache
     *
     * @param key       They key of the cache that will be prefixed by env
     * @param latitude  latitude of the element
     * @param longitude longitude of the element
     * @param element   value of the element used in the geo cache. We store the id used to get the key to get real element in cache.
     */
    public void addGeoMetadata(String key, double latitude, double longitude, CacheStoredType element, long hardTTL) {
        RedisGeoCommands.GeoLocation<CacheStoredType> geoLocation = null;
        String geoCacheKey = this.getKeyPrefixEnv() + GlobalConstants.SEPARATOR + GEO_PREFIX + key;
        geoLocation = new RedisGeoCommands.GeoLocation<>(element, new Point(latitude, longitude));
        log.debug("Ajout de lat/long {} {} pour le lieux {} , cle={}", latitude, longitude, element, geoCacheKey);
        this.geoOperations.add(geoCacheKey, geoLocation);
        redisTemplate.expire(geoCacheKey, hardTTL, TimeUnit.SECONDS);
    }

    /**
     * Search in a geoCache elements with a distance inside the radius
     *
     * @param geoKey             Key of the geo cache
     * @param latitude           latidude of the enter of the circle
     * @param longitude          longitude of the enter of the circle
     * @param radius             Radius to search element inside
     * @param radiusDistanceUnit Unit of distance of the radius
     * @return
     */
    private List<GeoResult<RedisGeoCommands.GeoLocation<CacheStoredType>>> searchByDistance(String geoKey,
                                                                                            double latitude, double longitude,
                                                                                            double radius, RedisGeoCommands.DistanceUnit radiusDistanceUnit) {

        RedisGeoCommands.GeoRadiusCommandArgs arguments = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        // enable acsending sort order
        arguments.sortAscending();
        // include distance calcul
        arguments.includeDistance();

        GeoResults<RedisGeoCommands.GeoLocation<CacheStoredType>> resultatSearch = this.geoOperations.radius(geoKey,
                new Circle(new Point(latitude, longitude),
                        new Distance(radius, radiusDistanceUnit)), arguments);

        List<GeoResult<RedisGeoCommands.GeoLocation<CacheStoredType>>> resultSearchList = new ArrayList<>();
        if (resultatSearch != null) {
            resultSearchList = resultatSearch.getContent();
        } else {
            log.warn("Issue retrieving information from geo cache : geoKey={}, lat={}, long={}, radius={}", geoKey, latitude, longitude, radius);
        }
        return resultSearchList;
    }


    /**
     * Search dto elements in cache associated with the distance between the lat/long passed and the element in the cache
     *
     * @param partnerId    Id of the msp in string format to search in the cache associated with it
     * @param serviceName  Name of the service used for the cache key. eg : aroundMe, availableAssets, ...
     * @param latitude     latitude of the center of the circle of research
     * @param longitude    longitude of the center of the circle of research
     * @param radius       radius of the search
     * @param distanceUnit distance unit of the radius
     * @param dtoClass     Class of object to use to parse the json stringified as Java POJO
     * @return
     */
    public List<Pair<Distance, DtoGenericType>> searchAndRetrieveByDistance(String partnerId, String serviceName,
                                                                            double latitude, double longitude,
                                                                            float radius, RedisGeoCommands.DistanceUnit distanceUnit, Class<DtoGenericType> dtoClass) {

        // initialise final list as result of the function
        List<Pair<Distance, DtoGenericType>> cacheElementWithDistance = new ArrayList<>();

        // prepare the geoKey prefix to get the right cache
        String geoKey = this.getKeyPrefixEnv() + GlobalConstants.SEPARATOR + GEO_PREFIX;

        // get geo elements from cache regarding a geoSearch. Element as result have has name the identifier of the object and also have geo metadata as calculated distance
        List<GeoResult<RedisGeoCommands.GeoLocation<CacheStoredType>>> foundGeoElement = this.searchByDistance(geoKey + serviceName + GlobalConstants.SEPARATOR + partnerId, latitude, longitude, radius, distanceUnit);

        // in order to get all dto from cache we have to build a collection with all key cache to do a multiget witch has better performance that iterating with simple get
        Set<String> elementsToRetrieve = new HashSet<>();
        foundGeoElement.forEach(geoLocationGeoResult -> elementsToRetrieve.add(geoLocationGeoResult.getContent().getName()));

        // build the key of the cache that have data elements with json value
        String prefixDtoElement = this.getKeyPrefixEnv() + GlobalConstants.SEPARATOR + serviceName + GlobalConstants.SEPARATOR + partnerId + GlobalConstants.SEPARATOR;

        // get all elements in cache in a map as DTO class as value

        Map<String, DtoGenericType> convertedElements;
        try {
            convertedElements = this.getValues(prefixDtoElement, elementsToRetrieve, dtoClass);
        } catch (Exception e) {
            convertedElements = Collections.emptyMap();
        }
        // iterate over geoResult to set the crawflydistance in the dto object that was retrieved based on key cache
        for (GeoResult<RedisGeoCommands.GeoLocation<CacheStoredType>> geoItem : foundGeoElement) {
            if (convertedElements != null) {
                DtoGenericType dtoObject = convertedElements.get(prefixDtoElement + geoItem.getContent().getName());

                // here need to define all path and structure of the geoloc where to set the crowfly distance for elements in cache
                if (dtoObject instanceof Asset) {
                    ((Asset) dtoObject).getOverriddenProperties().getLocation().setCrowflyDistance(String.valueOf(geoItem.getDistance().getValue()));
                } else {
                    log.error("Need to implement new case to set the distance on dto object {}", dtoClass);
                }

                // create Pair element in a list with the distance and the dto enriched object with the crowfly distance in the json object
                Pair<Distance, DtoGenericType> newResultItem = new MutablePair<>(geoItem.getDistance(), dtoObject);
                cacheElementWithDistance.add(newResultItem);
            }

        }
        return cacheElementWithDistance;
    }

    /**
     * Get Element from cache parsing string as className object passed
     *
     * @param key       key of the object in the cache
     * @param className classname for json parsing
     * @return Dto object json POJO
     */
    public DtoGenericType getValue(String key, Class<DtoGenericType> className) {
        DtoGenericType res = null;
        String cacheValue = String.valueOf(valueOperations.get(key));
        try {
            res = new ObjectMapper().readValue(cacheValue, className);
        } catch (JsonParseException e) {
            res = (DtoGenericType) cacheValue;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return res;
    }


    /**
     * Perform a multiget elements from cache with collection of keys passed and associate them with their keyCache
     *
     * @param prefix    prefix of the keyCache for elements to retrieve
     * @param objectIds keys which we need to get values
     * @param dtoClass  Json DTO class to use for the json string parsing
     * @return map of keyCache and dto object  generic regarding the kind type of CacheUtil used
     */
    public Map<String, DtoGenericType> getValues(String prefix, Collection<String> objectIds, Class<DtoGenericType> dtoClass) {

        List<String> fullKey = new ArrayList<>();
        // construct the full cache key based on the passed prefix and the id of the object passed to the method
        objectIds.forEach(id -> fullKey.add(prefix + id));

        // perform the redis multiget operation to retrieve all elements in the collection of keys in an efficient operation
        List<CacheStoredType> multiGetResults = this.valueOperations.multiGet(fullKey);
        // prepare the result map with key and parsed value from cache. As multiGet don't provide keys we need to iterate
        // over 2 list as same time to assiciate key and value
        Map<String, DtoGenericType> cachedElements = new HashMap<>();
        if (multiGetResults != null) {
            for (int i = 0; i < fullKey.size(); i++) {
                try {
                    cachedElements.put(fullKey.get(i), objectMapper.readValue(multiGetResults.get(i), dtoClass));
                } catch (IOException e) {
                    // decide if we continue to get other elements or not if one parsing is failing. Open decision.
                    log.warn("Warning parsing dto from cache key {}", fullKey.get(i), e.getMessage(), e);
                }
            }
        }
        return cachedElements;
    }

    /**
     * Retrieve all cache keys based of a pattern.
     *
     * @param pattern of key search
     * @return Collection of cache keys
     */
    public Set<String> getKeysPattern(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public Set<String> getKeysPatternWithoutPrefix(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * Retrieve all cache keys based of a prefix concatenated with env prefix.
     *
     * @param prefix base of the pattern that will be appended with wildcard at the end
     * @return Collection of cache keys
     */
    public Set<String> getKeysByPrefix(String prefix) {
        return getKeysPattern(this.keyPrefixEnv + GlobalConstants.SEPARATOR + prefix + "*");
    }

    /**
     * Clear the cache based on a pattern. Pattern will be prefixed by env prefix
     *
     * @param keyPattern
     * @param ignoreEnvPrefix if true don't add the prefix at beginning of the pattern
     */
    public void clearCache(String keyPattern, boolean ignoreEnvPrefix) {
        // first we need to scan to get the keys then use pipeline to send all delete commands
        Set<String> keysToDelete = this.redisTemplate.keys(ignoreEnvPrefix ? keyPattern : this.keyPrefixEnv + GlobalConstants.SEPARATOR + keyPattern);
        log.info("Pattern {} matching {} results, will be deleted from cache", this.keyPrefixEnv + GlobalConstants.SEPARATOR + keyPattern, keysToDelete.size());
        this.redisTemplate.delete(keysToDelete);
    }


    /**
     * Clear the cache based on a pattern by partner Id
     *
     * @param id
     */
    public void clearCacheById(String id) {
        Set<String> keysToDelete = this.redisTemplate.keys(STAR_PREFIX + id + STAR_PREFIX);
        this.redisTemplate.delete(keysToDelete);

    }

}
