package com.gateway.api;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = GatewayApplication.class,  webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = {  "com.gateway.api" })
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureDataJpa
@Ignore("Mother class of the tests")
public class ApiITTestCase {
}
