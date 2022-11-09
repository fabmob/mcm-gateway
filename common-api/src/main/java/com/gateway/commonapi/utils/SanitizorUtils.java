package com.gateway.commonapi.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;

@Slf4j
public class SanitizorUtils {

    private static final String[] BLACKLISTED_HEADER_CHARS = {":", "="};

    /**
     * Utility class
     */
    private SanitizorUtils() {
    }

    /**
     * Converts a multilines text to a single line text.
     * Help to reduce attacks from untrusted data (such as headers or request parameters) that are logged or relayed.
     *
     * @param multiLinesText
     * @return
     */
    public static String convertToSingleLine(String multiLinesText) {
        String singleLineText = null;
        if (multiLinesText != null) {
            singleLineText = multiLinesText.lines().collect(Collectors.joining(""));
        }
        return singleLineText;
    }

    /**
     * Replace special characters from header name or value, ie : or =
     * Remove new lines.
     * <p>
     * Help to reduce headers manipulation.
     *
     * @param header
     * @return
     */
    public static String sanitizeHeader(String header) {
        header = convertToSingleLine(header);
        if (StringUtils.isNotEmpty(header)) {
            header = StringUtils.replaceEach(header, BLACKLISTED_HEADER_CHARS, new String[]{});
        }
        return header;
    }
}
