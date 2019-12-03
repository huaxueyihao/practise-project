package com.spring.source.code.study.Aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

@Aspect
public class SleepHelper implements MethodBeforeAdvice, AfterReturningAdvice {

    public SleepHelper() {
    }

    @Pointcut("execution(* *.sleep())")
    public void sleeppoint() {
    }


    @AfterReturning
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("put on cloth。。。。");
    }

    @Before(value = "sleeppoint()")
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("take off cloth。。。。");
    }
}
