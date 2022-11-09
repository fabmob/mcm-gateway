package com.gateway.adapter.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.adapter.service.AuthenticationService;
import com.gateway.adapter.service.DefaultAdapterService;
import com.gateway.adapter.utils.CustomParamUtils;
import com.gateway.adapter.utils.DecodeUtils;
import com.gateway.adapter.utils.ParseUtils;
import com.gateway.adapter.utils.constant.AdapterMessageDict;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.*;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.dto.requestrelay.HeadersValuesTemplateFinalDTO;
import com.gateway.commonapi.dto.requestrelay.PartnerCallsFinalDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.JsonUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
    private ErrorMessages errorMessages;
    @Value("${gateway.service.dataapi.baseUrl}")
    private String dataApiUri;
    @Value("${gateway.service.requestRelay.url}")
    private String requestRelayUrl;
    @Value("${gateway.service.defaultAdapter.timeInMinutes}")
    private Long timeInMinutes;

    ParseUtils parseUtils = new ParseUtils();

    RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    private static final String CORRELATION_ID = String.valueOf(CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));
    private static final String SEPARATOR = ": ";

    /**
     * @param partnerActionId action identifier
     * @param partnerId       partner identifier
     * @return the action in the database
     */
    private PartnerActionDTO prepareAction(UUID partnerActionId, UUID partnerId) {
        String initialOutputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        PartnerActionDTO partnerBusinessAction;
        PartnerStandardDTO partnerStandard;

        String urlGetActionById = dataApiUri + CommonUtils.placeholderFormat(GET_ACTION_BY_ID_PATH, ACTION_ID_PARAM, partnerActionId.toString());
        try {
            ResponseEntity<PartnerActionDTO> partnerActionDTO = restTemplate.exchange(urlGetActionById, HttpMethod.GET, CommonUtils.setHeaders(), PartnerActionDTO.class);
            partnerBusinessAction = Objects.requireNonNull(partnerActionDTO.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } finally {
            CallUtils.saveOutputStandardInCallThread(initialOutputStandard);
        }

        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);
        String urlGetStandardByActionId = dataApiUri + CommonUtils.placeholderFormat("/partner-standards?partnerActionsId={actionId}", ACTION_ID_PARAM, partnerActionId.toString());
        try {
            ResponseEntity<PartnerStandardDTO[]> standard = restTemplate.exchange(urlGetStandardByActionId, HttpMethod.GET, CommonUtils.setHeaders(), PartnerStandardDTO[].class);
            partnerStandard = Objects.requireNonNull(standard.getBody())[0];
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } finally {
            CallUtils.saveOutputStandardInCallThread(initialOutputStandard);
        }

        if (partnerStandard.getPartnerId().equals(partnerId)) {
            return partnerBusinessAction;
        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(PARTNER_ACTION_WITH_THIS_PARTNER_META_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, partnerId.toString())));
        }
    }

    /**
     * retrieve partnerMeta in database by partnerId
     *
     * @param id partner identifier
     * @return the partner in the database
     */
    private PartnerMetaDTO preparePartner(UUID id) {
        String initialOutputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        String urlGetPartnerByPartnerId = dataApiUri + CommonUtils.placeholderFormat(GET_PARTNER_META_BY_ID_PATH, ID_PARAM, id.toString());

        try {
            ResponseEntity<PartnerMetaDTO> partnerMetaDto = restTemplate.exchange(urlGetPartnerByPartnerId, HttpMethod.GET, CommonUtils.setHeaders(), PartnerMetaDTO.class);
            return Objects.requireNonNull(partnerMetaDto.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetPartnerByPartnerId));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetPartnerByPartnerId));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetPartnerByPartnerId));
        } finally {
            CallUtils.saveOutputStandardInCallThread(initialOutputStandard);
        }

    }

    @Override
    public List<Object> adaptOperation(Map<String, String> params, UUID partnerActionId, UUID partnerId, Map<String, Object> originalBody) throws JSONException, IOException {
        List<Object> finalResponse = new ArrayList<>();

        PartnerCallsFinalDTO partnerCallsFinalDTO = new PartnerCallsFinalDTO();

        PartnerActionDTO partnerBusinessAction = this.prepareAction(partnerActionId, partnerId);

        partnerCallsFinalDTO.setPartnerActionId(partnerBusinessAction.getPartnerActionId());

        PartnerMetaDTO partnerMeta = preparePartner(partnerId);

        List<PartnerActionDTO> actions = this.prepareActionListByPartnerMetaId(partnerMeta.getPartnerId());

        // if the Partner has an authenticate action but the current action is not an authenticate action
        if (authenticationService.needAuthentication(actions)) {

            if (!partnerBusinessAction.isAuthentication()) {
                this.prepareAuthentication(actions, partnerMeta, partnerCallsFinalDTO, params, partnerId, originalBody, finalResponse);
                finalResponse = this.makeCallToPartner(partnerBusinessAction, partnerCallsFinalDTO, params, partnerId, originalBody);

            } else {
                // the current action is an authentication action
                // the Partner needs authentication, and we retrieve the authentication action
                this.prepareAuthentication(actions, partnerMeta, partnerCallsFinalDTO, params, partnerId, originalBody, finalResponse);
            }
        }

        // the Partner don't need authentication
        else {
            finalResponse = this.makeCallToPartner(partnerBusinessAction, partnerCallsFinalDTO, params, partnerId, originalBody);
        }

        return finalResponse;
    }


    /**
     * Do the transformation of the response and the concatenation of the responses
     *
     * @param partnerResponse       Partner response
     * @param partnerBusinessAction current action
     * @param partnerId             Partner identifier
     * @return list of objects in pivot format
     * @throws JSONException
     * @throws IOException
     */
    private List<Object> adaptResponse(String partnerResponse, PartnerActionDTO partnerBusinessAction, UUID partnerId) throws JSONException, IOException {
        List<Object> adaptedResponseObjects;
        if (StringUtils.isNotBlank(partnerResponse)) {
            adaptedResponseObjects = this.formatResponse(partnerResponse, partnerBusinessAction, partnerId);
        } else {
            throw new NotFoundException(CommonUtils.placeholderFormat(VALUE_NULL_OF_TYPE_JSONOBJECT));
        }
        return adaptedResponseObjects;
    }


    /**
     * save the token that we get from the partner in the database
     *
     * @param tokenDTO the token
     * @return the token that we just saved in DB
     */
    private TokenDTO saveTokenInDatabase(Object tokenDTO) {
        String initialOutputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        HttpEntity<Object> entity = new HttpEntity<>(tokenDTO, CommonUtils.setHttpHeaders());
        String urlPostToken = dataApiUri + CommonUtils.placeholderFormat(GET_TOKEN_PATH);

        try {
            ResponseEntity<TokenDTO> tokenResponse = restTemplate.exchange(urlPostToken, HttpMethod.POST, entity, TokenDTO.class);
            return tokenResponse.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), dataApiUri));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), dataApiUri));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), dataApiUri));
        } finally {
            CallUtils.saveOutputStandardInCallThread(initialOutputStandard);
        }

    }

    private List<PartnerActionDTO> prepareActionListByPartnerMetaId(UUID partnerId) {
        String initialOutputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        List<PartnerActionDTO> actionList;
        String urlGetCallsByActionId = dataApiUri + CommonUtils.placeholderFormat(PARTNER_ACTIONS_BASE_PATH + GET_BY_PARTNER_META_ID_PATH, PARTNER_ID_PARAM, String.valueOf(partnerId));

        try {
            ResponseEntity<PartnerActionDTO[]> partnerCallsDTO = restTemplate.exchange(urlGetCallsByActionId, HttpMethod.GET, CommonUtils.setHeaders(), PartnerActionDTO[].class);
            actionList = Arrays.asList(Objects.requireNonNull(partnerCallsDTO.getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } finally {
            CallUtils.saveOutputStandardInCallThread(initialOutputStandard);
        }
        return actionList;
    }


    /**
     * check if partner need authentication and prepare token for it
     *
     * @param partnerMeta          partnerMeta
     * @param partnerCallsFinalDTO the final call that we will send to request relay
     */
    private void prepareAuthentication(List<PartnerActionDTO> actions, PartnerMetaDTO partnerMeta, PartnerCallsFinalDTO partnerCallsFinalDTO, Map<String, String> params, UUID partnerId, Map<String, Object> originalBody, List<Object> finalResponse)
            throws JSONException, IOException {

        TokenDTO securityToken;

        //  we retrieve the authentication action
        PartnerActionDTO authenticationAction = authenticationService.needAuthenticationAction(actions);

        // we retrieve the token from the database
        securityToken = prepareTokenDTO(partnerMeta.getPartnerId());

        // we check if the token is still valid
        if (!CustomParamUtils.isValidToken(securityToken, timeInMinutes)) {
            log.info(THE_TOKEN_IS_NOT_VALID_OR_NOT_EXIST);

            // case : invalid token,  we launch a call to the partner to retrieve a new token
            finalResponse.addAll(makeCallToPartner(authenticationAction, partnerCallsFinalDTO, params, partnerId, originalBody));

        } else {

            // case : valid token,  we call the token that we have in database
            finalResponse.addAll(Collections.singletonList(this.prepareTokenDTO(partnerMeta.getPartnerId())));
        }

    }

    /**
     * all the steps to build the call request
     *
     * @param partnerAction        partnerAction
     * @param partnerCallsFinalDTO partnerCallFinal
     * @param params               params
     * @param partnerId            partnerId
     * @param originalBody         originalBody
     * @return list of objects returned by partner
     * @throws JSONException
     * @throws IOException
     */
    private List<Object> makeCallToPartner(PartnerActionDTO partnerAction, PartnerCallsFinalDTO partnerCallsFinalDTO, Map<String, String> params, UUID partnerId, Map<String, Object> originalBody) throws JSONException, IOException {
        List<Object> finalResponse = new ArrayList<>();
        List<PartnerCallsDTO> businessCalls = this.getBusinessCalls(partnerAction.getPartnerActionId());
        // iterate over collection of calls associated with the business action
        for (PartnerCallsDTO callDto : businessCalls) {
            if (callDto.getPartnerCallId() != null) {
                partnerCallsFinalDTO.setPartnerCallId(callDto.getPartnerCallId());
            }
            if (callDto.getMethod() != null) {
                partnerCallsFinalDTO.setMethod(callDto.getMethod());
            }
            this.prepareHeaders(callDto, partnerCallsFinalDTO, params, partnerId);

            if (callDto.getNbCalls().equals(1)) {
                String partnerResponse = adaptOperationForOneCall(callDto, params, null, partnerCallsFinalDTO, originalBody);
                finalResponse.addAll(adaptResponse(partnerResponse, partnerAction, partnerId));
            } else if (callDto.getNbCalls() > 1) {
                finalResponse.add(this.manageMultiCalls(callDto, params, partnerCallsFinalDTO, partnerAction, partnerId, originalBody));
            } else {
                throw new BadGatewayException(CommonUtils.placeholderFormat(NUMBER_OF_CALLS_MUST_BE_GREATER_THAN_ONE, PARTNER_CALL_ID, callDto.getPartnerCallId().toString()));
            }
        }
        return finalResponse;
    }


    /**
     * Assign parameterized call headers with right values
     *
     * @param partnerCallsDTO      partner call
     * @param partnerCallsFinalDTO the final call that we will send to request relay
     * @param headersValue         list des headers
     * @param partnerId            partner identifier
     */
    private void prepareHeaders(PartnerCallsDTO partnerCallsDTO, PartnerCallsFinalDTO partnerCallsFinalDTO, Map<String, String> headersValue, UUID partnerId) {
        // add final headers to partnerCallFinalDTO's headers
        log.info(PROCESSING_HEADERS);

        Set<HeadersValuesTemplateFinalDTO> headersTemplateFinal = new HashSet<>();

        List<HeadersDTO> headers = new ArrayList<>();
        if (partnerCallsDTO.getHeaders() != null) {
            headers = new ArrayList<>(partnerCallsDTO.getHeaders());
        }

        // Process headers templates
        if (!headers.isEmpty()) {
            prepareHeaderTemplate(headers.stream().filter(header -> header.getValueTemplate() != null).collect(Collectors.toList()));
        }
        // Process security headers
        if (!headers.isEmpty()) {
            headers.stream().filter(header -> header.getSecurityFlag() != null && header.getSecurityFlag() == 1).forEach(header -> {
                TokenDTO token = this.prepareTokenDTO(partnerId);
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
        partnerCallsFinalDTO.setHeaders(headersTemplateFinal);

    }

    /**
     * method to retrieve token related to partnerMeta with id given in param
     *
     * @param partnerId partner identifier
     * @return the database token associated with partnerId
     */
    private TokenDTO prepareTokenDTO(UUID partnerId) {
        String initialOutputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        String urlGetTokenByPartnerId = dataApiUri + CommonUtils.placeholderFormat(GET_TOKEN_PATH + GET_BY_PARTNER_META_ID_PATH, PARTNER_ID_PARAM, String.valueOf(partnerId));

        try {
            ResponseEntity<TokenDTO> tokenDTO = restTemplate.exchange(urlGetTokenByPartnerId, HttpMethod.GET, CommonUtils.setHeaders(), TokenDTO.class);
            return tokenDTO.getBody();
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetTokenByPartnerId));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetTokenByPartnerId));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetTokenByPartnerId));
        } finally {
            CallUtils.saveOutputStandardInCallThread(initialOutputStandard);
        }
    }

    /**
     * Perform headers which have a template and require a special actions like authentication headers
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

            // Get the values to enter the template
            List<HeadersValuesTemplateDTO> headersValuesTemplate = new ArrayList<>(header.getHeadersValuesTemplate());

            for (HeadersValuesTemplateDTO headerTemplate : headersValuesTemplate) {
                for (String key : matchList) {
                    if (headerTemplate.getKey().equals(key)) {
                        finalValue = CommonUtils.placeholderFormat(finalValue, key, headerTemplate.getValue());
                        break;
                    }
                }
            }
            String info = CommonUtils.placeholderFormat(PRE_PROCESS, FINAL_VALUE, finalValue);
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
     * @param partnerCallsDTO      partnerCallsDTO
     * @param paramsValue          list of params
     * @param partnerCallsFinalDTO partnerCallFinalDto where we set all prepared parameters
     */
    private void prepareUrlAndParams(PartnerCallsDTO partnerCallsDTO, Map<String, String> paramsValue,
                                     Map<String, List<String>> paramsMultiCalls, PartnerCallsFinalDTO partnerCallsFinalDTO, Map<String, Object> originalBody) {
        // concat final Url with params and assign it to partnerCalls' url
        log.info(PROCESSING_PARAMS);
        List<ParamsDTO> params;
        List<String> missingFields = new ArrayList<>();

        params = new ArrayList<>(partnerCallsDTO.getParams());
        String url = partnerCallsDTO.getUrl();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        UriComponentsBuilder logBuilder = UriComponentsBuilder.fromUriString(url);

        if (!CollectionUtils.isEmpty(params)) {
            for (ParamsDTO param : params) {

                String key = param.getKey();
                String value;

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

            if (!missingFields.isEmpty()) {
                String errorMessageMissingFields = String.join(", ", missingFields);
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
        partnerCallsFinalDTO.setUrl(url);

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
     * Construct and assign final body template to partnerCallsFinal
     *
     * @param callDto
     * @param partnerCallsFinalDTO
     * @param originalBody
     */
    private void prepareBody(PartnerCallsDTO callDto, PartnerCallsFinalDTO partnerCallsFinalDTO, Map<String, Object> originalBody) {
        // template treatment
        BodyDTO body = callDto.getBody();
        if (body != null && body.getTemplate() != null) {
            String template;
            String templateLogged;
            if (body.getTemplate().equals(ORIGINAL_BODY)) {
                JSONObject jsonBody = new JSONObject(originalBody);
                template = jsonBody.toString();
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

            partnerCallsFinalDTO.setBody(template);
            if (log.isDebugEnabled()) {
                log.debug(FINAL_BODY, templateLogged);
            }

        }
    }


    /**
     * prepare the list of calls
     *
     * @param actionId action identifier
     * @return list of partnerCallsDTO in the database related to this action
     */
    private List<PartnerCallsDTO> getBusinessCalls(UUID actionId) {
        String initialOutputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        List<PartnerCallsDTO> partnerBusinessCalls;
        String urlGetCallsByActionId = dataApiUri + CommonUtils.placeholderFormat(GET_CALLS_PATH + GET_BY_ACTIONS_ID_PATH, ACTION_ID_PARAM, String.valueOf(actionId));

        try {
            ResponseEntity<PartnerCallsDTO[]> partnerCallsDTO = restTemplate.exchange(urlGetCallsByActionId, HttpMethod.GET, CommonUtils.setHeaders(), PartnerCallsDTO[].class);
            partnerBusinessCalls = Arrays.asList(Objects.requireNonNull(partnerCallsDTO.getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCallsByActionId));
        } finally {
            CallUtils.saveOutputStandardInCallThread(initialOutputStandard);
        }
        return partnerBusinessCalls;
    }

    /**
     * prepare the list of Data Mapper
     *
     * @param actionId action identifier
     * @return list of DataMapperDTO in the database related to this action
     */
    private List<DataMapperDTO> prepareDataMappers(UUID actionId) {
        String initialOutputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        List<DataMapperDTO> dataMappers;
        String urlGetActionById = dataApiUri + CommonUtils.placeholderFormat(GET_DATA_MAPPER_BY_ID_PATH + GET_BY_ACTIONS_ID_PATH, ACTION_ID_PARAM, actionId.toString());

        try {
            ResponseEntity<DataMapperDTO[]> dataMappersDTO = restTemplate.exchange(urlGetActionById, HttpMethod.GET, CommonUtils.setHeaders(), DataMapperDTO[].class);
            dataMappers = Arrays.asList(Objects.requireNonNull(dataMappersDTO.getBody()));
        } catch (HttpClientErrorException.NotFound | NullPointerException e) {
            return Collections.emptyList();
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetActionById));
        } finally {
            CallUtils.saveOutputStandardInCallThread(initialOutputStandard);
        }
        return dataMappers;
    }

    /**
     * Decode And Parse partnerResponse
     *
     * @param list
     * @param jsonObjectResponse
     * @param dataMappers
     * @param partnerAction
     * @param partnerId
     * @throws JSONException
     * @throws IOException
     */
    private void decodeAndParse(List<Object> list, JSONObject jsonObjectResponse, List<DataMapperDTO> dataMappers, PartnerActionDTO partnerAction, UUID partnerId) throws JSONException, IOException {
        jsonObjectResponse = DecodeUtils.decodeDataResponse(jsonObjectResponse, dataMappers);

        if (partnerAction.isAuthentication()) {
            try {
                TokenDTO token = this.parseToken(jsonObjectResponse);
                token.setExpireAt(CustomParamUtils.tokenExpireAt(jsonObjectResponse));
                token.setPartnerId(partnerId);
                list.add(this.saveTokenInDatabase(token));
            } catch (Exception e) {
                throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE_TO_GATEWAY_DTO, GATEWAY_DTO, TokenDTO.class.toString()));
            }
        } else {
            parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, partnerAction, partnerId);
        }

    }

    /**
     * format the response in gateway format
     *
     * @param partnerResponse list of objects returned by partner
     * @param partnerAction   action identifier
     * @param partnerId
     * @return transformed fields in json format
     */
    private List<Object> formatResponse(String partnerResponse, PartnerActionDTO partnerAction, UUID partnerId) throws
            JSONException, IOException {
        List<Object> list = new ArrayList<>();
        //
        List<DataMapperDTO> dataMappers = prepareDataMappers(partnerAction.getPartnerActionId());
        //Retrieve The sub-object linked to Selector
        SelectorDTO selector = partnerAction.getSelector();
        if (selector != null) {
            partnerResponse = getDataBySelector(partnerResponse, selector);
        }
        // we have datamapper in database, so we have to do the transformation
        if (!dataMappers.isEmpty()) {
            if (partnerResponse.charAt(0) == '[') {
                JSONArray jsonArrayResponse;
                jsonArrayResponse = new JSONArray(partnerResponse);
                if (jsonArrayResponse.length() > 0) {
                    for (int i = 0; i < jsonArrayResponse.length(); i++) {
                        JSONObject jsonObjectResponse = jsonArrayResponse.getJSONObject(i);
                        // decode data and get jsonObject with champs transformed
                        decodeAndParse(list, jsonObjectResponse, dataMappers, partnerAction, partnerId);
                    }
                    return list;
                }
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(partnerResponse);
                    decodeAndParse(list, jsonObject, dataMappers, partnerAction, partnerId);
                } catch (Exception e) {
                    throw new NotFoundException(CommonUtils.placeholderFormat(CANNOT_PARSE + e.getMessage()));
                }
                return list;
            }
        } else {
            // case if datamapper is null , the partner response is already in gateway format
            // we check if this response is in an array
            if (partnerResponse.charAt(0) == '[') {
                JSONArray jsonArrayResponse = new JSONArray(partnerResponse);
                if (jsonArrayResponse.length() > 0) {
                    for (int i = 0; i < jsonArrayResponse.length(); i++) {
                        JSONObject jsonObjectResponse = jsonArrayResponse.getJSONObject(i);
                        parseUtils.parseToGatewayDTOByAction(list, jsonObjectResponse, partnerAction, partnerId);
                    }
                    return list;
                }
            }
            // if this response in pivot format is in an array
            else {
                JSONObject jsonObject = new JSONObject(partnerResponse);
                parseUtils.parseToGatewayDTOByAction(list, jsonObject, partnerAction, partnerId);
                return list;
            }
        }
        return list;
    }

    /**
     * makes the call to Request-relay microservice
     *
     * @param partnerCallsFinal partnerCallFinalDto contains all prepared parameters
     * @return the response of the partner, list of objects
     */
    private String manageCall(PartnerCallsFinalDTO partnerCallsFinal) {
        HttpEntity<PartnerCallsFinalDTO> entity = new HttpEntity<>(partnerCallsFinal, CommonUtils.setHeaders().getHeaders());
        String urlPostRequest = requestRelayUrl + CommonUtils.placeholderFormat(REST_PROTOCOL);

        boolean preserveOriginalErrors = false;
        String initialOutputStandard = CallUtils.getOutputStandardFromCallThread();
        if (StringUtils.isNotBlank(initialOutputStandard)) {
            preserveOriginalErrors = CommonUtils.shouldPreserveResponseStatus(initialOutputStandard);
        }

        try {
            ResponseEntity<String> partnerResponse = restTemplate.exchange(urlPostRequest, HttpMethod.POST, entity, String.class);
            return partnerResponse.getBody();
        } catch (HttpClientErrorException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                GenericError error = new GenericError(e.getResponseBodyAsString());
                throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), requestRelayUrl) + SEPARATOR + error.getDescription());
            }
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), requestRelayUrl) + SEPARATOR + e.getMessage());
            }
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, CORRELATION_ID, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), requestRelayUrl) + SEPARATOR + e.getMessage());
            }

        }
    }

    /**
     * Manage calls in case of multiCalls in case of nbCallS = 1
     *
     * @param callDto              current call
     * @param partnerCallsFinalDTO the call request
     * @return list of objects (partner responses)
     */
    private List<Object> manageMultiCalls(PartnerCallsDTO callDto, Map<String, String> params, PartnerCallsFinalDTO partnerCallsFinalDTO, PartnerActionDTO partnerAction, UUID partnerId, Map<String, Object> originalBody) throws JSONException, IOException {
        String responseBody;
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
            PartnerCallsDTO call = new PartnerCallsDTO(callDto.getPartnerCallId(), callDto.getUrl(), callDto.getMethod(), callDto.getIsMocked(), callDto.getNbCalls(),
                    callDto.getExecutionOrder(), callDto.getPartnerActionId(), callDto.getBody(), callDto.getHeaders(), callDto.getParams(), callDto.getParamsMultiCalls());
            // construct the call, send it to request relay and retrieve the partner response for one call
            responseBody = adaptOperationForOneCall(call, params, paramsMultiCalls, partnerCallsFinalDTO, originalBody);

            //concat partner responses of each call
            if (responseBody != null && responseBody.charAt(0) == '[') {
                String responseValues = responseBody.substring(1, responseBody.length() - 1);
                responseBodies.add(responseValues);
            } else {
                responseBodies.add(responseBody);
            }

            //  initialize date params
            CustomParamUtils.reinitParams(paramsOption, paramsMultiCalls);
        }


        //transform full partner response to GTW format
        return adaptResponse(String.valueOf(responseBodies), partnerAction, partnerId);
    }

    /**
     * adapts the operation of construction of the call  for a single call: prepare params, url, body and send the call request to requestRelay
     *
     * @param call                 partnerCallDto
     * @param paramsMultiCalls     params multi calls (timings params)
     * @param partnerCallsFinalDTO the call request
     * @param originalBody         original body
     * @return a partner response in string format
     */
    private String adaptOperationForOneCall(PartnerCallsDTO call, Map<String, String> params, Map<String, List<String>> paramsMultiCalls, PartnerCallsFinalDTO partnerCallsFinalDTO, Map<String, Object> originalBody) {
        this.prepareUrlAndParams(call, params, paramsMultiCalls, partnerCallsFinalDTO, originalBody);
        this.prepareBody(call, partnerCallsFinalDTO, originalBody);
        return this.manageCall(partnerCallsFinalDTO);
    }

    /**
     * Retrieve The sub-object linked to Selector
     *
     * @param partnerResponse
     * @param selector
     * @return partnerResponse
     */
    private String getDataBySelector(String partnerResponse, SelectorDTO selector) throws JSONException {
        if (StringUtils.isNotBlank(partnerResponse) && partnerResponse.charAt(0) == '[') {
            JsonElement selectedResponse = JsonUtils.getJsonArray(selector, partnerResponse);
            return selectedResponse != null ? selectedResponse.toString() : partnerResponse;
        } else {
            partnerResponse = String.valueOf(JsonUtils.getJsonElement(selector, partnerResponse));
        }
        return partnerResponse;
    }


    /**
     * parse objectJson in TokenDTO
     *
     * @param dataResponse jsonObjectResponse to parse in gateway format
     * @return Token objet
     * @throws IOException
     */
    public TokenDTO parseToken(JSONObject dataResponse) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(dataResponse.toString(), TokenDTO.class);
    }
}
