package com.gateway.requestrelay;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.gateway.requestrelay", "com.gateway.commonapi"})
@PropertySource("classpath:errors.properties")
@OpenAPIDefinition(info = @Info(
        title = "Request Relay",
        version = "2.0",
        description = "Swagger for the webservice relaying requests to Partners"),
        servers = {
                @Server(url = "https://${SERVICE_BASE_PATH:localhost:8085}${server.servlet.context-path}"),
                @Server(url = "http://${SERVICE_BASE_PATH:localhost:8085}${server.servlet.context-path}")
        }
)
public class RequestRelayApplication {

    public static void main(String[] args) {
        SpringApplication.run(RequestRelayApplication.class, args);
    }

}
