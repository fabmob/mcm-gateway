package com.gateway.dataapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.dataapi.DataApiITCase;
import com.gateway.dataapi.util.constant.DataApiPathDict;
import com.gateway.dataapi.util.enums.JsonResponseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
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
import org.springframework.web.context.WebApplicationContext;

import static com.gateway.dataapi.util.constant.DataApiPathDict.*;

@Slf4j
class MspMetaApiControllerTest extends DataApiITCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String MSP_METAS_EXPECTED_GET_MSP_METAS_OK_JSON = "mspMetas/expected/getMspMetas_ok.json";
    public static final String MSP_METAS_EXPECTED_GET_MSP_METAS_BY_ID_OK_JSON = "mspMetas/expected/getMspMetas_by_id_ok.json";
    public static final String MSP_METAS_EXPECTED_GET_MSP_METAS_BY_ID_NOT_FOUND_JSON = "mspMetas/expected/getMspMetas_by_id_not_found.json";
    public static final String MSP_METAS_REQUEST_POST_MSP_METAS_OK_JSON = "mspMetas/request/postMspMetas_ok.json";
    public static final String MSP_METAS_EXPECTED_POST_MSP_METAS_OK_JSON = "mspMetas/expected/postMspMetas_ok.json";
    public static final String MSP_METAS_REQUEST_PUT_MSP_METAS_OK_JSON = "mspMetas/request/putMspMetas_ok.json";
    public static final String MSP_METAS_EXPECTED_GET_MSP_METAS_AFTER_PUT_OK_JSON = "mspMetas/expected/getMspMetas_afterput_ok.json";
    public static final String MSP_METAS_REQUEST_PATCH_MSP_METAS_OK_JSON = "mspMetas/request/patchMspMetas_ok.json";
    public static final String MSP_METAS_EXPECTED_PATCH_MSP_METAS_OK_JSON = "mspMetas/expected/patchMspMetas_ok.json";
    public static final String MSP_METAS_REQUEST_POST_MSP_METAS_EMPTY_PRICE_LIST_JSON = "mspMetas/request/postMspMetas_emptyPriceList_ok.json";
    public static final String MSP_METAS_REQUEST_PATCH_MSP_METAS_EMPTY_PRICE_LIST_JSON = "mspMetas/request/patchMspMetas_emptyPriceList_ok.json";
    public static final String MSP_METAS_EXPECTED_PATCH_MSP_METAS_EMPTY_PRICE_LIST_JSON = "mspMetas/expected/patchMspMetas_emptyPriceList_ok.json";
    private static final String MSP_METAS_EXPECTED_POST_MSP_METAS_EMPTY_PRICE_LIST_JSON = "mspMetas/expected/postMspMetas_emptyPriceList_ok.json";
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
     * test GET on mspMetas
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts =  "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testGetMspMetas() throws Exception {
        testHttpRequestWithExpectedResult(MSP_METAS_BASE_PATH, HttpMethod.GET,
                HttpStatus.OK, null, MSP_METAS_EXPECTED_GET_MSP_METAS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test to get list of msp-metas", true, false);
    }

    /**
     * test GET on mspMetas by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts =  "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testGetMspMetasById() throws Exception {
        // change 1 by a UUID
        String mspId = "/b814c97e-df56-4651-ac50-11525537964f";
        testHttpRequestWithExpectedResult(MSP_METAS_BASE_PATH + mspId, HttpMethod.GET, HttpStatus.OK, null, MSP_METAS_EXPECTED_GET_MSP_METAS_BY_ID_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test GET /msp-metas by id", false, false);
    }

    /**
     * test GET not found on mspMetas by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts =  "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testGetMspMetasByIdNotFound() throws Exception {
        String mspId = "/00000000-0000-0001-0000-000000000666";
        testHttpRequestWithExpectedResult(MSP_METAS_BASE_PATH + mspId, HttpMethod.GET, HttpStatus.NOT_FOUND, null, MSP_METAS_EXPECTED_GET_MSP_METAS_BY_ID_NOT_FOUND_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test GET /msp-metas by id with not found exception", true, true);
    }

    /**
     * test DELETE on mspMetas
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts =  "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testDeleteMspMetasById() throws Exception {
        String mspId = "/b814c97e-df56-4651-ac50-11525537964f";
        testHttpRequestWithExpectedResult(MSP_METAS_BASE_PATH + mspId, HttpMethod.DELETE, HttpStatus.NO_CONTENT, null, "", null, "Test to delete msp-metas by id", false, false);
    }

    /**
     * test POST on mspMetas
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts =  "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testPostMspMetas() throws Exception {
        testHttpRequestWithExpectedResult(MSP_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.CREATED, MSP_METAS_REQUEST_POST_MSP_METAS_OK_JSON, MSP_METAS_EXPECTED_POST_MSP_METAS_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test to delete msp-metas by id", true, false);

    }

    /**
     * test PUT on mspMetas
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts =  "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testPutMspMetasById() throws Exception {
        final String mspId = "/b814c97e-df56-4651-ac50-11525537964f";
        testHttpRequestWithExpectedResult(MSP_METAS_BASE_PATH + mspId, HttpMethod.PUT, HttpStatus.NO_CONTENT, MSP_METAS_REQUEST_PUT_MSP_METAS_OK_JSON, "", null, "Test to put msp-metas by id", false, false);

        // call get to get the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(MSP_METAS_BASE_PATH+DataApiPathDict.MSP_META_PATH, ID, mspId), HttpMethod.GET, HttpStatus.OK, null, MSP_METAS_EXPECTED_GET_MSP_METAS_AFTER_PUT_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test to get msp-meta after put", false, false);
    }

    /**
     * test PATCH on mspMetas
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts =  "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testPatchMspMetasById() throws Exception {
        String mspId = "/b814c97e-df56-4651-ac50-11525537964f";
        testHttpRequestWithExpectedResult(MSP_METAS_BASE_PATH + mspId, HttpMethod.PATCH, HttpStatus.OK, MSP_METAS_REQUEST_PATCH_MSP_METAS_OK_JSON, MSP_METAS_EXPECTED_PATCH_MSP_METAS_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test to patch msp-metas by id", false, false);
    }


    /**
     * test PATCH on mspMetas without priceList
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testPatchMspMetasWithoutPriceList() throws Exception {
        String createdMspMeta = testHttpRequestWithExpectedResult(MSP_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.CREATED, MSP_METAS_REQUEST_POST_MSP_METAS_EMPTY_PRICE_LIST_JSON, MSP_METAS_EXPECTED_POST_MSP_METAS_EMPTY_PRICE_LIST_JSON, JsonResponseTypeEnum.JSON_OBJECT, "create msp meta without priceList", true, false);
        String uidString = extractMspId(createdMspMeta);
        // call get to get the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(MSP_METAS_BASE_PATH+DataApiPathDict.MSP_META_PATH, ID, uidString), HttpMethod.GET, HttpStatus.OK, null, MSP_METAS_EXPECTED_POST_MSP_METAS_EMPTY_PRICE_LIST_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test to get msp-meta after creation of without priceList", true, false);

        // call patch with the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(MSP_METAS_BASE_PATH+DataApiPathDict.MSP_META_PATH, ID, uidString), HttpMethod.PATCH, HttpStatus.OK, MSP_METAS_REQUEST_PATCH_MSP_METAS_EMPTY_PRICE_LIST_JSON, MSP_METAS_EXPECTED_PATCH_MSP_METAS_EMPTY_PRICE_LIST_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test to patch msp-metas by id", true, false);

        // delete
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(MSP_METAS_BASE_PATH+DataApiPathDict.MSP_META_PATH, ID, uidString), HttpMethod.DELETE, HttpStatus.NO_CONTENT, null, "", null, "Test to delete msp-metas by id", true, false);
    }

    private String extractMspId(String createdMspMeta) throws JSONException {
        JSONObject createdMspMetaObject = new JSONObject(createdMspMeta);
        return createdMspMetaObject.getString("mspId");
    }

    /**
     * test DELETE on mspMetas without priceList
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testDeleteMspMetasWithoutPriceList() throws Exception {
        String createdMspMeta = testHttpRequestWithExpectedResult(MSP_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.CREATED, MSP_METAS_REQUEST_POST_MSP_METAS_EMPTY_PRICE_LIST_JSON, MSP_METAS_EXPECTED_POST_MSP_METAS_EMPTY_PRICE_LIST_JSON, JsonResponseTypeEnum.JSON_OBJECT, "create msp meta without priceList", true, false);
        String uidString = extractMspId(createdMspMeta);
        // delete
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(MSP_METAS_BASE_PATH+DataApiPathDict.MSP_META_PATH, ID, uidString), HttpMethod.DELETE, HttpStatus.NO_CONTENT, null, "", null, "Test to delete msp-metas by id", false, false);
    }

    /**
     * Central management of the ApiControllerTest
     *
     * @param uri                      the uri of the operation
     * @param httpMethod               HTTP VERB (GET, PUT, POST, PATCH, DELETE)
     * @param httpStatusExpectedResult Https code status expected
     * @param requestPayloadPath       payload of the request (optional for some verbs)
     * @param expectedResultPath       path of the json expected answer file
     * @param resulType                JsonArray or JsonObject expected as result
     * @param message                  Message of the test running
     * @return the body of the response with UUID visible
     * @throws Exception
     */
    private String testHttpRequestWithExpectedResult(String uri, HttpMethod httpMethod,
                                                     HttpStatus httpStatusExpectedResult, String requestPayloadPath,
                                                     String expectedResultPath, JsonResponseTypeEnum resulType,
                                                     final String message, boolean ignoreUUID, boolean ignoreTimeStamp) throws Exception {

        String responseCall;
        ResultMatcher mockResultMatcher = WsTestUtil.getResultMatcher(httpStatusExpectedResult);
        String requestPayloadContent = StringUtils.isNotBlank(requestPayloadPath) ? WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + requestPayloadPath) : "";
        String globalPath = StringUtils.EMPTY;
        MockHttpServletRequestBuilder requestBuilder = WsTestUtil.getMockHttpServletRequestBuilder(globalPath, uri, httpMethod, requestPayloadContent);
        MediaType responseContentType = WsTestUtil.getMediaType(httpMethod);
        ResultMatcher contentTypeResultMatcher = WsTestUtil.getContentTypeResultMatcher(responseContentType, httpStatusExpectedResult);

        log.info("Test MspMetaApiControllerTests > {}", message);

        String content;
        // trigger the web service call
        if (responseContentType != null && contentTypeResultMatcher != null) {
            content = this.mockMvc.perform(requestBuilder).andExpect(mockResultMatcher).andExpect(contentTypeResultMatcher).andReturn().getResponse().getContentAsString();
        } else {
            content = this.mockMvc.perform(requestBuilder).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();
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

                    // in the case where we test the method post, we generate the ids randomly. we will therefore add this boolean , if it is true in the function,
                    // when we retrieve the contents whether it is the result or expected we will replace the UUIDs expected by the UUIDs result
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
