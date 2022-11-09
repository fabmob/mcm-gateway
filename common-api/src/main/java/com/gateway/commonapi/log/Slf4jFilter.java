package com.gateway.commonapi.log;

import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.monitoring.ThreadLocalUserSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class Slf4jFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER_NAME = GlobalConstants.CORRELATION_ID_HEADER;
    private static final String CORRELATION_ID_LOG_VAR_NAME = "CORRELATION_ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            //get the correlation id either from http header or current user context
            if (StringUtils.isNotBlank(request.getHeader(CORRELATION_ID_HEADER_NAME))) {
                MDC.put(CORRELATION_ID_LOG_VAR_NAME, request.getHeader(CORRELATION_ID_HEADER_NAME));
            } else {
                MDC.put(CORRELATION_ID_LOG_VAR_NAME, new ThreadLocalUserSession().get().getContextId());
            }

            chain.doFilter(request, response);
        } finally {
            removeCorrelationId();
        }
    }

    protected void removeCorrelationId() {
        MDC.remove(CORRELATION_ID_LOG_VAR_NAME);
    }
}