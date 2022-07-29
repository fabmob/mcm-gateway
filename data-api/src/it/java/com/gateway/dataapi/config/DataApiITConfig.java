package com.gateway.dataapi.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
public class DataApiITConfig {

}
