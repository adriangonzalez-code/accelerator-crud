package com.smoothiecorp.services.productsapi.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Timestamp;

@Slf4j
@Component
@Order(1)
public class RequestResponseFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        StringBuilder header = new StringBuilder();
        long start = System.currentTimeMillis();
        Timestamp requestTimeStamp = new Timestamp(start);

        header.append("request_timestamp: ").append(requestTimeStamp).append(", ");

        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(request, response, filterChain, header);
            long responseTimeInMillis = System.currentTimeMillis();
            Timestamp responseTimeStamp = new Timestamp(responseTimeInMillis);
            long responseTime = responseTimeInMillis - start;

            header.append("response_time: ").append(responseTimeStamp).append(", ")
                    .append("response_time_ms: ").append(responseTime).append("ms");

            log.info(header.toString());
        }
    }

    protected void doFilterWrapped(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, StringBuilder headerBuilder) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response, headerBuilder);
        }
    }

    protected void afterRequest(HttpServletRequest request, HttpServletResponse response, StringBuilder headerBuilder) {
        logRequestHeader(request, headerBuilder);
    }

    private void logRequestHeader(HttpServletRequest request, StringBuilder headerBuilder) {
        headerBuilder.append("operation: ").append(request.getMethod()).append(", ")
                .append("path: ").append(request.getRequestURI()).append(", ");
        val queryString = request.getQueryString();
        if (queryString != null) {
            headerBuilder.append("params: ").append(queryString).append(", ");
        }
    }
}
