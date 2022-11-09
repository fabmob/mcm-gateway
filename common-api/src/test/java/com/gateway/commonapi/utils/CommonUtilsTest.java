package com.gateway.commonapi.utils;

import com.gateway.commonapi.dto.exceptions.GenericError;
import com.gateway.commonapi.tests.UTTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class CommonUtilsTest extends UTTestCase {

    @Test
    public void testIsTompErrorFormat() {
        String rawResponseFromException = "{\n" +
                "  \"errorcode\": 500,\n" +
                "  \"type\": \"noType\",\n" +
                "  \"title\": \"Not Found\",\n" +
                "  \"status\": 500,\n" +
                "  \"detail\": \"Error message\",\n" +
                "  \"instance\": \"Gateway\"\n" +
                "}";
        String rawResponseFromException2 = "{\n" +
                "  \"Errorcode\": 500,\n" +
                "  \"Type\": \"noType\",\n" +
                "  \"titre\": \"Not Found\",\n" +
                "  \"Status\": 500,\n" +
                "  \"detail\": \"Error message\",\n" +
                "  \"instance\": \"Gateway\"\n" +
                "}";

        Assertions.assertEquals(true, CommonUtils.isTompErrorFormat(rawResponseFromException));
        Assertions.assertEquals(false, CommonUtils.isTompErrorFormat(rawResponseFromException2));
    }


    @Test
    public void testGetValidStatusTompError() {
        String rawResponseFromException = "{\n" +
                "  \"errorcode\": 510,\n" +
                "  \"type\": \"noType\",\n" +
                "  \"title\": \"error NotExtended\",\n" +
                "  \"status\": 510,\n" +
                "  \"detail\": \"Error message\",\n" +
                "  \"instance\": \"Gateway\"\n" +
                "}";
        HttpStatus rawStatusCodeFromException = HttpStatus.NOT_EXTENDED;
        GenericError bodyGenericError = new GenericError();
        bodyGenericError.setStatus(500);
        String expected = "{\n" +
                "  \"errorcode\": 500,\n" +
                "  \"type\": \"noType\",\n" +
                "  \"title\": \"error NotExtended\",\n" +
                "  \"status\": 500,\n" +
                "  \"detail\": \"Error message\",\n" +
                "  \"instance\": \"Gateway\"\n" +
                "}";
        Assertions.assertEquals(expected, CommonUtils.getValidStatusTompError(rawResponseFromException, rawStatusCodeFromException, bodyGenericError));
    }

    @Test
    public void testIsCarpoolErrorFormat() {
        String rawResponseFromException2 = "{\n" +
                "  \"errorcode\": 500,\n" +
                "  \"type\": \"noType\",\n" +
                "  \"title\": \"Not Found\",\n" +
                "  \"status\": 500,\n" +
                "  \"detail\": \"Error message\",\n" +
                "  \"instance\": \"Gateway\"\n" +
                "}";
        String rawResponseFromException = "{\n" +
                "  \"error\": \"string\"\n" +
                "}";
        Assertions.assertTrue(CommonUtils.isCarpoolErrorFormat(rawResponseFromException));
        Assertions.assertFalse(CommonUtils.isCarpoolErrorFormat(rawResponseFromException2));
    }

    @Test
    public void testIsJsonObject() {
        String rawResponseFromException = "{\n" +
                "  \"errorcode\": 500,\n" +
                "  \"type\": \"noType\",\n" +
                "  \"title\": \"Not Found\",\n" +
                "  \"status\": 500,\n" +
                "  \"detail\": \"Error message\",\n" +
                "  \"instance\": \"Gateway\"\n" +
                "}";
        String rawResponseFromException2 = "test false";
        Assertions.assertTrue(CommonUtils.isJsonObject(rawResponseFromException));
        Assertions.assertFalse(CommonUtils.isJsonObject(rawResponseFromException2));
    }

}
