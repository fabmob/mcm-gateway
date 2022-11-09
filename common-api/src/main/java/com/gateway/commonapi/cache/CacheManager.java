package com.gateway.commonapi.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gateway.commonapi.constants.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public abstract class CacheManager<T> {

    @Autowired
    private CacheUtil<String, T> cacheUtil;

    protected static ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    /**
     * Key Prefix representing the env name
     */
    @Value("${spring.redis.keys.prefix:local}")
    protected String keyPrefixEnv;

    public T getFromCache(UUID id, String prefix, Class<T> cachedClass) {
        String keyPrefixEnvironment = keyPrefixEnv + GlobalConstants.SEPARATOR;
        String keyPrefix = keyPrefixEnvironment.concat(prefix);
        return cacheUtil.getValue(keyPrefix + GlobalConstants.SEPARATOR + id.toString(), cachedClass);

    }

    public List<T> getAllFromCache(UUID id, String prefix, Class<T> cachedClass) {
        List<T> objectsDTO = new ArrayList<>();
        Set<String> keys = cacheUtil.getKeysByPrefix(prefix);

        String prefixEnvironment = keyPrefixEnv + GlobalConstants.SEPARATOR;
        // Filter keys to bring together those who concern the partner (id)
        List<String> keysById = keys.stream().filter(c -> c.contains(keyPrefixEnv + GlobalConstants.SEPARATOR + prefix + GlobalConstants.SEPARATOR + id.toString())).map(c -> c.substring(prefixEnvironment.length())).collect(Collectors.toList());

        String keyPrefixEnvironment = keyPrefixEnv + GlobalConstants.SEPARATOR;
        Map<String, T> objectTypeMap = cacheUtil.getValues(keyPrefixEnvironment, keysById, cachedClass);

        objectsDTO.addAll(objectTypeMap.values());

        return objectsDTO;
    }

    public void clearCache(String keyPattern, CacheUtil<String, T> objectTypeCacheUtil) {
        objectTypeCacheUtil.clearCache(keyPattern, false);
    }

    public void clearCacheById(String id) {
        cacheUtil.clearCacheById(id);
    }

}
