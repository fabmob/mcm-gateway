package com.gateway.requestrelay.service;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.commonapi.dto.requestrelay.MspCallsFinalDTO;
import com.gateway.commonapi.tests.WsTestUtil;
import com.gateway.requestrelay.RequestRelayITTestCase;
import com.gateway.requestrelay.service.impl.RequestRelayServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


class RequestRelayServiceTest extends RequestRelayITTestCase {

    public static final String IT_RESOURCES_PATH = "./src/test/resources/";
    public static final String RELAY_REQUEST_POST_OK_JSON = "request-relay/request/postRequestRelay_ok.json";
    public static final String RELAY_MOCK_POST_JSON = "request-relay/mock/postRequestRelayMock.json";


    @Autowired
    ObjectMapper objectMapper;


    @InjectMocks
    RequestRelayServiceImpl requestRelayService;

    @MockBean
    RequestRelayServiceImpl callService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testCalls() throws Exception {
        ResponseEntity<String> mspResponseMocked = createMockedMSPResponse();
        // mock makecall() operation with a stub object from createMocked function
        doReturn(mspResponseMocked).when(callService).makeCall(any(),any());
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+ RELAY_REQUEST_POST_OK_JSON );
        ObjectReader objectReader = objectMapper.reader().forType(new TypeReference<MspCallsFinalDTO>(){});
        MspCallsFinalDTO call = objectReader.readValue(mockStringyfied);
        assertEquals(requestRelayService.processCalls(call).replace("\n","").replace(" ",""),mspResponseMocked.getBody().replace(" ",""));

    }

    /**
     * Create a mock of MSP response
     * @return  Fake object for mock
     * @throws IOException
    */

    private ResponseEntity<String> createMockedMSPResponse() throws IOException {
        String mockStringyfied = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+ RELAY_MOCK_POST_JSON );
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type","utf-16");
        ResponseEntity<String> response =
                new ResponseEntity<String>(mockStringyfied,headers, HttpStatus.OK);

        return response;
    }

}
