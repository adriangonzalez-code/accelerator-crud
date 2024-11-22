package com.smoothiecorp.services.productsapi.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.time.Instant;
import java.util.Enumeration;

@Slf4j
@Component
public class RequestResponseFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = Instant.now().toEpochMilli();
        String requestTimestamp = Instant.now().toString();
        String method = request.getMethod();
        String path = request.getRequestURI();
        String queryParams = getQueryParams(request);

        // Wrap the response to capture its body
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            // Continue the filter chain with the wrapped response
            filterChain.doFilter(request, responseWrapper);
        } finally {
            long endTime = Instant.now().toEpochMilli();
            String responseTimestamp = Instant.now().toString();
            long duration = endTime - startTime;

            int status = responseWrapper.getStatus();
            String messageCode = HttpStatus.valueOf(status).getReasonPhrase();

            String responseBody = getResponseBody(responseWrapper);

            // Log details
            log.info("Response returned: {}", responseBody);
            log.info(
                    "Request completed: [Request Timestamp: {}, Response Timestamp: {}, Duration: {} ms, Path: {}, Query Params: {}, Method: {}, Http Status: {}, Message Code: {}]",
                    requestTimestamp,
                    responseTimestamp,
                    duration,
                    path,
                    queryParams,
                    method,
                    status,
                    messageCode
            );

            // Ensure the response is correctly written back to the client
            responseWrapper.copyBodyToResponse();
        }
    }

    private String getQueryParams(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        StringBuilder queryParams = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            String param = parameterNames.nextElement();
            String value = request.getParameter(param);
            queryParams.append(param).append("=").append(value).append("&");
        }
        if (!queryParams.isEmpty()) {
            queryParams.deleteCharAt(queryParams.length() - 1); // Remove trailing "&"
        }
        return queryParams.toString();
    }

    private String getResponseBody(ContentCachingResponseWrapper responseWrapper) {
        try {
            byte[] content = responseWrapper.getContentAsByteArray();
            return new String(content, responseWrapper.getCharacterEncoding());
        } catch (Exception e) {
            log.error("Error reading response body", e);
            return "Error reading response body";
        }
    }
}
