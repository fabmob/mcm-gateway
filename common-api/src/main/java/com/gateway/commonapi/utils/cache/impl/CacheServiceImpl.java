package com.gateway.commonapi.utils.cache.impl;

import com.gateway.commonapi.cache.CacheStatus;
import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.CacheParamDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import com.gateway.commonapi.utils.ExceptionUtils;
import com.gateway.commonapi.utils.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.gateway.commonapi.constants.GatewayApiPathDict.BASE_ERROR_MESSAGE;
import static com.gateway.commonapi.constants.GatewayApiPathDict.CACHE_PARAM_ENDPOINT;

/**
 * service used to populate the cache with mocked data
 */
@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    private RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    @Value("${gateway.service.dataapi.baseUrl}")
    private String uri;
    @Autowired
    private ErrorMessages errorMessages;

    private List<CacheParamDTO> getCacheParamDatabase(UUID partnerId, String actionType) {
        String correlationId = String.valueOf(CommonUtils.setHeaders().getHeaders().get(GlobalConstants.CORRELATION_ID_HEADER));
        List<CacheParamDTO> cacheParams;
        String urlGetCacheParam = uri + CACHE_PARAM_ENDPOINT + "?partnerId=" + partnerId + "&actionType=" + actionType;
        try {
            ResponseEntity<CacheParamDTO[]> cacheParam = restTemplate.exchange(urlGetCacheParam, HttpMethod.GET, CommonUtils.setHeaders(), CacheParamDTO[].class);
            cacheParams = List.of(Objects.requireNonNull(cacheParam.getBody()));
        } catch (HttpClientErrorException.NotFound e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw ExceptionUtils.getMappedGatewayRuntimeException(e, MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCacheParam));
        } catch (RestClientException e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new BadGatewayException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCacheParam));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetCacheParam));
        }
        return cacheParams;
    }

    public CacheParamDTO getCacheParam(UUID partnerId, String actionType) {
        try {
            List<CacheParamDTO> cacheParams = this.getCacheParamDatabase(partnerId, actionType);
            return cacheParams.get(0);
        } catch (Exception e) {
            // if cache param generate an exception, we make cacheParam as null to continue processing via rooting
            return null;
        }
    }

    @Override
    public boolean useCache() {
        return CacheStatus.getInstance().isEnabled();
    }


}
