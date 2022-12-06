package com.example.canya.category.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

//@Aspect
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class CategoryAOP {
//
//    @Pointcut("@annotation(com.example.canya.annotations.Search)")
//    private void search(){}
//
//    @Pointcut("@annotation(com.example.canya.annotations.SetPageable)")
//    private void setPageable(){}
//
//    @Around(value = "setPageable() && args(..,page,size)")
//    private Pageable setPageable(ProceedingJoinPoint joinPoint, Integer page, Integer size) throws Throwable {
//        Pageable pageable = PageRequest.of(page, size);
//        return pageable;
//    }
//
//}
