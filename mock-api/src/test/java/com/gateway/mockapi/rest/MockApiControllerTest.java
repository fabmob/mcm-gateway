package com.gateway.mockapi.rest;

import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.mockapi.MockApiITTestCase;
import com.gateway.mockapi.service.MockApiService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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

import static com.gateway.mockapi.utils.constant.MockApiPathDict.MOCK_API_PATH;
import static com.gateway.mockapi.utils.constant.MockApiPathDict.MOCK_OPERATION_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Test class for #ApiControllerTests
 */
@Slf4j
class MockApiControllerTest extends MockApiITTestCase {

    /*
     * Mock MVC
     */
    @Autowired
    private MockMvc mockMvc;

    /*
     * Web Application Context
     */
    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    MockApiService service;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testImplementedMockResponses() throws Exception {
        assertEquals("{}", this.testHttpControllerCall(HttpMethod.GET, "200/response.json", 200, 0));
        assertEquals("{}", this.testHttpControllerCall(HttpMethod.POST, "200/response.json", 200, 0));
        assertEquals("{}", this.testHttpControllerCall(HttpMethod.PUT, "200/response.json", 200, 0));
        assertEquals("{}", this.testHttpControllerCall(HttpMethod.PATCH, "200/response.json", 200, 0));
        assertEquals("{}", this.testHttpControllerCall(HttpMethod.DELETE, "200/response.json", 200, 0));
    }

    @Test
    void testImplementedMockDelayResponses() throws Exception {
        long chronoStart = java.lang.System.currentTimeMillis();
        this.testHttpControllerCall(HttpMethod.GET, "200/response.json", 200, 2500);
        long chronoEnd = java.lang.System.currentTimeMillis();
        long duration = chronoEnd - chronoStart;
        log.info("Method responded in : " + duration + "ms");
        assertTrue(chronoEnd - chronoStart > 2500);

    }

    private String testHttpControllerCall(HttpMethod method, String mockPath, int expectedResponseCode, Integer delay) throws Exception {
        MediaType responseContentType = MediaType.APPLICATION_JSON;
        ResultMatcher mockResultMatcher = WsTestUtil.getResultMatcher(HttpStatus.valueOf(expectedResponseCode));
        MockHttpServletRequestBuilder requestBuilder = WsTestUtil.getMockHttpServletRequestBuilder("", MOCK_API_PATH + "/" + MOCK_OPERATION_NAME, method, "");

        Mockito.when(service.getMockedResponseCode(anyString())).thenReturn(expectedResponseCode);
        Mockito.when(service.getMockedBody(anyString())).thenReturn("{}");

        return this.mockMvc.
                perform(requestBuilder.header("mock-path", mockPath).header("mock-delay", delay))
                .andExpect(mockResultMatcher)
                .andExpect(MockMvcResultMatchers.content().contentType(responseContentType))
                .andReturn()
                .getResponse().getContentAsString();
    }
}
