package com.gateway.commonapi.restConfig;

import com.gateway.commonapi.constants.GlobalConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Configuration class to have a centralized and common RestTemplate for all services and to set a request timeout using RestTemplate and environment variables
 */
@Configuration
public class RestConfig {

    // the timeout variable setted up in environment variables, it's equal 30s by default
    private int gatewayTimeout = Integer.parseInt(StringUtils.isNotEmpty(System.getenv(GlobalConstants.GATEWAY_TIMEOUT)) ? (System.getenv(GlobalConstants.GATEWAY_TIMEOUT)) : String.valueOf(GlobalConstants.DEFAULT_TIMEOUT));

    @Bean
    public RestTemplate restTemplate() {
        // Timeout configuration with RestTemplate
        return new RestTemplateBuilder().setReadTimeout(Duration.ofSeconds(gatewayTimeout)).build();
    }
}
