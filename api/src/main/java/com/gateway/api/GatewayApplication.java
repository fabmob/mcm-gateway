package com.gateway.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = { "com.gateway.api", "com.gateway.commonapi" })
@PropertySource("classpath:errors.properties")
@OpenAPIDefinition(info = @Info(title = "Gateway MaaS API", version = "2.0", description = "Swagger for the Gateway API"), tags = {
        @Tag(name="MSPs Information", description="MSPs information and useful links"),
        @Tag(name="Traveler Information")
})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
