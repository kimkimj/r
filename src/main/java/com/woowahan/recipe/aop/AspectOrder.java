package com.woowahan.recipe.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
public class AspectOrder {

    @Component
    @Aspect
    @Order
    public static class LogAspect {
        @Around("com.woowahan.recipe.aop.PointCuts.all()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            long startTime = System.currentTimeMillis();
            Signature result = joinPoint.getSignature();
            long endTime = System.currentTimeMillis();
            log.info("[logging] {} ({}ms)", result, endTime-startTime);
            return joinPoint.proceed();
        }
    }

}
