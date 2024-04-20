package com.bbs.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class LogAspect {

    // 定义切点，匹配使用LogAnnotation注解的方法
    @Pointcut("@annotation(LogAnnotation)")
    public void annotationPointcut() {
    }

    // 环绕通知，可以在方法前后执行
    @Around("annotationPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取方法上的LogAnnotation注解
        LogAnnotation logAnnotation = method.getAnnotation(LogAnnotation.class);
        if (logAnnotation != null) {
            // 打印注解中的value值
            log.info(logAnnotation.value());
        }

        String methodName = method.getName();
        log.info("开始执行方法: {}", methodName);

        // 执行方法
        Object result = joinPoint.proceed();

        // 方法执行后
        log.info("方法 {} 执行完成，结果: {}", methodName, result);
        return result;
    }

}
