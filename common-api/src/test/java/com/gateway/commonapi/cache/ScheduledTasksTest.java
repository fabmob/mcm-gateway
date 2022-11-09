package com.gateway.commonapi.cache;

import com.gateway.commonapi.TestingApplication;
import com.gateway.commonapi.dto.data.GatewayParamsDTO;
import com.gateway.commonapi.properties.ErrorMessages;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = {"com.gateway.commonapi"})
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Ignore("Mother class of the tests")
class ScheduledTasksTest {


    @Mock
    private ErrorMessages errorMessage;

    @InjectMocks
    private ScheduledTasks scheduledTasks;

    @Mock
    private RestTemplate restTemplate;


    @Test
    void getCacheActivationValue() {
        ResponseEntity<GatewayParamsDTO> gatewayParamsDTO = ResponseEntity.status(HttpStatus.OK).body(new GatewayParamsDTO("key", "true"));
        lenient().when(restTemplate.exchange(ArgumentMatchers.contains("gateway-params/CACHE_ACTIVATION"), ArgumentMatchers.eq(HttpMethod.GET), any(), ArgumentMatchers.eq(GatewayParamsDTO.class))).thenReturn(gatewayParamsDTO);

        scheduledTasks.getCacheActivationValue();
        assertTrue(CacheStatus.getInstance().isEnabled());

    }

}