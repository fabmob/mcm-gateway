package com.gateway.dataapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.gateway.database.model"})
@EnableJpaRepositories(basePackages = {"com.gateway.database"})
@ComponentScan(basePackages = {"com.gateway.database", "com.gateway.dataapi", "com.gateway.commonapi.properties", "com.gateway.commonapi", "com.gateway.commonapi.filter"})
@PropertySource("classpath:errors.properties")
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Gateway Data API",
        version = "2.0",
        description = "Gateway Data API description"),
        servers = {
                @Server(url = "https://${SERVICE_BASE_PATH:localhost:8081}${server.servlet.context-path}"),
                @Server(url = "http://${SERVICE_BASE_PATH:localhost:8081}${server.servlet.context-path}")
        }
)
public class DataApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataApplication.class, args);
    }
}