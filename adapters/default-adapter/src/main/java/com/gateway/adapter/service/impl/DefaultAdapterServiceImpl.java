package com.gateway.adapter.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gateway.adapter.utils.constant.AdapterMessageDict;
import com.gateway.adapter.service.AuthenticationService;
import com.gateway.adapter.service.DefaultAdapterService;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.*;
import com.gateway.commonapi.dto.requestrelay.HeadersValuesTemplateFinalDTO;
import com.gateway.commonapi.dto.requestrelay.MspCallsFinalDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.monitoring.UserContext;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.MessageFormat;
import java.time.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.gateway.adapter.utils.constant.AdapterMessageDict.*;
import static com.gateway.adapter.utils.constant.AdapterPathDict.*;
import static com.gateway.adapter.utils.CustomUtils.*;


@Slf4j
@Service
public class DefaultAdapterServiceImpl implements DefaultAdapterService {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private ErrorMessages errorMessages;
    @Value("${gateway.service.requestRelay.url}")
    private String uri;
    @Value("${gateway.service.dataapi.url}")
    private String dataApiUri;
    @Value("${gateway.service.requestRelay.isMocked}")
    private Boolean isRequestRelayMocked;
    @Value("${gateway.service.requestRelay.url}")
    private String requestRelayUrl;

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

