package com.gateway.commonapi.cache;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public final class CacheStatus {
    @JsonIgnore
    private static CacheStatus cacheStatus = new CacheStatus();
    private boolean isEnabled = false;

    private CacheStatus() {
    }

    public static CacheStatus getInstance() {
        return cacheStatus;
    }
}
