package com.gateway.dataapi.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.constants.DataApiPathDict;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
import com.gateway.dataapi.DataApiITCase;
import com.gateway.dataapi.service.PartnerMetaService;
import com.gateway.dataapi.util.enums.JsonResponseTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static com.gateway.commonapi.constants.CacheManagerDict.CACHE_MANAGER_REFRESH_PARTNERS_PATH;
import static com.gateway.commonapi.constants.DataApiPathDict.ID;
import static com.gateway.commonapi.constants.DataApiPathDict.PARTNER_METAS_BASE_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@Slf4j
class PartnerMetaApiControllerTest extends DataApiITCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_OK_JSON = "partnerMetas/expected/getPartnerMetas_ok.json";
    public static final String PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_BY_ID_OK_JSON = "partnerMetas/expected/getPartnerMetas_by_id_ok.json";
    public static final String PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_BY_ID_NOT_FOUND_JSON = "partnerMetas/expected/getPartnerMetas_by_id_not_found.json";
    public static final String PARTNER_METAS_REQUEST_POST_PARTNER_METAS_OK_JSON = "partnerMetas/request/postPartnerMetas_ok.json";
    public static final String PARTNER_METAS_EXPECTED_POST_PARTNER_METAS_OK_JSON = "partnerMetas/expected/postPartnerMetas_ok.json";
    public static final String PARTNER_METAS_REQUEST_PUT_PARTNER_METAS_OK_JSON = "partnerMetas/request/putPartnerMetas_ok.json";
    public static final String PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_AFTER_PUT_OK_JSON = "partnerMetas/expected/getPartnerMetas_afterput_ok.json";
    public static final String PARTNER_METAS_REQUEST_PATCH_PARTNER_METAS_OK_JSON = "partnerMetas/request/patchPartnerMetas_ok.json";
    public static final String PARTNER_METAS_EXPECTED_PATCH_PARTNER_METAS_OK_JSON = "partnerMetas/expected/patchPartnerMetas_ok.json";
    public static final String PARTNER_METAS_REQUEST_POST_PARTNER_METAS_EMPTY_PRICE_LIST_JSON = "partnerMetas/request/postPartnerMetas_emptyPriceList_ok.json";
    public static final String PARTNER_METAS_REQUEST_PATCH_PARTNER_METAS_EMPTY_PRICE_LIST_JSON = "partnerMetas/request/patchPartnerMetas_emptyPriceList_ok.json";
    public static final String PARTNER_METAS_EXPECTED_PATCH_PARTNER_METAS_EMPTY_PRICE_LIST_JSON = "partnerMetas/expected/patchPartnerMetas_emptyPriceList_ok.json";
    private static final String PARTNER_METAS_EXPECTED_POST_PARTNER_METAS_EMPTY_PRICE_LIST_JSON = "partnerMetas/expected/postPartnerMetas_emptyPriceList_ok.json";

    public static final String PARTNER_METAS_REQUEST_POST_PARTNER_METAS_KO_NOT_MAAS = "partnerMetas/request/postPartnerMetas_ko_notMaas.json";
    public static final String PARTNER_METAS_REQUEST_POST_PARTNER_METAS_KO_NOT_MSP = "partnerMetas/request/postPartnerMetas_ko_notMsp.json";
    /*
     * Mock MVC
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartnerMetaService partnerMetaService;

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    /*
     * Web Application Context
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        ReflectionTestUtils.setField(partnerMetaService, "restTemplate", restTemplate);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).defaultResponseCharacterEncoding(StandardCharsets.UTF_8).build();
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, this.objectMapper);
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);
    }

    /**
     * test GET on PartnerMetas
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testGetPartnerMetas() throws Exception {
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH, HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test to get list of partner-metas", true, false);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testGetPartnerMetasFilters() throws Exception {
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH + "?type=CARPOOLING", HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test to get list of partner-metas", true, false);
        //verify(partnerMetaService, times(1)).getPartnerMetasByType(any(), any());
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH + "?partnerType=MSP", HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test to get list of partner-metas", true, false);
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH + "?name=Dott", HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test to get list of partner-metas", true, false);
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH + "?operator=Dott", HttpMethod.GET,
                HttpStatus.OK, null, PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test to get list of partner-metas", true, false);

    }

    /**
     * test GET on PartnerMetas by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testGetPartnerMetasById() throws Exception {
        // change 1 by a UUID
        String partnerId = "/b814c97e-df56-4651-ac50-11525537964f";
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH + partnerId, HttpMethod.GET, HttpStatus.OK, null, PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_BY_ID_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test GET /partner-metas by id", false, false);
    }

    /**
     * test GET not found on PartnerMetas by Id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testGetPartnerMetasByIdNotFound() throws Exception {
        String partnerId = "/00000000-0000-0001-0000-000000000666";
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH + partnerId, HttpMethod.GET, HttpStatus.NOT_FOUND, null, PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_BY_ID_NOT_FOUND_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test GET /partner-metas by id with not found exception", true, true);
    }

    /**
     * test DELETE on PartnerMetas
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testDeletePartnerMetasById() throws Exception {
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        String partnerId = "/b814c97e-df56-4651-ac50-11525537964f";
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH + partnerId, HttpMethod.DELETE, HttpStatus.NO_CONTENT, null, "", null, "Test to delete partner-metas by id", false, false);
    }

    /**
     * test POST on PartnerMetas
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testPostPartnerMetas() throws Exception {
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.CREATED, PARTNER_METAS_REQUEST_POST_PARTNER_METAS_OK_JSON, PARTNER_METAS_EXPECTED_POST_PARTNER_METAS_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test post partner", true, false);

        HttpClientErrorException exception = HttpClientErrorException.create("error", HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(), null, null, null);
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(exception);

        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.NOT_FOUND, PARTNER_METAS_REQUEST_POST_PARTNER_METAS_OK_JSON, null, null, "Test post partner with error on cache refresh", true, false);

        RestClientException exception2 = new RestClientException("error");
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(exception2);

        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.BAD_GATEWAY, PARTNER_METAS_REQUEST_POST_PARTNER_METAS_OK_JSON, null, null, "Test post partner with error on cache refresh", true, false);

        InternalException exception3 = new InternalException("error");
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenThrow(exception3);

        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.SERVICE_UNAVAILABLE, PARTNER_METAS_REQUEST_POST_PARTNER_METAS_OK_JSON, null, null, "Test post partner with error on cache refresh", true, false);

    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testPostPartnerMetasWithIncompatibleEnumMaas() throws Exception {
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.BAD_REQUEST, PARTNER_METAS_REQUEST_POST_PARTNER_METAS_KO_NOT_MAAS, "", JsonResponseTypeEnum.EMPTY, "Test to verify partnerType and type are compatible", true, false);
    }

    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testPostPartnerMetasWithIncompatibleEnumMsp() throws Exception {
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.BAD_REQUEST, PARTNER_METAS_REQUEST_POST_PARTNER_METAS_KO_NOT_MSP, "", JsonResponseTypeEnum.EMPTY, "Test to verify partnerType and type are compatible", true, false);
    }

    /**
     * test PUT on PartnerMetas
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testPutPartnerMetasById() throws Exception {
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        final String partnerId = "/b814c97e-df56-4651-ac50-11525537964f";
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH + partnerId, HttpMethod.PUT, HttpStatus.NO_CONTENT, PARTNER_METAS_REQUEST_PUT_PARTNER_METAS_OK_JSON, "", null, "Test to put partner-metas by id", false, false);

        // call get to get the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(PARTNER_METAS_BASE_PATH + DataApiPathDict.PARTNER_META_PATH, ID, partnerId), HttpMethod.GET, HttpStatus.OK, null, PARTNER_METAS_EXPECTED_GET_PARTNER_METAS_AFTER_PUT_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test to get partner-meta after put", false, false);
    }

    /**
     * test PATCH on PartnerMetas
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testPatchPartnerMetasById() throws Exception {
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        String partnerId = "/b814c97e-df56-4651-ac50-11525537964f";
        testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH + partnerId, HttpMethod.PATCH, HttpStatus.OK, PARTNER_METAS_REQUEST_PATCH_PARTNER_METAS_OK_JSON, PARTNER_METAS_EXPECTED_PATCH_PARTNER_METAS_OK_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test to patch partner-metas by id", false, false);
    }


    /**
     * test PATCH on PartnerMetas without priceList
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testPatchPartnerMetasWithoutPriceList() throws Exception {
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        String createdPartnerMeta = testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.CREATED, PARTNER_METAS_REQUEST_POST_PARTNER_METAS_EMPTY_PRICE_LIST_JSON, PARTNER_METAS_EXPECTED_POST_PARTNER_METAS_EMPTY_PRICE_LIST_JSON, JsonResponseTypeEnum.JSON_OBJECT, "create partner meta without priceList", true, false);
        String uidString = extractPartnerId(createdPartnerMeta);
        // call get to get the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(PARTNER_METAS_BASE_PATH + DataApiPathDict.PARTNER_META_PATH, ID, uidString), HttpMethod.GET, HttpStatus.OK, null, PARTNER_METAS_EXPECTED_POST_PARTNER_METAS_EMPTY_PRICE_LIST_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test to get partner-meta after creation of without priceList", true, false);

        // call patch with the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(PARTNER_METAS_BASE_PATH + DataApiPathDict.PARTNER_META_PATH, ID, uidString), HttpMethod.PATCH, HttpStatus.OK, PARTNER_METAS_REQUEST_PATCH_PARTNER_METAS_EMPTY_PRICE_LIST_JSON, PARTNER_METAS_EXPECTED_PATCH_PARTNER_METAS_EMPTY_PRICE_LIST_JSON, JsonResponseTypeEnum.JSON_OBJECT, "Test to patch partner-metas by id", true, false);

        // delete
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(PARTNER_METAS_BASE_PATH + DataApiPathDict.PARTNER_META_PATH, ID, uidString), HttpMethod.DELETE, HttpStatus.NO_CONTENT, null, "", null, "Test to delete partner-metas by id", true, false);
    }

    private String extractPartnerId(String createdPartnerMeta) throws JSONException {
        JSONObject createdPartnerMetaObject = new JSONObject(createdPartnerMeta);
        return createdPartnerMetaObject.getString("partnerId");
    }

    /**
     * test DELETE on PartnerMetas without priceList
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({
            @Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    })
    void testDeletePartnerMetasWithoutPriceList() throws Exception {
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains(CACHE_MANAGER_REFRESH_PARTNERS_PATH),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object.class))).thenReturn(null);

        String createdPartnerMeta = testHttpRequestWithExpectedResult(PARTNER_METAS_BASE_PATH, HttpMethod.POST, HttpStatus.CREATED, PARTNER_METAS_REQUEST_POST_PARTNER_METAS_EMPTY_PRICE_LIST_JSON, PARTNER_METAS_EXPECTED_POST_PARTNER_METAS_EMPTY_PRICE_LIST_JSON, JsonResponseTypeEnum.JSON_OBJECT, "create partner meta without priceList", true, false);
        String uidString = extractPartnerId(createdPartnerMeta);
        // delete
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(PARTNER_METAS_BASE_PATH + DataApiPathDict.PARTNER_META_PATH, ID, uidString), HttpMethod.DELETE, HttpStatus.NO_CONTENT, null, "", null, "Test to delete partner-metas by id", false, false);
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

        log.info("Test PartnerMetaApiControllerTests > {}", message);

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