        if (mspStandard.getMspMetaId().equals(mspId)) {
            return mspBusinessAction;
        } else {
            throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalNotFoundDescription(), CommonUtils.placeholderFormat(MSP_ACTION_WITH_THIS_MSP_META_ID_IS_NOT_FOUND, FIRST_PLACEHOLDER, mspId.toString())));
        }
    }

    @Override
    public List<Object> adaptOperation(Map<String, String> params, UUID mspActionId, UUID mspId, Map<String, Object> originalBody) {
        // change this to switch to true call and remove it on code integration
        TokenDTO securityToken;

        MspCallsFinalDTO mspCallsFinalDTO = new MspCallsFinalDTO();

        MspActionDTO mspBusinessAction = this.prepareAction(mspActionId, mspId);

        mspCallsFinalDTO.setMspActionId(mspBusinessAction.getMspActionId());

        // TODO replace here by getting the mspMeta dto from crud
        MspMetaDTO mspMeta = new MspMetaDTO();
        // TODO https://gitlab-dev.cicd.moncomptemobilite.fr/mcm/std-maas/gateway/-/issues/124
        this.prepareAuthentication(mspMeta, mspCallsFinalDTO);

        List<MspCallsDTO> businessCalls = this.getBusinessCalls(mspActionId);

        List<Object> finalResponse = new ArrayList<>();

        // iterate over collection of calls associated with the business action
        for (MspCallsDTO callDto : businessCalls) {
            if (callDto.getMspCallId() != null) {
                mspCallsFinalDTO.setMspCallId(callDto.getMspCallId());
            }
            if (callDto.getMethod() != null) {
                mspCallsFinalDTO.setMethod(callDto.getMethod());
            }
            this.prepareHeaders(callDto, mspCallsFinalDTO, params, mspId);
            this.prepareUrlAndParams(callDto, params, mspCallsFinalDTO);

            // https://gitlab-dev.cicd.moncomptemobilite.fr/mcm/std-maas/gateway/-/issues/126
            this.prepareBody(callDto, mspCallsFinalDTO, originalBody);

            if (callDto.getNbCalls().equals(1)) {
                List<Object> mspResponse = this.manageCall(mspCallsFinalDTO);
                List<Object> adaptedResponseObjects = this.formatResponse(mspResponse, mspBusinessAction);
                finalResponse.addAll(adaptedResponseObjects);

            } else if (callDto.getNbCalls() > 0) {
                // TODO https://gitlab-dev.cicd.moncomptemobilite.fr/mcm/std-maas/gateway/-/issues/127  format responses within this method
                finalResponse.addAll(this.manageMultiCalls(callDto, mspCallsFinalDTO));
            } else {
                throw new BadGatewayException(CommonUtils.placeholderFormat("Number of calls not > 0 for {mspCallId}", "mspCallId", callDto.getMspCallId().toString()));
            }
        }
        return finalResponse;
    }

    /**
     * @param mspMeta          mspMeta
     * @param mspCallsFinalDTO the final call that we will send to request relay
     */
    private void prepareAuthentication(MspMetaDTO mspMeta, MspCallsFinalDTO mspCallsFinalDTO) {
        TokenDTO securityToken;
        // TODO https://gitlab-dev.cicd.moncomptemobilite.fr/mcm/std-maas/gateway/-/issues/124
        if (authenticationService.needAuthentication(mspMeta)) {
            //TODO get authentication action
            MspActionDTO authenticationAction = new MspActionDTO();
            securityToken = authenticationService.getOrUpdateToken(authenticationAction, mspMeta);
        }
        //TODO : add final token to MspCallFinalDTO's headers
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
                TokenDTO token = this.getTokenDTO(mspId);
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
    private TokenDTO getTokenDTO(UUID mspId) {

        String urlGetTokenByMspId = dataApiUri + CommonUtils.placeholderFormat(GET_TOKEN_PATH + GET_BY_MSP_META_ID_PATH, "mspId", String.valueOf(mspId));

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
                        finalValue = CommonUtils.placeholderFormat(finalValue,key, headerTemplate.getValue());
                        break;
                    }
                }
            }
            String info = CommonUtils.placeholderFormat(PRE_POCESS, "finalValue", finalValue);
            log.info(info, finalValue);

            // Specific function processing (encoding...)
            if (header.getProcessFunction() != null) {
                finalValue = CommonUtils.processFunction(finalValue, header.getProcessFunction());
            }
            // Add a prefix
            finalValue = Objects.toString(header.getValuePrefix(), "") + finalValue;
            header.setValue(finalValue);

            String callInfo = CommonUtils.placeholderFormat(CALL_IN_PROGRESS, "finalValue", finalValue);
            log.info(callInfo, finalValue);
        }
    }

    /**
     * Assign parameterized call parameters with right values
     *
     * @param mspCallsDTO      mspCallsDTO
     * @param paramsValue      list of params
     * @param mspCallsFinalDTO mspCallFinalDto where we set all prepared parameters
     */
    private void prepareUrlAndParams(MspCallsDTO mspCallsDTO, Map<String, String> paramsValue, MspCallsFinalDTO mspCallsFinalDTO) {
        // concat final Url with params and assign it to mspCalls' url
        log.info("Processing parameters...");

        List<ParamsDTO> params = new ArrayList<>(mspCallsDTO.getParams());
        String url = mspCallsDTO.getUrl();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (!CollectionUtils.isEmpty(params)) {
            for (ParamsDTO param : params) {
                String value = param.getValue();
                if (StringUtils.isNotBlank(value)) {
                    builder.queryParam(param.getKey(), value);
                }
            }
        }
        if (paramsValue != null) {
            for (Map.Entry<String, String> entry : paramsValue.entrySet()) {
                if (entry.getValue() != null && !Objects.equals(entry.getKey(), ACTION_ID_PARAM) && !Objects.equals(entry.getKey(), MSP_ID_PARAM)) {
                    builder.queryParam(entry.getKey(), entry.getValue());
                }
            }
        }
        url = builder.build().toUri().toString();
        mspCallsFinalDTO.setUrl(url);
        log.info("Final url : " + url);
    }


    /**
     * Construct and assign final body template to MspCallsFinal
     * @param callDto
     * @param mspCallsFinalDTO
     * @param originalBody
     */

    private void prepareBody(MspCallsDTO callDto, MspCallsFinalDTO mspCallsFinalDTO, Map<String, Object> originalBody) {


        BodyDTO body = callDto.getBody();
        if(body != null && body.getTemplate()!= null){
            String template = body.getTemplate();
            String templateLogged = template;
            Set<BodyParamsDTO> bodyParams = body.getBodyParams();

            for(BodyParamsDTO param : bodyParams){
                String finalValue;
                if(param.getKey() == null && param.getValue() != null){
                    finalValue = param.getValue();
                } else{
                    try{
                        finalValue = (String) originalBody.get(param.getKey());
                        if(finalValue == null){
                            throw new InternalException(CommonUtils.placeholderFormat(MISSING_BODY_FIELD, FIRST_PLACEHOLDER, param.getKey()));
                        }
                    } catch (Exception e){
                        throw new InternalException(CommonUtils.placeholderFormat(MISSING_BODY_FIELD, FIRST_PLACEHOLDER, param.getKey()));
                    }

                    String precision = param.getPrecision();
                    String timezone = param.getTimezone();
                    String precisedValue = StringUtils.isNotBlank(precision) ? String.valueOf(assignPrecision(OffsetDateTime.parse(finalValue), precision)) : finalValue;
                    finalValue = StringUtils.isNotBlank(timezone) ? String.valueOf(formatDateTime(OffsetDateTime.parse(precisedValue),timezone)) : precisedValue;
                }
                String keyMapper = param.getKeyMapper();
                template = template.replace("${"+keyMapper+"}",finalValue);

                Integer sensitive = param.getSensitive();
                templateLogged = templateLogged.replace("${"+keyMapper+"}", censoredBodyParam(sensitive,finalValue));
            }


            mspCallsFinalDTO.setBody(template);
            log.debug("Final body: "+ templateLogged);
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

        String urlGetCallsByActionId = dataApiUri + CommonUtils.placeholderFormat(GET_CALLS_PATH + GET_BY_ACTIONS_ID_PATH , ACTION_ID_PARAM, String.valueOf(actionId));

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


    private List<Object> formatResponse(List<Object> mspResponse, MspActionDTO mspAction) {
        //TOTO replace here conversion of response msp format into gateway one
        return mspResponse;
    }

    /**
     * makes the call to Request-relay microservice
     *
     * @param mspCallsFinal mspCallFinalDto contains all prepared parameters
     * @return the response of the msp, list of objects
     */
    private List<Object> manageCall(MspCallsFinalDTO mspCallsFinal) {
        // replace call infor from  MspCallsDTO into MspCallsFinalDTO

        UserContext userContext = new ThreadLocalUserSession().get();
        String correlationId = userContext.getContextId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
        HttpEntity<MspCallsFinalDTO> entity = new HttpEntity<>(mspCallsFinal, httpHeaders);

        try {
            ResponseEntity<String> mspResponse = restTemplate.exchange(requestRelayUrl + "?protocol=REST", HttpMethod.POST, entity, String.class);
            String response = mspResponse.getBody();
            ObjectMapper mapper = new ObjectMapper();
            if (response != null && response.charAt(0) == '[') {
                return mapper.readValue(response, new TypeReference<>() {
                });
            } else {
                List<Object> responseToList = new ArrayList<>();
                responseToList.add(mapper.readValue(response, new TypeReference<>() {
                }));
                return responseToList;
            }
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
     * makes the call to Request-relay microservice in case of multiple calls
     *
     * @param callDto          callDto
     * @param mspCallsFinalDTO spCallFinalDto contains all prepared parameters
     * @return the response of the msp, list of objects
     */
    private List<Object> manageMultiCalls(MspCallsDTO callDto, MspCallsFinalDTO mspCallsFinalDTO) {
        return new ArrayList<>();
    }


}
