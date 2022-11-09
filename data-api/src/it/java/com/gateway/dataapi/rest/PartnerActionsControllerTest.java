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

class PartnerActionsControllerTest extends DataApiITCase {
    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String PARTNER_ACTIONS_EXPECTED_GET_PARTNER_ACTIONS_OK_JSON = "partnerActions/expected/getPartnerActions_ok.json";
    public static final String PARTNER_ACTIONS_EXPECTED_GET_PARTNER_ACTIONS_BY_ID_OK_JSON = "partnerActions/expected/getPartnerActions_by_id_ok.json";
    public static final String PARTNER_ACTIONS_EXPECTED_GET_PARTNER_ACTIONS_BY_PARTNER_ID_OK_JSON = "partnerActions/expected/getPartnerActions_by_PartnerMeta_id_ok.json";
    public static final String PARTNER_ACTIONS_EXPECTED_GET_PARTNER_ACTIONS_BY_ID_NOT_FOUND_JSON = "partnerActions/expected/getPartnerActions_by_id_not_found.json";
    public static final String PARTNER_ACTIONS_REQUEST_POST_PARTNER_ACTIONS_OK_JSON = "partnerActions/request/postPartnerActions_ok.json";
    public static final String PARTNER_ACTIONS_EXPECTED_POST_PARTNER_ACTIONS_OK_JSON = "partnerActions/expected/postPartnerActions_ok.json";
    public static final String PARTNER_ACTIONS_REQUEST_PUT_PARTNER_ACTIONS_OK_JSON = "partnerActions/request/putPartnerActions_ok.json";
    public static final String PARTNER_ACTIONS_EXPECTED_GET_PARTNER_ACTIONS_AFTER_PUT_OK_JSON = "partnerActions/expected/getPartnerActions_afterput_ok.json";

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
     * test GET All PartnerActions
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerActions() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_ACTIONS_BASE_PATH, HttpMethod.GET, HttpStatus.OK, null,
                PARTNER_ACTIONS_EXPECTED_GET_PARTNER_ACTIONS_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get list of partner-actions", true, false, null);
    }

    /**
     * test GET partnerActions by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerActionsId() throws Exception {
        final String partnerActionId = "e222f848-0f53-46fd-97fa-6b1f9f3db265";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_ACTIONS_BASE_PATH + DataApiPathDict.PARTNER_ACTION_PATH, ID, partnerActionId), HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_ACTIONS_EXPECTED_GET_PARTNER_ACTIONS_BY_ID_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test GET /partner-actions by id", false, false, null);
    }

    /**
     * test GET PartnerActions with PartnerMetaId
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerActionsByPartnerMetaId() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("partnerMetaId", "b814c97e-df56-4651-ac50-11525537964f");

        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_ACTIONS_BASE_PATH), HttpMethod.GET, HttpStatus.OK, null,
                PARTNER_ACTIONS_EXPECTED_GET_PARTNER_ACTIONS_BY_PARTNER_ID_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get  partner-actions by partnerMetaId", true, false, parameters);
    }

    /**
     * test GET not found PartnerActions by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerActionsById_notfound() throws Exception {
        final String partnerActionId = "00000000-0000-0001-0000-000000000666";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_ACTIONS_BASE_PATH + DataApiPathDict.PARTNER_ACTION_PATH, ID, partnerActionId), HttpMethod.GET,
                HttpStatus.NOT_FOUND, null, PARTNER_ACTIONS_EXPECTED_GET_PARTNER_ACTIONS_BY_ID_NOT_FOUND_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test GET /partner-actions by id with not found exception", true, true, null);
    }

    /**
     * test DELETE PartnerActions
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeletePartnerActionsById() throws Exception {
        final String partnerActionId = "e222f848-0f53-46fd-97fa-6b1f9f3db265";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_ACTIONS_BASE_PATH + DataApiPathDict.PARTNER_ACTION_PATH, ID, partnerActionId), HttpMethod.DELETE,
                HttpStatus.NO_CONTENT, null, "", null, "Test to delete partner-actions by id", false, false, null);
    }

    /**
     * test POST PartnerActions
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostPartnerActions() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_ACTIONS_BASE_PATH, HttpMethod.POST, HttpStatus.CREATED,
                PARTNER_ACTIONS_REQUEST_POST_PARTNER_ACTIONS_OK_JSON, PARTNER_ACTIONS_EXPECTED_POST_PARTNER_ACTIONS_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create partner-actions", true, false, null);

    }

    /**
     * test PUT PartnerActions
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPutPartnerActionsById() throws Exception {
        final String partnerActionId = "e222f848-0f53-46fd-97fa-6b1f9f3db265";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_ACTIONS_BASE_PATH + DataApiPathDict.PARTNER_ACTION_PATH, ID, partnerActionId), HttpMethod.PUT,
                HttpStatus.NO_CONTENT, PARTNER_ACTIONS_REQUEST_PUT_PARTNER_ACTIONS_OK_JSON, "", null,
                "Test to put partner-actions ", false, false, null);

        // call get to get the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_ACTIONS_BASE_PATH + DataApiPathDict.PARTNER_ACTION_PATH, ID, partnerActionId), HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_ACTIONS_EXPECTED_GET_PARTNER_ACTIONS_AFTER_PUT_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to get partner-actions after put", false, false, null);
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
