package com.spring.book.study.chapter07;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class AspectJTest {

    @Pointcut("execution(public * com.spring.book.study.chapter07.*.*(..))")
    public void test() {

    }

    @Before("test()")
    public void beforeTest() {
        System.out.println("beforeTest");
    }

    @After("test()")
    public void afterTest(){
        System.out.println("afterTest");
    }

    @Around("test()")
    public Object aroundTest(ProceedingJoinPoint p) {
        System.out.println("before1");
        Object o = null;
        try {
            o = p.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("after1");
        return o;
    }

}
