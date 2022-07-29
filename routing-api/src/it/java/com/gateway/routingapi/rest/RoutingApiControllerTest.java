package com.gateway.routingapi.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.tests.enums.JsonResponseTypeEnum;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.routingapi.service.RoutingService;
import com.gateway.routingapi.service.impl.RoutingServiceImpl;
import com.gateway.routingapi.util.constant.RoutingDict;
import com.gateway.routingapi.utils.RoutingITTestCase;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.gateway.routingapi.util.constant.RoutingDict.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@Slf4j
class RoutingApiControllerTest extends RoutingITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String ROUTING_MOCK_ADAPT_GET_JSON = "mock/routeMock.json";
    public static final String ROUTING_EXPECTED_ADAPT_GET_OK_JSON = "expected/route_ok.json";
    public static final String REQUEST_BODY_OK_JSON = "request/requestBody.json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    RoutingServiceImpl routingService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        MockitoAnnotations.initMocks(this);
        JacksonTester.initFields(this, this.objectMapper);
    }
    /**
     * Test execute Default adapter PostOperation
     *
     * @throws Exception
     */
    @Test
    void testAdaptPostOperation() throws Exception {

        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        String actionName = "GLOBAL_VIEW_SEARCH";
        Map<String, String> params = new HashMap<>();

        String uriRouting = CommonUtils.placeholderFormat(
                ROUTE_PATH + ROUTING_MSP_ID_PATH, "mspId", mspId.toString() +
                        ROUTING_ACTION_NAME_ID_PATH, "actionName", actionName);

        testHttpRequestWithExpectedResult(uriRouting, HttpMethod.POST, HttpStatus.OK,
                REQUEST_BODY_OK_JSON, ROUTING_EXPECTED_ADAPT_GET_OK_JSON,
                JsonResponseTypeEnum.JSON_ARRAY, "Test adapt post");
    }


    /**
     * Test execute Default adapter PostOperation
     *
     * @throws Exception
     */
    @Test
    void testAdaptPostOperationNull() throws Exception {

        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        String actionName = "GLOBAL_VIEW_SEARCH";
        Map<String, String> params = new HashMap<>();

        String uriRouting = CommonUtils.placeholderFormat(
                ROUTE_PATH + ROUTING_MSP_ID_PATH, "mspId", mspId.toString() +
                        ROUTING_ACTION_NAME_ID_PATH, "actionName", actionName);

        testHttpRequestWithExpectedResult(uriRouting, HttpMethod.POST, HttpStatus.OK,
                null, null,
                null, "Test adapt post");
    }



    /**
     * Central management of the Routing  ControllerTest
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

        Object mspResponseMocked = createMockedResponse();
        Object mspResponseMockedNull = createMockedResponseNull();


        // mock makecall() operation with a stub object from createMocked function
        doReturn(mspResponseMocked).when(routingService).routeOperation(any(), any(), any(), any());
        // mock makecall() operation with a stub object from createMocked function
        doReturn(mspResponseMocked).when(routingService).routeOperation(null,null,null,null);

        // preparing the service call and expected elements
        ResultMatcher mockResultMatcher = WsTestUtil.getResultMatcher(httpStatusExpectedResult);
        String requestPayloadContent = StringUtils.isNotBlank(requestPayloadPath) ? WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + requestPayloadPath) : "";
        MockHttpServletRequestBuilder requestBuilder = WsTestUtil.getMockHttpServletRequestBuilder(RoutingDict.GLOBAL_PATH, uri, httpMethod, requestPayloadContent);
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
    private Object createMockedResponse() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + ROUTING_MOCK_ADAPT_GET_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<Object>() {
        });

        return objectReader.readValue(mockStringyfied);
    }

    /**
     * Create a mock of MSP response
     *
     * @return Fake object for mock
     */
    private Object createMockedResponseNull() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + ROUTING_MOCK_ADAPT_GET_JSON);
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<Object>() {
        });

        return objectReader.readValue(mockStringyfied);
    }

}
