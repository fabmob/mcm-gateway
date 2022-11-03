package com.gateway.adapter.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.adapter.service.CustomAdapterService;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.tests.WsTestUtil;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CustomAdapterServiceImpl implements CustomAdapterService {
	
	public static final String STATION_MOCK_JSON = "./adapters/custom-adapter/src/main/resources/mock/getStationsMock.json";

    /**
     * @param params       List of parameters
     * @param partnerActionId  action identifier
     * @param partnerId        Partner identifier
     * @param originalBody
     * @return list of objects returned by the Partner
     */
    @Override
    public List<Object> adaptOperation(Map<String, String> params, UUID partnerActionId, UUID partnerId, Map<String, Object> originalBody) throws IOException, IntrospectionException {
        List<Object> mocklist = new ArrayList<>();
        mocklist.add(this.createMockResponse());
        return mocklist;
    }

    /**
     * Create a Mock response
     * @return 
     */
    private Object createMockResponse() {
        Object response = new Object();
        try {
            String jsonStringyfied = WsTestUtil.readJsonFromFilePath(STATION_MOCK_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(jsonStringyfied, Object.class);
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
        return response;
    }
}
