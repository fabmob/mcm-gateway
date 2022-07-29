package com.gateway.adapter.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.adapter.service.AuthenticationService;
import com.gateway.commonapi.dto.data.*;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.tests.WsTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.*;
import java.util.*;

import static com.gateway.adapter.utils.constant.AdapterMessageDict.HIDDEN_TEXT;
import static com.gateway.adapter.utils.CustomUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
 class DefaultAdapterServiceImplTest {
    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String DEFAULT_ADAPTER_EXPECTED_ADAPT_GET_OK_JSON = "default-adapter/expected/adaptGet_ok.json";
    public static final String DEFAULT_ADAPTER_EXPECTED_ADAPT_GET_WHEN_RESPONSE_IS_OBJECT_JSON = "default-adapter/expected/adaptGet_jcDecaux_object_ok.json";
    public static final String REQUEST_BODY_OK_JSON = "default-adapter/request/requestBody.json";
    public static final String REQUEST_BODY_EXCEPTION_JSON = "default-adapter/request/requestBodyException.json";


    @Autowired
    ObjectMapper objectMapper;

    @InjectMocks
    private DefaultAdapterServiceImpl defaultAdapterServiceImpl;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    AuthenticationService authenticationService;

    @Value("${gateway.service.requestRelay.url}")
    private String uri;
    @Value("${gateway.service.dataapi.url}")
    private String dataApiUri ;
    @Value("${gateway.service.requestRelay.isMocked}")
    private Boolean isRequestRelayMocked;
    @Value("${gateway.service.requestRelay.url}")
    private String requestRelayUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void restTemplate() {
        assertNotNull(defaultAdapterServiceImpl.restTemplate());
    }

    private MspActionDTO createMockMspAction() {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        MspActionDTO mspActionDTO = new MspActionDTO();
        mspActionDTO.setMspActionId(mspActionId);
        mspActionDTO.setAction("VEHICULE_SEARCH");
        return mspActionDTO;
    }

    private MspStandardDTO[] createMockMspStandard() {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        MspStandardDTO mspStandardDTO = new MspStandardDTO();
        mspStandardDTO.setMspActionsId(mspActionId);
        mspStandardDTO.setMspMetaId(mspId);
        return new MspStandardDTO[]{mspStandardDTO};
    }

    private MspCallsDTO[] createMockMspCallsDTOWithoutBody() {
        List<MspCallsDTO> mspCallsDTOlist = new ArrayList<MspCallsDTO>();
        MspCallsDTO callDTO = new MspCallsDTO();
        callDTO.setMspCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        callDTO.setMethod("POST");

        Set<HeadersDTO> headers = new HashSet<>();
        HeadersDTO headersDTO = new HeadersDTO();
        headersDTO.setValueTemplate("{user}:{password}");
        headersDTO.setSecurityFlag(0);
        headersDTO.setProcessFunction("BASE64");

        Set<HeadersValuesTemplateDTO> headersValuesTemplate = new HashSet<>();
        HeadersValuesTemplateDTO headerValuesTemplateDTO = new HeadersValuesTemplateDTO();
        headerValuesTemplateDTO.setKey("user");
        headerValuesTemplateDTO.setValue("{MAAS_CITYSCOOT_CLIENT_ID:kFEkfWQt8UATcsccM7Ey}");
        headersValuesTemplate.add(headerValuesTemplateDTO);
        headersDTO.setHeadersValuesTemplate(headersValuesTemplate);
        headers.add(headersDTO);

        callDTO.setHeaders(headers);
        callDTO.setNbCalls(1);

        ParamsDTO paramsDTO = new ParamsDTO();
        paramsDTO.setValue("lyon");
        paramsDTO.setKey("contract");
        Set<ParamsDTO> paramsDTOSet = new HashSet<>();
        paramsDTOSet.add(paramsDTO);

        callDTO.setParams(paramsDTOSet);
        callDTO.setUrl("https://api.jcdecaux.com/vls/v1/stations");


        mspCallsDTOlist.add(callDTO);
        return mspCallsDTOlist.toArray(MspCallsDTO[]::new);
    }


    private MspCallsDTO[] createMockMspCallsDTOWithBody() {
        List<MspCallsDTO> mspCallsDTOlist = new ArrayList<MspCallsDTO>();
        MspCallsDTO callDTO = new MspCallsDTO();
        callDTO.setMspCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        callDTO.setMethod("POST");

        Set<HeadersDTO> headers = new HashSet<>();
        HeadersDTO headersDTO = new HeadersDTO();
        headersDTO.setValueTemplate("{user}:{password}");
        headersDTO.setSecurityFlag(0);
        headersDTO.setProcessFunction("BASE64");

        Set<HeadersValuesTemplateDTO> headersValuesTemplate = new HashSet<>();
        HeadersValuesTemplateDTO headerValuesTemplateDTO = new HeadersValuesTemplateDTO();
        headerValuesTemplateDTO.setKey("user");
        headerValuesTemplateDTO.setValue("{MAAS_CITYSCOOT_CLIENT_ID:kFEkfWQt8UATcsccM7Ey}");
        headersValuesTemplate.add(headerValuesTemplateDTO);
        headersDTO.setHeadersValuesTemplate(headersValuesTemplate);
        headers.add(headersDTO);

        callDTO.setHeaders(headers);
        callDTO.setNbCalls(1);

        ParamsDTO paramsDTO = new ParamsDTO();
        paramsDTO.setValue("lyon");
        paramsDTO.setKey("contract");
        Set<ParamsDTO> paramsDTOSet = new HashSet<>();
        paramsDTOSet.add(paramsDTO);

        callDTO.setParams(paramsDTOSet);
        callDTO.setUrl("https://api.jcdecaux.com/vls/v1/stations");

        BodyParamsDTO bodyParamsDTO = new BodyParamsDTO(null,"TEST",null,0,"test",null,null,null);
        BodyParamsDTO bodyParams2DTO = new BodyParamsDTO(null,"STATION_ID","stationId",0,null,null,null,null);
        BodyParamsDTO bodyParams3DTO = new BodyParamsDTO(null,"DATE","date",1,null,"+15","Europe/Paris",null);
        Set<BodyParamsDTO> bodyParams = new HashSet<>();
        bodyParams.add(bodyParamsDTO);
        bodyParams.add(bodyParams2DTO);
        bodyParams.add(bodyParams3DTO);
        BodyDTO body = new BodyDTO();
        body.setTemplate("\"test\":\"${TEST}\",\"station\":\"${STATION_ID}\",\"date\":\"${DATE}\"");
        body.setBodyParams(bodyParams);

        callDTO.setBody(body);

        mspCallsDTOlist.add(callDTO);
        return mspCallsDTOlist.toArray(MspCallsDTO[]::new);
    }

    private MspCallsDTO[] createMockMspCallsDTOWhenNbCallsSupOf1() {
        List<MspCallsDTO> mspCallsDTOlist = new ArrayList<MspCallsDTO>();
        MspCallsDTO callDTO = new MspCallsDTO();
        callDTO.setMspCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        callDTO.setMethod("POST");

        Set<HeadersDTO> headers = new HashSet<>();
        HeadersDTO headersDTO = new HeadersDTO();
        headersDTO.setValueTemplate("{user}:{password}");
        headersDTO.setSecurityFlag(0);
        headersDTO.setProcessFunction("BASE64");

        Set<HeadersValuesTemplateDTO> headersValuesTemplate = new HashSet<>();
        HeadersValuesTemplateDTO headerValuesTemplateDTO = new HeadersValuesTemplateDTO();
        headerValuesTemplateDTO.setKey("user");
        headerValuesTemplateDTO.setValue("{MAAS_CITYSCOOT_CLIENT_ID:kFEkfWQt8UATcsccM7Ey}");
        headersValuesTemplate.add(headerValuesTemplateDTO);
        headersDTO.setHeadersValuesTemplate(headersValuesTemplate);
        headers.add(headersDTO);

        callDTO.setHeaders(headers);
        callDTO.setNbCalls(3);

        ParamsDTO paramsDTO = new ParamsDTO();
        paramsDTO.setValue("lyon");
        paramsDTO.setKey("contract");
        Set<ParamsDTO> paramsDTOSet = new HashSet<>();
        paramsDTOSet.add(paramsDTO);

        callDTO.setParams(paramsDTOSet);
        callDTO.setUrl("https://api.jcdecaux.com/vls/v1/stations");

        mspCallsDTOlist.add(callDTO);
        return mspCallsDTOlist.toArray(MspCallsDTO[]::new);
    }

    private MspCallsDTO[] createMockMspCallsDTOWhenSecurityFlagIsNotNull() {
        List<MspCallsDTO> mspCallsDTOlist = new ArrayList<>();
        MspCallsDTO callDTO = new MspCallsDTO();
        callDTO.setMspCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
        callDTO.setMethod("POST");

        Set<HeadersDTO> headers = new HashSet<>();
        HeadersDTO headersDTO = new HeadersDTO();
        headersDTO.setValueTemplate("{user}:{password}");
        headersDTO.setSecurityFlag(1);
        headersDTO.setProcessFunction("BASE64");

        Set<HeadersValuesTemplateDTO> headersValuesTemplate = new HashSet<>();
        HeadersValuesTemplateDTO headerValuesTemplateDTO = new HeadersValuesTemplateDTO();
        headerValuesTemplateDTO.setKey("user");
        headerValuesTemplateDTO.setValue("{MAAS_CITYSCOOT_CLIENT_ID:kFEkfWQt8UATcsccM7Ey}");
        headersValuesTemplate.add(headerValuesTemplateDTO);

        headersDTO.setHeadersValuesTemplate(headersValuesTemplate);
        headers.add(headersDTO);

        callDTO.setHeaders(headers);
        callDTO.setNbCalls(1);

        ParamsDTO paramsDTO = new ParamsDTO();
        paramsDTO.setValue("lyon");
        paramsDTO.setKey("contract");
        Set<ParamsDTO> paramsDTOSet = new HashSet<>();
        paramsDTOSet.add(paramsDTO);

        callDTO.setParams(paramsDTOSet);
        callDTO.setUrl("https://api.jcdecaux.com/vls/v1/stations");

        mspCallsDTOlist.add(callDTO);
        MspCallsDTO[] mspCallsDTO = mspCallsDTOlist.toArray(MspCallsDTO[]::new);
        return mspCallsDTO;
    }

    private String createMockResponse() throws IOException {
        return  WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + DEFAULT_ADAPTER_EXPECTED_ADAPT_GET_OK_JSON);
    }

    private String createMockResponseWhenResponseIsNotAList() throws IOException {
        return  WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + DEFAULT_ADAPTER_EXPECTED_ADAPT_GET_WHEN_RESPONSE_IS_OBJECT_JSON);
    }

    private TokenDTO createMockTokenDTO() {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setTokenId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f881"));
        return tokenDTO;
    }

    @Test
     void testAdaptGetOperation() throws IOException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);
        objectResponse.getBody();

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId,null);
        assertEquals(((HashMap) response.get(0)).get("contract_name"), "lyon");
    }

    @Test
    void testAdaptPostOperation() throws IOException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        String requestBodyString = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+REQUEST_BODY_OK_JSON);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader objectReader = mapper.reader().forType(new TypeReference<Map<String, Object>>() {});
        Map<String, Object> requestBody = objectReader.readValue(requestBodyString);

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);
        objectResponse.getBody();


        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, (Map<String, Object>) requestBody);
        assertEquals(((HashMap) response.get(0)).get("contract_name"), "lyon");
    }


    @Test
    void testAdaptPostOperationException() throws IOException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        String requestBodyString = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH+REQUEST_BODY_EXCEPTION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader objectReader = mapper.reader().forType(new TypeReference<Map<String, Object>>() {});
        Map<String, Object> requestBody = objectReader.readValue(requestBodyString);

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);
        objectResponse.getBody();


        assertThrows(InternalException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, requestBody);
        });
    }



    @Test
     void testAdaptGetOperationWhenSecurityFlagIsNotNull() throws IOException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);


        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWhenSecurityFlagIsNotNull());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        assertEquals(((HashMap) response.get(0)).get("contract_name"), "lyon");
    }

    @Test
    public void testAdaptGetOperationExceptionsWhenActionIsNull()  {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");

        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);});
        String actualMessage = exception.getMessage();
        assertEquals(null,actualMessage);
    }

    @Test
    public void testAdaptGetOperationExceptionsWhenCallsAreNull()  {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");

        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);});
        String actualMessage = exception.getMessage();
        assertEquals(null,actualMessage);
    }

    @Test
    public void testAdaptGetOperationExceptionsWhenResponseRequestRelayIsNull()  {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");

        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object[].class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);});
        String actualMessage = exception.getMessage();
        assertEquals(null,actualMessage);
    }



    @Test
    void testAdaptGetOperationResponseIsNotArray() throws IOException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);


        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseWhenResponseIsNotAList());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);
        objectResponse.getBody();

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        assertEquals(((HashMap) response.get(0)).get("contract_name"), "lyon");
    }


    @Test
    void testFormatDateTime() throws IllegalFormatException{

        //TimeZones to test
        String timeZoneTokyo = "Asia/Tokyo"; //No summer/winter hours UTC+9
        String timeZoneParis = "Europe/Paris"; //Has summer/winter hours UTC+1/UTC+2
        String timeZoneCairo = "Africa/Cairo"; //No summer/winter hours UTC+2
        String timeZoneLondon = "Europe/London"; //Has summer/winter hours UTC+0/UTC+1
        String timeZoneBrazil = "Brazil/Acre";
        String timeZoneKiritimati = "Pacific/Kiritimati";

        //------WINTER HOURS------//
        CharSequence instantExpected = "2022-12-22T10:15:30Z"; //Z equals 00:00 offset
        OffsetDateTime odt = OffsetDateTime.parse(instantExpected);

        assertEquals("2022-12-22T19:15:30+09:00", formatDateTime(odt, timeZoneTokyo).toString());
        assertEquals("2022-12-22T11:15:30+01:00", formatDateTime(odt, timeZoneParis).toString());
        assertEquals("2022-12-22T12:15:30+02:00", formatDateTime(odt, timeZoneCairo).toString());
        assertEquals("2022-12-22T10:15:30Z", formatDateTime(odt, timeZoneLondon).toString());
        assertEquals("2022-12-22T05:15:30-05:00", formatDateTime(odt, timeZoneBrazil).toString());
        assertEquals("2022-12-23T00:15:30+14:00", formatDateTime(odt, timeZoneKiritimati).toString());


        //------SUMMER HOURS------//
        CharSequence instantExpected2 = "2022-06-22T10:15:30Z";
        OffsetDateTime odt2 = OffsetDateTime.parse(instantExpected2);
        assertEquals("2022-06-22T19:15:30+09:00", formatDateTime(odt2, timeZoneTokyo).toString());
        assertEquals("2022-06-22T12:15:30+02:00", formatDateTime(odt2, timeZoneParis).toString());
        assertEquals("2022-06-22T12:15:30+02:00", formatDateTime(odt2, timeZoneCairo).toString());
        assertEquals("2022-06-22T11:15:30+01:00", formatDateTime(odt2, timeZoneLondon).toString());

        assertThrows(InternalException.class, () -> {
            formatDateTime(odt, "Cui/cui");
        });
    }

    @Test
    void testAssignPrecision(){
        String exampleOffsetDateTime = "1977-04-22T01:00-01:00"; //Visual example of OffsetDateTime format passed as argument
        OffsetDateTime odt = OffsetDateTime.parse(exampleOffsetDateTime);

        odt = assignPrecision(odt, "15");
        assertEquals("1977-04-22T01:15-01:00", odt.toString());

        odt = assignPrecision(odt, "60");
        assertEquals("1977-04-22T02:15-01:00", odt.toString());

        odt = assignPrecision(odt, "-30");
        assertEquals("1977-04-22T01:45-01:00", odt.toString());

        OffsetDateTime finalOdt = odt;
        assertThrows(InternalException.class, () -> {
            assignPrecision(finalOdt, "oui");
        });

    }

    @Test
    void testCensoredBodyParam(){
        String censoredValue = "MotDePasse!";
        String unCensoredValue = "STATION_SEARCH";
        assertEquals(HIDDEN_TEXT, censoredBodyParam(1, censoredValue));
        assertEquals(unCensoredValue, censoredBodyParam(0, unCensoredValue));
    }

}
