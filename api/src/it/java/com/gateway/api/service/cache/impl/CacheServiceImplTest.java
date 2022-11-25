package com.gateway.api.service.cache.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.api.ApiITTestCase;
import com.gateway.commonapi.cache.GatewayParamStatusManager;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.utils.cache.impl.CacheServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;


@Slf4j
public class CacheServiceImplTest extends ApiITTestCase {

    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private CacheServiceImpl cacheService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GatewayParamStatusManager gatewayParamStatusManager;


    @Test
    public void getCacheParam() throws IOException {

        UUID partnerId = UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759");
        String actionType = "STATION_SEARCH";

        ResponseEntity<CacheParamDTO[]> cacheParams = ResponseEntity.status(HttpStatus.OK).body(this.creatMockCacheParam());
        Mockito.when(restTemplate.exchange(ArgumentMatchers.endsWith("&actionType=STATION_SEARCH"),
                ArgumentMatchers.eq(HttpMethod.GET), any(), ArgumentMatchers.eq(CacheParamDTO[].class))).thenReturn(cacheParams);

        assertEquals(600, cacheService.getCacheParam(partnerId, actionType).getHardTTL());


        Mockito.when(restTemplate.exchange(ArgumentMatchers.endsWith("&actionType=STATION_SEARCH"),
                ArgumentMatchers.eq(HttpMethod.GET), any(), ArgumentMatchers.eq(CacheParamDTO[].class))).thenThrow(HttpClientErrorException.NotFound.class);
        assertEquals(null, cacheService.getCacheParam(partnerId, actionType));


        Mockito.when(restTemplate.exchange(ArgumentMatchers.endsWith("&actionType=STATION_SEARCH"),
                ArgumentMatchers.eq(HttpMethod.GET), any(), ArgumentMatchers.eq(CacheParamDTO[].class))).thenThrow(RestClientException.class);
        assertEquals(null, cacheService.getCacheParam(partnerId, actionType));

        Mockito.when(restTemplate.exchange(ArgumentMatchers.endsWith("&actionType=STATION_SEARCH"),
                ArgumentMatchers.eq(HttpMethod.GET), any(), ArgumentMatchers.eq(CacheParamDTO[].class))).thenThrow(NullPointerException.class);
        assertEquals(null, cacheService.getCacheParam(partnerId, actionType));


    }

    @Test
    public void getCacheStatusTest() {
        Mockito.when(gatewayParamStatusManager.getCacheStatus()).thenReturn(true);
        assertTrue(gatewayParamStatusManager.getCacheStatus());
    }

    @Test
    public void useCacheTest() {
        Mockito.when(gatewayParamStatusManager.getCacheStatus()).thenReturn(true);
        assertTrue(cacheService.useCache());
    }

    private CacheParamDTO[] creatMockCacheParam() throws IOException {
        CacheParamDTO cacheParam = new CacheParamDTO();
        cacheParam.setCacheParamId(UUID.fromString("b814c97e-df56-4651-ac50-11525537964f"));
        cacheParam.setPartnerId(UUID.fromString("14390fdf-34c1-41c9-885e-6ce66505b759"));
        cacheParam.setHardTTL(600);
        return new CacheParamDTO[]{cacheParam};
    }
}