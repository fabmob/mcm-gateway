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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static com.gateway.commonapi.constants.DataApiPathDict.ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class PartnerCallsControllerTest extends DataApiITCase {
    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String PARTNER_CALLS_EXPECTED_GET_PARTNER_CALLS_OK_JSON = "partnerCalls/expected/getPartnerCalls_ok.json";
    public static final String PARTNER_CALLS_EXPECTED_GET_PARTNER_CALLS_BY_ID_OK_JSON = "partnerCalls/expected/getPartnerCalls_by_id_ok.json";
    public static final String PARTNER_CALLS_EXPECTED_GET_PARTNER_CALLS_BY_PARTNER_META_ID_OK_JSON = "partnerCalls/expected/getPartnerCalls_by_PartnerMeta_id_ok.json";
    public static final String PARTNER_CALLS_EXPECTED_GET_PARTNER_CALLS_BY_ID_NOT_FOUND_JSON = "partnerCalls/expected/getPartnerCalls_by_id_not_found.json";
    public static final String PARTNER_CALLS_REQUEST_POST_PARTNER_CALLS_OK_JSON = "partnerCalls/request/postPartnerCalls_ok.json";
    public static final String PARTNER_CALLS_EXPECTED_POST_PARTNER_CALLS_OK_JSON = "partnerCalls/expected/postPartnerCalls_ok.json";
    public static final String PARTNER_CALLS_REQUEST_PUT_PARTNER_CALLS_OK_JSON = "partnerCalls/request/putPartnerCalls_ok.json";
    public static final String PARTNER_CALLS_EXPECTED_GET_PARTNER_CALLS_AFTER_PUT_OK_JSON = "partnerCalls/expected/getPartnerCalls_afterput_ok.json";
    public static final String PARTNER_CALLS_REQUEST_POST_PARTNER_CALLS_WITH_BODY_ID_JSON = "partnerCalls/request/postPartnerCalls_ko_bodyID.json";
    public static final String PARTNER_CALLS_EXPECTED_POST_PARTNER_CALLS_KO_BODY_ID_JSON = "partnerCalls/expected/postPartnerCalls_ko_bodyID.json";
    public static final String PARTNER_CALLS_REQUEST_POST_PARTNER_CALLS_WITH_HEADERS_ID_JSON = "partnerCalls/request/postPartnerCalls_ko_headersID.json";
    public static final String PARTNER_CALLS_EXPECTED_POST_PARTNER_CALLS_KO_HEADERS_ID_JSON = "partnerCalls/expected/postPartnerCalls_ko_headersID.json";
    private static final String PARTNER_CALLS_REQUEST_POST_PARTNER_CALLS_WITH_PARAMS_ID_JSON = "partnerCalls/request/postPartnerCalls_ko_paramsID.json";
    private static final String PARTNER_CALLS_EXPECTED_POST_PARTNER_CALLS_KO_PARAMS_ID_JSON = "partnerCalls/expected/postPartnerCalls_ko_paramsID.json";
    private static final String PARTNER_CALLS_REQUEST_POST_PARTNER_CALLS_WITH_PARAMS_MULTI_CALLS_ID_JSON = "partnerCalls/request/postPartnerCalls_ko_paramsMultiCallsID.json";
    private static final String PARTNER_CALLS_EXPECTED_POST_PARTNER_CALLS_KO_PARAMS_MULTI_CALLS_ID_JSON = "partnerCalls/expected/postPartnerCalls_ko_paramsMultiCallsID.json";

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
     * test GET All PartnerCalls
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerCalls() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALLS_PATH, HttpMethod.GET, HttpStatus.OK, null,
                PARTNER_CALLS_EXPECTED_GET_PARTNER_CALLS_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY, "Test get list of Partner-calls",
                true, false, null);
    }

    /**
     * test GET PartnerCalls by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerCallsId() throws Exception {
        final String partnerCallId = "cfceccab-e4d0-4622-bde1-cd2dc73088c4";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALL_PATH, ID, partnerCallId), HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_CALLS_EXPECTED_GET_PARTNER_CALLS_BY_ID_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT,
                "Test GET /partner-calls by id", false, false, null);
    }

    /**
     * test GET PartnerCalls with PartnerMetaId
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerCallsByPartnerMetaId() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("partnerActionId", "e222f848-0f53-46fd-97fa-6b1f9f3db265");

        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALLS_PATH, HttpMethod.GET, HttpStatus.OK, null,
                PARTNER_CALLS_EXPECTED_GET_PARTNER_CALLS_BY_PARTNER_META_ID_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get list of partner-calls", true, false, parameters);
    }

    /**
     * test GET not found PartnerCalls by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerCallsByIdNotfound() throws Exception {
        final String partnerCallId = "00000000-0000-0001-0000-000000000666";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALL_PATH, ID, partnerCallId), HttpMethod.GET,
                HttpStatus.NOT_FOUND, null, PARTNER_CALLS_EXPECTED_GET_PARTNER_CALLS_BY_ID_NOT_FOUND_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test GET /partner-calls by id with not found exception", true, true, null);
    }

    /**
     * test POST PartnerCalls
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostPartnerCalls() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALLS_PATH, HttpMethod.POST, HttpStatus.CREATED,
                PARTNER_CALLS_REQUEST_POST_PARTNER_CALLS_OK_JSON, PARTNER_CALLS_EXPECTED_POST_PARTNER_CALLS_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create partner-calls", true, false, null);

    }

    /**
     * test PUT PartnerCalls
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPutPartnerCallsById() throws Exception {
        final String partnerCallId = "cfceccab-e4d0-4622-bde1-cd2dc73088c4";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALL_PATH, ID, partnerCallId), HttpMethod.PUT,
                HttpStatus.NO_CONTENT, PARTNER_CALLS_REQUEST_PUT_PARTNER_CALLS_OK_JSON, "", null, "Test to put partner-calls ",
                false, false, null);

        // call get to get the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALL_PATH, ID, partnerCallId), HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_CALLS_EXPECTED_GET_PARTNER_CALLS_AFTER_PUT_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to get partner-calls after put", false, false, null);
    }

    /**
     * test DELETE PartnerCalls
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeletePartnerCallsById() throws Exception {
        final String partnerCallId = "cfceccab-e4d0-4622-bde1-cd2dc73088c4";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALL_PATH, ID, partnerCallId), HttpMethod.DELETE,
                HttpStatus.NO_CONTENT, null, "", null, "Test to delete partner-calls by id", false, false, null);
    }

    /**
     * test POST PartnerCalls with BodyId
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostPartnerCallsWithBodyId() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALLS_PATH, HttpMethod.POST, HttpStatus.BAD_REQUEST,
                PARTNER_CALLS_REQUEST_POST_PARTNER_CALLS_WITH_BODY_ID_JSON, PARTNER_CALLS_EXPECTED_POST_PARTNER_CALLS_KO_BODY_ID_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create partner-calls with body id ", true, true, null);

    }

    /**
     * test POST PartnerCalls with HeadersId
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostPartnerCallsWithHeadersId() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALLS_PATH, HttpMethod.POST, HttpStatus.BAD_REQUEST,
                PARTNER_CALLS_REQUEST_POST_PARTNER_CALLS_WITH_HEADERS_ID_JSON, PARTNER_CALLS_EXPECTED_POST_PARTNER_CALLS_KO_HEADERS_ID_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create partner-calls with headers id ", true, true, null);

    }

    /**
     * test POST partnerCalls with ParamsId
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostPartnerCallsWithParamsId() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALLS_PATH, HttpMethod.POST, HttpStatus.BAD_REQUEST,
                PARTNER_CALLS_REQUEST_POST_PARTNER_CALLS_WITH_PARAMS_ID_JSON, PARTNER_CALLS_EXPECTED_POST_PARTNER_CALLS_KO_PARAMS_ID_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create partner-calls with params id ", true, true, null);

    }

    /**
     * test POST PartnerCalls with ParamsMultiCallsId
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostPartnerCallsWithParamsMultiCallsId() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_CALLS_BASE_PATH + DataApiPathDict.PARTNER_CALLS_PATH, HttpMethod.POST, HttpStatus.BAD_REQUEST,
                PARTNER_CALLS_REQUEST_POST_PARTNER_CALLS_WITH_PARAMS_MULTI_CALLS_ID_JSON, PARTNER_CALLS_EXPECTED_POST_PARTNER_CALLS_KO_PARAMS_MULTI_CALLS_ID_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create partner-calls with params  multi calls id ", true, true, null);

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
