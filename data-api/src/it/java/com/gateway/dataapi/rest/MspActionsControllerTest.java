package com.gateway.dataapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.dataapi.DataApiITCase;
import com.gateway.dataapi.util.constant.DataApiPathDict;
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

class MspActionsControllerTest extends DataApiITCase {
    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String MSP_ACTIONS_EXPECTED_GET_MSP_ACTIONS_OK_JSON = "mspActions/expected/getMspActions_ok.json";
    public static final String MSP_ACTIONS_EXPECTED_GET_MSP_ACTIONS_BY_ID_OK_JSON = "mspActions/expected/getMspActions_by_id_ok.json";
    public static final String MSP_ACTIONS_EXPECTED_GET_MSP_ACTIONS_BY_ID_NOT_FOUND_JSON = "mspActions/expected/getMspActions_by_id_not_found.json";
    public static final String MSP_ACTIONS_REQUEST_POST_MSP_ACTIONS_OK_JSON = "mspActions/request/postMspActions_ok.json";
    public static final String MSP_ACTIONS_EXPECTED_POST_MSP_ACTIONS_OK_JSON = "mspActions/expected/postMspActions_ok.json";
    public static final String MSP_ACTIONS_REQUEST_PUT_MSP_ACTIONS_OK_JSON = "mspActions/request/putMspActions_ok.json";
    public static final String MSP_ACTIONS_EXPECTED_GET_MSP_ACTIONS_AFTER_PUT_OK_JSON = "mspActions/expected/getMspActions_afterput_ok.json";

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
     * test GET All mspActions
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetMspActions() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.MSP_ACTIONS_BASE_PATH, HttpMethod.GET, HttpStatus.OK, null,
                MSP_ACTIONS_EXPECTED_GET_MSP_ACTIONS_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get list of msp-actions", true, false, null);
    }

    /**
     * test GET mspActions by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetMspActionsId() throws Exception {
        final String mspActionId = "e222f848-0f53-46fd-97fa-6b1f9f3db265";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.MSP_ACTIONS_BASE_PATH + DataApiPathDict.MSP_ACTION_PATH, ID, mspActionId), HttpMethod.GET,
                HttpStatus.OK, null, MSP_ACTIONS_EXPECTED_GET_MSP_ACTIONS_BY_ID_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test GET /msp-actions by id", false, false, null);
    }

    /**
     * test GET not found mspActions by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetMspActionsById_notfound() throws Exception {
        final String mspActionId = "00000000-0000-0001-0000-000000000666";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.MSP_ACTIONS_BASE_PATH + DataApiPathDict.MSP_ACTION_PATH, ID, mspActionId), HttpMethod.GET,
                HttpStatus.NOT_FOUND, null, MSP_ACTIONS_EXPECTED_GET_MSP_ACTIONS_BY_ID_NOT_FOUND_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test GET /msp-actions by id with not found exception", true, true, null);
    }

    /**
     * test DELETE mspActions
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeleteMspActionsById() throws Exception {
        final String mspActionId = "e222f848-0f53-46fd-97fa-6b1f9f3db265";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.MSP_ACTIONS_BASE_PATH + DataApiPathDict.MSP_ACTION_PATH, ID, mspActionId), HttpMethod.DELETE,
                HttpStatus.NO_CONTENT, null, "", null, "Test to delete msp-actions by id", false, false, null);
    }

    /**
     * test POST mspActions
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostMspActions() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.MSP_ACTIONS_BASE_PATH, HttpMethod.POST, HttpStatus.CREATED,
                MSP_ACTIONS_REQUEST_POST_MSP_ACTIONS_OK_JSON, MSP_ACTIONS_EXPECTED_POST_MSP_ACTIONS_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create msp-actions", true, false, null);

    }

    /**
     * test PUT mspActions
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPutMspActionsById() throws Exception {
        final String mspActionId = "e222f848-0f53-46fd-97fa-6b1f9f3db265";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.MSP_ACTIONS_BASE_PATH + DataApiPathDict.MSP_ACTION_PATH, ID, mspActionId), HttpMethod.PUT,
                HttpStatus.NO_CONTENT, MSP_ACTIONS_REQUEST_PUT_MSP_ACTIONS_OK_JSON, "", null,
                "Test to put msp-actions ", false, false, null);

        // call get to get the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.MSP_ACTIONS_BASE_PATH + DataApiPathDict.MSP_ACTION_PATH, ID, mspActionId), HttpMethod.GET,
                HttpStatus.OK, null, MSP_ACTIONS_EXPECTED_GET_MSP_ACTIONS_AFTER_PUT_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to get msp-actions after put", false, false, null);
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
