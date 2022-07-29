package com.gateway.requestrelay;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = { "com.gateway.requestrelay", "com.gateway.commonapi" })
@PropertySource("classpath:errors.properties")
@OpenAPIDefinition(info = @Info(title = "Request Relay", version = "2.0", description = "Swagger for the webservice relaying requests to MSPs"))
public class RequestRelayApplication {

    public static void main(String[] args) {
        SpringApplication.run(RequestRelayApplication.class, args);
    }

}
