package com.gateway.routingapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.dto.adapter.GenericResponse;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.routingapi.service.RoutingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class RoutingServiceImpl implements RoutingService {


    @Autowired
    RestTemplate restTemplate;
    @Value("${gateway.service.adapter.path}")
    private String uri;
    @Autowired
    private ErrorMessages errorMessages;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    public GenericResponse routeGetOperation(Map<String, String> params, UUID mspId, String ActionName) {


        // TODO
        // need to call the standard table to get versions and actions which is active for the msp
        // create the uri
        // forward the request

        //TODO remove this when implementing true http call
        return createMockResponse();
    }


    private GenericResponse createMockResponse() {
        GenericResponse genericResponse = new GenericResponse();
        try {
            String jsonStringyfied = WsTestUtil.readJsonFromFilePath("./routing-api/src/main/resources/mock/getStationsMock.json");
            ObjectMapper objectMapper = new ObjectMapper();
            genericResponse.setBody(objectMapper.readValue(jsonStringyfied, Object.class));
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
        return genericResponse;
    }
}
