package com.gateway.adapter.service.impl;

import com.gateway.adapter.service.AuthenticationService;
import com.gateway.adapter.service.DefaultAdapterService;
import com.gateway.adapter.utils.DecodeUtils;
import com.gateway.adapter.utils.ParseUtils;
import com.gateway.adapter.utils.constant.AdapterMessageDict;
import com.gateway.adapter.utils.CustomParamUtils;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.*;
import com.gateway.commonapi.dto.data.MspCallsDTO;
import com.gateway.commonapi.dto.requestrelay.HeadersValuesTemplateFinalDTO;
import com.gateway.commonapi.dto.requestrelay.MspCallsFinalDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.JsonUtils;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.gateway.adapter.utils.CustomUtils.*;
import static com.gateway.adapter.utils.constant.AdapterMessageDict.*;
import static com.gateway.adapter.utils.constant.AdapterPathDict.*;


@Slf4j
@Service
public class DefaultAdapterServiceImpl implements DefaultAdapterService {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ErrorMessages errorMessages;
    @Value("${gateway.service.dataapi.url}")
    private String dataApiUri;
    @Value("${gateway.service.requestRelay.isMocked}")
    private Boolean isRequestRelayMocked;
    @Value("${gateway.service.requestRelay.url}")
    private String requestRelayUrl;
    @Value("${gateway.service.defaultAdapter.timeInMinutes}")
    private Long timeInMinutes;

