package com.bbs.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1) // 该切面的执行顺序
public class SimpleAspect {
    // 方案一
//    @Before("within(com.bbs.controller..*)")
//    public void beforeAdvice() {
//        System.out.println("在controller执行方法之前");
//    }

    // 方案二
//    @Pointcut("within(com.bbs.controller..*)")
//    public void inControllerPackage() {
//    }
//
//    @Before("inControllerPackage()")
//    public void beforeControllerMethod() {
//        System.out.println("在controller执行方法之前");
//    }

    // 方案三
//    @Before("@within(com.bbs.aop.SimpleAnnotation)")
//    public void beforeControllerMethod() {
//        System.out.println("在controller执行方法之前");
//    }

    // 方案四
    @Before("@annotation(com.bbs.aop.SimpleAnnotation)")
    public void beforeAnnotatedMethod() {
        System.out.println("在执行用 @SimpleAnnotation 注释的方法之前");
    }

}
