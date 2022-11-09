package com.gateway.mockapi.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

class HttpServletRequestUtilsTest {

    @Mock
    HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetParametersAsString() {
        assertEquals("", HttpServletRequestUtils.getParametersAsString(null));

        Map<String, String[]> parameters = new HashMap<>();
        Mockito.when(request.getParameterMap()).thenReturn(parameters);
        assertEquals("", HttpServletRequestUtils.getParametersAsString(request));

        parameters.put("key1", new String[]{"value1"});
        assertEquals("key1=[value1]", HttpServletRequestUtils.getParametersAsString(request));

        parameters.put("key2", new String[]{"value2_1", "value2_2"});
        assertEquals("key1=[value1], key2=[value2_1, value2_2]", HttpServletRequestUtils.getParametersAsString(request));

        parameters.put("key\n3", new String[]{"value\n3"});
        assertEquals("key1=[value1], key2=[value2_1, value2_2], key3=[value3]", HttpServletRequestUtils.getParametersAsString(request));
    }

    @Test
    void testGetHeadersAsString() {
        assertEquals("", HttpServletRequestUtils.getHeadersAsString(null));

        Mockito.when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.EMPTY_LIST));
        assertEquals("", HttpServletRequestUtils.getHeadersAsString(request));

        Mockito.when(request.getHeaderNames()).thenReturn(Collections.enumeration(List.of("key1")));
        Mockito.when(request.getHeaders("key1")).thenReturn(Collections.enumeration(List.of("value1")));
        assertEquals("key1=[value1]", HttpServletRequestUtils.getHeadersAsString(request));

        Mockito.when(request.getHeaderNames()).thenReturn(Collections.enumeration(List.of("key1", "key2")));
        Mockito.when(request.getHeaders("key1")).thenReturn(Collections.enumeration(List.of("value1")));
        Mockito.when(request.getHeaders("key2")).thenReturn(Collections.enumeration(List.of("value2_1", "value2_2")));
        assertEquals("key1=[value1], key2=[value2_1, value2_2]", HttpServletRequestUtils.getHeadersAsString(request));

        Mockito.when(request.getHeaderNames()).thenReturn(Collections.enumeration(List.of("key\n1")));
        Mockito.when(request.getHeaders("key\n1")).thenReturn(Collections.enumeration(List.of("value\n1")));
        assertEquals("key1=[value1]", HttpServletRequestUtils.getHeadersAsString(request));
    }

    @Test
    void testGetBodyAsString() throws IOException {
        assertEquals("", HttpServletRequestUtils.getBodyAsString(null));

        BufferedReader bodyReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("{\"key\": \"value\"}".getBytes())));
        Mockito.when(request.getReader()).thenReturn(bodyReader);
        assertEquals("{\"key\": \"value\"}", HttpServletRequestUtils.getBodyAsString(request));

        Mockito.when(request.getReader()).thenThrow(new InterruptedIOException("io error"));
        assertEquals("error while reading body : io error", HttpServletRequestUtils.getBodyAsString(request));
    }
}