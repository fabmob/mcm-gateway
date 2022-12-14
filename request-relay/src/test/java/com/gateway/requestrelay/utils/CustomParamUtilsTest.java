package com.gateway.requestrelay.utils;


import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
public class CustomParamUtilsTest extends CustomParamUtils {

    @TestConfiguration
    public static class CustomParamUtilsTestContextConfiguration {
        @Bean
        public CustomParamUtils customParamUtils() {
            return new CustomParamUtils();
        }
    }


    @Test
    public void testIsUtf16ContentType() {
        HttpHeaders headersUpperCase = new HttpHeaders();
        headersUpperCase.set("Content-Type", "application/json;charset=UTF-16");
        Boolean responseUpperCase = CustomParamUtils.isUtf16ContentType(headersUpperCase);
        assertEquals(true, responseUpperCase);

        HttpHeaders headersLowerCase = new HttpHeaders();
        headersLowerCase.set("Content-Type", "application/json;charset=utf-16");
        Boolean responseLowerCase = CustomParamUtils.isUtf16ContentType(headersLowerCase);
        assertEquals(true, responseLowerCase);

    }


    @Test
    public void testConvertUtf16ToUtf8() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-16");

        String content = "test";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_16);
        String utf16content = new String(bytes, StandardCharsets.UTF_16);

        ResponseEntity<String> responseWithBody = new ResponseEntity<>(utf16content, headers, HttpStatus.OK);
        String respWithBody = CustomParamUtils.convertUtf16ToUtf8(responseWithBody);
        assertEquals("test", respWithBody);

        ResponseEntity<String> responseNullBody = new ResponseEntity<>(null, headers, HttpStatus.OK);
        String respNullBody = CustomParamUtils.convertUtf16ToUtf8(responseNullBody);
        String expected = StringUtils.EMPTY;
        assertEquals(respNullBody, expected);


    }


}
