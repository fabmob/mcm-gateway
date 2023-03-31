package com.gateway.routingapi.service.impl;

import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.AdaptersDTO;
import com.gateway.commonapi.dto.data.PartnerStandardDTO;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.restConfig.RestConfig;
import com.gateway.commonapi.utils.CallUtils;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
import com.gateway.routingapi.service.RoutingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.*;

import static com.gateway.routingapi.util.constant.RoutingDict.*;
import static com.gateway.routingapi.util.constant.RoutingMessageDict.*;


@Slf4j
@Service
public class RoutingServiceImpl implements RoutingService {

    RestConfig restConfig = new RestConfig();
    RestTemplate restTemplate = restConfig.restTemplate();
    @Value("${gateway.service.dataapi.baseUrl}")
    private String dataApiUri;
    @Value("${gateway.service.adapter.default-adapter.baseUrl}")
    private String defaultAdapterUri;
    @Value("${gateway.service.adapter.custom-adapter.baseUrl}")
    private String customAdapterUri;
    @Autowired
    private ErrorMessages errorMessages;

    private static final String SEPARATOR = ": ";

    @Override
    public Object routeOperation(Map<String, String> params, UUID partnerId, String actionName, Optional<Map<String, Object>> body) {
        // need to call the standard table to get versions and actions which is active for the msp
        PartnerStandardDTO standard = this.activeVersionSearch(partnerId, actionName);
        Object response = null;
        if (standard != null) {
            UUID actionId = standard.getPartnerActionsId();
            // create the uri
            String uriCall = this.createURI(standard);
            // forward the request
            response = forwardRequest(uriCall, partnerId, actionId, body, params);
        } else {
            log.debug(NO_ACTIVE_ACTION_FOUND, actionName, partnerId);
        }
        return response;
    }

