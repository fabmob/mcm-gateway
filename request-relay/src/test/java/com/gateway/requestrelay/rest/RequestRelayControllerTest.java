package com.gateway.requestrelay.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.commonapi.tests.enums.JsonResponseTypeEnum;
import com.gateway.requestrelay.RequestRelayITTestCase;
import com.gateway.requestrelay.service.impl.RequestRelayServiceImpl;
import com.gateway.requestrelay.utils.enums.Protocol;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;


import static com.gateway.requestrelay.utils.constant.RequestRelayPathDict.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * Test class for #RequestRelayControllerTests
 */
@Slf4j
class RequestRelayControllerTest extends RequestRelayITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/test/resources/";
    public static final String RELAY_REQUEST_POST_OK_JSON = "request-relay/request/postRequestRelay_ok.json";
    public static final String RELAY_EXPECTED_POST_OK_JSON = "request-relay/expected/postRequestRelay_ok.json";
    public static final String RELAY_MOCK_POST_JSON = "request-relay/mock/postRequestRelayMock.json";
    public static final String RELAY_SOAP_EXPECTED_POST_JSON = "request-relay/expected/postRequestRelaySOAP_exception.json";

    /*
     * Mock MVC
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    RequestRelayServiceImpl mockRequestRelayService;


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
     * Test execute request-relay operation (REST)
     * @throws Exception
     */
    @Test
    void testExecuteREST() throws Exception {
        final Protocol protocol = Protocol.REST ;
        testHttpRequestWithExpectedResult(REQUEST_RELAY_FULL_PATH + protocol, HttpMethod.POST, HttpStatus.OK,
                RELAY_REQUEST_POST_OK_JSON,RELAY_EXPECTED_POST_OK_JSON ,
                JsonResponseTypeEnum.JSON_ARRAY, "Test post request-relay");
    }

    /**
     * Test execute request-relay operation (SOAP)
     */
    @Test
    void testExecuteSOAP()  {
        final Protocol protocol = Protocol.SOAP ;
        String requestPayloadPath = RELAY_REQUEST_POST_OK_JSON;
        String uri = REQUEST_RELAY_FULL_PATH + protocol;
        HttpMethod httpMethod = HttpMethod.POST;
       try{
           testHttpRequestWithExpectedResult(uri, httpMethod, HttpStatus.SERVICE_UNAVAILABLE,
                   requestPayloadPath,RELAY_SOAP_EXPECTED_POST_JSON ,
                   JsonResponseTypeEnum.JSON_ARRAY, "SOAP exception");
       } catch (Exception e){
           Assertions.assertEquals(e.getCause().getMessage(),new UnavailableException("SOAP case not implemented yet").getServiceUnavailable().getDescription());

       }


    }




    /**
     * Central management of the RequestRelayControllerTest
     * @param uri the uri of the operation
     * @param httpMethod HTTP VERB (GET, PUT, POST, PATCH, DELETE)
     * @param httpStatusExpectedResult Https code status expected
     * @param requestPayloadPath payload of the request (optionnal for some verbs)
     * @param expectedResultPath path of the json expected answer file
     * @param resulType JsonArray or JsonObject expected as result
     * @param message Message of the test running
     * @throws Exception
     */
    private void testHttpRequestWithExpectedResult(String uri, HttpMethod httpMethod,
                                                   HttpStatus httpStatusExpectedResult, String requestPayloadPath,
                                                   String expectedResultPath, JsonResponseTypeEnum resulType,
                                                   final String message) throws  Exception {

       ResponseEntity<String> mspResponseMocked = createMockedMSPResponse();

        // mock makecall() operation with a stub object from createMocked function
        doReturn(mspResponseMocked.getBody()).when(mockRequestRelayService).processCalls(any());


        // preparing the service call and expected elements
        ResultMatcher mockResultMatcher = WsTestUtil.getResultMatcher(httpStatusExpectedResult);
        String requestPayloadContent = StringUtils.isNotBlank(requestPayloadPath) ? WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + requestPayloadPath) : "";
        MockHttpServletRequestBuilder requestBuilder = WsTestUtil.getMockHttpServletRequestBuilder("", uri, httpMethod, requestPayloadContent);

        // trigger the call and check expected encoding and http status code
        String content;
        content = this.mockMvc.perform(requestBuilder).andExpect(mockResultMatcher).andReturn().getResponse().getContentAsString();



        String pathFile = IT_RESOURCES_PATH + expectedResultPath;
        // get the expected json response as string
        final String jsonRef = StringUtils.isBlank(expectedResultPath) ? "" : WsTestUtil.readJsonFromFilePath(pathFile);

        // compare the expected json with the result
        if (resulType != null) {
            switch (resulType) {
                case JSON_ARRAY:
                    JSONObject json = new JSONObject(jsonRef);
                    JSONArray expectedJsonArray = json.toJSONArray(json.names());
                    JSONObject jsonresult = new JSONObject(content);
                    JSONArray resultJsonArray = json.toJSONArray(jsonresult.names());

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
     * @return  Fake object for mock
     * @throws IOException
     */
    private ResponseEntity<String> createMockedMSPResponse() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+ RELAY_MOCK_POST_JSON );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_UTF8_VALUE));
        ResponseEntity<String> response =
                new ResponseEntity<String>(mockStringyfied,headers,HttpStatus.OK);

        return response;
    }



}
