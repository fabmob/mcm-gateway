package com.gateway.commonapi.utils.cache;


import com.gateway.commonapi.dto.data.CacheParamDTO;

import java.util.UUID;

public interface CacheService {

    CacheParamDTO getCacheParam(UUID partnerId, String actionType);

    boolean useCache();
    
}
