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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.*;

import static com.gateway.adapter.utils.CustomUtils.*;
import static com.gateway.adapter.utils.constant.AdapterMessageDict.HIDDEN_TEXT;
import static com.gateway.commonapi.utils.enums.ActionsEnum.AVAILABLE_ASSET_SEARCH;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class DefaultAdapterServiceImplTest {
    public static final String IT_RESOURCES_PATH = "./src/it/resources/";
    public static final String DEFAULT_ADAPTER_MOCK_ADAPT_GET_OK_JSON = "default-adapter/mock/adaptGetMock.json";
    public static final String MOCK_TOKEN_RESPONSE_OK_JSON = "default-adapter/mock/mock_token_response.json";
    public static final String TEST_WITH_SELECTOR_ONE_OBJECT = "default-adapter/mock/test_with_selector_partner_one_object.json";
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

    @Value("${gateway.service.dataapi.baseUrl}")
    private String dataApiUri;
    @Value("${gateway.service.requestRelay.isMocked}")
    private Boolean isRequestRelayMocked;
    @Value("${gateway.service.requestRelay.url}")
    private String requestRelayUrl;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private PartnerActionDTO createMockPartnerAction() {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        PartnerActionDTO partnerActionDTO = new PartnerActionDTO();
        partnerActionDTO.setPartnerActionId(partnerActionId);
        partnerActionDTO.setAction(AVAILABLE_ASSET_SEARCH);
        return partnerActionDTO;
    }

    private PartnerActionDTO createMockAuthenticationAction() {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        PartnerActionDTO partnerActionDTO = new PartnerActionDTO();
        partnerActionDTO.setPartnerActionId(partnerActionId);
        partnerActionDTO.setAction(AVAILABLE_ASSET_SEARCH);
        partnerActionDTO.setAuthentication(true);
        return partnerActionDTO;
    }

    private PartnerActionDTO createMockPartnerActionWithSelector() {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        PartnerActionDTO partnerActionDTO = new PartnerActionDTO();
        partnerActionDTO.setPartnerActionId(partnerActionId);
        SelectorDTO selectorDTO = new SelectorDTO();
        selectorDTO.setSelectorId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f865"));
        selectorDTO.setKey("assetsType");
        partnerActionDTO.setSelector(selectorDTO);
        partnerActionDTO.setAction(AVAILABLE_ASSET_SEARCH);
        return partnerActionDTO;
    }

    private PartnerStandardDTO[] createMockPartnerStandard() {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        PartnerStandardDTO partnerStandardDTO = new PartnerStandardDTO();
        partnerStandardDTO.setPartnerActionsId(partnerActionId);
        partnerStandardDTO.setPartnerId(partnerId);
        return new PartnerStandardDTO[]{partnerStandardDTO};
    }

    private PartnerCallsDTO[] createMockPartnerCallsDTOWithoutBody() {
        List<PartnerCallsDTO> partnerCallsDTOlist = new ArrayList<PartnerCallsDTO>();

        PartnerCallsDTO callDTOMultiCalls = new PartnerCallsDTO();
        callDTOMultiCalls.setPartnerCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f886"));
        callDTOMultiCalls.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        callDTOMultiCalls.setMethod("POST");

        PartnerCallsDTO callDTO = new PartnerCallsDTO();
        callDTO.setPartnerCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
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
        callDTO.setUrl("");
        callDTOMultiCalls.setParams(paramsDTOSet);
        callDTOMultiCalls.setUrl("");

        partnerCallsDTOlist.add(callDTO);
        partnerCallsDTOlist.add(callDTOMultiCalls);
        return partnerCallsDTOlist.toArray(PartnerCallsDTO[]::new);
    }


    private PartnerCallsDTO[] createMockPartnerCallsDTOWithBody() {
        List<PartnerCallsDTO> partnerCallsDTOlist = new ArrayList<PartnerCallsDTO>();
        PartnerCallsDTO callDTO = new PartnerCallsDTO();
        callDTO.setPartnerCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888"));
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
        callDTO.setUrl("");

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

        partnerCallsDTOlist.add(callDTO);
        return partnerCallsDTOlist.toArray(PartnerCallsDTO[]::new);
    }

    private DataMapperDTO[] createMockDataMapperDTO() {
        List<DataMapperDTO> dataMapperList = new ArrayList<DataMapperDTO>();

        //datamapper with '.'
        DataMapperDTO dataMapper = new DataMapperDTO();
        dataMapper.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef2"));
        dataMapper.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper.setDefaultValue("default");
        dataMapper.setExternalField("vehicles");
        dataMapper.setInternalField("assets.assetId");
        dataMapper.setContainedValue("data.vehicleId");
        dataMapper.setIsArray(1);
        dataMapperList.add(dataMapper);

        //datamapper converting array of string to boolean
        DataMapperDTO dataMapper2 = new DataMapperDTO();
        dataMapper2.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef3"));
        dataMapper2.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper2.setExternalField("vehicles");
        dataMapper2.setInternalField("assets.overriddenProperties.travelAbroad");
        dataMapper2.setContainedValue("data.characteristics.travelAbroad");
        dataMapper2.setIsArray(1);
        dataMapperList.add(dataMapper2);

        //datamapper with default value given
        DataMapperDTO dataMapper3 = new DataMapperDTO();
        dataMapper3.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef4"));
        dataMapper3.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper3.setExternalField("vehicles");
        dataMapper3.setInternalField("assets.assetType");
        dataMapper3.setDefaultValue("TAXI");
        dataMapper3.setIsArray(1);
        dataMapperList.add(dataMapper3);


        return dataMapperList.toArray(DataMapperDTO[]::new);
    }

    private DataMapperDTO[] createMockDataMapperDTODefaultValueSimple() {
        List<DataMapperDTO> dataMapperList = new ArrayList<DataMapperDTO>();
        DataMapperDTO dataMapper = new DataMapperDTO();
        dataMapper.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef2"));
        dataMapper.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper.setDefaultValue("public bus");
        dataMapper.setExternalField(null);
        dataMapper.setInternalField("assetSubClass");
        dataMapperList.add(dataMapper);
        return dataMapperList.toArray(DataMapperDTO[]::new);
    }

    private DataMapperDTO[] createMockDataMapperDTODefaultValue() {
        List<DataMapperDTO> dataMapperList = new ArrayList<DataMapperDTO>();
        DataMapperDTO dataMapper = new DataMapperDTO();
        dataMapper.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef2"));
        dataMapper.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper.setDefaultValue("Place de la République");
        dataMapper.setExternalField(null);
        dataMapper.setInternalField("sharedProperties.name");
        dataMapperList.add(dataMapper);
        return dataMapperList.toArray(DataMapperDTO[]::new);
    }


    private DataMapperDTO[] createMockDataMapperDTOSimpleElements() {
        List<DataMapperDTO> dataMapperList = new ArrayList<DataMapperDTO>();
        DataMapperDTO dataMapper = new DataMapperDTO();
        dataMapper.setDataMapperId(UUID.fromString("231866de-4e4f-4ab7-948b-372ce1acaef2"));
        dataMapper.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        dataMapper.setExternalField("vehicleId");
        dataMapper.setInternalField("assetTypeId");
        dataMapperList.add(dataMapper);
        return dataMapperList.toArray(DataMapperDTO[]::new);
    }


    private PartnerCallsDTO[] createMockPartnerCallsDTOWhenSecurityFlagIsNotNull() {
        List<PartnerCallsDTO> partnerCallsDTOlist = new ArrayList<>();
        PartnerCallsDTO callDTO = new PartnerCallsDTO();
        callDTO.setPartnerCallId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f880"));
        callDTO.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
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
        callDTO.setUrl("");

        partnerCallsDTOlist.add(callDTO);
        PartnerCallsDTO[] partnerCallsDTO = partnerCallsDTOlist.toArray(PartnerCallsDTO[]::new);
        return partnerCallsDTO;
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
        tokenDTO.setAccessToken("MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3");
        return tokenDTO;
    }

    private PartnerMetaDTO createMockPartnerMeta() {
        PartnerMetaDTO partnerMetaDTO = new PartnerMetaDTO();
        partnerMetaDTO.setPartnerId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866"));
        return partnerMetaDTO;
    }

    private PartnerActionDTO[] createMockAuthenticationActionsOfPartner() {
        List<PartnerActionDTO> actions = new ArrayList<PartnerActionDTO>();
        PartnerActionDTO authenticationAction = new PartnerActionDTO();
        authenticationAction.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        authenticationAction.setAuthentication(true);
        actions.add(authenticationAction);
        return actions.toArray(PartnerActionDTO[]::new);
    }

    private PartnerActionDTO[] createMockActionsOfPartner() {
        List<PartnerActionDTO> actions = new ArrayList<PartnerActionDTO>();
        PartnerActionDTO actionDTO = new PartnerActionDTO();
        actionDTO.setPartnerActionId(UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801"));
        actions.add(actionDTO);
        return actions.toArray(PartnerActionDTO[]::new);
    }

    @Test
    public void testAdaptGetOperation() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        //List<AssetType> list = (List<AssetType>) response.get(0);
        AssetType list = (AssetType) response.get(0);
        System.out.println(list);
        assertEquals("f457579d-02f8-4479-b97b-ffb678e3f899", list.getAssets().get(0).getAssetId());

        // some test of exceptions
        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("500");

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"), ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenThrow(HttpClientErrorException.NotFound.class);

        assertThrows(NullPointerException.class, () -> defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null));

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"), ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenThrow(RestClientException.class);
        assertThrows(BadGatewayException.class, () -> defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null));

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"), ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenThrow(NullPointerException.class);
        assertThrows(UnavailableException.class, () -> defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null));


    }


    @Test
    public void testAuthenticationAction() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> tokenObjectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(tokenObjectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        ResponseEntity<TokenDTO> tokenSavedInDatabase = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenSavedInDatabase);

        Mockito.when(authenticationService.needAuthentication(List.of(actionsBypartnerId.getBody()))).thenReturn(true);

        Mockito.when(authenticationService.needAuthenticationAction(List.of(actionsBypartnerId.getBody()))).thenReturn(partnerActionDTOResponse.getBody());

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);

        TokenDTO list = (TokenDTO) response.get(0);
        assertEquals("MTQ0NjJkZmQ5OTM2NDE1ZTZjNGZmZjI3", list.getAccessToken());

    }


    @Test
    public void testAuthenticationException() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> tokenObjectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(tokenObjectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        HttpClientErrorException.NotFound adapterException = (HttpClientErrorException.NotFound) HttpClientErrorException.create(HttpStatus.NOT_FOUND, "exception", null, null, null);

        ResponseEntity<String> tokenSavedInDatabase = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenThrow(adapterException);

        Mockito.when(authenticationService.needAuthentication(List.of(actionsBypartnerId.getBody()))).thenReturn(true);

        Mockito.when(authenticationService.needAuthenticationAction(List.of(actionsBypartnerId.getBody()))).thenReturn(partnerActionDTOResponse.getBody());

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("exception");

        assertThrows(NotFoundException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        });

    }


    @Test
    public void testAuthenticationExceptionRestClient() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> tokenObjectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(tokenObjectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        RestClientException adapterException = new RestClientException("exception");

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenThrow(adapterException);

        Mockito.when(authenticationService.needAuthentication(List.of(actionsBypartnerId.getBody()))).thenReturn(true);

        Mockito.when(authenticationService.needAuthenticationAction(List.of(actionsBypartnerId.getBody()))).thenReturn(partnerActionDTOResponse.getBody());

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("exception");

        assertThrows(NotFoundException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        });

    }


    @Test
    public void testAuthenticationExceptionAny() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> tokenObjectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(tokenObjectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockAuthenticationActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        InternalException adapterException = new InternalException("exception");
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenThrow(adapterException);

        Mockito.when(authenticationService.needAuthentication(List.of(actionsBypartnerId.getBody()))).thenReturn(true);

        Mockito.when(authenticationService.needAuthenticationAction(List.of(actionsBypartnerId.getBody()))).thenReturn(partnerActionDTOResponse.getBody());

        lenient().when(errorMessage.getTechnicalRestHttpClientError()).thenReturn("exception");

        assertThrows(NotFoundException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        });

    }


    @Test
    public void testAdaptGetOperationUnspecifiedSelectorException() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseSelectorOneObject());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals(null, list.getAssets());

    }


    @Test
    public void testAdaptGetOperationDataMapperNull() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockAsset());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("asset1", list.getAssets().get(0).getAssetId());

    }

    @Test
    public void testAdaptGetOperationResponseNullException() throws JSONException, IOException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        assertThrows(Exception.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        });


    }

    @Test
    public void testWithSelectorPartnerReturnedObject() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerActionWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseSelectorOneObject());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("f457579d-02f8-4479-b97b-ffb678e3f899", list.getAssets().get(0).getAssetId());
    }


    @Test
    public void testWithSelectorPartnerReturnedArray() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerActionWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("f457579d-02f8-4479-b97b-ffb678e3f899", list.getAssets().get(0).getAssetId());
    }

    @Test
    public void testAdaptGetOperationWhenSecurityFlagIsNotNull() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);


        ResponseEntity<TokenDTO> tokenResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockTokenDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/tokens?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(TokenDTO.class))).thenReturn(tokenResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWhenSecurityFlagIsNotNull());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("f457579d-02f8-4479-b97b-ffb678e3f899", list.getAssets().get(0).getAssetId());
    }

    @Test
    public void testAdaptGetOperationExceptionsWhenActionIsNull() {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");

        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        });
        String actualMessage = exception.getMessage();
        assertEquals(null, actualMessage);
    }

    @Test
    public void testAdaptPostOperation() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        String requestBodyString = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + REQUEST_BODY_OK_JSON);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader objectReader = mapper.reader().forType(new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> requestBody = objectReader.readValue(requestBodyString);

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);


        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);
        objectResponse.getBody();

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, (Map<String, Object>) requestBody);
        AssetType list = (AssetType) response.get(0);
        assertEquals("f457579d-02f8-4479-b97b-ffb678e3f899", list.getAssets().get(0).getAssetId());
    }


    @Test
    public void testAdaptPostOperationException() throws IOException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f888");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        String requestBodyString = WsTestUtil.readJsonFromFilePath(IT_RESOURCES_PATH + REQUEST_BODY_EXCEPTION_JSON);
        ObjectMapper mapper = new ObjectMapper();
        ObjectReader objectReader = mapper.reader().forType(new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> requestBody = objectReader.readValue(requestBodyString);

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f888"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-calls?f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponse());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);
        objectResponse.getBody();

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTO());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        assertThrows(NullPointerException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, requestBody);
        });
    }

    @Test
    public void testAdaptGetOperationExceptionsWhenCallsAreNull() {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");

        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(null);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        });
        String actualMessage = exception.getMessage();
        assertEquals(null, actualMessage);
    }

    @Test
    public void testAdaptGetOperationExceptionsWhenResponseRequestRelayIsNull() {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");

        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(Object[].class))).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        });
        String actualMessage = exception.getMessage();
        assertEquals(null, actualMessage);
    }

    @Test
    public void testFormatDateTime() throws IllegalFormatException {

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
    public void testAssignPrecision() {
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
    public void testCensoredBodyParam() {
        String censoredValue = "MotDePasse!";
        String unCensoredValue = "STATION_SEARCH";
        assertEquals(HIDDEN_TEXT, censoredParam(1, censoredValue));
        assertEquals(unCensoredValue, censoredParam(0, unCensoredValue));
    }

    @Test
    public void testAdaptGetOperationObjectDataMapperNull() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerActionWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseSelectorOneObjectWithoutDataMapping());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(null);
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        //List<AssetType> list = (List<AssetType>) response.get(0);
        AssetType list = (AssetType) response.get(0);
        System.out.println(list);
        assertEquals(null, list.getAssets().get(0).getAssetId());

    }

    @Test
    public void testWithDefaultValue() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerActionWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseDefaultValue());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTODefaultValue());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("Place de la République", list.getSharedProperties().getName());
    }

    @Test
    public void testWithDefaultValueSimpleElements() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerActionWithSelector());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseDefaultValue());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTODefaultValueSimple());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("public bus", list.getAssetSubClass());
    }


    @Test
    public void testSimpleElements() throws IOException, JSONException {
        UUID partnerActionId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f801");
        UUID partnerId = UUID.fromString("f457579d-02f8-4479-b97b-ffb678e3f866");
        Map<String, String> params = new HashMap<>();
        params.put("partnerId", "f457579d-02f8-4479-b97b-ffb678e3f880");

        ResponseEntity<PartnerActionDTO> partnerActionDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerAction());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions/f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO.class))).thenReturn(partnerActionDTOResponse);

        ResponseEntity<PartnerMetaDTO> partnerMetaResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerMeta());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-metas/f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerMetaDTO.class))).thenReturn(partnerMetaResponse);

        ResponseEntity<PartnerStandardDTO[]> partnerStandardDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerStandard());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-standards?partnerActionsId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerStandardDTO[].class))).thenReturn(partnerStandardDTOResponse);

        ResponseEntity<PartnerCallsDTO[]> partnerCallsDTOResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockPartnerCallsDTOWithoutBody());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("partner-calls?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerCallsDTO[].class))).thenReturn(partnerCallsDTOResponse);

        ResponseEntity<String> objectResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockResponseElementsSimple());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("?protocol=REST"),
                ArgumentMatchers.eq(HttpMethod.POST),
                any(),
                ArgumentMatchers.eq(String.class))).thenReturn(objectResponse);

        ResponseEntity<DataMapperDTO[]> dataMapperResponse = ResponseEntity.status(HttpStatus.OK).body(this.createMockDataMapperDTOSimpleElements());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/data-mappers?partnerActionId=f457579d-02f8-4479-b97b-ffb678e3f801"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(DataMapperDTO[].class))).thenReturn(dataMapperResponse);

        ResponseEntity<PartnerActionDTO[]> actionsBypartnerId = ResponseEntity.status(HttpStatus.OK).body(this.createMockActionsOfPartner());
        lenient().when(restTemplate.exchange(ArgumentMatchers.endsWith("/partner-actions?partnerMetaId=f457579d-02f8-4479-b97b-ffb678e3f866"),
                ArgumentMatchers.eq(HttpMethod.GET),
                any(),
                ArgumentMatchers.eq(PartnerActionDTO[].class))).thenReturn(actionsBypartnerId);

        List<Object> response = defaultAdapterServiceImpl.adaptOperation(params, partnerActionId, partnerId, null);
        AssetType list = (AssetType) response.get(0);
        assertEquals("505", list.getAssetTypeId());
    }

}
