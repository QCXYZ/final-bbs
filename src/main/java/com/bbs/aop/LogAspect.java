package com.bbs.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

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
            System.out.println(logAnnotation.value());
        }

        String methodName = method.getName();
        System.out.println("开始执行方法: " + methodName);

        try {
            // 执行方法
            Object result = joinPoint.proceed();

            // 方法执行后
            System.out.println("方法 " + methodName + " 执行完成，结果: " + result);
            return result;
        } catch (Throwable throwable) {
            // 异常处理
            System.out.println("方法 " + methodName + " 抛出异常: " + throwable);
            throw throwable;
        }
    }

}
