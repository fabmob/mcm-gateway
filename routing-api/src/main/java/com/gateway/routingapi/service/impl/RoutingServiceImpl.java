package com.gateway.routingapi.service.impl;

import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.AdaptersDTO;
import com.gateway.commonapi.dto.data.MspStandardDTO;
import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.routingapi.service.RoutingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
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


    @Autowired
    RestTemplate restTemplate;
    @Value("${gateway.service.dataapi.url}")
    private String dataApiUri;
    @Value("${gateway.service.adapter.default-adapter.baseUrl}")
    private String defaultAdapterUri;
    @Value("${gateway.service.adapter.custom-adapter.baseUrl}")
    private String customAdapterUri;
    @Autowired
    private ErrorMessages errorMessages;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private final String correlationId = String.valueOf(CommonUtils.setHeader().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));


    @Override
    public Object routeOperation(Map<String, String> params, UUID mspId, String actionName, Optional<Map<String, Object>> body) {
        // need to call the standard table to get versions and actions which is active for the msp
        MspStandardDTO standard = this.activeVersionSearch(mspId, actionName);
        Object response = null;
        if (standard != null) {
            UUID actionId = standard.getMspActionsId();
            // create the uri
            String uriCall = this.creatURI(standard);
            // forward the request
            response = forwardRequest(uriCall, mspId, actionId, body, params);
        } else {
            log.debug(NO_ACTIVE_ACTION_FOUND, actionName, mspId);
        }
        return response;
    }

    /**
     * Return Active Version
     *
     * @param mspId
     * @param actionName
     * @return MspStandardDTO
     */
    private MspStandardDTO activeVersionSearch(UUID mspId, String actionName) {
        MspStandardDTO mspBusinessVersion;
        String urlGetVersion;
        String mspMetaIdValue = mspId != null ? mspId.toString() : null;
        urlGetVersion = dataApiUri + CommonUtils.placeholderFormat(GET_VERSION_PATH + GET_BY_ACTIONS_NAME_PATH, MSP_ACTIONS_NAME, actionName
                + GET_BY_MSP_META_ID_PATH, MSP_ID_PARAM, mspMetaIdValue + GET_IS_ACTIVE_TRUE_PATH);
        log.debug(ROUTING_SERVICE_CALL_URL, urlGetVersion);
        try {
            ResponseEntity<MspStandardDTO[]> mspStandardDTO = restTemplate.exchange
                    (urlGetVersion, HttpMethod.GET, CommonUtils.setHeader(), MspStandardDTO[].class);
            mspBusinessVersion = Objects.requireNonNull(mspStandardDTO.getBody())[0];
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            return null;
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetVersion));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetVersion));
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
        String adapterName;
        String urlGetAdapterWithId = dataApiUri + CommonUtils.placeholderFormat(GET_ADAPTERS_BY_ID_PATH, ADAPTERS_ID_PARAM, adaptersId.toString());
        try {
            ResponseEntity<AdaptersDTO> mspActionDTO = restTemplate.exchange(urlGetAdapterWithId, HttpMethod.GET, CommonUtils.setHeader(), AdaptersDTO.class);
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
        }
        return adapterName;
    }

    /**
     * Create Uri
     *
     * @param standard
     * @return Uri
     */
    private String creatURI(MspStandardDTO standard) {
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
     * @param mspId
     * @param actionId
     * @param body
     * @return
     */

    private Object forwardRequest(String uriCall, UUID mspId, UUID actionId, Optional<Map<String, Object>> body, Map<String, String> params) {
        String mspActionsIdValue = actionId != null ? actionId.toString() : null;
        String mspMetaIdValue = mspId != null ? mspId.toString() : null;
        String urlCallAdapters;
        Object mspBusinessResponse;
        urlCallAdapters = uriCall + CommonUtils.placeholderFormat(GET_BY_MSP_ACTIONS_ID_PATH, ACTION_ID_PARAM, mspActionsIdValue
                + GET_BY_MSP_META_ID_PATH, MSP_ID_PARAM, mspMetaIdValue);

        String urlTemplate = CommonUtils.constructUrlTemplate(urlCallAdapters, params);

        log.debug(ROUTING_SERVICE_CALL_URL, urlTemplate);

        try {
            ResponseEntity<Object> response = restTemplate.postForEntity(urlTemplate, body, Object.class);
            mspBusinessResponse = Objects.requireNonNull(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error(FAIL_CONTACTING_URL_WITH_MESSAGE, urlTemplate, e.getMessage(), e);
            GenericError error = new GenericError(e.getResponseBodyAsString());
            throw new NotFoundException(error.getDescription());
        } catch (HttpServerErrorException e) {
            log.error(FAIL_CONTACTING_URL_WITH_MESSAGE, urlTemplate, e.getMessage(), e);
            GenericError error = new GenericError(e.getResponseBodyAsString());
            throw new InternalException(error.getDescription());

        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCallAdapters));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlCallAdapters));
        }
        return mspBusinessResponse;
    }

}
