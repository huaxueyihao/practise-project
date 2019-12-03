package com.spring.source.code.study.Aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SleepHelperTwo {

    public SleepHelperTwo() {
    }

    @Pointcut("execution(* com.spring.source.code.study.Aop.Sleepable.sleep())")
    public void sleeppoint() {
    }


    @AfterReturning("sleeppoint()")
    public void afterReturning() throws Throwable {
        System.out.println("put on cloth。。。。");
    }


    @Before("sleeppoint()")
    public void before() throws Throwable {
        System.out.println("take off cloth。。。。");
    }
}
