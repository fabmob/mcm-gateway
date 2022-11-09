package com.gateway.dataapi;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = DataApplication.class,  webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = { "com.gateway.dataapi" , "com.gateway.database", "com.gateway.commonapi.properties", "com.gateway.commonapi", "com.gateway.commonapi.filter"})
@PropertySource("classpath:errors.properties")
@EntityScan(basePackages = { "com.gateway.database.model" })
@TestPropertySource(properties = {"classpath:application.yml"})
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Ignore("Mother class of the tests")
public class DataApiITCase {
}
