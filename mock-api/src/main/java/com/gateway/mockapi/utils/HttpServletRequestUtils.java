package com.gateway.mockapi.utils;

import com.gateway.commonapi.utils.SanitizorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Slf4j
public class HttpServletRequestUtils {

    public static final String IO_ERROR = "error while reading body : ";

    /**
     * Default constructor.
     *
     * @throws IllegalStateException Utility class, constructor should not be used.
     */
    private HttpServletRequestUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Extracts the parameters from the request, in a single line text
     *
     * @param request
     * @return
     */
    public static String getParametersAsString(HttpServletRequest request) {
        StringBuilder parameters = new StringBuilder("");
        if (request != null && request.getParameterMap() != null) {
            parameters.append(request.getParameterMap().entrySet().stream().map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue())).collect(Collectors.joining(", ")));
        }
        return SanitizorUtils.convertToSingleLine(parameters.toString());
    }

    /**
     * Extracts the headers from the request, in a single line text
     *
     * @param request
     * @return
     */
    public static String getHeadersAsString(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder("");
        if (request != null && request.getHeaderNames() != null) {
            boolean firstElementWritten = false;
            for (String headerName : Collections.list(request.getHeaderNames())) {
                if (firstElementWritten) {
                    headers.append(", ");
                }
                headers.append(headerName);
                headers.append("=[");
                headers.append(Collections.list(request.getHeaders(headerName)).stream().collect(Collectors.joining(", ")));
                headers.append("]");
                firstElementWritten = true;
            }
        }
        return SanitizorUtils.convertToSingleLine(headers.toString());
    }

    /**
     * Extracts the body from the request, in a single line text
     *
     * @param request
     * @return
     */
    public static String getBodyAsString(HttpServletRequest request) {
        StringBuilder body = new StringBuilder("");
        if (request != null) {
            try {
                body.append(IOUtils.toString(request.getReader()));
            } catch (IOException e) {
                log.error(IO_ERROR, e);
                body.append(IO_ERROR);
                body.append(e.getMessage());
            }
        }
        return SanitizorUtils.convertToSingleLine(body.toString());
    }

}
