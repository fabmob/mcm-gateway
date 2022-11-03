package com.gateway.mockapi.service.impl;

import com.gateway.commonapi.dto.data.SelectorDTO;
import com.gateway.commonapi.exception.BadRequestException;
import com.gateway.commonapi.exception.InternalException;
import com.gateway.commonapi.exception.NotFoundException;
import com.gateway.commonapi.exception.UnauthorizedException;
import com.gateway.commonapi.properties.ErrorMessages;
import com.gateway.commonapi.utils.JsonUtils;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class MockApiServiceTest {

    @InjectMocks
    private MockApiServiceImpl service;

    @Mock
    private ErrorMessages errorMessages;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(service, "mockDirectory", "src/test/resources/mocks");

        lenient().when(errorMessages.getTechnicalNotFoundDescription()).thenReturn("not found");
    }

    @Test
    void testGetMockedResponseCode() {
        assertEquals(200, service.getMockedResponseCode("sub/200"));
        assertEquals(201, service.getMockedResponseCode("sub\\201"));
        assertEquals(200, service.getMockedResponseCode("sub/../sub/200"));

        assertThrows(NotFoundException.class, () -> service.getMockedResponseCode("sub/204"));
        assertThrows(NotFoundException.class, () -> service.getMockedResponseCode("sub/other"));
        assertThrows(BadRequestException.class, () -> service.getMockedResponseCode(null));
        assertThrows(BadRequestException.class, () -> service.getMockedResponseCode(""));
        assertThrows(BadRequestException.class, () -> service.getMockedResponseCode("sub/"));
        assertThrows(UnauthorizedException.class, () -> service.getMockedResponseCode("sub/../../pathTraversal.json"));
    }

    @Test
    void testGetMockedBody() throws JSONException {
        String body = service.getMockedBody("sub/200\\response.json");
        assertNotNull(body);
        SelectorDTO selector = new SelectorDTO();
        selector.setSelectorId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        selector.setKey("key2");
        assertEquals("value2", JsonUtils.getJsonElement(selector, body).getAsString());

        assertThrows(NotFoundException.class, () -> service.getMockedBody("sub/201/response.json"));
        assertThrows(NotFoundException.class, () -> service.getMockedBody("sub/204/response.json"));
        assertThrows(NotFoundException.class, () -> service.getMockedBody("sub/205"));
        assertThrows(BadRequestException.class, () -> service.getMockedBody(null));
        assertThrows(BadRequestException.class, () -> service.getMockedBody(""));
        assertThrows(UnauthorizedException.class, () -> service.getMockedBody("sub/../../pathTraversal.json"));

        ReflectionTestUtils.setField(service, "mockDirectory", null);
        assertThrows(InternalException.class, () -> service.getMockedBody("sub/200\\response.json"));
    }
}