    ParseUtils parseUtils = new ParseUtils();

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private final String correlationId = String.valueOf(CommonUtils.setHeader().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));


    /**
     * @param mspActionId action identifier
     * @param mspId       Msp identifier
     * @return the action in the database
     */
    private MspActionDTO prepareAction(UUID mspActionId, UUID mspId) {

        MspActionDTO mspBusinessAction;
        MspStandardDTO mspStandard;

        String urlGetActionById = dataApiUri + CommonUtils.placeholderFormat(GET_ACTION_BY_ID_PATH, ACTION_ID_PARAM, mspActionId.toString());

        try {
            ResponseEntity<MspActionDTO> mspActionDTO = restTemplate.exchange(urlGetActionById, HttpMethod.GET, CommonUtils.setHeader(), MspActionDTO.class);
            mspBusinessAction = Objects.requireNonNull(mspActionDTO.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        }
        String urlGetStandardByActionId = dataApiUri + CommonUtils.placeholderFormat("/msp-standards?mspActionsId={actionId}", ACTION_ID_PARAM, mspActionId.toString());
        try {
            ResponseEntity<MspStandardDTO[]> standard = restTemplate.exchange(urlGetStandardByActionId, HttpMethod.GET, CommonUtils.setHeader(), MspStandardDTO[].class);
            mspStandard = Objects.requireNonNull(standard.getBody())[0];
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        }

        if (mspStandard.getMspId().equals(mspId)) {
            return mspBusinessAction;
        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_ACTION_WITH_THIS_MSP_META_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, mspId.toString())));
        }
    }

    /**
     * retrieve mspMeta in database by mspId
     *
     * @param id Msp identifier
     * @return the msp in the database
     */
    private MspMetaDTO prepareMsp(UUID id) {
        MspMetaDTO mspMeta;
        String urlGetMspByMspId = dataApiUri + CommonUtils.placeholderFormat(GET_MSPMETA_BY_ID_PATH, ID_PARAM, id.toString());

        try {
            ResponseEntity<MspMetaDTO> mspMetaDto = restTemplate.exchange(urlGetMspByMspId, HttpMethod.GET, CommonUtils.setHeader(), MspMetaDTO.class);
            return mspMeta = Objects.requireNonNull(mspMetaDto.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMspByMspId));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMspByMspId));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetMspByMspId));
        }
    }

    @Override
    public List<Object> adaptOperation(Map<String, String> params, UUID mspActionId, UUID mspId, Map<String, Object> originalBody) throws JSONException, IOException {
        List<Object> finalResponse = new ArrayList<>();

        MspCallsFinalDTO mspCallsFinalDTO = new MspCallsFinalDTO();

        MspActionDTO mspBusinessAction = this.prepareAction(mspActionId, mspId);

        mspCallsFinalDTO.setMspActionId(mspBusinessAction.getMspActionId());

        MspMetaDTO mspMeta = prepareMsp(mspId);

        List<MspActionDTO> actions = this.prepareActionListByMspMetaId(mspMeta.getMspId());

        // if the msp has an authenticate action but the current action is not an authenticate action
        if (authenticationService.needAuthentication(actions)) {

            if (!mspBusinessAction.isAuthentication()) {
                this.prepareAuthentication(actions, mspMeta, mspCallsFinalDTO, params, mspId, originalBody, finalResponse);
                finalResponse = this.makeCallToMsp(mspBusinessAction, mspCallsFinalDTO, params, mspId, originalBody);

            } else {
                // the current action is an authentication action
                // the msp needs authentication and we retrieve the authentication action
                this.prepareAuthentication(actions, mspMeta, mspCallsFinalDTO, params, mspId, originalBody, finalResponse);
            }
        }

        // the msp don't need authentication
        else {
            finalResponse = this.makeCallToMsp(mspBusinessAction, mspCallsFinalDTO, params, mspId, originalBody);
        }

        return finalResponse;
    }


    /**
     * Do the transformation of the response and the concatenation of the responses
     *
     * @param mspResponse       msp response
     * @param mspBusinessAction current action
     * @param mspId             msp identifier
     * @return list of objects in pivot format
     * @throws JSONException
     * @throws IOException
     */
    private List<Object> adaptResponse(String mspResponse, MspActionDTO mspBusinessAction, UUID mspId) throws JSONException, IOException {
        List<Object> adaptedResponseObjects = null;
        if (StringUtils.isNotBlank(mspResponse)) {
            adaptedResponseObjects = this.formatResponse(mspResponse, mspBusinessAction, mspId);
        } else {
            throw new NotFoundException(CommonUtils.placeholderFormat(VALUE_NULL_OF_TYPE_JSONOBJECT));
        }
        return adaptedResponseObjects;
    }


    /**
     * save the token that we get from the msp in the database
     *
     * @param tokenDTO the token
     * @return the token that we just saved in DB
     */
    private String saveTokenInDatabase(Object tokenDTO) {
        HttpEntity<Object> entity = new HttpEntity<>(tokenDTO, CommonUtils.setHttpHeaders());
        String urlPostToken = dataApiUri + CommonUtils.placeholderFormat(GET_TOKEN_PATH);

        try {
            ResponseEntity<String> tokenResponse = restTemplate.exchange(urlPostToken, HttpMethod.POST, entity, String.class);
            return tokenResponse.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), dataApiUri));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), dataApiUri));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), dataApiUri));
        }

    }

    private List<MspActionDTO> prepareActionListByMspMetaId(UUID mspId) {
        List<MspActionDTO> actionList;
        String urlGetCallsByActionId = dataApiUri + CommonUtils.placeholderFormat(MSP_ACTIONS_BASE_PATH + GET_BY_MSP_META_ID_PATH, MSP_ID_PARAM, String.valueOf(mspId));

        try {
            ResponseEntity<MspActionDTO[]> mspCallsDTO = restTemplate.exchange(urlGetCallsByActionId, HttpMethod.GET, CommonUtils.setHeader(), MspActionDTO[].class);
            actionList = Arrays.asList(Objects.requireNonNull(mspCallsDTO.getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        }
        return actionList;
    }


    /**
     * check if msp need authentication and prepare token for it
     *
     * @param mspMeta          mspMeta
     * @param mspCallsFinalDTO the final call that we will send to request relay
     */
    private void prepareAuthentication(List<MspActionDTO> actions, MspMetaDTO mspMeta, MspCallsFinalDTO mspCallsFinalDTO, Map<String, String> params, UUID mspId, Map<String, Object> originalBody, List<Object> finalResponse)
            throws JSONException, IOException {

        TokenDTO securityToken;

        //  we retrieve the authentication action
        MspActionDTO authenticationAction = authenticationService.needAuthenticationAction(actions);

        // we retrieve the token from the database
        securityToken = prepareTokenDTO(mspMeta.getMspId());

        // we check if the token is still valid
        if (!CustomParamUtils.isValidToken(securityToken, timeInMinutes)) {
            log.info(THE_TOKEN_IS_NOT_VALID_OR_NOT_EXIST);

            // case : invalid token,  we launch a call to the msp to retrieve a new token
            finalResponse.addAll(makeCallToMsp(authenticationAction, mspCallsFinalDTO, params, mspId, originalBody));

            TokenDTO token = (TokenDTO) finalResponse.get(0);

            saveTokenInDatabase(token);
        } else {

            // case : valid token,  we call the token that we have in database
            finalResponse.addAll(Collections.singletonList(this.prepareTokenDTO(mspMeta.getMspId())));
        }

    }

    /**
     * all the steps to build the call request
     *
     * @param mspAction        mspAction
     * @param mspCallsFinalDTO mspCallFinal
     * @param params           params
     * @param mspId            mspId
     * @param originalBody     originalBody
     * @return list of objects returned by msp
     * @throws JSONException
     * @throws IOException
     */
    private List<Object> makeCallToMsp(MspActionDTO mspAction, MspCallsFinalDTO mspCallsFinalDTO, Map<String, String> params, UUID mspId, Map<String, Object> originalBody) throws JSONException, IOException {
        List<Object> finalResponse = new ArrayList<>();
        List<MspCallsDTO> businessCalls = this.getBusinessCalls(mspAction.getMspActionId());
        // iterate over collection of calls associated with the business action
        for (MspCallsDTO callDto : businessCalls) {
            if (callDto.getMspCallId() != null) {
                mspCallsFinalDTO.setMspCallId(callDto.getMspCallId());
            }
            if (callDto.getMethod() != null) {
                mspCallsFinalDTO.setMethod(callDto.getMethod());
            }
            this.prepareHeaders(callDto, mspCallsFinalDTO, params, mspId);

            if (callDto.getNbCalls().equals(1)) {
                String mspResponse = adaptOperationForOneCall(callDto, params, null, mspCallsFinalDTO, mspId, originalBody);
                finalResponse.addAll(adaptResponse(mspResponse, mspAction, mspId));
            } else if (callDto.getNbCalls() > 1) {
                finalResponse.add(this.manageMultiCalls(callDto, params, mspCallsFinalDTO, mspAction, mspId, originalBody));
            } else {
                throw new BadGatewayException(CommonUtils.placeholderFormat(NUMBER_OF_CALLS_MUST_BE_GREATER_THAN_ONE, MSP_CALL_ID, callDto.getMspCallId().toString()));
            }
        }
        return finalResponse;
    }


    /**
     * Assign parameterized call headers with right values
     *
     * @param mspCallsDTO      msp call
     * @param mspCallsFinalDTO the final call that we will send to request relay
     * @param headersValue     list des headers
     * @param mspId            Msp identifier
     */
    private void prepareHeaders(MspCallsDTO mspCallsDTO, MspCallsFinalDTO mspCallsFinalDTO, Map<String, String> headersValue, UUID mspId) {
        // add final headers to MspCallFinalDTO's headers
        log.info(PROCESSING_HEADERS);

        Set<HeadersValuesTemplateFinalDTO> headersTemplateFinal = new HashSet<>();

        List<HeadersDTO> headers = new ArrayList<>();
        if (mspCallsDTO.getHeaders() != null) {
            headers = new ArrayList<>(mspCallsDTO.getHeaders());
        }

        // Process headers templates
        if (!headers.isEmpty()) {
            prepareHeaderTemplate(headers.stream().filter(header -> header.getValueTemplate() != null).collect(Collectors.toList()));
        }
        // Process security headers
        if (!headers.isEmpty()) {
            headers.stream().filter(header -> header.getSecurityFlag() != null && header.getSecurityFlag() == 1).forEach(header -> {
                TokenDTO token = this.prepareTokenDTO(mspId);
                String key = header.getKey();
                String value = headersValue == null ^ (headersValue != null && headersValue.get(key) == null) ? "" : headersValue.get(key);
                String accessToken = (token == null) ? value : token.getAccessToken();
                String fullValue = Objects.toString(header.getValuePrefix(), "") + accessToken;

                HeadersValuesTemplateFinalDTO headersValuesTemplateFinal = new HeadersValuesTemplateFinalDTO();
                headersValuesTemplateFinal.setKey(key);
                headersValuesTemplateFinal.setValue(fullValue);
                headersTemplateFinal.add(headersValuesTemplateFinal);

                boolean sensitive = (header.getSensitive() != null) && (header.getSensitive() == 1);
                log.info("{}: {}", key, sensitive ? AdapterMessageDict.HIDDEN_TEXT : fullValue);
            });

        }
        // Process standard headers
        if (!headers.isEmpty()) {
            headers.stream().filter(header -> header.getSecurityFlag() == null || header.getSecurityFlag() == 0).forEach(header -> {
                String fullValue = header.getValue();

                HeadersValuesTemplateFinalDTO headersValuesTemplateFinal = new HeadersValuesTemplateFinalDTO();
                headersValuesTemplateFinal.setKey(header.getKey());
                headersValuesTemplateFinal.setValue(fullValue);
                headersTemplateFinal.add(headersValuesTemplateFinal);

                boolean sensitive = (header.getSensitive() != null) && (header.getSensitive() == 1);
                log.info("{}: {}", header.getKey(), sensitive ? AdapterMessageDict.HIDDEN_TEXT : fullValue);
            });
        }
        mspCallsFinalDTO.setHeaders(headersTemplateFinal);

    }

    /**
     * method to retrieve token related to mspMeta with id given in param
     *
     * @param mspId Msp identifier
     * @return the database token associated with mspId
     */
    private TokenDTO prepareTokenDTO(UUID mspId) {
        String urlGetTokenByMspId = dataApiUri + CommonUtils.placeholderFormat(GET_TOKEN_PATH + GET_BY_MSP_META_ID_PATH, MSP_ID_PARAM, String.valueOf(mspId));

        try {
            ResponseEntity<TokenDTO> tokenDTO = restTemplate.exchange(urlGetTokenByMspId, HttpMethod.GET, CommonUtils.setHeader(), TokenDTO.class);
            return tokenDTO.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetTokenByMspId));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetTokenByMspId));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetTokenByMspId));
        }
    }

    /**
     * Perform headers which have a template and require a special actions like autentification headers
     *
     * @param headers list of headers
     */
    private void prepareHeaderTemplate(List<HeadersDTO> headers) {
        for (HeadersDTO header : headers) {
            Assert.hasText(header.getValueTemplate(), "Empty template header!!");
            String finalValue = header.getValueTemplate();
            List<String> matchList = new ArrayList<>();

            // Pattern to retrieve the arguments present between two braces
            Pattern regex = Pattern.compile(ARGUMENTS_HEADER_PATTERN);
            Matcher regexMatcher = regex.matcher(header.getValueTemplate());

            // Retrieves a list of template parameters
            // Finds Matching Pattern in String
            while (regexMatcher.find()) {
                // Fetching Group from String
                matchList.add(regexMatcher.group(1));
            }

            // Get the values to enter in the template
            List<HeadersValuesTemplateDTO> headersValuesTemplate = new ArrayList<>(header.getHeadersValuesTemplate());

            for (HeadersValuesTemplateDTO headerTemplate : headersValuesTemplate) {
                for (String key : matchList) {
                    if (headerTemplate.getKey().equals(key)) {
                        finalValue = CommonUtils.placeholderFormat(finalValue, key, headerTemplate.getValue());
                        break;
                    }
                }
            }
            String info = CommonUtils.placeholderFormat(PRE_POCESS, FINAL_VALUE, finalValue);
            log.info(info, finalValue);

            // Specific function processing (encoding...)
            if (header.getProcessFunction() != null) {
                finalValue = CommonUtils.processFunction(finalValue, header.getProcessFunction());
            }
            // Add a prefix
            finalValue = Objects.toString(header.getValuePrefix(), "") + finalValue;
            header.setValue(finalValue);

            String callInfo = CommonUtils.placeholderFormat(CALL_IN_PROGRESS, FINAL_VALUE, finalValue);
            log.info(callInfo, finalValue);
        }
    }

    /**
     * Assign parameterized call url+parameters with right values
     *
     * @param mspCallsDTO      mspCallsDTO
     * @param paramsValue      list of params
     * @param mspCallsFinalDTO mspCallFinalDto where we set all prepared parameters
     */
    private void prepareUrlAndParams(MspCallsDTO mspCallsDTO, Map<String, String> paramsValue,
                                     Map<String, List<String>> paramsMultiCalls, MspCallsFinalDTO mspCallsFinalDTO, Map<String, Object> originalBody) {
        // concat final Url with params and assign it to mspCalls' url
        log.info(PROCESSING_PARAMS);
        List<ParamsDTO> params = null;
        List<String> missingFields = new ArrayList<>();

        params = new ArrayList<>(mspCallsDTO.getParams());
        String url = mspCallsDTO.getUrl();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        UriComponentsBuilder logBuilder = UriComponentsBuilder.fromUriString(url);

        if (!CollectionUtils.isEmpty(params)) {
            for (ParamsDTO param : params) {

                String key = param.getKey();
                String value = null;

                //value given in params
                if (StringUtils.isNotBlank(param.getValue()) && StringUtils.isBlank(param.getKeyMapper())) {
                    value = param.getValue();

                    //value to get via keyMapper
                } else if (StringUtils.isNotBlank(param.getKeyMapper())) {
                    String keyMapper = param.getKeyMapper();

                    String valueFoundInParamsMap = findKeyInParamsMap(keyMapper, paramsValue);
                    value = StringUtils.isNotBlank(valueFoundInParamsMap) ? valueFoundInParamsMap : findKeyInOriginalBody(keyMapper, originalBody);
                    if (!StringUtils.isNotBlank(value)) {
                        //if the value of the param was not passed as input but there is String 'null' in the 'value' column
                        // then the param is optional so its absence does not throw an error
                        if (StringUtils.isNotBlank(param.getValue()) && param.getValue().equals("null")) {
                            continue;
                        } else {
                            missingFields.add(keyMapper);
                            continue;
                        }

                    }
                } else {
                    throw new NotFoundException(CommonUtils.placeholderFormat(MISSING_VALUE, FIRST_PLACEHOLDER, param.getParamsId().toString()));

                }

                //dealing with precision and timezone
                String precision = param.getPrecision();
                String timezone = param.getTimezone();
                String precisedValue = StringUtils.isNotBlank(precision) ? String.valueOf(assignPrecision(OffsetDateTime.parse(value), precision)) : value;
                value = StringUtils.isNotBlank(timezone) ? String.valueOf(formatDateTime(OffsetDateTime.parse(precisedValue), timezone)) : precisedValue;

                builder.queryParam(key, value);
                //dealing with sensitive fields
                Integer sensitive = param.getSensitive();
                logBuilder.queryParam(key, censoredParam(sensitive, value));
            }

            if(!missingFields.isEmpty()) {
                String errorMessageMissingFields = String.join(", ",missingFields);
                throw new NotFoundException(CommonUtils.placeholderFormat(MISSING_FIELD, FIRST_PLACEHOLDER, errorMessageMissingFields));
            }
        }

        if (paramsMultiCalls != null) {
            for (Map.Entry<String, List<String>> entry : paramsMultiCalls.entrySet()) {
                if (!CollectionUtils.isEmpty(entry.getValue())) {
                    for (String paramValue : entry.getValue()) {
                        builder.queryParam(entry.getKey(), paramValue);
                        logBuilder.queryParam(entry.getKey(), paramValue);
                    }
                }
            }
        }

        url = builder.build().toUri().toString();
        mspCallsFinalDTO.setUrl(url);

        String urlLogged = logBuilder.build().toUri().toString();
        log.info(FINAL_URL, urlLogged);
    }

    /**
     * Looks for a field value by key in paramsValue
     *
     * @param key
     * @param paramsValue
     * @return String value found
     */
    private String findKeyInParamsMap(String key, Map<String, String> paramsValue) {

        String foundedValue = null;
        if (paramsValue != null) {
            for (Map.Entry<String, String> entry : paramsValue.entrySet()) {
                if (entry.getKey().equals(key)) {
                    foundedValue = entry.getValue();
                    break;
                }
            }
        }

        return foundedValue;
    }

    /**
     * Looks for a field value by key in originalBody
     *
     * @param key
     * @param originalBody
     * @return String value found
     */
    private String findKeyInOriginalBody(String key, Map<String, Object> originalBody) {
        String foundedValue = null;
        if (originalBody != null) {
            for (Map.Entry<String, Object> entry : originalBody.entrySet()) {
                if (entry.getKey().equals(key)) {
                    if (entry.getValue() != null) {
                        foundedValue = entry.getValue().toString();
                        break;
                    }
                    throw new NotFoundException(CommonUtils.placeholderFormat(MISSING_FIELD, FIRST_PLACEHOLDER, entry.getKey()));

                }
            }
        }
        return foundedValue;
    }


    /**
     * Construct and assign final body template to MspCallsFinal
     *
     * @param callDto
     * @param mspCallsFinalDTO
     * @param originalBody
     */
    private void prepareBody(MspCallsDTO callDto, MspCallsFinalDTO mspCallsFinalDTO, Map<String, Object> originalBody) {

        BodyDTO body = callDto.getBody();
        if (body != null && body.getTemplate() != null) {
            String template;
            String templateLogged;
            if(body.getTemplate().equals(ORIGINAL_BODY)){
                template = originalBody.toString();
                templateLogged = template;
            } else {

                template = body.getTemplate();
                templateLogged = template;
                Set<BodyParamsDTO> bodyParams = body.getBodyParams();

                for (BodyParamsDTO param : bodyParams) {
                    String finalValue;
                    if (param.getKey() == null && param.getValue() != null) {
                        finalValue = param.getValue();
                    } else {
                        try {
                            finalValue = (String) originalBody.get(param.getKey());
                            if (finalValue == null) {
                                throw new InternalException(CommonUtils.placeholderFormat(MISSING_BODY_FIELD, FIRST_PLACEHOLDER, param.getKey()));
                            }
                        } catch (Exception e) {
                            throw new InternalException(CommonUtils.placeholderFormat(MISSING_BODY_FIELD, FIRST_PLACEHOLDER, param.getKey()));
                        }

                        String precision = param.getPrecision();
                        String timezone = param.getTimezone();
                        String precisedValue = StringUtils.isNotBlank(precision) ? String.valueOf(assignPrecision(OffsetDateTime.parse(finalValue), precision)) : finalValue;
                        finalValue = StringUtils.isNotBlank(timezone) ? String.valueOf(formatDateTime(OffsetDateTime.parse(precisedValue), timezone)) : precisedValue;
                    }
                    String keyMapper = param.getKeyMapper();
                    template = template.replace("${" + keyMapper + "}", finalValue);

                    Integer sensitive = param.getSensitive();
                    templateLogged = templateLogged.replace("${" + keyMapper + "}", censoredParam(sensitive, finalValue));
                }

            }

            mspCallsFinalDTO.setBody(template);
            if (log.isDebugEnabled()) {
                log.debug(FINAL_BODY, templateLogged);
            }

        }
    }


    /**
     * prepare the list of calls
     *
     * @param actionId action identifier
     * @return list of mspCallsDTO in the database related to this action
     */
    private List<MspCallsDTO> getBusinessCalls(UUID actionId) {
        List<MspCallsDTO> mspBusinessCalls;
        String urlGetCallsByActionId = dataApiUri + CommonUtils.placeholderFormat(GET_CALLS_PATH + GET_BY_ACTIONS_ID_PATH, ACTION_ID_PARAM, String.valueOf(actionId));

        try {
            ResponseEntity<MspCallsDTO[]> mspCallsDTO = restTemplate.exchange(urlGetCallsByActionId, HttpMethod.GET, CommonUtils.setHeader(), MspCallsDTO[].class);
            mspBusinessCalls = Arrays.asList(Objects.requireNonNull(mspCallsDTO.getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        }
        return mspBusinessCalls;
    }

    /**
     * prepare the list of Data Mapper
     *
     * @param actionId action identifier
     * @return list of DataMapperDTO in the database related to this action
     */
    private List<DataMapperDTO> prepareDataMappers(UUID actionId) {
        List<DataMapperDTO> dataMappers;
        String urlGetActionById = dataApiUri + CommonUtils.placeholderFormat(GET_DATA_MAPPER_BY_ID_PATH + GET_BY_ACTIONS_ID_PATH, ACTION_ID_PARAM, actionId.toString());

        try {
            ResponseEntity<DataMapperDTO[]> dataMappersDTO = restTemplate.exchange(urlGetActionById, HttpMethod.GET, CommonUtils.setHeader(), DataMapperDTO[].class);
            dataMappers = Arrays.asList(Objects.requireNonNull(dataMappersDTO.getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            return Collections.emptyList();
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } catch (NullPointerException e) {
            return Collections.emptyList();
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        }
        return dataMappers;
    }

    /**
     * Decode And Parse MspResponse
     *
     * @param list
     * @param jsonObjectResponse
     * @param dataMappers
     * @param mspAction
     * @param mspId
     * @throws JSONException
     * @throws IOException
     */
    private void decodeAndParse(List<Object> list, JSONObject jsonObjectResponse, List<DataMapperDTO> dataMappers, MspActionDTO mspAction, UUID mspId) throws JSONException, IOException {
        jsonObjectResponse = DecodeUtils.decodeDataResponse(jsonObjectResponse, dataMappers);
        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspAction, mspId);
    }

    /**
     * format the response in gateway format
     *
     * @param mspResponse list of objects returned by msp
     * @param mspAction   action identifier
     * @param mspId
     * @return transformed fields in json format
     */
    private List<Object> formatResponse(String mspResponse, MspActionDTO mspAction, UUID mspId) throws
            JSONException, IOException {
        List<Object> list = new ArrayList<>();
        //
        List<DataMapperDTO> dataMappers = prepareDataMappers(mspAction.getMspActionId());
        //Retrieve The sub-object linked to Selector
        SelectorDTO selector = mspAction.getSelector();
        if (selector != null) {
            mspResponse = getDataBySelector(mspResponse, selector);
        }
        // we have datamapper in database so we have to do the transfo
        if (!dataMappers.isEmpty()) {
            if (mspResponse.charAt(0) == '[') {
                JSONArray jsonArrayResponse;
                jsonArrayResponse = new JSONArray(mspResponse);
                if (jsonArrayResponse.length() > 0) {
                    for (int i = 0; i < jsonArrayResponse.length(); i++) {
                        JSONObject jsonObjectResponse = jsonArrayResponse.getJSONObject(i);
                        // decode data and get jsonObject with champs transformed
                        decodeAndParse(list, jsonObjectResponse, dataMappers, mspAction, mspId);
                    }
                    return list;
                }
            } else {
                // Retour de l'msp correspond à un objet
                try {
                    JSONObject jsonObject = new JSONObject(mspResponse);
                    decodeAndParse(list, jsonObject, dataMappers, mspAction, mspId);
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE + e.getMessage()));
                }
                return list;
            }
        } else {
            // case if datamapper is null , the msp response is already in gateway format
            // we check if this response is in an array
            if (mspResponse.charAt(0) == '[') {
                JSONArray jsonArrayResponse = new JSONArray(mspResponse);
                if (jsonArrayResponse.length() > 0) {
                    for (int i = 0; i < jsonArrayResponse.length(); i++) {
                        JSONObject jsonObjectResponse = jsonArrayResponse.getJSONObject(i);
                        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, mspAction, mspId);
                    }
                    return list;
                }
            }
            // if this response in pivot format is in an array
            else {
                JSONObject jsonObject = new JSONObject(mspResponse);
                parseUtils.parseToGatewayDTOByAction(list, jsonObject, mspAction, mspId);
                return list;
            }
        }
        return list;
    }

    /**
     * makes the call to Request-relay microservice
     *
     * @param mspCallsFinal mspCallFinalDto contains all prepared parameters
     * @return the response of the msp, list of objects
     */
    private String manageCall(MspCallsFinalDTO mspCallsFinal) {
        HttpEntity<MspCallsFinalDTO> entity = new HttpEntity<>(mspCallsFinal, CommonUtils.setHttpHeaders());
        String urlPostRequest = requestRelayUrl + CommonUtils.placeholderFormat(REST_PROTOCOL);

        try {
            ResponseEntity<String> mspResponse = restTemplate.exchange(urlPostRequest, HttpMethod.POST, entity, String.class);
            return mspResponse.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), requestRelayUrl));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), requestRelayUrl));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), requestRelayUrl));
        }
    }

    /**
     * Manage calls in case of multiCalls in case of nbCallS = 1
     *
     * @param callDto          current call
     * @param mspCallsFinalDTO the call request
     * @return list of objects (msp responses)
     */
    private List<Object> manageMultiCalls(MspCallsDTO callDto, Map<String, String> params, MspCallsFinalDTO mspCallsFinalDTO, MspActionDTO mspAction, UUID mspId, Map<String, Object> originalBody) throws JSONException, IOException {
        String responseBody = null;
        List<Object> responseBodies = new ArrayList<>();
        Map<String, List<String>> paramsMultiCalls = new HashMap<>();
        Map<String, ParamsMultiCallsDTO> paramsOption = new HashMap<>();

        List<ParamsMultiCallsDTO> paramsSchedule = new ArrayList<>(callDto.getParamsMultiCalls());

        // prepare params from params multi calls
        for (ParamsMultiCallsDTO recursiveParams : paramsSchedule) {
            String key = recursiveParams.getKey();
            paramsOption.put(key, recursiveParams);
            List<String> list = paramsMultiCalls.get(key);
            if (CollectionUtils.isEmpty(list)) {
                list = new ArrayList<>();
                list.add(CustomParamUtils.decodeInitValue(recursiveParams));
                paramsMultiCalls.put(key, list);
            } else {
                list.add(CustomParamUtils.decodeInitValue(recursiveParams));
            }
        }

        for (int i = 1; i < callDto.getNbCalls() + 1; i++) {
            MspCallsDTO call = new MspCallsDTO(callDto.getMspCallId(), callDto.getUrl(), callDto.getMethod(), callDto.getIsMocked(), callDto.getNbCalls(),
                    callDto.getExecutionOrder(), callDto.getMspActionId(), callDto.getBody(), callDto.getHeaders(), callDto.getParams(), callDto.getParamsMultiCalls());
            // construct the call, send it to request relay and retrieve the msp response for one call
            responseBody = adaptOperationForOneCall(call, params, paramsMultiCalls, mspCallsFinalDTO, mspId, originalBody);

            //concat msp responses of each call
            if (responseBody!=null && responseBody.charAt(0) == '[') {
                String responseValues = responseBody.substring(1, responseBody.length() - 1);
                responseBodies.add(responseValues);
            } else {
                responseBodies.add(responseBody);
            }

            //  initialize date params
            CustomParamUtils.reinitParams(paramsOption, paramsMultiCalls);
        }


        //transform full msp response to GTW format
        return adaptResponse(String.valueOf(responseBodies), mspAction, mspId);
    }

    /**
     * adapts the operation of construction of the call  for a single call: prepare params, url, body and send the call request to requestRelay
     *
     * @param call             mspCallDto
     * @param paramsMultiCalls params multi calls (timings params)
     * @param mspCallsFinalDTO the call request
     * @param mspId            msp identifier
     * @param originalBody     original body
     * @return a msp response in string format
     */
    private String adaptOperationForOneCall(MspCallsDTO call, Map<String, String> params, Map<String, List<String>> paramsMultiCalls, MspCallsFinalDTO mspCallsFinalDTO, UUID mspId, Map<String, Object> originalBody) {
        this.prepareUrlAndParams(call, params, paramsMultiCalls, mspCallsFinalDTO, originalBody);
        this.prepareBody(call, mspCallsFinalDTO, originalBody);
        return this.manageCall(mspCallsFinalDTO);
    }

    /**
     * Retrieve The sub-object linked to Selector
     *
     * @param mspResponse
     * @param selector
     * @return mspResponse
     */
    private String getDataBySelector(String mspResponse, SelectorDTO selector) throws JSONException {
        if (StringUtils.isNotBlank(mspResponse) && mspResponse.charAt(0) == '[') {
            JsonElement selectedResponse = JsonUtils.getJsonArray(selector, mspResponse);
            return selectedResponse != null ? selectedResponse.toString() : mspResponse;
        } else {
            mspResponse = String.valueOf(JsonUtils.getJsonElement(selector, mspResponse));
        }
        return mspResponse;
    }
}
