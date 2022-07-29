package com.gateway.dataapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.tests.enums.JsonResponseTypeEnum;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.dataapi.DataApiITCase;
import com.gateway.dataapi.util.constant.DataApiPathDict;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static com.gateway.dataapi.util.constant.DataApiPathDict.ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class TokenControllerTest extends DataApiITCase {
    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String TOKEN_EXPECTED_GET_TOKEN_OK_JSON = "token/expected/getToken_ok.json";
    public static final String TOKEN_EXPECTED_GET_TOKEN_BY_ID_OK_JSON = "token/expected/getToken_by_id_ok.json";
    public static final String TOKEN_EXPECTED_GET_TOKEN_BY_MSP_META_ID_OK_JSON = "token/expected/getToken_by_MspMeta_id_ok.json";
    public static final String TOKEN_EXPECTED_GET_TOKEN_BY_ID_NOT_FOUND_JSON = "token/expected/getToken_by_id_not_found.json";

    public static final String TOKEN_REQUEST_POST_TOKEN_OK_JSON = "token/request/postToken_ok.json";
    public static final String TOKEN_EXPECTED_POST_TOKEN_OK_JSON = "token/expected/postToken_ok.json";
    public static final String TOKEN_REQUEST_PUT_TOKEN_OK_JSON = "token/request/putToken_ok.json";
    public static final String TOKEN_EXPECTED_GET_TOKEN_AFTER_PUT_OK_JSON = "token/expected/getToken_afterput_ok.json";

    /*
     * Mock MVC
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
     * test GET token by id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetTokensById() throws Exception {
        final String tokenId = "c13684f5-5438-4633-9af2-1e835bb7bc9e";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.TOKENS_BASE_PATH +DataApiPathDict.TOKEN_PATH, ID, tokenId), HttpMethod.GET,
                HttpStatus.OK, null, TOKEN_EXPECTED_GET_TOKEN_BY_ID_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT,
                "Test GET /tokens by id", false, false, null);
    }

    /**
     * test GET token by MspMeta id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetTokensByMspMetaId() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("mspMetaId", "b814c97e-df56-4651-ac50-11525537964f");
        testHttpRequestWithExpectedResult(DataApiPathDict.TOKENS_BASE_PATH +DataApiPathDict.TOKENS_PATH, HttpMethod.GET, HttpStatus.OK, null,
                TOKEN_EXPECTED_GET_TOKEN_BY_MSP_META_ID_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test token by mspMetaId",
                true, false, parameters);
    }

    /**
     * test GET token by id not found
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetTokensByIdNotFound() throws Exception {
        final String tokenId = "00000000-0000-0001-0000-000000000666";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.TOKENS_BASE_PATH +DataApiPathDict.TOKEN_PATH, ID, tokenId), HttpMethod.GET,
                HttpStatus.NOT_FOUND, null, TOKEN_EXPECTED_GET_TOKEN_BY_ID_NOT_FOUND_JSON, JsonResponseTypeEnum.JSON_OBJECT,
                "Test GET token by id with not found exception", true, true, null);

    }

    /**
     * test post token
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostToken() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.TOKENS_BASE_PATH +DataApiPathDict.TOKENS_PATH, HttpMethod.POST, HttpStatus.CREATED,
                TOKEN_REQUEST_POST_TOKEN_OK_JSON, TOKEN_EXPECTED_POST_TOKEN_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT,
                "Test to create tokens", true, false, null);

    }

    /**
     * test delete token
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeleteToken() throws Exception {
        final String tokenId = "c13684f5-5438-4633-9af2-1e835bb7bc9e";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.TOKENS_BASE_PATH +DataApiPathDict.TOKEN_PATH, ID, tokenId), HttpMethod.DELETE,
                HttpStatus.NO_CONTENT, null, "", null, "Test to delete token by id", false, false, null);
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
        ResultMatcher contentTypeResultMatcher = WsTestUtil.getContentTypeResultMatcher(responseContentType, httpStatusExpectedResult);

        // trigger the web service call
        String content;
        if (parameters == null || parameters.isEmpty()) {
            if (responseContentType != null && contentTypeResultMatcher != null) {
                content = this.mockMvc.perform(requestBuilder).andExpect(mockResultMatcher).andExpect(contentTypeResultMatcher).andReturn().getResponse().getContentAsString();
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