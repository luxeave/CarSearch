package com.example.carsearch.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.example.carsearch.service.*.*(..)) || " +
            "execution(* com.example.carsearch.controller.*.*(..)) || " +
            "execution(* com.example.carsearch.repository.*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.debug("Entering {}.{}", className, methodName);

        try {
            long startTime = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            logger.debug("Exiting {}.{} - Execution time: {} ms",
                    className, methodName, (endTime - startTime));

            return result;
        } catch (Exception e) {
            logger.error("Exception in {}.{}: {}",
                    className, methodName, e.getMessage(), e);
            throw e;
        }
    }
}