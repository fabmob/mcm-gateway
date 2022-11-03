package com.gateway.commonapi.cache;

import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.dto.data.GatewayParamsDTO;
import com.gateway.commonapi.exception.BadGatewayException;
import com.gateway.commonapi.exception.UnavailableException;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.monitoring.UserContext;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.gateway.commonapi.constants.DataApiPathDict.*;
import static com.gateway.commonapi.constants.GlobalConstants.BASE_ERROR_MESSAGE;
import static com.gateway.commonapi.constants.GlobalConstants.CACHE_ACTIVATION;

@Slf4j
@Component
public class ScheduledTasks {

    RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private ErrorMessages errorMessages;

    @Value("${gateway.service.dataapi.baseUrl}")
    private String dataApiUri;

    @Scheduled(fixedDelay = 60000)
    public void getCacheActivationValue() {

        String cacheActivated = Boolean.FALSE.toString();
        boolean previousValue = CacheStatus.getInstance().isEnabled();

        // get the CORRELATION_ID of the current thread and forward as http header
        UserContext userContext = new ThreadLocalUserSession().get();
        String correlationId = userContext.getContextId();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(GlobalConstants.CORRELATION_ID_HEADER, correlationId);
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        String urlGetGtwCacheParam = dataApiUri + GATEWAY_PARAMS_BASE_PATH + CommonUtils.placeholderFormat(GATEWAY_PARAM_PATH, PARAM_KEY, CACHE_ACTIVATION);

        try {
            ResponseEntity<GatewayParamsDTO> response = restTemplate.exchange(urlGetGtwCacheParam, HttpMethod.GET, entity, GatewayParamsDTO.class);
            GatewayParamsDTO gatewayParamsDTO = response.getBody();
            cacheActivated = (gatewayParamsDTO != null && gatewayParamsDTO.getParamValue() != null) ? gatewayParamsDTO.getParamValue() : Boolean.FALSE.toString();
        } catch (HttpClientErrorException.NotFound e) {
            log.info("Scheduler setted singleton CacheStatus to false");
            CacheStatus.getInstance().setEnabled(false);
        } catch (RestClientException e) {
            log.info("Scheduler setted singleton CacheStatus to false");
            String msg = "Error occured in Scheduler while calling CACHE_ACTIVATION: " + e.getMessage();
            throw new BadGatewayException(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, msg));
        } catch (Exception e) {
            log.error(MessageFormat.format(BASE_ERROR_MESSAGE, correlationId, e.getMessage()), e);
            throw new UnavailableException(MessageFormat.format(errorMessages.getTechnicalRestHttpClientError(), urlGetGtwCacheParam));
        }

        CacheStatus.getInstance().setEnabled(Boolean.parseBoolean(cacheActivated));
        if (!Objects.equals(CacheStatus.getInstance().isEnabled(), previousValue)) {
            log.info("Scheduler detected change in cache status :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
            log.info("Cache Activated changed from : {} to {}", previousValue, CacheStatus.getInstance().isEnabled());
        }

    }

}
