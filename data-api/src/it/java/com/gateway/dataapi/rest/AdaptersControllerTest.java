package com.gateway.dataapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.nio.charset.StandardCharsets;

import static com.gateway.commonapi.constants.DataApiPathDict.ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class AdaptersControllerTest extends DataApiITCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String ADAPTERS_EXPECTED_GET_ADAPTERS_OK_JSON = "adapters/expected/getAdapters_ok.json";
    public static final String ADAPTERS_EXPECTED_GET_ADAPTERS_BY_ID_OK_JSON = "adapters/expected/getAdapters_by_id_ok.json";
    public static final String ADAPTERS_EXPECTED_GET_ADAPTERS_BY_ID_NOT_FOUND_JSON = "adapters/expected/getAdapters_by_id_not_found.json";
    public static final String ADAPTERS_REQUEST_POST_ADAPTERS_OK_JSON = "adapters/request/postAdapters_ok.json";
    public static final String ADAPTERS_EXPECTED_POST_ADAPTERS_OK_JSON = "adapters/expected/postAdapters_ok.json";
    public static final String ADAPTERS_EXPECTED_DELETE_ADAPTERS_BY_ID_CONFLICT_EXCEPTION_JSON = "adapters/expected/deleteAdapter_by_id_conflict_exception.json";

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
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build();
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, this.objectMapper);
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);
    }

    /**
     * test GET All Adapters
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetAdapters() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.ADAPTERS_BASE_PATH + DataApiPathDict.ADAPTERS_PATH, HttpMethod.GET, HttpStatus.OK, null,
                ADAPTERS_EXPECTED_GET_ADAPTERS_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get list of adapters", true, false, null);
    }

    /**
     * test GET Adapters by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetAdaptersId() throws Exception {
        final String adapterId = "40142f60-9694-479f-a6cd-28b199b5e240";
        testHttpRequestWithExpectedResult
                (CommonUtils.placeholderFormat(DataApiPathDict.ADAPTERS_BASE_PATH + DataApiPathDict.ADAPTER_PATH, ID, adapterId)
                        ,HttpMethod.GET,
                        HttpStatus.OK, null, ADAPTERS_EXPECTED_GET_ADAPTERS_BY_ID_OK_JSON,
                        JsonResponseTypeEnum.JSON_OBJECT, "Test GET /adapters by id", false, false, null);
    }

    /**
     * test GET not found adapters by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetAdaptersByIdNotfound() throws Exception {
        final String adapterId = "00000000-0000-0001-0000-000000000666";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.ADAPTERS_BASE_PATH + DataApiPathDict.ADAPTER_PATH, ID, adapterId), HttpMethod.GET,
                HttpStatus.NOT_FOUND, null, ADAPTERS_EXPECTED_GET_ADAPTERS_BY_ID_NOT_FOUND_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test GET adapters by id with not found exception", true, true, null);
    }

    /**
     * test DELETE partnerActions
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeleteAdaptersById() throws Exception {
        final String adapterId = "40142f60-9694-479f-a6cd-28b199b5e241";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.ADAPTERS_BASE_PATH + DataApiPathDict.ADAPTER_PATH, ID, adapterId), HttpMethod.DELETE,
                HttpStatus.NO_CONTENT, null, "", null, "Test to delete adapters by id", false, false, null);
    }

    /**
     * test POST Adapters
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostAdapters() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.ADAPTERS_BASE_PATH + DataApiPathDict.ADAPTERS_PATH, HttpMethod.POST, HttpStatus.CREATED,
                ADAPTERS_REQUEST_POST_ADAPTERS_OK_JSON, ADAPTERS_EXPECTED_POST_ADAPTERS_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create adapters", true, false, null);

    }

    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeleteByIdNotFound() throws Exception{
        final String adapterId = "00000000-9694-479f-a6cd-28b199b5e241";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.ADAPTERS_BASE_PATH + DataApiPathDict.ADAPTER_PATH, ID, adapterId), HttpMethod.DELETE,
                HttpStatus.NOT_FOUND, null, "", null, "Test to delete adapters by id", false, false, null);
    }

    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeleteByIdConflictException() throws Exception{
        final String adapterId = "40142f60-9694-479f-a6cd-28b199b5e240";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.ADAPTERS_BASE_PATH + DataApiPathDict.ADAPTER_PATH, ID, adapterId), HttpMethod.DELETE,
                HttpStatus.CONFLICT, null, ADAPTERS_EXPECTED_DELETE_ADAPTERS_BY_ID_CONFLICT_EXCEPTION_JSON, null, "Test to delete adapters by id", false, false, null);
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