package com.gateway.commonapi.monitoring;

import com.gateway.commonapi.constants.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
public class CorrelationHeaderFilter implements Filter {

    /**
     * Filter on http request to generate a correlationId if needed and store it in thread
     * @param servletRequest request
     * @param servletResponse response
     * @param filterChain the filter Chain
     * @throws IOException exception
     * @throws ServletException exception
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String currentCorrelationId = httpServletRequest.getHeader(GlobalConstants.CORRELATION_ID_HEADER);
        log.info(httpServletRequest.getServletPath());
        // for synchronous requests
        if (!currentRequestIsAsyncDispatcher(httpServletRequest)) {
            if (currentCorrelationId == null) {
                currentCorrelationId = new ThreadLocalUserSession().get().getContextId();
                log.info("No correlationId found in Header for ({}). Generated : {}", httpServletRequest.getServletPath() , currentCorrelationId);
            } else {
                log.info("Found correlationId in Header for ({}) : {}", httpServletRequest.getServletPath() , currentCorrelationId);
            }
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
