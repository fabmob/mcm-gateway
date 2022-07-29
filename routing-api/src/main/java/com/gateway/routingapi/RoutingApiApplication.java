package com.gateway.routingapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.gateway.routingapi", "com.gateway.commonapi"})
@PropertySource("classpath:errors.properties")
@OpenAPIDefinition(info = @Info(
        title = "Routing API",
        version = "2.0",
        description = "Swagger for the routing API"),
        tags = {@Tag(
                name = "Routing",
                description = "Routing api to redirect to adapters regarding version, action and standard")},
        servers = {
                @Server(url = "https://${SERVICE_BASE_PATH:localhost:8082}${server.servlet.context-path}"),
                @Server(url = "http://${SERVICE_BASE_PATH:localhost:8082}${server.servlet.context-path}")
        }
)
public class RoutingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoutingApiApplication.class, args);
    }

}
