package com.gateway.commonapi.utils;


import com.gateway.commonapi.tests.UTTestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class SanitizorUtilsTest extends UTTestCase {


    @Test
    public void testConvertToSingleLine() {
        Assertions.assertNull(SanitizorUtils.convertToSingleLine(null));
        Assertions.assertEquals("", SanitizorUtils.convertToSingleLine(""));

        Assertions.assertEquals("single line", SanitizorUtils.convertToSingleLine("single line"));

        String multiline = String.join(
                System.getProperty("line.separator"),
                "{",
                "\t\"adapterId\": \"123\",",
                "\t\"adapterName\": \"default-adapter\"",
                "}");
        Assertions.assertEquals("{\t\"adapterId\": \"123\",\t\"adapterName\": \"default-adapter\"}", SanitizorUtils.convertToSingleLine(multiline));
    }

    @Test
    public void testSanitizeHeader() {
        Assertions.assertNull(SanitizorUtils.sanitizeHeader(null));
        Assertions.assertEquals("", SanitizorUtils.sanitizeHeader(""));

        Assertions.assertEquals("single line", SanitizorUtils.sanitizeHeader("single line"));

        String multiline = String.join(
                System.getProperty("line.separator"),
                "my", "key");
        Assertions.assertEquals("mykey", SanitizorUtils.sanitizeHeader(multiline));
    }


}

