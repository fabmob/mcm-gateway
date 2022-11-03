package com.gateway.commonapi.monitoring;


import com.gateway.commonapi.constants.GlobalConstants;
import com.gateway.commonapi.utils.SanitizorUtils;
import com.gateway.commonapi.utils.enums.StandardEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CorrelationHeaderFilter implements Filter {


    /**
     * Filter on http request to generate a CORRELATION_ID if needed and store it in thread
     *
     * @param servletRequest  request
     * @param servletResponse response
     * @param filterChain     the filter Chain
     * @throws IOException      exception
     * @throws ServletException exception
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String currentCorrelationId = httpServletRequest.getHeader(GlobalConstants.CORRELATION_ID_HEADER);
        log.info(httpServletRequest.getServletPath());
        UserContext currentThreadUserContext = new ThreadLocalUserSession().get();
        // for synchronous requests
        if (!currentRequestIsAsyncDispatcher(httpServletRequest)) {
            if (currentCorrelationId == null) {
                currentCorrelationId = currentThreadUserContext.getContextId();
                log.debug("No CORRELATION_ID found in Header for ({}). Generated : {}", httpServletRequest.getServletPath(), currentCorrelationId);

            } else {
                log.debug("Found CORRELATION_ID in Header for ({}) : {}", httpServletRequest.getServletPath(), currentCorrelationId);
            }
        }
        // add CORRELATION_ID header to response headers for trace purpose
        ((HttpServletResponse) servletResponse).addHeader(GlobalConstants.CORRELATION_ID_HEADER, SanitizorUtils.sanitizeHeader(currentCorrelationId));

        // transfer output-standard header to response headers in present in request ones.
        String outputStandardHeader = httpServletRequest.getHeader(GlobalConstants.OUTPUT_STANDARD);
        if (StringUtils.isNotEmpty(outputStandardHeader)) {
            currentThreadUserContext.setOutputStandard(StandardEnum.fromValue(outputStandardHeader));
            ((HttpServletResponse) servletResponse).addHeader(GlobalConstants.OUTPUT_STANDARD, SanitizorUtils.sanitizeHeader(outputStandardHeader));
        }

        //TODO check how to manage asynchronous requests

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    private boolean currentRequestIsAsyncDispatcher(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getDispatcherType().equals(DispatcherType.ASYNC);
    }

    @Override
    public void destroy() {
        // nothing to do here
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // nothing to do here
    }
}
