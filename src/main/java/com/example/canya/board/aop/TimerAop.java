package com.example.canya.board.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class TimerAop {
    @Pointcut("execution(* com.example.canya.board.controller..*.*(..))")
    private void cut(){};

    @Pointcut("@annotation(com.example.canya.annotations.Timer)")
    private void Timer(){};

    @Around("cut() && Timer()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();
        stopWatch.stop();
        log.info("총 걸린 시간 : "+ joinPoint.getSignature() + " : " + stopWatch.getTotalTimeMillis() +" ms");
        return result;
    }

}