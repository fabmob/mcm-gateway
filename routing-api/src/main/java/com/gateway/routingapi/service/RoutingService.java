package com.gateway.routingapi.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public interface RoutingService {
    
	/**
     * Forward the request to Adapters (default/Custom) and return the response 
     * @param params
     * @param mspId
     * @param actionName
     * @param originalBody
     * @return
     */
    Object routeOperation(Map<String, String> params, UUID mspId, String actionName, Optional<Map<String, Object>> originalBody);
}

