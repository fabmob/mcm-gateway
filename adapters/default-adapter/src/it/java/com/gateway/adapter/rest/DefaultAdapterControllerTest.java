package com.gateway.adapter.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.adapter.AdapterITTestCase;
import com.gateway.adapter.service.AuthenticationService;
import com.gateway.adapter.utils.constant.AdapterPathDict;
import com.gateway.adapter.service.impl.DefaultAdapterServiceImpl;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.tests.enums.JsonResponseTypeEnum;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.*;

import static com.gateway.adapter.utils.constant.AdapterPathDict.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@Slf4j
class DefaultAdapterControllerTest extends AdapterITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String DEFAULT_ADAPTER_MOCK_ADAPT_GET_JSON = "default-adapter/mock/adaptGetMock.json";
    public static final String DEFAULT_ADAPTER_EXPECTED_ADAPT_GET_OK_JSON = "default-adapter/expected/adaptGet_ok.json";
    public static final String REQUEST_BODY_OK_JSON = "default-adapter/request/requestBody.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    DefaultAdapterServiceImpl defaultAdapterService;

    @MockBean
    AuthenticationService authenticationService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, this.objectMapper);
    }

    /**
     * Test execute Default adapter GetOperation
     *
     * @throws Exception
     */
    @Test
    void testAdaptGetOperation() throws Exception {

        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880");
        Map<String, String> params = new HashMap<>();

        String uriDefaultAdapter = CommonUtils.placeholderFormat(ADAPTER_PATH + DEFAULT_ADAPTER_GET_BY_ACTIONS_ID_PATH, "actionId", mspActionId +
                DEFAULT_ADAPTER_MSP_ID_PATH, "mspId", String.valueOf(mspId));

        testHttpRequestWithExpectedResult(uriDefaultAdapter, HttpMethod.GET, HttpStatus.OK,
                null, DEFAULT_ADAPTER_EXPECTED_ADAPT_GET_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test adapt get");
    }

    /**
     * Test execute Default adapter PostOperation
     *
     * @throws Exception
     */
    @Test
    void testAdaptPostOperation() throws Exception {

        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880");
        Map<String, String> params = new HashMap<>();

        String uriDefaultAdapter = CommonUtils.placeholderFormat(ADAPTER_PATH + DEFAULT_ADAPTER_GET_BY_ACTIONS_ID_PATH, "actionId", mspActionId +
                DEFAULT_ADAPTER_MSP_ID_PATH, "mspId", String.valueOf(mspId));

        testHttpRequestWithExpectedResult(uriDefaultAdapter, HttpMethod.POST, HttpStatus.OK,
                REQUEST_BODY_OK_JSON, DEFAULT_ADAPTER_EXPECTED_ADAPT_GET_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test adapt post");
    }


    /**
     * Test execute Default adapter PostOperation with no given body
     *
     * @throws Exception
     */
    @Test
    void testAdaptPostNullBodyOperation() throws Exception {

        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880");
        Map<String, String> params = new HashMap<>();

        String uriDefaultAdapter = CommonUtils.placeholderFormat(ADAPTER_PATH + DEFAULT_ADAPTER_GET_BY_ACTIONS_ID_PATH, "actionId", mspActionId +
                DEFAULT_ADAPTER_MSP_ID_PATH, "mspId", String.valueOf(mspId));

        testHttpRequestWithExpectedResult(uriDefaultAdapter, HttpMethod.POST, HttpStatus.OK,
                null, DEFAULT_ADAPTER_EXPECTED_ADAPT_GET_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test adapt post");
    }


    /**
     * Central management of the Default Adapter ControllerTest
     *
     * @param uri                      the uri of the operation
     * @param httpMethod               HTTP VERB (GET, PUT, POST, PATCH, DELETE)
     * @param httpStatusExpectedResult Https code status expected
     * @param requestPayloadPath       payload of the request (optionnal for some verbs)
     * @param expectedResultPath       path of the json expected answer file
     * @param resulType                JsonArray or JsonObject expected as result
     * @param message                  Message of the test running
     */


    private void testHttpRequestWithExpectedResult(String uri, HttpMethod httpMethod,
                                                   HttpStatus httpStatusExpectedResult, String requestPayloadPath,
                                                   String expectedResultPath, JsonResponseTypeEnum resulType,
                                                   final String message) throws Exception {

        List<Object> mspResponseMocked = createMockedResponse();

        // mock makecall() operation with a stub object from createMocked function
        doReturn(mspResponseMocked).when(defaultAdapterService).adaptOperation(any(), any(),any(),any());

        // preparing the service call and expected elements
        ResultMatcher mockResultMatcher = WsTestUtil.getResultMatcher(httpStatusExpectedResult);
        String requestPayloadContent = StringUtils.isNotBlank(requestPayloadPath) ? WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + requestPayloadPath) : "";
        MockHttpServletRequestBuilder requestBuilder = WsTestUtil.getMockHttpServletRequestBuilder(AdapterPathDict.GLOBAL_PATH, uri, httpMethod, requestPayloadContent);
        MediaType responseContentType = WsTestUtil.getMediaType(httpMethod);

        // trigger the call and check expected encoding and http status code
        String content;
        if (responseContentType != null) {
            content = this.mockMvc.
                    perform(requestBuilder)
                    .andExpect(mockResultMatcher)
                    .andExpect(MockMvcResultMatchers.content().contentType(responseContentType))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } else {
            content = this.mockMvc
                    .perform(
                            requestBuilder
                    )
                    .andExpect(mockResultMatcher)
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        }

        String pathFile = IT_RESOURCES_PATH + expectedResultPath;
        // get the expected json response as string
        final String jsonRef = StringUtils.isBlank(expectedResultPath) ? "" : WsTestUtil.readJsonFromFilePath(pathFile);

        // compare the expected json with the result
        if (resulType != null) {
            switch (resulType) {
                case JSON_ARRAY:
                    JSONArray expectedJsonArray = new JSONArray(jsonRef);
                    JSONArray resultJsonArray = new JSONArray(content);
                    Assertions.assertEquals(expectedJsonArray.toString(4), resultJsonArray.toString(4), message);
                    break;
                case JSON_OBJECT:
                    JSONObject expectedJsonObject = new JSONObject(jsonRef);
                    JSONObject resultJsonObject = new JSONObject(content);
                    Assertions.assertEquals(expectedJsonObject.toString(4), resultJsonObject.toString(4), message);
                    break;
                default:
                    break;
            }
            log.info("Test executed : {}", message);
        }
    }


    /**
     * Create a mock of MSP response
     *
     * @return Fake object for mock
     */
    private List<Object> createMockedResponse() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + DEFAULT_ADAPTER_MOCK_ADAPT_GET_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<List<Object>>() {
        });

        return objectReader.readValue(mockStringyfied);
    }
}