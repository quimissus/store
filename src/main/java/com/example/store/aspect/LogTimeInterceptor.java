package com.example.store.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LogTimeInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogTimeInterceptor.class);

    @Around("@annotation(LogTime)")
    //@Around("@annotation(com.example.store.intercept.LogTime)")
    public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        String methodName = joinPoint.getSignature().toShortString();
        LOGGER.info("{} executed method in {} ms", methodName, duration);
        return result;
    }
}
