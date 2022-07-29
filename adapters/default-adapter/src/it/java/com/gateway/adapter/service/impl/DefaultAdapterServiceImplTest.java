package com.gateway.adapter.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.gateway.adapter.service.AuthenticationService;
import com.gateway.commonapi.dto.api.AssetType;
import com.gateway.commonapi.dto.data.*;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.tests.WsTestUtil;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;

import static com.gateway.adapter.utils.CustomUtils.*;
import static com.gateway.adapter.utils.constant.AdapterMessageDict.HIDDEN_TEXT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)

class DefaultAdapterServiceImplTest {
    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String DEFAULT_ADAPTER_MOCK_ADAPT_GET_OK_JSON = "default-adapter/mock/adaptGetMock.json";
    public static final String MOCK_TOKEN_RESPONSE_OK_JSON = "default-adapter/mock/mock_token_response.json";
    public static final String TEST_WITH_SELECTOR_ONE_OBJECT = "default-adapter/mock/test_with_selector_msp_one_object.json";
    public static final String TEST_WITH_SELECTOR_NO_MAPPING = "default-adapter/mock/test_no_data_mapper.json";
    public static final String TEST_WITH_SELECTOR_DEFAULT_VALUE_SIMPLE_ELEMENTS = "default-adapter/mock/test_default_value.json";
    public static final String TEST_WITH_SELECTOR_ELEMENTS_SIMPLE = "default-adapter/mock/test_elements_simple.json";
    public static final String DEFAULT_ADAPTER_EXPECTED_ADAPT_GET_WHEN_RESPONSE_IS_OBJECT_JSON = "default-adapter/expected/adaptGet_jcDecaux_object_ok.json";
    public static final String REQUEST_BODY_OK_JSON = "default-adapter/request/requestBody.json";
    public static final String REQUEST_BODY_EXCEPTION_JSON = "default-adapter/request/requestBodyException.json";
    public static final String TEST_WITH_SELECTOR = "default-adapter/mock/test_with_selector.json";
    public static final String MOCK_ASSET = "default-adapter/mock/mock_AvailableAsset.json";

    @Mock
    private ErrorMessages errorMessage;
    @InjectMocks
    private DefaultAdapterServiceImpl defaultAdapterServiceImpl;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    AuthenticationService authenticationService;


    @Value("${gateway.service.requestRelay.url}")
    private String uri;

