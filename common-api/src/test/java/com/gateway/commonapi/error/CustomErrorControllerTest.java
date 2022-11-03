package com.gateway.commonapi.error;

import com.gateway.commonapi.ApiITTestCase;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.handler.RestResponseEntityExceptionHandler;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Slf4j
public class CustomErrorControllerTest extends ApiITTestCase {

    @Autowired
    ErrorMessages errorMessages;

    @Autowired
    RestResponseEntityExceptionHandler exceptionHandler;

    @Autowired
    CustomErrorController errorController;

    @Before
    public void setup() {
        new ThreadLocalUserSession().get().setOutputStandard(StandardEnum.GATEWAY);
        new ThreadLocalUserSession().get().setContextId(UUID.randomUUID().toString());
    }

    @Test
    public void testHtmlError() {
        ResponseEntity response = errorController.errorHtml(null);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testJsonError()  {
        assertThrows(BadRequestException.class,()->errorController.error(null));
    }
}
