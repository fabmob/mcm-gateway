package com.gateway.routingapi.service;

import com.gateway.commonapi.dto.adapter.GenericResponse;

import java.util.Map;
import java.util.UUID;


public interface RoutingService {
    GenericResponse routeGetOperation(Map<String, String> params, UUID mspId, String actionName);
}

