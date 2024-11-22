package com.smoothiecorp.services.productsapi.aspects.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoothiecorp.services.productsapi.aspects.LogExecution;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogExecutionAspect {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Environment environment;

    @Around("@annotation(logExecution)")
    public Object trackExecution(ProceedingJoinPoint joinPoint, LogExecution logExecution) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        log.info(">>> [START] {}: {}", methodName, logExecution.message());

        Object result = null;
        try {
            result = joinPoint.proceed(); // Ejecuta el método original
        } catch (Throwable ex) {
            long duration = System.currentTimeMillis() - startTime;
            log.info(">>> [END - EXCEPTION] {}: Duration={}ms", methodName, duration);
            throw ex; // Re-lanza la excepción para que sea manejada fuera
        }

        long duration = System.currentTimeMillis() - startTime;
        log.info(">>> [END] {}: Duration={}ms", methodName, duration);

        boolean logReturn = resolveFlag(logExecution.logReturn(), logExecution.logReturnProperty());
        boolean asJson = resolveFlag(logExecution.asJson(), logExecution.asJsonProperty());

        if (logReturn) {
            logReturnValue(result, asJson);
        }

        return result;
    }

    private void logReturnValue(Object result, boolean asJson) {
        try {
            if (asJson) {
                log.info(">>> [RETURN] Response (JSON): {}", objectMapper.writeValueAsString(result));
            } else {
                log.info(">>> [RETURN] Response: {}", result);
            }
        } catch (Exception ex) {
            log.warn(">>> [RETURN] Could not serialize response: {}", ex.getMessage());
        }
    }

    private boolean resolveFlag(boolean annotationFlag, String propertyKey) {
        if (propertyKey.isEmpty()) {
            return annotationFlag; // Usa el valor del annotation si no se definió una propiedad
        }
        String propertyValue = environment.getProperty(propertyKey);
        return propertyValue != null ? Boolean.parseBoolean(propertyValue) : annotationFlag;
    }
}
