package com.gateway.dataapi.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ApiOriginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type");

        chain.doFilter(request, response);
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
