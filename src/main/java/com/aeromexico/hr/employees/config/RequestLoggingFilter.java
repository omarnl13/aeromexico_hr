package com.aeromexico.hr.employees.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String AUTHORIZATION = "authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        LOGGER.info("Request received: method={}, uri={}", request.getMethod(), request.getRequestURI());
        logHeaders(request);
        try {
            filterChain.doFilter(request, response);
        } finally {
            long elapsed = System.currentTimeMillis() - startTime;
            LOGGER.info("Request completed: method={}, uri={}, status={}, elapsedMs={}",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), elapsed);
        }
    }

    private void logHeaders(HttpServletRequest request) {
        Collections.list(request.getHeaderNames()).forEach(headerName -> {
            String value = AUTHORIZATION.equalsIgnoreCase(headerName) ? "***" : request.getHeader(headerName);
            LOGGER.info("Header received: {}={}", headerName, value);
        });
    }
}
