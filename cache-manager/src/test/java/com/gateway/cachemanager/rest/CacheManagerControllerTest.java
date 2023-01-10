package com.gateway.cachemanager.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.cachemanager.CacheManagerITTestCase;
import com.gateway.cachemanager.service.CacheManagerServiceImpl;
import com.gateway.commonapi.cache.GatewayParamStatusManager;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.tests.enums.JsonResponseTypeEnum;
import com.gateway.commonapi.utils.enums.ActionsEnum;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static com.gateway.commonapi.constants.CacheManagerDict.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Slf4j
class CacheManagerControllerTest extends CacheManagerITTestCase{

    public static final String IT_RESOURCES_PATH = "./src/test/resources/";
    public static final String CACHE_GET_STATUS_OK_JSON = "expected/cacheStatus.json";
    public static final String CACHE_POSITIONS_REQUEST_JSON = "request/requestPositions.json";

    /*
     * Mock MVC
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CacheManagerServiceImpl cacheManagerService;

    @Autowired
    CacheManagerServiceImpl cacheManagerServiceCalled;

    @MockBean
    GatewayParamStatusManager cacheUtil;


    /*
     * Web Application Context
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build();
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, this.objectMapper);
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);
    }

    /**
     * Test clear cache
     * @throws Exception
     */
    @Test
    void testClearCache() throws Exception {

        testHttpRequestWithExpectedResult(CACHE_MANAGER_PATH + CACHE_MANAGER_CLEAR_PATH, HttpMethod.DELETE, HttpStatus.NO_CONTENT,
                null , null,
                JsonResponseTypeEnum.EMPTY,"Test clear all cache",null);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("partnerIds", "87930fdf-34c1-41c9-885e-6ce66505b594");
        testHttpRequestWithExpectedResult(CACHE_MANAGER_PATH + CACHE_MANAGER_CLEAR_PATH, HttpMethod.DELETE, HttpStatus.NOT_FOUND,
                null , null,
                JsonResponseTypeEnum.EMPTY,"Test clear cache on partnerIds", parameters);
    }

    /**
     * Test get cache status
     * @throws Exception
     */
    @Test
    void testGetCacheStatus() throws Exception {

        testHttpRequestWithExpectedResult(CACHE_MANAGER_PATH + CACHE_MANAGER_STATUS_PATH, HttpMethod.GET, HttpStatus.OK,
                null , CACHE_GET_STATUS_OK_JSON ,
                JsonResponseTypeEnum.JSON_OBJECT,"Test get cache status",null);

    }

    /**
     * Test update cache status
     * @throws Exception
     */
    @Test
    void testPutCacheStatus() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("isEnabled", "false");
        testHttpRequestWithExpectedResult(CACHE_MANAGER_PATH + CACHE_MANAGER_STATUS_PATH, HttpMethod.PUT, HttpStatus.OK,
                null, CACHE_GET_STATUS_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test update cache status", parameters);


    }

