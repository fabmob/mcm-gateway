package com.gateway.dataapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.dataapi.DataApiITCase;
import com.gateway.dataapi.util.enums.JsonResponseTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;


import static com.gateway.dataapi.util.constant.DataApiPathDict.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class CacheParamControllerTest extends DataApiITCase {
    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String GET_ALL_CACHE_PARAM_OK_JSON = "cacheParam/expected/getAllCacheParam_ok.json";
    public static final String GET_ALL_CACHE_PARAM_WITH_CRITERIA_OK_JSON = "cacheParam/expected/getByCriteria_ok.json";

    public static final String GET_CACHE_PARAM_BY_PK_OK_JSON = "cacheParam/expected/getCacheParam_by_pk_ok.json";

    public static final String POST_CACHE_PARAM_OK_JSON = "cacheParam/request/postCacheParam_ok.json";
    public static final String EXPECTED_POST_CACHE_PARAM_OK_JSON = "cacheParam/expected/postCacheParam_ok.json";
    public static final String PUT_CACHE_PARAM_OK_JSON = "cacheParam/request/putCacheParam_ok.json";
    public static final String GET_CACHE_PARAM_AFTER_PUT_OK_JSON = "cacheParam/expected/getCacheParam_afterput_ok.json";


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, this.objectMapper);
    }

    /**
     * test GET All cacheParam
     *
     * @throws Exception e
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetAllCacheParam() throws Exception {
        testHttpRequestWithExpectedResult(CACHE_PARAM_BASE_PATH, HttpMethod.GET, HttpStatus.OK, null,
                GET_ALL_CACHE_PARAM_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get list of cache params", false, false, null);
    }

    /**
     * test GET All with criteria cacheParam
     *
     * @throws Exception e
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetByCriteria() throws Exception {
        String uri = CACHE_PARAM_BASE_PATH + "?actionType=VEHICULE_SEARCH";
        testHttpRequestWithExpectedResult(uri, HttpMethod.GET, HttpStatus.OK, null,
                GET_ALL_CACHE_PARAM_WITH_CRITERIA_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get list with criteria of cache params", false, false, null);
    }

    /**
     * test GET CacheParam by ID
     *
     * @throws Exception e
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetCacheParamByID() throws Exception {
        String uri = "/cache-params/{cacheParamId}";
        final String cacheParamId = "885a4c3d-b459-41d6-b1c1-e30a82ea777b";
        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(uri, "cacheParamId", cacheParamId), HttpMethod.GET,
                HttpStatus.OK, null, GET_CACHE_PARAM_BY_PK_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT,
                "Test GET /cache-param by PK", false, false, null);
    }

    /**
     * test POST CacheParam
     *
     * @throws Exception e
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostCacheParam() throws Exception {
        testHttpRequestWithExpectedResult(CACHE_PARAM_BASE_PATH, HttpMethod.POST, HttpStatus.CREATED,
                POST_CACHE_PARAM_OK_JSON, EXPECTED_POST_CACHE_PARAM_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create cache param", true, false, null);

    }



    /**
     * test PUT CacheParam
     *
     * @throws Exception e
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPutCacheParamByID() throws Exception {
        final String cacheParamId = "885a4c3d-b459-41d6-b1c1-e30a82ea777b";
        String uri = "/cache-params/{cacheParamId}";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(uri, "cacheParamId", cacheParamId), HttpMethod.PUT,
                HttpStatus.NO_CONTENT, PUT_CACHE_PARAM_OK_JSON, "", null, "Test to put CacheParam ",
                false, false, null);


        String uriGet = "/cache-params/{cacheParamId}";
        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(uriGet, "cacheParamId", cacheParamId), HttpMethod.GET,
                HttpStatus.OK, null, GET_CACHE_PARAM_AFTER_PUT_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT,
                "Test GET /cache-param by PK", false, false, null);
       }



    /**
     * test Delete Cache Param by ID
     *
     * @throws Exception e
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeleteCacheParamByID() throws Exception {
        final String cacheParamId = "885a4c3d-b459-41d6-b1c1-e30a82ea777b";
        String uri = "/cache-params/{cacheParamId}";

        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(uri, "cacheParamId", cacheParamId), HttpMethod.DELETE,
                HttpStatus.NO_CONTENT, null, null, null,
                "Test delete /cache-param by ID", false, false, null);
    }
    
    

    
    private String testHttpRequestWithExpectedResult(String uri, HttpMethod httpMethod,
                                                     HttpStatus httpStatusExpectedResult, String requestPayloadPath, String expectedResultPath,
                                                     JsonResponseTypeEnum resulType, final String message, boolean ignoreUUID, boolean ignoreTimeStamp,
                                                     MultiValueMap<String, String> parameters) throws Exception {

        String responseCall;
        ResultMatcher mockResultMatcher = WsTestUtil.getResultMatcher(httpStatusExpectedResult);
        String requestPayloadContent = StringUtils.isNotBlank(requestPayloadPath)
                ? WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + requestPayloadPath)
                : "";
        String globalPath = StringUtils.EMPTY;
        MockHttpServletRequestBuilder requestBuilder = WsTestUtil
                .getMockHttpServletRequestBuilder(globalPath, uri, httpMethod, requestPayloadContent);
        MediaType responseContentType = WsTestUtil.getMediaType(httpMethod);
        ResultMatcher contentTypeResultMatcher = WsTestUtil.getContentTypeResultMatcher(responseContentType,
                httpStatusExpectedResult);

        // trigger the web service call
        String content;
        if (parameters == null || parameters.isEmpty()) {
            if (responseContentType != null && contentTypeResultMatcher != null) {
                content = this.mockMvc.perform(requestBuilder).andExpect(mockResultMatcher)
                        .andExpect(contentTypeResultMatcher).andReturn().getResponse().getContentAsString();
            } else {
                content = this.mockMvc.perform(requestBuilder).andExpect(mockResultMatcher).andReturn().getResponse()
                        .getContentAsString();
            }
        } else {
            content = this.mockMvc.perform(get(globalPath + uri).params(parameters))
                    .andExpect(mockResultMatcher).andExpect(contentTypeResultMatcher).andReturn().getResponse()
                    .getContentAsString();
        }
        responseCall = content;
        String pathFile = IT_RESOURCES_PATH + expectedResultPath;
        String jsonRef = StringUtils.isBlank(expectedResultPath) ? "" : WsTestUtil.readJsonFromFilePath(pathFile);

        // if we need to hidde UUID we replace with fake value
        if (Boolean.TRUE.equals(ignoreUUID)) {
            jsonRef = WsTestUtil.ignoreUUID(jsonRef);
            content = WsTestUtil.ignoreUUID(content);
        }

        // if we need to hidde TimeStamp we replace with fake value
        if (Boolean.TRUE.equals(ignoreTimeStamp)) {
            jsonRef = WsTestUtil.ignoreTimeStamp(jsonRef);
            content = WsTestUtil.ignoreTimeStamp(content);
        }
        if (resulType != null) {
            switch (resulType) {
                case JSON_ARRAY:
                    JSONArray expectedJsonArray = new JSONArray(jsonRef);
                    JSONArray resultJsonArray = new JSONArray(content);
                    // reformat json and compare to have a string comparison result
                    Assertions.assertEquals(expectedJsonArray.toString(4), resultJsonArray.toString(4), message);
                    break;
                case JSON_OBJECT:

                    // in the case where we test the method post, we generate the ids randomly. we
                    // will therefore add this boolean , if it is true in the function,
                    // when we retrieve the contents whether it is the result or expected we will
                    // replace the UUIDs expected by the UUIDs result
                    JSONObject expectedJsonObject = new JSONObject(jsonRef);
                    JSONObject resultJsonObject = new JSONObject(content);

                    // reformat json and compare to have a string comparison result
                    Assertions.assertEquals(expectedJsonObject.toString(4), resultJsonObject.toString(4), message);
                    break;
                case EMPTY:
                    break;
                default:
                    break;
            }
        }
        return responseCall;
    }
}
