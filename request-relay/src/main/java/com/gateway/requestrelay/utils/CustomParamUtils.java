package com.gateway.requestrelay.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;


import static com.gateway.requestrelay.utils.constant.RequestRelayMessageDict.*;

@Slf4j
public class CustomParamUtils {

    CustomParamUtils() {
    }


    /**
     * Indicates if the headers contain UTF16
     * @param headers : HTTP headers {@link HttpHeaders}
     *
     * @return True if has UTF16
     */
    public static boolean isUtf16ContentType(HttpHeaders headers) {
        boolean result = false;
        List<String> contentTypeHeaders = headers.get(CONTENT_TYPE_HEADER);
        if(!CollectionUtils.isEmpty(contentTypeHeaders)) {
            for(String header : contentTypeHeaders) {

                if(header.contains(UTF_16_VALUE) || header.contains(UTF_16_VALUE.toLowerCase()) ) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Converts UTF16 format to UTF8
     * @param content : Content to convert
     *
     * @return Converted content
     */
    public static String convertUtf16ToUtf8(ResponseEntity<String> content)  {
        String result = StringUtils.EMPTY;
        try {
            String body = content.getBody();
            if(body!=null){
                InputStream inputStream = new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_16));
                int nRead;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] data = new byte[1024];
                while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                result = buffer.toString(StandardCharsets.UTF_8);
                String replacement = "";
                result = result.replaceAll(REGEX,replacement);
            }

        } catch (IOException | NullPointerException e) {
                log.error(CONVERSION_ERROR_MSG, e);
        }


        return result;
    }


}
