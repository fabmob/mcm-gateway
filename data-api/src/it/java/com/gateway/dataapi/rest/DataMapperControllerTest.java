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

class DataMapperControllerTest extends DataApiITCase {
    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String DATA_MAPPER_EXPECTED_GET_DATA_MAPPER_OK_JSON = "dataMappers/expected/getdataMappers_ok.json";
    public static final String DATA_MAPPER_EXPECTED_GET_DATA_MAPPER_BY_ID_OK_JSON = "dataMappers/expected/getdataMappers_by_id_ok.json";
    public static final String DATA_MAPPER_EXPECTED_GET_DATA_MAPPER_BY_MSP_ACTIONS_ID_OK_JSON = "dataMappers/expected/getdataMappers_by_MspAction_id_ok.json";
    public static final String DATA_MAPPER_EXPECTED_GET_DATA_MAPPER_BY_ID_NOT_FOUND_JSON = "dataMappers/expected/getdataMappers_by_id_not_found.json";
    public static final String DATA_MAPPER_REQUEST_POST_DATA_MAPPER_OK_JSON = "dataMappers/request/postdataMappers_ok.json";
    public static final String DATA_MAPPER_EXPECTED_POST_DATA_MAPPER_OK_JSON = "dataMappers/expected/postdataMappers_ok.json";
    public static final String DATA_MAPPER_REQUEST_PUT_DATA_MAPPER_OK_JSON = "dataMappers/request/putdataMappers_ok.json";
    public static final String DATA_MAPPER_EXPECTED_GET_DATA_MAPPER_AFTER_PUT_OK_JSON = "dataMappers/expected/getdataMappers_afterput_ok.json";
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
     * test GET All dataMappers
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetdataMappers() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.DATA_MAPPER_BASE_PATH + DataApiPathDict.DATA_MAPPERS_PATH, HttpMethod.GET, HttpStatus.OK, null,
                DATA_MAPPER_EXPECTED_GET_DATA_MAPPER_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test get list of dataMappers", true, false, null);
    }

    /**
     * test GET dataMappers by id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetdataMappersById() throws Exception {
        final String dataMapperId = "231866de-4e4f-4ab7-948b-372ce1acaef2";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.DATA_MAPPER_BASE_PATH + DataApiPathDict.DATA_MAPPER_PATH, ID, dataMapperId), HttpMethod.GET,
                HttpStatus.OK, null, DATA_MAPPER_EXPECTED_GET_DATA_MAPPER_BY_ID_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test GET /dataMappers by id", false, false, null);
    }

    /**
     * test GET dataMappers by MspAction id
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetdataMappersByMspMetaId() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("mspActionId", "5e71dcb3-ca60-4a2a-9896-c509ad1ca756");

        testHttpRequestWithExpectedResult(DataApiPathDict.DATA_MAPPER_BASE_PATH, HttpMethod.GET, HttpStatus.OK, null,
                DATA_MAPPER_EXPECTED_GET_DATA_MAPPER_BY_MSP_ACTIONS_ID_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test dataMapper by mspMetaId", true, false, parameters);
    }

    /**
     * test GET dataMapper by id not found
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testGetdataMappersByIdNotFound() throws Exception {
        final String dataMapperId = "abb5c250-d06f-416c-9de9-6748ac6828cb";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.DATA_MAPPER_BASE_PATH + DataApiPathDict.DATA_MAPPER_PATH, ID, dataMapperId), HttpMethod.GET,
                HttpStatus.NOT_FOUND, null, DATA_MAPPER_EXPECTED_GET_DATA_MAPPER_BY_ID_NOT_FOUND_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test GET datamapper by id with not found exception", true, true,
                null);

    }

    /**
     * test post DataMapper
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPostDataMapper() throws Exception {
        testHttpRequestWithExpectedResult(DataApiPathDict.DATA_MAPPER_BASE_PATH + DataApiPathDict.DATA_MAPPERS_PATH, HttpMethod.POST, HttpStatus.CREATED,
                DATA_MAPPER_REQUEST_POST_DATA_MAPPER_OK_JSON, DATA_MAPPER_EXPECTED_POST_DATA_MAPPER_OK_JSON, JsonResponseTypeEnum.JSON_ARRAY,
                "Test to create dataMapper", true, false, null);

    }

    /**
     * test delete DataMapper
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testDeleteDataMapper() throws Exception {
        final String dataMapperId = "0e51c003-6222-449d-9f08-20a7e2b8d94d";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.DATA_MAPPER_BASE_PATH + DataApiPathDict.DATA_MAPPER_PATH, ID, dataMapperId), HttpMethod.DELETE,
                HttpStatus.NO_CONTENT, null, "", null, "Test to delete dataMapper by id", false,
                false, null);
    }

    /**
     * test put DataMapper
     *
     * @throws Exception
     */
    @Test
    @SqlGroup({@Sql(scripts = "classpath:cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(scripts = "classpath:jdd_dataMapping.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)})
    void testPutDataMapper() throws Exception {
        final String dataMapperId = "231866de-4e4f-4ab7-948b-372ce1acaef2";
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.DATA_MAPPER_BASE_PATH + DataApiPathDict.DATA_MAPPER_PATH, ID, dataMapperId), HttpMethod.PUT,
                HttpStatus.NO_CONTENT, DATA_MAPPER_REQUEST_PUT_DATA_MAPPER_OK_JSON, "", null, "Test to put dataMapper ",
                false, false, null);

        // call get to get the id
        testHttpRequestWithExpectedResult(CommonUtils.placeholderFormat(DataApiPathDict.DATA_MAPPER_BASE_PATH + DataApiPathDict.DATA_MAPPER_PATH, ID, dataMapperId), HttpMethod.GET,
                HttpStatus.OK, null, DATA_MAPPER_EXPECTED_GET_DATA_MAPPER_AFTER_PUT_OK_JSON,
                JsonResponseTypeEnum.JSON_OBJECT, "Test to get dataMapper after put", false, false, null);
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
