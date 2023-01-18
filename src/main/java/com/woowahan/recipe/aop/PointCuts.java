package com.woowahan.recipe.aop;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class PointCuts {

    // com.woowahan.recipe 하위 패키지
    @Pointcut("execution(* com.woowahan.recipe..*(..))")
    public void all() {}

}
