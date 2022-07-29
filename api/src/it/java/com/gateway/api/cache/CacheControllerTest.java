package com.gateway.api.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.api.ApiITTestCase;
import com.gateway.commonapi.cache.AroundMeCacheManager;
import com.gateway.commonapi.cache.MspMetaCacheManager;
import com.gateway.commonapi.dto.api.GlobalView;
import com.gateway.commonapi.dto.api.geojson.Point;
import com.gateway.commonapi.dto.data.MspMetaDTO;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.tests.WsTestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
class CacheControllerTest extends ApiITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    /*
     * Mock MVC
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Value("${appli.use.cache}")
    protected boolean useCache;

    @Autowired
    private MspMetaCacheManager mspCacheManager;

    @Autowired
    private AroundMeCacheManager aroundMeCacheManager;

    /*
     * Web Application Context
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, this.objectMapper);
    }

    /**
     * test GET on mspMetas
     *
     * @throws Exception
     */
    @Test
    void testGetAroundMe() throws Exception {
        MspMetaDTO mspMetaDTO;
        if (this.useCache) {
            UUID mspId1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
            UUID mspId2 = UUID.fromString("00000000-0000-0000-0000-000000000002");

            mspCacheManager.clearCache("*");
            aroundMeCacheManager.clearCache("*");
            aroundMeCacheManager.populateAssetCache(AroundMeCacheManager.createMockAssets(), mspId1);
            mspMetaDTO = mspCacheManager.getFromCache(mspId1);
            log.info("ELEMENT FROM GET cache:{}", mspMetaDTO);
            List<UUID> mspIds = Stream.of(mspId1, mspId2).collect(Collectors.toList());

            Point fakeUserLoc = new Point(1.444209, 43.604652);
            log.info("gonna search {} with radius {} meters", fakeUserLoc, 20000);

            GlobalView globalview = aroundMeCacheManager.searchElementsFromGeoCache(mspIds,
                    fakeUserLoc.getLatitude(), fakeUserLoc.getLongitude(), 20000,
                    RedisGeoCommands.DistanceUnit.METERS);

            Assertions.assertEquals(5, globalview.getAssets().size());

            log.info("gonna search {} with radius {} meters", fakeUserLoc, 200);
            globalview = aroundMeCacheManager.searchElementsFromGeoCache(mspIds,
                    fakeUserLoc.getLatitude(), fakeUserLoc.getLongitude(), 200,
                    RedisGeoCommands.DistanceUnit.METERS);

            Assertions.assertEquals(2, globalview.getAssets().size());
        }
    }

    @Test
    void testGetMspMetas() throws Exception {
        MspMetaDTO mspMetaDTO;
        if (this.useCache) {
            UUID id = UUID.fromString("b814c97e-df56-4651-ac50-11525537964f");

            List<MspMetaDTO> mspMetas = new ArrayList<>();
            mspCacheManager.populateCache(this.createMspMetas());
            log.info("POPULATE CACHE");
            mspMetaDTO = mspCacheManager.getFromCache(id);
            long startList = System.currentTimeMillis();
            log.info("{} elements found", mspCacheManager.getAllMspsFromCache().size());
            long finishList = System.currentTimeMillis();
            long timeElapsedList = finishList - startList;
            log.info("timeElapsed getting from list cache = {} ms", timeElapsedList);


            List<MspMetaDTO> msps = mspCacheManager.getAllMspsFromCache();
            Assertions.assertEquals(2,msps.size());
        }
    }

    public static List<MspMetaDTO> createMspMetas() {
        try {
            String mockStringyfied = WsTestUtil.readJsonFromFilePath("./src/it/resources/msps-mock.json");
            if(mockStringyfied.isBlank()) {
                log.error("Content file is empty or file not found");
            }
            ObjectReader objectReader = new ObjectMapper().reader().forType(new TypeReference<List<MspMetaDTO>>() {
            });
            return objectReader.readValue(mockStringyfied);
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
