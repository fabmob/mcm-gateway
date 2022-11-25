package com.gateway.dataapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.cache.GatewayParamStatusManager;
import com.gateway.commonapi.constants.DataApiPathDict;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import java.nio.charset.StandardCharsets;

import static com.gateway.commonapi.constants.DataApiPathDict.PARAM_KEY;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class GatewayParamsControllerTest extends DataApiITCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";

    public static final String GTW_PARAMS_EXPECTED_GET_ALL = "gatewayParams/expected/getAllGatewayParams_ok.json";
    public static final String GTW_PARAMS_EXPECTED_GET_BY_ID = "gatewayParams/expected/getGatewayParams_by_id_ok.json";
    public static final String GTW_PARAMS_EXPECTED_GET_BY_ID_ERROR = "gatewayParams/expected/paramKeyNotFound.json";
    public static final String GTW_PARAMS_EXPECTED_POST_OK_JSON = "gatewayParams/expected/postGatewayParams.json";
    public static final String GTW_PARAMS_UPDATE_JSON = "gatewayParams/request/updateGatewayParams.json";

    @Value("${gateway.service.dataapi.baseUrl}")
    private String uri;
    /*
     * Mock MVC
     */
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GatewayParamStatusManager gatewayParamStatusManager;

    @Autowired
    private ObjectMapper objectMapper;

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
     * test GET All Gateway Params
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetAllGatewayParams() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.GATEWAY_PARAMS_BASE_PATH, HttpMethod.GET, HttpStatus.OK, null,
                GTW_PARAMS_EXPECTED_GET_ALL, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get list of Gateway Params", true, false, null);
    }

    /**
     * test GET Gateway Params by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetGatewayParamById() throws Exception {
        final String paramKey = "CACHE_ACTIVATION";
        final String unknownKey = "test";
        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(DataApiPathDict.GATEWAY_PARAMS_BASE_PATH + DataApiPathDict.GATEWAY_PARAM_PATH, PARAM_KEY, paramKey), HttpMethod.GET,
                HttpStatus.OK, null, GTW_PARAMS_EXPECTED_GET_BY_ID, JsonResponseTypeEnum.JSON_OBJECT,
                "Test GET Gateway Params by param key", false, false, null);

        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(DataApiPathDict.GATEWAY_PARAMS_BASE_PATH + DataApiPathDict.GATEWAY_PARAM_PATH, PARAM_KEY, unknownKey), HttpMethod.GET,
                HttpStatus.NOT_FOUND, null, GTW_PARAMS_EXPECTED_GET_BY_ID_ERROR, JsonResponseTypeEnum.JSON_OBJECT,
                "Test GET Gateway Params by param key not found", false, false, null);
    }

    /**
     * test POST Gateway Params
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testAddGatewayParam() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.GATEWAY_PARAMS_BASE_PATH, HttpMethod.POST, HttpStatus.CREATED,
                GTW_PARAMS_EXPECTED_POST_OK_JSON, GTW_PARAMS_EXPECTED_POST_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create Gateway Params", true, false, null);
    }

    /**
     * test update Gateway Params
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testUpdateGatewayParams() throws Exception {
        final String paramKey = "CACHE_ACTIVATION";
        final String unknownKey = "test";
        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(DataApiPathDict.GATEWAY_PARAMS_BASE_PATH + DataApiPathDict.GATEWAY_PARAM_PATH, PARAM_KEY, paramKey), HttpMethod.PUT,
                HttpStatus.NO_CONTENT, GTW_PARAMS_UPDATE_JSON, null, JsonResponseTypeEnum.EMPTY,
                "Test update Gateway Params by param key", false, false, null);

        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(DataApiPathDict.GATEWAY_PARAMS_BASE_PATH + DataApiPathDict.GATEWAY_PARAM_PATH, PARAM_KEY, paramKey), HttpMethod.GET,
                HttpStatus.OK, null, GTW_PARAMS_UPDATE_JSON, JsonResponseTypeEnum.JSON_OBJECT,
                "Test update Gateway Params by param key", false, false, null);

        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(DataApiPathDict.GATEWAY_PARAMS_BASE_PATH + DataApiPathDict.GATEWAY_PARAM_PATH, PARAM_KEY, unknownKey), HttpMethod.PUT,
                HttpStatus.NOT_FOUND, GTW_PARAMS_UPDATE_JSON, GTW_PARAMS_EXPECTED_GET_BY_ID_ERROR, JsonResponseTypeEnum.JSON_OBJECT,
                "Test update Gateway Params by param key not found", false, false, null);
    }

    /**
     * test Delete Gateway Params by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeleteGatewayParams() throws Exception {
        final String paramKey = "CACHE_ACTIVATION";
        final String unknownKey = "test";
        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(DataApiPathDict.GATEWAY_PARAMS_BASE_PATH + DataApiPathDict.GATEWAY_PARAM_PATH, PARAM_KEY, paramKey), HttpMethod.DELETE,
                HttpStatus.NO_CONTENT, null, null, JsonResponseTypeEnum.EMPTY,
                "Test DELETE Gateway Params by param key", false, false, null);

        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(DataApiPathDict.GATEWAY_PARAMS_BASE_PATH + DataApiPathDict.GATEWAY_PARAM_PATH, PARAM_KEY, unknownKey), HttpMethod.DELETE,
                HttpStatus.NOT_FOUND, null, GTW_PARAMS_EXPECTED_GET_BY_ID_ERROR, JsonResponseTypeEnum.JSON_OBJECT,
                "Test GET Gateway Params by param key not found", false, false, null);
    }


    private String testHttpRequestWithExpectedResult(String uri, HttpMethod httpMethod,
                                                     HttpStatus httpStatusExpectedResult, String requestPayloadPath, String expectedResultPath,
                                                     JsonResponseTypeEnum resulType, final String message, boolean ignoreUUID, boolean ignoreTimeStamp,
                                                     MultiValueMap<String, String> parameters) throws Exception {


        doNothing().when(gatewayParamStatusManager).synchronizeCacheStatus();

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

                    if (expectedJsonObject.has("callId")) {
                        expectedJsonObject.remove("callId");
                        expectedJsonObject.remove("timestamp");
                    }
                    if (resultJsonObject.has("callId")) {
                        resultJsonObject.remove("callId");
                        resultJsonObject.remove("timestamp");
                    }

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