    @Value("${gateway.service.dataapi.url}")
    private String dataApiUri;
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
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        MspActionDTO mspActionDTO = new MspActionDTO();
        mspActionDTO.setMspActionId(mspActionId);
        mspActionDTO.setAction("AVAILABLE_ASSET_SEARCH");
        return mspActionDTO;
    }

    private MspActionDTO createMockAuthenticationAction() {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        MspActionDTO mspActionDTO = new MspActionDTO();
        mspActionDTO.setMspActionId(mspActionId);
        mspActionDTO.setAction("");
        mspActionDTO.setAuthentication(true);
        return mspActionDTO;
    }

    private MspActionDTO createMockMspActionWithSelector() {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        MspActionDTO mspActionDTO = new MspActionDTO();
        mspActionDTO.setMspActionId(mspActionId);
        SelectorDTO selectorDTO = new SelectorDTO();
        selectorDTO.setSelectorId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f865"));
        selectorDTO.setKey("assetsType");
        mspActionDTO.setSelector(selectorDTO);
        mspActionDTO.setAction("AVAILABLE_ASSET_SEARCH");
        return mspActionDTO;
    }

    private MspStandardDTO[] createMockMspStandard() {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        MspStandardDTO mspStandardDTO = new MspStandardDTO();
        mspStandardDTO.setMspActionsId(mspActionId);
        mspStandardDTO.setMspId(mspId);
        return new MspStandardDTO[]{mspStandardDTO};
    }

    private MspCallsDTO[] createMockMspCallsDTOWithoutBody() {
        List<MspCallsDTO> mspCallsDTOlist = new ArrayList<MspCallsDTO>();

        MspCallsDTO callDTOMultiCalls = new MspCallsDTO();
        callDTOMultiCalls.setMspCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f886"));
        callDTOMultiCalls.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        callDTOMultiCalls.setMethod("POST");

        MspCallsDTO callDTO = new MspCallsDTO();
        callDTO.setMspCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
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

        callDTOMultiCalls.setHeaders(headers);
        callDTOMultiCalls.setNbCalls(2);
        Set<ParamsMultiCallsDTO> paramsMultiCalls = new HashSet<>();
        ParamsMultiCallsDTO param = new ParamsMultiCallsDTO();
        param.setKey("TripStartDate");
        param.setInitValue("NOW");
        param.setValueOffset("30");
        param.setTimezone("Europe/Paris");
        paramsMultiCalls.add(param);
        callDTOMultiCalls.setParamsMultiCalls(paramsMultiCalls);

        ParamsDTO paramsDTO = new ParamsDTO();
        paramsDTO.setValue("lyon");
        paramsDTO.setKey("contract");
        Set<ParamsDTO> paramsDTOSet = new HashSet<>();
        paramsDTOSet.add(paramsDTO);

        callDTO.setParams(paramsDTOSet);
        callDTO.setUrl("https://api.jcdecaux.com/vls/v1/stations");
        callDTOMultiCalls.setParams(paramsDTOSet);
        callDTOMultiCalls.setUrl("http://demo7362099.mockable.io/mockedmsp/postrequest");

        mspCallsDTOlist.add(callDTO);
        mspCallsDTOlist.add(callDTOMultiCalls);
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

        ParamsDTO paramsDTO2 = new ParamsDTO();
        paramsDTO2.setKeyMapper("key");
        paramsDTO2.setKey("key");


        Set<ParamsDTO> paramsDTOSet = new HashSet<>();
        paramsDTOSet.add(paramsDTO);
        paramsDTOSet.add(paramsDTO2);

        callDTO.setParams(paramsDTOSet);
        callDTO.setUrl("https://api.jcdecaux.com/vls/v1/stations");

        BodyParamsDTO bodyParamsDTO = new BodyParamsDTO(null, "TEST", null, 0, "test", null, null, null);
        BodyParamsDTO bodyParams2DTO = new BodyParamsDTO(null, "STATION_ID", "stationId", 0, null, null, null, null);
        BodyParamsDTO bodyParams3DTO = new BodyParamsDTO(null, "DATE", "date", 1, null, "+15", "Europe/Paris", null);
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

    private DataMapperDTO[] createMockDataMapperDTO() {
        List<DataMapperDTO> dataMapperList = new ArrayList<DataMapperDTO>();

        //datamapper with '.'
        DataMapperDTO dataMapper = new DataMapperDTO();
        dataMapper.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef2"));
        dataMapper.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper.setDefaultValue("default");
        dataMapper.setChampExterne("vehicules");
        dataMapper.setChampInterne("assets.assetId");
        dataMapper.setContainedValue("data.vehiculeId");
        dataMapper.setIsArray(1);
        dataMapperList.add(dataMapper);

        //datamapper converting array of string to boolean
        DataMapperDTO dataMapper2 = new DataMapperDTO();
        dataMapper2.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef3"));
        dataMapper2.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper2.setChampExterne("vehicules");
        dataMapper2.setChampInterne("assets.overriddenProperties.travelAbroad");
        dataMapper2.setContainedValue("data.characteristics.travelAbroad");
        dataMapper2.setIsArray(1);
        dataMapperList.add(dataMapper2);

        //datamapper with default value given
        DataMapperDTO dataMapper3 = new DataMapperDTO();
        dataMapper3.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef4"));
        dataMapper3.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper3.setChampExterne("vehicules");
        dataMapper3.setChampInterne("assets.assetType");
        dataMapper3.setDefaultValue("TAXI");
        dataMapper3.setIsArray(1);
        dataMapperList.add(dataMapper3);


        return dataMapperList.toArray(DataMapperDTO[]::new);
    }

    private DataMapperDTO[] createMockDataMapperDTODefaultValueSimple() {
        List<DataMapperDTO> dataMapperList = new ArrayList<DataMapperDTO>();
        DataMapperDTO dataMapper = new DataMapperDTO();
        dataMapper.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef2"));
        dataMapper.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper.setDefaultValue("public bus");
        dataMapper.setChampExterne(null);
        dataMapper.setChampInterne("assetSubClass");
        dataMapperList.add(dataMapper);
        return dataMapperList.toArray(DataMapperDTO[]::new);
    }

    private DataMapperDTO[] createMockDataMapperDTODefaultValue() {
        List<DataMapperDTO> dataMapperList = new ArrayList<DataMapperDTO>();
        DataMapperDTO dataMapper = new DataMapperDTO();
        dataMapper.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef2"));
        dataMapper.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper.setDefaultValue("Place de la République");
        dataMapper.setChampExterne(null);
        dataMapper.setChampInterne("sharedProperties.name");
        dataMapperList.add(dataMapper);
        return dataMapperList.toArray(DataMapperDTO[]::new);
    }


    private DataMapperDTO[] createMockDataMapperDTOSimpleElements() {
        List<DataMapperDTO> dataMapperList = new ArrayList<DataMapperDTO>();
        DataMapperDTO dataMapper = new DataMapperDTO();
        dataMapper.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef2"));
        dataMapper.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper.setChampExterne("vehiculeId");
        dataMapper.setChampInterne("assetTypeId");
        dataMapperList.add(dataMapper);
        return dataMapperList.toArray(DataMapperDTO[]::new);
    }


    private MspCallsDTO[] createMockMspCallsDTOWhenSecurityFlagIsNotNull() {
        List<MspCallsDTO> mspCallsDTOlist = new ArrayList<>();
        MspCallsDTO callDTO = new MspCallsDTO();
        callDTO.setMspCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
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
        return WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + DEFAULT_ADAPTER_MOCK_ADAPT_GET_OK_JSON);
    }

    private String createMockTokenResponse() throws IOException {
        return WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + MOCK_TOKEN_RESPONSE_OK_JSON);
    }

    private String createMockResponseSelectorOneObject() throws IOException {
        return WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + TEST_WITH_SELECTOR_ONE_OBJECT);
    }

    private String createMockResponseSelectorOneObjectWithoutDataMapping() throws IOException {
        return WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + TEST_WITH_SELECTOR_NO_MAPPING);
    }


    private String createMockResponseDefaultValue() throws IOException {
        return WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + TEST_WITH_SELECTOR_DEFAULT_VALUE_SIMPLE_ELEMENTS);
    }

    private String createMockResponseElementsSimple() throws IOException {
        return WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + TEST_WITH_SELECTOR_ELEMENTS_SIMPLE);
    }


    private String createMockAsset() throws IOException {
        return WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + MOCK_ASSET);
    }

    private String createMockResponseWithSelector() throws IOException {
        return WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + TEST_WITH_SELECTOR);
    }

    private String createMockResponseWhenResponseIsNotAList() throws IOException {
        return WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + DEFAULT_ADAPTER_EXPECTED_ADAPT_GET_WHEN_RESPONSE_IS_OBJECT_JSON);
    }

    private TokenDTO createMockTokenDTO() {
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setTokenId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f000"));
        return tokenDTO;
    }

    private MspMetaDTO createMockMspMeta() {
        MspMetaDTO mspMetaDTO = new MspMetaDTO();
        mspMetaDTO.setMspId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866"));
        return mspMetaDTO;
    }

    private MspActionDTO[] createMockAuthenticationActionsOfMsp(){
        List<MspActionDTO> actions = new ArrayList<MspActionDTO>();
        MspActionDTO authenticationAction = new MspActionDTO();
        authenticationAction.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        authenticationAction.setAuthentication(true);
        actions.add(authenticationAction);
        return actions.toArray(MspActionDTO[]::new);
    }

    private MspActionDTO[] createMockActionsOfMsp(){
        List<MspActionDTO> actions = new ArrayList<MspActionDTO>();
        MspActionDTO actionDTO = new MspActionDTO();
        actionDTO.setMspActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        actions.add(actionDTO);
        return actions.toArray(MspActionDTO[]::new);
    }

    @Test
    void testAdaptGetOperation() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        //List<AssetType> list = (List<AssetType>) response.get(0);
        AssetType list = (AssetType) response.get(0);
        System.out.println(list);
        assertEquals("f457579d-02f8-4479-b97b-ffb678e3f899", list.getAssets().get(0).getAssetId());

        // some test of exceptions
        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("500");

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenThrow(HttpClientErrorException.NotFound.class);

        assertThrows(NullPointerException.class, () -> defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null));

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenThrow(RestClientException.class);
        assertThrows(BadGatewayException.class, () -> defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null));

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenThrow(NullPointerException.class);
        assertThrows(UnavailableException.class, () -> defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null));


    }


    @Test
    void testAuthenticationAction () throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> tokenObjectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(tokenObjectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        ResponseEntity<String> tokenSavedInDatabase = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(tokenSavedInDatabase);

        Mockito.when(authenticationService.needAuthentication( List.of(actionsByMspId.getBody()))).thenReturn(true);

        Mockito.when( authenticationService.needAuthenticationAction( List.of(actionsByMspId.getBody()))).thenReturn(mspActionDTOResponse.getBody());

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);

        TokenDTO list = (TokenDTO) response.get(0);
        assertEquals("MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3", list.getAccessToken());

    }




    @Test
    void testAuthenticationException () throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> tokenObjectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(tokenObjectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        HttpClientErrorException.NotFound adapterException = (HttpClientErrorException.NotFound) HttpClientErrorException.create(HttpStatus.NOT_FOUND,"exception",null,null,null);

        ResponseEntity<String> tokenSavedInDatabase = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenThrow(adapterException);

        Mockito.when(authenticationService.needAuthentication( List.of(actionsByMspId.getBody()))).thenReturn(true);

        Mockito.when( authenticationService.needAuthenticationAction( List.of(actionsByMspId.getBody()))).thenReturn(mspActionDTOResponse.getBody());

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("exception");

        assertThrows(NotFoundException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        });

    }


    @Test
    void testAuthenticationExceptionRestClient () throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> tokenObjectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(tokenObjectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        RestClientException adapterException = new RestClientException("exception");

        ResponseEntity<String> tokenSavedInDatabase = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenThrow(adapterException);

        Mockito.when(authenticationService.needAuthentication( List.of(actionsByMspId.getBody()))).thenReturn(true);

        Mockito.when( authenticationService.needAuthenticationAction( List.of(actionsByMspId.getBody()))).thenReturn(mspActionDTOResponse.getBody());

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("exception");

        assertThrows(BadGatewayException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        });

    }


    @Test
    void testAuthenticationExceptionAny () throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> tokenObjectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(tokenObjectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        InternalException adapterException = new InternalException("exception");
        ResponseEntity<String> tokenSavedInDatabase = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenThrow(adapterException);

        Mockito.when(authenticationService.needAuthentication( List.of(actionsByMspId.getBody()))).thenReturn(true);

        Mockito.when( authenticationService.needAuthenticationAction( List.of(actionsByMspId.getBody()))).thenReturn(mspActionDTOResponse.getBody());

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("exception");

        assertThrows(UnavailableException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        });

    }


    @Test
    void testAdaptGetOperationUnspecifiedSelectorException() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseSelectorOneObject());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals(null, list.getAssets());

    }


    @Test
    void testAdaptGetOperationDataMapperNull() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAsset());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("asset1", list.getAssets().get(0).getAssetId());

    }

    @Test
    void testAdaptGetOperationResponseNullException() throws JSONException, IOException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        assertThrows(Exception.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        });


    }

    @Test
    void testWithSelectorMspReturnedObject() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspActionWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseSelectorOneObject());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("f457579d-02f8-4479-b97b-ffb678e3f899", list.getAssets().get(0).getAssetId());
    }


    @Test
    void testWithSelectorMspReturnedArray() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspActionWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("f457579d-02f8-4479-b97b-ffb678e3f899", list.getAssets().get(0).getAssetId());
    }

    @Test
    void testAdaptGetOperationWhenSecurityFlagIsNotNull() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);


        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWhenSecurityFlagIsNotNull());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
      AssetType list = (AssetType) response.get(0);
        assertEquals("f457579d-02f8-4479-b97b-ffb678e3f899", list.getAssets().get(0).getAssetId());
    }

    @Test
    public void testAdaptGetOperationExceptionsWhenActionIsNull() {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");

        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        });
        String actualMessage = exception.getMessage();
        assertEquals(null, actualMessage);
    }

    @Test
    void testAdaptPostOperation() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        String requestBodyString = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + REQUEST_BODY_OK_JSON);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader objectReader = mapper.reader().forType(new TypeReference<Map<String, Object>>() {
        });
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
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);
        objectResponse.getBody();

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, (Map<String, Object>) requestBody);
        AssetType list = (AssetType) response.get(0);
        assertEquals("f457579d-02f8-4479-b97b-ffb678e3f899", list.getAssets().get(0).getAssetId());
    }


    @Test
    void testAdaptPostOperationException() throws IOException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        String requestBodyString = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + REQUEST_BODY_EXCEPTION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader objectReader = mapper.reader().forType(new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> requestBody = objectReader.readValue(requestBodyString);

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);
        objectResponse.getBody();

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        assertThrows(NullPointerException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, requestBody);
        });
    }

    @Test
    public void testAdaptGetOperationExceptionsWhenCallsAreNull() {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");

        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(null);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        });
        String actualMessage = exception.getMessage();
        assertEquals(null, actualMessage);
    }

    @Test
    public void testAdaptGetOperationExceptionsWhenResponseRequestRelayIsNull() {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");

        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object[].class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        });
        String actualMessage = exception.getMessage();
        assertEquals(null, actualMessage);
    }

    @Test
    void testFormatDateTime() throws IllegalFormatException {

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
    void testAssignPrecision() {
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
    void testCensoredBodyParam() {
        String censoredValue = "MotDePasse!";
        String unCensoredValue = "STATION_SEARCH";
        assertEquals(HIDDEN_TEXT, censoredParam(1, censoredValue));
        assertEquals(unCensoredValue, censoredParam(0, unCensoredValue));
    }

    @Test
    void testAdaptGetOperationObjectDataMapperNull() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspActionWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseSelectorOneObjectWithoutDataMapping());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        //List<AssetType> list = (List<AssetType>) response.get(0);
        AssetType list = (AssetType) response.get(0);
        System.out.println(list);
        assertEquals(null, list.getAssets().get(0).getAssetId());

    }

    @Test
    void testWithDefaultValue() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspActionWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseDefaultValue());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTODefaultValue());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("Place de la République", list.getSharedProperties().getName());
    }

    @Test
    void testWithDefaultValueSimpleElements() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspActionWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseDefaultValue());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTODefaultValueSimple());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("public bus", list.getAssetSubClass());
    }


    @Test
    void testSimpleElements() throws IOException, JSONException {
        UUID mspActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID mspId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("mspId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<MspActionDTO> mspActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO.class))).thenReturn(mspActionDTOResponse);

        ResponseEntity<MspMetaDTO> mspMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspMetaDTO.class))).thenReturn(mspMetaResponse);

        ResponseEntity<MspStandardDTO[]> mspStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-standards?mspActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspStandardDTO[].class))).thenReturn(mspStandardDTOResponse);

        ResponseEntity<MspCallsDTO[]> mspCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockMspCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-calls?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspCallsDTO[].class))).thenReturn(mspCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseElementsSimple());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTOSimpleElements());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?mspActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<MspActionDTO[]> actionsByMspId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfMsp());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/msp-actions?mspMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(MspActionDTO[].class))).thenReturn(actionsByMspId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, mspActionId, mspId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("505", list.getAssetTypeId());
    }

}