    /**
     * Return Active Version
     *
     * @param partnerId
     * @param actionName
     * @return PartnerStandardDTO
     */
    private PartnerStandardDTO activeVersionSearch(UUID partnerId, String actionName) {
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        PartnerStandardDTO mspBusinessVersion;
        String urlGetVersion;
        String mspMetaIdValue = partnerId != null ? partnerId.toString() : null;
        urlGetVersion = dataApiUri + CommonUtils.placeholderFormat(GET_VERSION_PATH + GET_BY_ACTIONS_NAME_PATH, PARTNER_ACTIONS_NAME, actionName
                + GET_BY_MSP_META_ID_PATH, MSP_ID_PARAM, mspMetaIdValue + GET_IS_ACTIVE_TRUE_PATH);
        log.debug(ROUTING_SERVICE_CALL_URL, urlGetVersion);
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

        try {
            ResponseEntity<PartnerStandardDTO[]> mspStandardDTO = restTemplate.exchange(urlGetVersion, HttpMethod.GET, CommonUtils.setHeaders(), PartnerStandardDTO[].class);
            mspBusinessVersion = Objects.requireNonNull(mspStandardDTO.getBody())[0];
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new NotFoundException(CommonUtils.placeholderFormat(NO_ACTIVE_ACTION_FOUND, PARTNER_ACTIONS_NAME, actionName, PARTNER_ID, (partnerId != null ? partnerId.toString() : StringUtils.EMPTY)));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetVersion));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetVersion));
        } finally {
            CallUtils.saveOutputStandardInCallThread(outputStandard);
        }
        return mspBusinessVersion;

    }

    /**
     * Get Adapter Name
     *
     * @param adaptersId
     * @return Adapter Name
     */
    private String getAdaptersName(UUID adaptersId) {
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        CallUtils.saveOutputStandardInCallThread(StandardEnum.GATEWAY);

        String adapterName;
        String urlGetAdapterWithId = dataApiUri + CommonUtils.placeholderFormat(GET_ADAPTERS_BY_ID_PATH, ADAPTERS_ID_PARAM, adaptersId.toString());
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

        try {
            ResponseEntity<AdaptersDTO> mspActionDTO = restTemplate.exchange(urlGetAdapterWithId, HttpMethod.GET, CommonUtils.setHeaders(), AdaptersDTO.class);
            AdaptersDTO mspBusinessAdapters = Objects.requireNonNull(mspActionDTO.getBody());
            adapterName = mspBusinessAdapters.getAdapterName();
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            return null;
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetAdapterWithId));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetAdapterWithId));
        } finally {
            CallUtils.saveOutputStandardInCallThread(outputStandard);
        }
        return adapterName;
    }

    /**
     * Create Uri
     *
     * @param standard
     * @return Uri
     */
    private String createURI(PartnerStandardDTO standard) {
        //Version Standard
        String versionStandard = standard.getVersionStandard().toLowerCase(Locale.ROOT);

        //Get Adapter name
        UUID adaptersId = standard.getAdaptersId();
        String adapterName = getAdaptersName(adaptersId);

        //create URL
        if (adapterName != null && adapterName.equals(DEFAULT_ADAPTER_NAME)) {
            defaultAdapterUri = defaultAdapterUri.replace(GENERIC_ADAPTER_NAME, adapterName);
            return String.format("%s/%s/%s", defaultAdapterUri, versionStandard, ADAPT);
        } else {
            customAdapterUri = customAdapterUri.replace(GENERIC_ADAPTER_NAME, adapterName);
            return String.format("%s/%s/%s", customAdapterUri, versionStandard, ADAPT);
        }
    }

    /**
     * Forward Request to Adapters
     *
     * @param uriCall
     * @param partnerId
     * @param actionId
     * @param body
     * @return
     */

    private Object forwardRequest(String uriCall, UUID partnerId, UUID actionId, Optional<Map<String, Object>> body, Map<String, String> params) {

        String mspActionsIdValue = actionId != null ? actionId.toString() : null;
        String mspMetaIdValue = partnerId != null ? partnerId.toString() : null;
        String urlCallAdapters;
        Object mspBusinessResponse;
        urlCallAdapters = uriCall + CommonUtils.placeholderFormat(GET_BY_MSP_ACTIONS_ID_PATH, ACTION_ID_PARAM, mspActionsIdValue
                + GET_BY_MSP_META_ID_PATH, MSP_ID_PARAM, mspMetaIdValue);

        String urlTemplate = CommonUtils.constructUrlTemplate(urlCallAdapters, params);
        HttpEntity<Optional<Map<String, Object>>> entity = new HttpEntity<>(body, CommonUtils.setHeaders().getHeaders());
        log.debug(ROUTING_SERVICE_CALL_URL, urlTemplate);

        boolean preserveOriginalErrors = false;
        String outputStandard = CallUtils.getOutputStandardFromCallThread();
        if (StringUtils.isNotBlank(outputStandard)) {
            preserveOriginalErrors = CommonUtils.shouldPreserveResponseStatus(outputStandard);
        }
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().getFirst(GlobalConstants.CORRELATION_ID_HEADER));

        try {
            ResponseEntity<Object> response = restTemplate.exchange(urlTemplate, HttpMethod.POST, entity, Object.class);
            mspBusinessResponse = Objects.requireNonNull(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error(FAIL_CONTACTING_URL_WITH_MESSAGE, urlTemplate, e.getMessage(), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                GenericError error = new GenericError(e.getResponseBodyAsString());
                throw new NotFoundException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCallAdapters) + SEPARATOR + error.getDescription());
            }
        } catch (HttpServerErrorException e) {
            log.error(FAIL_CONTACTING_URL_WITH_MESSAGE, urlTemplate, e.getMessage(), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                GenericError error = new GenericError(e.getResponseBodyAsString());
                throw new InternalException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCallAdapters) + SEPARATOR + error.getDescription());
            }
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCallAdapters) + SEPARATOR + e.getMessage());
            }
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            if (preserveOriginalErrors) {
                throw e;
            } else {
                throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCallAdapters) + SEPARATOR + e.getMessage());
            }
        }
        return mspBusinessResponse;
    }

}
