package com.gateway.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.servers.Server;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.gateway.api", "com.gateway.commonapi"})
@PropertySource("classpath:errors.properties")
@OpenAPIDefinition(info = @Info(
        title = "Gateway MaaS API",
        version = "2.0",
        description = "Swagger for the Gateway API"),
        tags = {@Tag(
                name = "MSPs Information",
                description = "MSPs information and useful links"),
                @Tag(
                        name = "Traveler Information"),
                @Tag(
                        name = "Carpooling")},
        servers = {
                @Server(url = "https://${SERVICE_BASE_PATH:localhost:8080}${server.servlet.context-path}"),
                @Server(url = "http://${SERVICE_BASE_PATH:localhost:8080}${server.servlet.context-path}")}
)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

}
