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

class PartnerStandardControllerTest extends DataApiITCase {
    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_OK_JSON = "partnerStandard/expected/getPartnerStandard_ok.json";
    public static final String PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_BY_ID_OK_JSON = "partnerStandard/expected/getPartnerStandard_by_id_ok.json";
    public static final String PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_BY_PARTNER_META_ID_OK_JSON = "partnerStandard/expected/getPartnerStandard_by_PartnerMeta_id_ok.json";
    public static final String PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_BY_PARTNER_ACTIONS_ID_OK_JSON = "partnerStandard/expected/getPartnerStandard_by_PartnerActions_id_ok.json";

    public static final String PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_BY_ID_NOT_FOUND_JSON = "partnerStandard/expected/getPartnerStandard_by_id_not_found.json";
    public static final String PARTNER_STANDARD_REQUEST_POST_PARTNER_STANDARD_OK_JSON = "partnerStandard/request/postPartnerStandard_ok.json";
    public static final String PARTNER_STANDARD_EXPECTED_POST_PARTNER_STANDARD_OK_JSON = "partnerStandard/expected/postPartnerStandard_ok.json";
    public static final String PARTNER_STANDARD_REQUEST_POST_PARTNER_STANDARD_CONFLICT_JSON = "partnerStandard/request/postPartnerStandard_conflict.json";
    public static final String PARTNER_STANDARD_EXPECTED_POST_PARTNER_STANDARD_CONFLICT_JSON = "partnerStandard/expected/postPartnerStandard_conflict.json";
    public static final String PARTNER_STANDARD_REQUEST_PUT_PARTNER_STANDARD_OK_JSON = "partnerStandard/request/putPartnerStandard_ok.json";
    public static final String PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_AFTER_PUT_OK_JSON = "partnerStandard/expected/getPartnerStandard_afterput_ok.json";
    public static final String PARTNER_STANDARD_REQUEST_PUT_PARTNER_STANDARD_CONFLICT_JSON = "partnerStandard/request/putPartnerStandard_conflict.json";
    public static final String PARTNER_STANDARD_EXPECTED_PUT_PARTNER_STANDARD_CONFLICT_JSON = "partnerStandard/request/putPartnerStandard_conflict.json";

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
     * test GET All PartnerStandard
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerStandard() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_STANDARD_BASE_PATH + DataApiPathDict.PARTNER_STANDARDS_PATH, HttpMethod.GET, HttpStatus.OK, null,
                PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get list of Partner-standard", true, false, null);
    }

    /**
     * test GET PartnerStandard by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerStandardById() throws Exception {
        final String partnerStandardId = "f7082f2f-317b-4e4c-ba77-54ce2f0dbfff";
        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_STANDARD_BASE_PATH + DataApiPathDict.PARTNER_STANDARD_PATH, ID, partnerStandardId), HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_BY_ID_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT,
                "Test GET /partner-standard by id", false, false, null);
    }

    /**
     * test Delete PartnerStandard by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeletePartnerStandardById() throws Exception {
        final String partnerStandardId = "f7082f2f-317b-4e4c-ba77-54ce2f0dbfff";
        testHttpRequestWithExpectedResult(
                CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_STANDARD_BASE_PATH + DataApiPathDict.PARTNER_STANDARD_PATH, ID, partnerStandardId), HttpMethod.DELETE,
                HttpStatus.NO_CONTENT, null, null, null,
                "Test delete /partner-standard by id", false, false, null);
    }


    /**
     * test GET PartnerStandard with PartnerMetaId
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerStandardByPartnerMetaId() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("partnerId", "b814c97e-df56-4651-ac50-11525537964f");

        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_STANDARD_BASE_PATH, HttpMethod.GET, HttpStatus.OK, null,
                PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_BY_PARTNER_META_ID_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get  partner-standard by PartnerMetaID", true, false, parameters);
    }

    /**
     * test GET PartnerStandard with PartnerActions
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerStandardByPartnerActionsId() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("partnerActionsId", "7c8f8780-a6fa-495d-8b36-24a20539adca");

        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_STANDARD_BASE_PATH+DataApiPathDict.PARTNER_STANDARDS_PATH,
                HttpMethod.GET, HttpStatus.OK, null,
                PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_BY_PARTNER_ACTIONS_ID_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get  partner-standard By partnerActionsId", true, false, parameters);
    }

    /**
     * test GET PartnerStandard with CRITERIA
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerStandardByCriteria() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("partnerId", "b814c97e-df56-4651-ac50-11525537964f");
        parameters.add("partnerActionsId", "7c8f8780-a6fa-495d-8b36-24a20539adca");
        parameters.add("partnerActionsName", "PING");
        parameters.add("versionStandard", "V1.1");
        parameters.add("versionDataMapping", "V1.0");
        parameters.add("isActive", String.valueOf(false));
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_STANDARD_BASE_PATH+DataApiPathDict.PARTNER_STANDARDS_PATH,
                HttpMethod.GET, HttpStatus.OK, null,
                PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_BY_PARTNER_ACTIONS_ID_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get  partner-standard By partnerActionsId", true, false, parameters);
    }


    /**
     * test GET not found PartnerStandard by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetPartnerStandardByIdNotfound() throws Exception {
        final String partnerStandardId = "00000000-0000-0001-0000-000000000666";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_STANDARD_BASE_PATH + DataApiPathDict.PARTNER_STANDARD_PATH, ID, partnerStandardId), HttpMethod.GET,
                HttpStatus.NOT_FOUND, null, PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_BY_ID_NOT_FOUND_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test GET /partner-standard by id with not found exception", true, true, null);
    }

    /**
     * test POST PartnerStandard
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostPartnerStandard() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_STANDARD_BASE_PATH + DataApiPathDict.PARTNER_STANDARDS_PATH, HttpMethod.POST, HttpStatus.CREATED,
                PARTNER_STANDARD_REQUEST_POST_PARTNER_STANDARD_OK_JSON, PARTNER_STANDARD_EXPECTED_POST_PARTNER_STANDARD_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create partner-standard", true, false, null);

    }

    /**
     * test POST PartnerStandard
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostPartnerStandardConflict() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.PARTNER_STANDARD_BASE_PATH + DataApiPathDict.PARTNER_STANDARDS_PATH, HttpMethod.POST, HttpStatus.CONFLICT,
                PARTNER_STANDARD_REQUEST_POST_PARTNER_STANDARD_CONFLICT_JSON, PARTNER_STANDARD_EXPECTED_POST_PARTNER_STANDARD_CONFLICT_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to create partner-standard on a MSP without ping and an action PING", true, true, null);
    }

    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPutPartnerStandardConflict() throws Exception {
        final String partnerStandardId = "f7082f2f-317b-4e4c-ba77-54ce2f0dbfff";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_STANDARD_BASE_PATH + DataApiPathDict.PARTNER_STANDARD_PATH, ID, partnerStandardId), HttpMethod.PUT,
                HttpStatus.CONFLICT, PARTNER_STANDARD_REQUEST_PUT_PARTNER_STANDARD_CONFLICT_JSON, PARTNER_STANDARD_EXPECTED_PUT_PARTNER_STANDARD_CONFLICT_JSON, null, "Test to put partner-standard ",
                true, true, null);
    }

    /**
     * test PUT PartnerStandard
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPutPartnerStandardById() throws Exception {
        final String partnerStandardId = "f7082f2f-317b-4e4c-ba77-54ce2f0dbfff";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_STANDARD_BASE_PATH + DataApiPathDict.PARTNER_STANDARD_PATH, ID, partnerStandardId), HttpMethod.PUT,
                HttpStatus.NO_CONTENT, PARTNER_STANDARD_REQUEST_PUT_PARTNER_STANDARD_OK_JSON, "", null, "Test to put partner-standard ",
                false, false, null);

        // call get to get the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.PARTNER_STANDARD_BASE_PATH + DataApiPathDict.PARTNER_STANDARD_PATH, ID, partnerStandardId), HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_STANDARD_EXPECTED_GET_PARTNER_STANDARD_AFTER_PUT_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to get partner-standard after put", false, false, null);
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