    /**
     * Test refresh partnerMetas of cache
     *
     * @throws Exception
     */
    @Test
    void testRefreshPartners() throws Exception {
        testHttpRequestWithExpectedResult(CACHE_MANAGER_PATH + CACHE_MANAGER_REFRESH_PARTNERS_PATH, HttpMethod.POST, HttpStatus.NO_CONTENT,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test refresh cache partners", null);


    }

    /**
     * Test refresh partnerMetas of cache
     *
     * @throws Exception
     */
    @Test
    void testRefresh() throws Exception {

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("partnerId", "b814c97e-df56-4651-ac50-11525537964f");
        parameters.add("actionName", ActionsEnum.ASSET_SEARCH.value);
        testHttpRequestWithExpectedResult(CACHE_MANAGER_PATH + CACHE_MANAGER_REFRESH_PATH, HttpMethod.POST, HttpStatus.NO_CONTENT,
                null, null,
                JsonResponseTypeEnum.EMPTY, "Test refresh cache partners", parameters);

        testHttpRequestWithExpectedResult(CACHE_MANAGER_PATH + CACHE_MANAGER_REFRESH_PATH + "?partnerId=b814c97e-df56-4651-ac50-11525537964f&actionName=ASSET_SEARCH", HttpMethod.POST, HttpStatus.NO_CONTENT,
                CACHE_POSITIONS_REQUEST_JSON, null,
                JsonResponseTypeEnum.EMPTY, "Test refresh cache partners", null);

        testHttpRequestWithExpectedResult(CACHE_MANAGER_PATH + CACHE_MANAGER_REFRESH_PATH + "?partnerId=b814c97e-df56-4651-ac50-11525537964f&actionName=ASSET_SEARCH", HttpMethod.POST, HttpStatus.BAD_REQUEST,
                CACHE_GET_STATUS_OK_JSON, null,
                JsonResponseTypeEnum.EMPTY, "Test refresh cache partners", null);


    }


    private void testHttpRequestWithExpectedResult(String uri, HttpMethod httpMethod,
                                                   HttpStatus httpStatusExpectedResult, String requestPayloadPath,
                                                   String expectedResultPath, JsonResponseTypeEnum resulType,
                                                   final String message, MultiValueMap<String, String> parameters) throws Exception {
        doNothing().when(cacheUtil).synchronizeCacheStatus();
        doReturn(cacheUtil.getCacheStatus()).when(cacheManagerService).putCacheStatus(false);
        doReturn(cacheUtil.getCacheStatus()).when(cacheManagerService).getCacheStatus();

        // preparing the service call and expected elements
        ResultMatcher mockResultMatcher = WsTestUtil.getResultMatcher(httpStatusExpectedResult);
        String requestPayloadContent = StringUtils.isNotBlank(requestPayloadPath) ? WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + requestPayloadPath) : "";
        MockHttpServletRequestBuilder requestBuilder = WsTestUtil.getMockHttpServletRequestBuilder("", uri, httpMethod, requestPayloadContent);
        MediaType responseContentType = WsTestUtil.getMediaType(httpMethod);

        // trigger the call and check expected encoding and http status code
        String content;
        if (parameters == null || parameters.isEmpty()) {
            if (responseContentType != null && expectedResultPath != null) {
                content = this.mockMvc.
                        perform(requestBuilder)
                        .andExpect(mockResultMatcher)
                        .andExpect(MockMvcResultMatchers.content().contentType(responseContentType))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
            } else {
                content = this.mockMvc.perform(requestBuilder).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
            }
        } else {
            if (expectedResultPath != null) {
                switch (httpMethod) {
                    case GET:
                        content = this.mockMvc.perform(get("" + uri).params(parameters)).andExpect(mockResultMatcher).andExpect(MockMvcResultMatchers.content().contentType(responseContentType)).andReturn().getResponse().getContentAsString();
                        break;
                    case PUT:
                        content = this.mockMvc.perform(put("" + uri).params(parameters)).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
                        break;
                    case POST:
                        content = this.mockMvc.perform(post("" + uri).params(parameters)).andExpect(mockResultMatcher).andExpect(MockMvcResultMatchers.content().contentType(responseContentType)).andReturn().getResponse().getContentAsString();
                        break;

                    default:
                        content = null;
                }
            } else {
                switch (httpMethod) {
                    case GET:
                        content = this.mockMvc.perform(get("" + uri).params(parameters)).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
                        break;
                    case PUT:
                        content = this.mockMvc.perform(put("" + uri).params(parameters)).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
                        break;
                    case PATCH:
                        content = this.mockMvc.perform(patch("" + uri).params(parameters)).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
                        break;
                    case POST:
                        content = this.mockMvc.perform(post("" + uri).params(parameters)).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
                        break;

                    default:
                        content = null;
                }
            }


        }

        String pathFile = IT_RESOURCES_PATH + expectedResultPath;
        // get the expected json response as string
        final String jsonRef = StringUtils.isBlank(expectedResultPath) ? "" : WsTestUtil.readJsonFromFilePath(pathFile);

        // compare the expected json with the result
        if (resulType != null) {
            switch (resulType) {
                case JSON_ARRAY:
                    JSONArray expectedJsonArray = new JSONArray(jsonRef);
                    JSONArray resultJsonArray = new JSONArray(content);
                    Assertions.assertEquals(expectedJsonArray.toString(4), resultJsonArray.toString(4), message);
                    break;
                case JSON_OBJECT:
                    JSONObject expectedJsonObject = new JSONObject(jsonRef);
                    JSONObject resultJsonObject = new JSONObject(content);

                    Assertions.assertEquals(expectedJsonObject.toString(4), resultJsonObject.toString(4), message);

                    break;
                default:
                    break;
            }
            log.info("Test executed : {}", message);
        }
    }
}
